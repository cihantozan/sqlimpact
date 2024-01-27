package sqlimpact.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.WithItem;

public class SqlTreeUtil {
	
	private HashMap<Object, SqlTree> allNodes;
	private List<SqlTree> columns;
	private DBUtil dbUtil;
	private List<SqlTree> tableNodes;
	
	private HashMap<Table, DBUtil.Table> tables;	
	private HashMap<SqlTree, ColumnProperties> columnsTables;
	private HashMap<SqlTree, ColumnLocation> columnLocations;
	private HashSet<FullColumnImpact> fullColumnImpactList;	
	private HashSet<TableImpact> tableImpactList;

			
	public HashMap<Object, SqlTree> getAllNodes() {return allNodes;}
	public void setAllNodes(HashMap<Object, SqlTree> allNodes) {this.allNodes = allNodes;}		
	public List<SqlTree> getColumns() {return columns;}
	public void setColumns(List<SqlTree> columns) {this.columns = columns;}		
	public DBUtil getDbUtil() {return dbUtil;}
	public void setDbUtil(DBUtil dbUtil) {this.dbUtil = dbUtil;}	
	public List<SqlTree> getTableNodes() {return tableNodes;}
	public void setTableNodes(List<SqlTree> tableNodes) {this.tableNodes = tableNodes;}	
	
	public HashMap<Table, DBUtil.Table> getTables() {return tables;}
	public HashMap<SqlTree, ColumnProperties> getColumnsTables() {return columnsTables;}	
	public HashMap<SqlTree, ColumnLocation> getColumnLocations() {return columnLocations;}
	public HashSet<FullColumnImpact> getFullColumnImpactList() {return fullColumnImpactList;}
	public HashSet<TableImpact> getTableImpactList() {return tableImpactList;}
	
	public SqlTree getPlainSelectOfNode(SqlTree node) {
		if(node.getData()!=null && node.getData() instanceof PlainSelect) {
			return node;
		}
		else {
			if(node.getParent()!=null) return getPlainSelectOfNode(node.getParent());
		}
		return null;
	}
	public List<FromItem> getFromList(PlainSelect select){
		List<FromItem> list=new ArrayList<FromItem>();
		list.add(select.getFromItem());
		
		if(select.getJoins()!=null) {
			for(Join join:select.getJoins()) {
				list.add(join.getFromItem());
			}
		}
		return list;
	}
	public PlainSelect findSelectOfTableInWithQuery(Table table, SqlTree node) {
		
		if(node==null) {
			node=this.allNodes.get(table).getParent();
			if(node==null) return null;
		}
		
		if(node.getData() instanceof PlainSelect) {
			PlainSelect select=(PlainSelect)node.getData();
			if(select.getWithItemsList()!=null) {
				for(WithItem withItem: select.getWithItemsList()) {
					if(withItem.getAlias().getName().equals(table.getName())) {
						return withItem.getSelect().getPlainSelect();
					}
				}				
			}			
		}
		
		if(node.getParent()!=null) return findSelectOfTableInWithQuery(table, node.getParent());
		else return null;
	}
	public SqlTree findSelect(SqlTree node) {
		
		if(node.getData() instanceof PlainSelect) {
			return node;
		}
		else {			
			if(node.getParent()!=null) return findSelect(node.getParent());
		}
		return null;
	}
	
	private List<Column> getAllColumnsofStar(SqlTree starNode) {
		String alias=null;
		if(starNode.getData() instanceof AllTableColumns && ((AllTableColumns)starNode.getData()).getTable()!=null) {
			alias=((AllTableColumns)starNode.getData()).getTable().getName();
		}
		
		SqlTree selectNode=getPlainSelectOfNode(starNode);
		PlainSelect select=(PlainSelect)selectNode.getData();
		List<FromItem> fromItemList = getFromList(select);
		
		List<Column> starColumnList=new ArrayList<Column>();
		
		for(FromItem fromItem:fromItemList) {
			
			if(
					alias==null || 
					alias!=null &&
					(
						  (fromItem.getAlias()!=null && fromItem.getAlias().getName().equals(alias)) ||
						  (fromItem.getAlias()==null && (fromItem instanceof Table) && ((Table)fromItem).getName().equals(alias))
					)
			  ) {
			
				PlainSelect withQueryOfTable=null;
				if(fromItem instanceof Table) withQueryOfTable=findSelectOfTableInWithQuery((Table)fromItem, selectNode);
				
				//gerçek tablo ise
				if((fromItem instanceof Table) && withQueryOfTable==null) {					
					DBUtil.Table table = tables.get(((Table)fromItem));
					HashSet<String> columnList = table.getColumnList();
															
					for(String columnName:columnList) {						
						Column column=new Column(((Table)fromItem),columnName);
						starColumnList.add(column);
					}					
				}
				//subquery veya with query ise
				else {
					
					PlainSelect subQuery=null;					
					if(withQueryOfTable==null) subQuery=((ParenthesedSelect)fromItem).getPlainSelect();
					else subQuery=withQueryOfTable;
					
					for(SelectItem subSelectItem : subQuery.getSelectItems()) {
						if( subSelectItem.getExpression() instanceof AllColumns ) {
							SqlTree newStarNode=allNodes.get((AllColumns)subSelectItem.getExpression());
							List<Column> subSelectStarColumns = getAllColumnsofStar(newStarNode);
							for (Column subSelectStarColumn : subSelectStarColumns) {
								Column newColumn=new Column(subSelectStarColumn.getColumnName());
								if(fromItem.getAlias()!=null) newColumn.setTable(new Table(fromItem.getAlias().getName()));
								starColumnList.add(newColumn);
							}
						}
						else {
							String subSelectColumnAlias="unknown";
							if(subSelectItem.getAlias()!=null) subSelectColumnAlias=subSelectItem.getAlias().getName();
							else if(subSelectItem.getExpression() instanceof Column) subSelectColumnAlias=((Column)subSelectItem.getExpression()).getColumnName();
							
							Column newColumn = new Column(subSelectColumnAlias);
							if(fromItem.getAlias()!=null) newColumn.setTable(new Table(fromItem.getAlias().getName()));
							starColumnList.add(newColumn);
						}
					}
					
					
				}
				
			}			
		}
		return starColumnList;		
	}
	private ColumnProperties getTableOfColumn(SqlTree columnNode,SqlTree selectNode,List<SqlTree> visitedSelectNodes) {
		
		if(this.columnsTables.containsKey(columnNode)) return this.columnsTables.get(columnNode);
		
		if(selectNode==null) selectNode=getPlainSelectOfNode(columnNode);
		if(visitedSelectNodes==null) visitedSelectNodes=new ArrayList<SqlTree>();
		if(visitedSelectNodes.contains(selectNode)) return null;
		visitedSelectNodes.add(selectNode);
		
		String alias=null;
		Column column=(Column)columnNode.getData();
		if(column.getTable()!=null) {
			if(column.getTable().getAlias()!=null) alias=column.getTable().getAlias().getName(); // TODO bu satıra giriyor mu kontrol et
			else alias=column.getTable().getName();
		}
		
		List<FromItem> fromList = getFromList((PlainSelect)selectNode.getData());
		
		//fromdaki tabloları dolaş
		for(FromItem fromItem:fromList) {

			//alias varsa fromItem aliasına eşit ise veya tablo adı aliasa eşit ise bak , alias yoksa mutlaka bak
			if( 
				 (
				    alias!=null 
				    && (
				    		   (fromItem.getAlias()!=null && alias.equals(fromItem.getAlias().getName()))
				    		|| ( fromItem.getAlias()==null && (fromItem instanceof Table) && ((Table) fromItem).getName().equals(alias) )
				       )
				 )
				 || alias==null
			  ) {
				
				PlainSelect withQueryOfTable=null;
				if(fromItem instanceof Table) withQueryOfTable=findSelectOfTableInWithQuery((Table)fromItem,null);
				
				//gerçek tablo ise
				if((fromItem instanceof Table) && withQueryOfTable==null) {
					DBUtil.Table table=this.tables.get((Table)fromItem);
					if( (alias==null && table!=null && table.columnContains(column.getColumnName())) || alias!=null) {
						return new ColumnProperties(column.getColumnName(), table);
					}											
				}
				//subquery veya with query ise
				else {
					PlainSelect subQuery=null;
					
					if(withQueryOfTable==null) subQuery=((ParenthesedSelect)fromItem).getPlainSelect();
					else subQuery=withQueryOfTable;
					
					Column foundColumn =  findColumnInSelect(column.getColumnName(), subQuery);
					if(foundColumn!=null) return getTableOfColumn(this.allNodes.get(foundColumn), this.allNodes.get(subQuery),visitedSelectNodes);
				}								
			}			
		}
		
		//en son üst select cümlelerinde ara, exist veya where koşulunda subquery içinde üstten gelen kolon kullanılmışsa
		//Node parent = select.getASTNode().jjtGetParent();		
		SqlTree parent=selectNode.getParent();		
		if(parent!=null) {
			SqlTree parentSelectNode= findSelect(parent);
			if(parentSelectNode!=null) {
				return getTableOfColumn(columnNode, parentSelectNode,visitedSelectNodes);
			}
		}		
		return null;
		
	}	
	private Column findColumnInSelect(String columnName,PlainSelect select) {
		for(SelectItem selectItem:select.getSelectItems()) {
			if(
					(selectItem.getExpression() instanceof Column)
					&&
					(
						selectItem.getAlias()!=null && selectItem.getAlias().getName().equals(columnName) 
					    || ((Column)selectItem.getExpression()).getColumnName().equals(columnName)
					)
			  ) {				
					return (Column)selectItem.getExpression();
			 }
			
		}
		return null;
	}
	private ColumnLocation getColumnLocation(SqlTree columnNode,SqlTree currentNode, BinaryExpression binaryExpression) {
		/*
		 select
		 from
		 join
		 where
		 group by
		 having
		 order by
		 update
		 merge
		 delete
		 insert
		 */
		
		
		if(currentNode==null) {
			if(columnNode.getParent()!=null) currentNode=columnNode.getParent();
			else return null;
		}
		
		if(binaryExpression==null && currentNode.getData()!=null && currentNode.getData() instanceof BinaryExpression) binaryExpression=(BinaryExpression)currentNode.getData();
				
		String location=null;
		
		if(currentNode.getData().equals("SELECT_ITEMS")) location="SELECT";
		else if(currentNode.getData().equals("FROM")) location="FROM";		
		else if(currentNode.getData().equals("JOIN_ON_EXP")) location="JOIN_ON_EXP";
		else if(currentNode.getData().equals("WHERE")) location="WHERE";
		else if(currentNode.getData().equals("HAVING")) location="HAVING";
		else if(currentNode.getData().equals("HIERARCHICAL")) location="HIERARCHICAL";
		else if(currentNode.getData().equals("ORDER_BY")) location="ORDER_BY";
		else if(currentNode.getParent()!=null) return getColumnLocation(columnNode,currentNode.getParent(),binaryExpression);
		
		return new ColumnLocation(location, binaryExpression);		
	}
	
	private void createTables(String connectionSchema) {
		this.tables=new HashMap<Table, DBUtil.Table>();
		for(SqlTree node: tableNodes) {
			Table table=(Table)node.getData();
			DBUtil.Table dbTable=this.dbUtil.searchTable(connectionSchema,table.getSchemaName(), table.getName());
			if(dbTable!=null) {
				this.tables.putIfAbsent(table, dbTable);
			}
		}
	}
	private void starColumnReplace() {
		HashMap<SqlTree, List<Column>> starColumns=new HashMap<SqlTree, List<Column>>();
		
		for(SqlTree node:columns) {
			if(node.getData() instanceof AllColumns) {
				List<Column> columns=getAllColumnsofStar(node);
				starColumns.put(node, columns);
			}
		}
		
		for(SqlTree node : starColumns.keySet()) {
			
			Object data = node.getData();
			
			SqlTree selectNode=getPlainSelectOfNode(node);
			List<SelectItem<?>> selectItemList = ((PlainSelect)selectNode.getData()).getSelectItems();
			List<Column> columnList=starColumns.get(node);
			
			for(int i=0; i<selectItemList.size(); i++) {
				if(selectItemList.get(i).getExpression() == node.getData()) {
					for(int k=0;k<columnList.size();k++) {
						SelectItem<Column> newSelectItem=new SelectItem<Column>(columnList.get(k));
						if(k==0) {
							selectItemList.set(i, newSelectItem);
							node.setData(columnList.get(k));
							this.allNodes.put(columnList.get(k), node);
						}
						else {
							selectItemList.add(i+k, newSelectItem);
							SqlTree newColumnNode = node.getParent().addChild(columnList.get(k));
							this.columns.add(newColumnNode);
							this.allNodes.put(newColumnNode.getData(), newColumnNode);	
						}						
					}
				}
			}
			this.allNodes.remove(data);			
		}
		
	}
	private void setColumnTablesAndLocations() {
		this.columnsTables=new HashMap<SqlTree, SqlTreeUtil.ColumnProperties>();
		this.columnLocations=new HashMap<SqlTree, SqlTreeUtil.ColumnLocation>();
		
		for(SqlTree columnNode:this.columns) {								
			ColumnProperties columnProperties=getTableOfColumn(columnNode,null,null);
			this.columnsTables.putIfAbsent(columnNode, columnProperties);
			
			ColumnLocation location=getColumnLocation(columnNode, null, null);			
			this.columnLocations.putIfAbsent(columnNode, location);			
		}
	}
	private void createFullColumnImpact() {	
		this.fullColumnImpactList=new HashSet<SqlTreeUtil.FullColumnImpact>(); 
		for(SqlTree columnNode : this.columnLocations.keySet()) {
			ColumnLocation location=this.columnLocations.get(columnNode);
			ColumnProperties columnProperties = this.columnsTables.get(columnNode);
			if(columnProperties!=null) {
				FullColumnImpact impact=new FullColumnImpact(columnProperties.getColumnName(), columnProperties.getTable().getRealOwnerName(), columnProperties.getTable().getRealTableName(), location.getLocation(), location.binaryExpressionToString());
				this.fullColumnImpactList.add(impact);
			}
			else {
				System.out.println("kolon bilgileri yok : "+columnNode.getData().toString());
			}
		}		
	}
	private void createTableImpact() {
		this.tableImpactList=new HashSet<SqlTreeUtil.TableImpact>();
		for(Table table:this.tables.keySet()) {
			TableImpact tableImpact=new TableImpact(this.tables.get(table).getRealOwnerName(), this.tables.get(table).getRealTableName());
			this.tableImpactList.add(tableImpact);
		}
	}
	
	public void parse(String sql, String connectionSchema, DBUtil dbUtil) throws JSQLParserException {
		sql=sql.toUpperCase();
		connectionSchema=connectionSchema.toUpperCase();
		Statement st=CCJSqlParserUtil.parse(sql);
		
		SqlTreeCreator s=new SqlTreeCreator();
		s.createSqlTree(st);
		this.allNodes=s.getAllNodes();
		this.columns=s.getColumns();
		this.dbUtil=dbUtil;
		this.tableNodes=s.getTables();
		
		createTables(connectionSchema);
		starColumnReplace();
		setColumnTablesAndLocations();
		createFullColumnImpact();
		createTableImpact();
	}
	
	
	class TableImpact {
		private String tableOwner;
		private String tableName;
		public String getTableOwner() {return tableOwner;}
		public void setTableOwner(String tableOwner) {this.tableOwner = tableOwner;}
		public String getTableName() {return tableName;}
		public void setTableName(String tableName) {this.tableName = tableName;}
		public TableImpact(String tableOwner, String tableName) {
			super();
			this.tableOwner = tableOwner;
			this.tableName = tableName;
		}
		@Override
		public int hashCode() {			
			return Objects.hash(this.tableOwner,this.tableName);
		}
		private boolean compareObjects(Object obj1,Object obj2) {
			if(obj1==null && obj2==null) return true;
			else if(obj1==null || obj2==null) return false;
			else return obj1.equals(obj2);
		}
		@Override
		public boolean equals(Object obj) {
			TableImpact table=(TableImpact)obj;
			return compareObjects(this.tableOwner, table.getTableOwner()) && compareObjects(this.tableName, table.getTableName()); 			
		}
		@Override
		public String toString() {
			return "TableImpact [tableOwner=" + tableOwner + ", tableName=" + tableName + "]";
		}
		
		
	}
	class FullColumnImpact {
		private String columnName;
		private String tableOwner;
		private String tableName;
		private String location;
		private String binaryExpression;
		public String getColumnName() {
			return columnName;
		}
		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}
		public String getTableOwner() {
			return tableOwner;
		}
		public void setTableOwner(String tableOwner) {
			this.tableOwner = tableOwner;
		}
		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getBinaryExpression() {
			return binaryExpression;
		}
		public void setBinaryExpression(String binaryExpression) {
			this.binaryExpression = binaryExpression;
		}
		public FullColumnImpact(String columnName, String tableOwner, String tableName, String location,
				String binaryExpression) {
			super();
			this.columnName = columnName;
			this.tableOwner = tableOwner;
			this.tableName = tableName;
			this.location = location;
			this.binaryExpression = binaryExpression;
		}
		@Override
		public int hashCode() {			
			return Objects.hash(this.columnName,this.tableOwner,this.tableName,this.location,this.binaryExpression);
		}
		
		private boolean compareObjects(Object obj1,Object obj2) {
			if(obj1==null && obj2==null) return true;
			else if(obj1==null || obj2==null) return false;
			else return obj1.equals(obj2);
		}
		
		@Override
		public boolean equals(Object obj) {
			FullColumnImpact obj2=(FullColumnImpact) obj;
			return compareObjects(this.columnName,obj2.getColumnName())
				&& compareObjects(this.tableOwner,obj2.getTableOwner())
				&& compareObjects(this.tableName,obj2.getTableName())
				&& compareObjects(this.location,obj2.getLocation())
				&& compareObjects(this.binaryExpression,obj2.getBinaryExpression());				
		}
		@Override
		public String toString() {
			return "FullColumnImpact [columnName=" + columnName + ", tableOwner=" + tableOwner + ", tableName="
					+ tableName + ", location=" + location + ", binaryExpression=" + binaryExpression + "]";
		}
		
		
		
		
	}
	
	class ColumnProperties {
		
		private String columnName;
		private DBUtil.Table table;
		
		
		public ColumnProperties() {
			super();
		}
		public ColumnProperties(String columnName, DBUtil.Table table) {
			super();
			this.columnName = columnName;
			this.table = table;
		}

		@Override
		public String toString() {
			return "ColumnProperties [columnName=" + columnName + ", table=" + table + "]";
		}
		
		
		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public DBUtil.Table getTable() {
			return table;
		}
		public void setTable(DBUtil.Table table) {
			this.table = table;
		}
	
	}
	class ColumnLocation{
		private String location;
		private BinaryExpression binaryExpression;
		
		public String getLocation() { return location; }
		public void setLocation(String location) { this.location = location; }
		public BinaryExpression getBinaryExpression() { return binaryExpression; }
		public void setBinaryExpression(BinaryExpression binaryExpression) { this.binaryExpression = binaryExpression; }
		
		public ColumnLocation(String location, BinaryExpression binaryExpression) {
			super();
			this.location = location;
			this.binaryExpression = binaryExpression;
		}
		public ColumnLocation() {}
		
		@Override
		public String toString() {
			return "ColumnLocation [location=" + location + ", binaryExpression=" + binaryExpression + "]";
		}
		
		public String binaryExpressionToString() {
			
			int threshold=1000;
			
			if(this.binaryExpression!=null) {
				String left=this.binaryExpression.getLeftExpression().toString();
				String right=this.binaryExpression.getRightExpression().toString();
				if(this.binaryExpression.getLeftExpression().toString().length()>=threshold) left=this.binaryExpression.getLeftExpression().toString().substring(0, threshold-3)+"...";
				if(this.binaryExpression.getRightExpression().toString().length()>=threshold) right=this.binaryExpression.getRightExpression().toString().substring(0, threshold-3)+"...";
				return left + " " + this.binaryExpression.getStringExpression() + " " + right; 
			}
			else return null;
		}
		
	}
	
}
