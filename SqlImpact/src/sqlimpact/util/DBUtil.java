package sqlimpact.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import org.w3c.dom.css.ViewCSS;



public class DBUtil {
	
	private TableList tableList;
	private ViewNameList viewList;
	
	public TableList getTableList() {
		return tableList;
	}
	public ViewNameList getViewList() {
		return viewList;
	}


	public DBUtil() {
		super();
	}
	
	public Table searchTable(String connectionSchema,String owner,String tableName) {
		
		DBUtil.Table table=null;
		
		DBUtil.SchemaList schemaList = tableList.getSchemaList(tableName);
		if(schemaList!=null) {			
			if(owner==null) {
				table = schemaList.getTable("PUBLIC");
				if(table==null) {
					table = schemaList.getTable(connectionSchema);
				}
			}
			else {
				table = schemaList.getTable(owner);
			}
			
			if(table!=null) {				
				return table;				
			}
		}
		return null;
	}
	
	public View searchView(String connectionSchema, String owner, String viewName) {
		View view=null;
		
		ViewSchemaList viewSchemaList=viewList.getViewSchemaList(viewName);
		if(viewSchemaList!=null) {
			if(owner==null) {
				view=viewSchemaList.getView(connectionSchema);
			}
			else {
				view=viewSchemaList.getView(owner);
			}			
		}
		return view;
	}
	
	public void getMetadataFromDB() throws ClassNotFoundException, SQLException {
		tableList=new TableList();
		String query="SELECT owner,table_name,owner real_owner,table_name real_table_name,column_name\n"
				+ "FROM all_tab_columns a\n"
				+ "JOIN all_users b ON b.USERNAME = a.OWNER AND b.ORACLE_MAINTAINED <> 'Y'\n"
				+ "UNION ALL\n"
				+ "SELECT a.owner,a.synonym_name table_name, a.table_owner real_owner, a.table_name real_table_name, c.column_name\n"
				+ "FROM all_synonyms a\n"
				+ "JOIN all_users b ON b.USERNAME = a.TABLE_OWNER AND b.ORACLE_MAINTAINED <> 'Y'\n"
				+ "JOIN all_tab_columns c ON c.owner=a.table_owner AND c.table_name=a.table_name";

		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con=DriverManager.getConnection("jdbc:oracle:thin:@//172.17.0.2:1521/ORCLPDB1","USER1","cihan123");
		Statement stmt=con.createStatement();			
		ResultSet rs=stmt.executeQuery(query);
		while(rs.next()) {
			String owner=rs.getString(1);
			String tableName=rs.getString(2);
			String realOwner=rs.getString(3);
			String realTableName=rs.getString(4);
			String columnName=rs.getString(5);
			
			tableList.add(owner, tableName, realOwner, realTableName, columnName);
			
			
		}
		rs.close();
		stmt.close();
		con.close();
				
	}
	public void getMetadataFromFile(String fileName) throws FileNotFoundException {
		tableList=new TableList();
		Scanner scanner = new Scanner(new File(fileName),"windows-1254");
		scanner.useDelimiter("\r\n");
		String rowStr;
		String[] columns;
		while(scanner.hasNext()) {
			rowStr=scanner.next();
			columns=rowStr.split(";");
			tableList.add(columns[0], columns[1], columns[2], columns[3], columns[4]);
		}
		scanner.close();
	}
	
	public void getViewListFromFile(String fileName) throws FileNotFoundException {
		this.viewList=new ViewNameList();
		
		Scanner scanner = new Scanner(new File(fileName),"windows-1254");
		scanner.useDelimiter("½");
		String rowStr;
		String[] columns;
		while(scanner.hasNext()) {
			rowStr=scanner.next();
			columns=rowStr.toUpperCase().split("¼");
			viewList.add(columns[0], columns[1], columns[2]);
		}
		scanner.close();
	}
	
	class ViewNameList {
		private HashMap<String, ViewSchemaList> viewNameList;

		public HashMap<String, ViewSchemaList> getViewNameList() {
			return viewNameList;
		}

		public void setViewNameList(HashMap<String, ViewSchemaList> viewNameList) {
			this.viewNameList = viewNameList;
		}

		@Override
		public String toString() {
			return "ViewNameList [viewNameList=" + viewNameList + "]";
		}

		public void add(String owner, String viewName, String sql) {
			if(viewNameList==null) viewNameList=new HashMap<String, DBUtil.ViewSchemaList>();
			
			if(!viewNameList.containsKey(viewName)) {
				ViewSchemaList viewSchemaList=new ViewSchemaList();
				viewSchemaList.add(owner, viewName, sql);
				viewNameList.put(viewName, viewSchemaList);
			}
			
		}
		public ViewSchemaList getViewSchemaList(String viewName) {
			return viewNameList.get(viewName);
		}
		
		
	}
	class ViewSchemaList {
		private HashMap<String, View> schemaList;

		public HashMap<String, View> getSchemaList() {
			return schemaList;
		}

		public void setSchemaList(HashMap<String, View> schemaList) {
			this.schemaList = schemaList;
		}

		@Override
		public String toString() {
			return "ViewSchemaList [schemaList=" + schemaList + "]";
		}
		
		public void add(String owner,String viewName,  String sql) {
			if(schemaList==null) schemaList=new HashMap<String, DBUtil.View>();
			
			if(!schemaList.containsKey(owner)) {
				schemaList.put(owner, new View(owner, viewName, sql));
			}
			
		}
		public View getView(String owner) {
			return schemaList.get(owner);
		}
		
	}
	class View {
		private String owner;
		private String viewName;
		private String sql;
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public String getViewName() {
			return viewName;
		}
		public void setViewName(String viewName) {
			this.viewName = viewName;
		}
		public String getSql() {
			return sql;
		}
		public void setSql(String sql) {
			this.sql = sql;
		}
		public View(String owner, String viewName, String sql) {
			super();
			this.owner = owner;
			this.viewName = viewName;
			this.sql = sql;
		}
		@Override
		public String toString() {
			return "View [owner=" + owner + ", viewName=" + viewName + "]";
		}
		
		
	}
	
	class Table {
		private String owner;
		private String tableName;
		private String realOwnerName;
		private String realTableName;
		private HashSet<String> columnList;
		
		public boolean columnContains(String columnName) {
			return columnList.contains(columnName.toUpperCase());			
		}
		public void addColumn(String columnName) {
			if(columnList==null) columnList=new HashSet<String>();
			if(! columnList.contains(columnName)) columnList.add(columnName);
		}

		public Table() {
			super();
		}

		

		public Table(String owner, String tableName, String realOwnerName, String realTableName,
				HashSet<String> columnList) {
			super();
			this.owner = owner;
			this.tableName = tableName;
			this.realOwnerName = realOwnerName;
			this.realTableName = realTableName;
			this.columnList = columnList;
		}
		
		public Table(String owner, String tableName, String realOwnerName, String realTableName) {
			super();
			this.owner = owner;
			this.tableName = tableName;
			this.realOwnerName = realOwnerName;
			this.realTableName = realTableName;
		}

		public String getOwner() {
			return owner;
		}

		public void setOwner(String owner) {
			this.owner = owner;
		}

		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		public String getRealTableName() {
			return realTableName;
		}

		public void setRealTableName(String realTableName) {
			this.realTableName = realTableName;
		}				

		public String getRealOwnerName() {
			return realOwnerName;
		}

		public void setRealOwnerName(String realOwnerName) {
			this.realOwnerName = realOwnerName;
		}

		public HashSet<String> getColumnList() {
			return columnList;
		}

		public void setColumnList(HashSet<String> columnList) {
			this.columnList = columnList;
		}
		@Override
		public String toString() {
			return "Table [owner=" + owner + ", tableName=" + tableName + ", realOwnerName=" + realOwnerName
					+ ", realTableName=" + realTableName + ", columnList=" + columnList + "]";
		}

		
		
		
	}
	class SchemaList {
		
		private HashMap<String,Table> list;
		
		public SchemaList() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		public void add(String owner,String tableName,String realOwner,String realTableName,String columnName) {
			
			
			if(list==null) list=new HashMap<String, DBUtil.Table>();
			
			if(! list.containsKey(owner)) {
				Table table=new Table(owner, tableName, realOwner, realTableName);
				table.addColumn(columnName);
				list.put(owner, table);
			}
			else {
				list.get(owner).addColumn(columnName);
			}
		}
		public Table getTable(String owner) {
			return list.get(owner.toUpperCase());
		}

		

		public HashMap<String, Table> getList() {
			return list;
		}

		public void setList(HashMap<String, Table> list) {
			this.list = list;
		}

		

		
		
		
		
	}
	class TableList {
		
		private HashMap<String,SchemaList> list;
		
		public SchemaList getSchemaList(String tableName) {
			return list.get(tableName.toUpperCase());
		}
		public void add(String owner,String tableName,String realOwner,String realTableName,String columnName) {

			if(list==null) list=new HashMap<String, DBUtil.SchemaList>();
			
			if(! list.containsKey(tableName)) {
				SchemaList schemaList=new SchemaList();
				schemaList.add(owner, tableName, realOwner, realTableName, columnName);
				list.put(tableName, schemaList);
			}
			else {
				list.get(tableName).add(owner, tableName, realOwner, realTableName, columnName);
			}

		}
		
		
		public HashMap<String, SchemaList> getList() {
			return list;
		}
		public void setList(HashMap<String, SchemaList> list) {
			this.list = list;
		}
		public TableList(HashMap<String, DBUtil.SchemaList> list) {
			super();
			this.list = list;
		}
		public TableList() {
			super();
			// TODO Auto-generated constructor stub
		}
		@Override
		public String toString() {
			return "TableList [list=" + list + "]";
		}
		
		
	}
	

}
