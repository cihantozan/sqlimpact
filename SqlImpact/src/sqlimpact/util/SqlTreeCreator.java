/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package sqlimpact.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.AllValue;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.ArrayConstructor;
import net.sf.jsqlparser.expression.ArrayExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.CollateExpression;
import net.sf.jsqlparser.expression.ConnectByRootOperator;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.JsonAggregateFunction;
import net.sf.jsqlparser.expression.JsonExpression;
import net.sf.jsqlparser.expression.JsonFunction;
import net.sf.jsqlparser.expression.JsonFunctionExpression;
import net.sf.jsqlparser.expression.KeepExpression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.MySQLGroupConcat;
import net.sf.jsqlparser.expression.NextValExpression;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.NumericBind;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.expression.OracleHint;
import net.sf.jsqlparser.expression.OracleNamedFunctionParameter;
import net.sf.jsqlparser.expression.OverlapsCondition;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.RangeExpression;
import net.sf.jsqlparser.expression.RowConstructor;
import net.sf.jsqlparser.expression.RowGetExpression;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeKeyExpression;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.TimezoneExpression;
import net.sf.jsqlparser.expression.TranscodingFunction;
import net.sf.jsqlparser.expression.TrimFunction;
import net.sf.jsqlparser.expression.UserVariable;
import net.sf.jsqlparser.expression.VariableAssignment;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.XMLSerializeExpr;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.IntegerDivision;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.conditional.XorExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.ContainedBy;
import net.sf.jsqlparser.expression.operators.relational.Contains;
import net.sf.jsqlparser.expression.operators.relational.DoubleAnd;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.FullTextSearch;
import net.sf.jsqlparser.expression.operators.relational.GeometryDistance;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsBooleanExpression;
import net.sf.jsqlparser.expression.operators.relational.IsDistinctExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.JsonOperator;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MemberOfExpression;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.SimilarToExpression;
import net.sf.jsqlparser.expression.operators.relational.TSQLLeftJoin;
import net.sf.jsqlparser.expression.operators.relational.TSQLRightJoin;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Block;
import net.sf.jsqlparser.statement.Commit;
import net.sf.jsqlparser.statement.CreateFunctionalStatement;
import net.sf.jsqlparser.statement.DeclareStatement;
import net.sf.jsqlparser.statement.DescribeStatement;
import net.sf.jsqlparser.statement.ExplainStatement;
import net.sf.jsqlparser.statement.IfElseStatement;
import net.sf.jsqlparser.statement.PurgeObjectType;
import net.sf.jsqlparser.statement.PurgeStatement;
import net.sf.jsqlparser.statement.ResetStatement;
import net.sf.jsqlparser.statement.RollbackStatement;
import net.sf.jsqlparser.statement.SavepointStatement;
import net.sf.jsqlparser.statement.SetStatement;
import net.sf.jsqlparser.statement.ShowColumnsStatement;
import net.sf.jsqlparser.statement.ShowStatement;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.UnsupportedStatement;
import net.sf.jsqlparser.statement.UseStatement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterSession;
import net.sf.jsqlparser.statement.alter.AlterSystemStatement;
import net.sf.jsqlparser.statement.alter.RenameTableStatement;
import net.sf.jsqlparser.statement.alter.sequence.AlterSequence;
import net.sf.jsqlparser.statement.analyze.Analyze;
import net.sf.jsqlparser.statement.comment.Comment;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.schema.CreateSchema;
import net.sf.jsqlparser.statement.create.sequence.CreateSequence;
import net.sf.jsqlparser.statement.create.synonym.CreateSynonym;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.grant.Grant;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.refresh.RefreshMaterializedViewStatement;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.ParenthesedFromItem;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.TableFunction;
import net.sf.jsqlparser.statement.select.TableStatement;
import net.sf.jsqlparser.statement.select.Values;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.show.ShowIndexStatement;
import net.sf.jsqlparser.statement.show.ShowTablesStatement;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;

/**
 * Find all used tables within an select statement.
 *
 * <p>
 * Override extractTableName method to modify the extracted table names (e.g. without schema).
 */
@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.UncommentedEmptyMethodBody"})
public class SqlTreeCreator implements SelectVisitor, FromItemVisitor, ExpressionVisitor,
        SelectItemVisitor, StatementVisitor, SqlTreeListener {

    private static final String NOT_SUPPORTED_YET = "Not supported yet.";

    private SqlTree sqlTree;
    private SqlTree currentNode;
    private List<SqlTree> tables;
    private List<SqlTree> columns;
    
    
    
    private HashMap<Object, SqlTree> allNodes;
    

    public SqlTree getSqlTree() {
		return sqlTree;
	}	
	public List<SqlTree> getTables() {
		return tables;
	}	
	public List<SqlTree> getColumns() {
		return columns;
	}		
	public HashMap<Object, SqlTree> getAllNodes() {
		return allNodes;
	}
	
	
	
	public void createSqlTree(Statement statement) {
		
		allNodes=new HashMap<Object, SqlTree>();
		
    	sqlTree=new SqlTree(null,null);
    	this.tables=new ArrayList<SqlTree>();
    	this.columns=new ArrayList<SqlTree>();
    	
    	sqlTree.setSqlTreeListener(this);
    	currentNode=sqlTree;
        init();
        statement.accept(this);        
    }
    
    
	@Override
	public void onSqlTreeChildAdd(SqlTree node)  {
		if(!allNodes.containsKey(node.getData()) ) {
			allNodes.put(node.getData(), node);
		}
		else {
			String a="false";
			if(node.getData()==node.getParent().getData()) a="true";
			
			if(node.getData().getClass()!=String.class && node.getData().getClass()!=StringValue.class && node.getData().getClass()!=LongValue.class) {
				System.out.println(a+" - "+node.getData().getClass().toString()+ "-" + node.getData().toString());				
			}
		}
		
		
	}
  

    @Override
    public void visit(Select select) {
    	
		/*
		 * SqlTree newNode=new SqlTree(currentNode, select);
		 * currentNode.addChild(newNode); SqlTree backupCurrentNode=currentNode;
		 * currentNode=newNode;
		 * 
		 * List<WithItem> withItemsList = select.getWithItemsList(); if (withItemsList
		 * != null && !withItemsList.isEmpty()) { for (WithItem withItem :
		 * withItemsList) { withItem.accept((SelectVisitor) this); } }
		 * select.accept((SelectVisitor) this);
		 * 
		 * currentNode=backupCurrentNode;
		 */
    	
    	select.accept((SelectVisitor) this);
    }

    @Override
    public void visit(TranscodingFunction transcodingFunction) {
        transcodingFunction.getExpression().accept(this);
    }

    @Override
    public void visit(TrimFunction trimFunction) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(trimFunction);
    	
    	
        if (trimFunction.getExpression() != null) {
            trimFunction.getExpression().accept(this);
        }
        if (trimFunction.getFromExpression() != null) {
            trimFunction.getFromExpression().accept(this);
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(RangeExpression rangeExpression) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(rangeExpression);
    	
        rangeExpression.getStartExpression().accept(this);
        rangeExpression.getEndExpression().accept(this);
        
        currentNode=backupCurrentNode;
    }

    /**
     * Main entry for this Tool class. A list of found tables is returned.
     */
    
   

    @Override
    public void visit(WithItem withItem) {        
        withItem.getSelect().accept((SelectVisitor) this);
    }

    @Override
    public void visit(ParenthesedSelect selectBody) {
        List<WithItem> withItemsList = selectBody.getWithItemsList();
        if (withItemsList != null && !withItemsList.isEmpty()) {
            for (WithItem withItem : withItemsList) {
                withItem.accept((SelectVisitor) this);
            }
        }
        selectBody.getSelect().accept((SelectVisitor) this);
    }

    @Override
    public void visit(PlainSelect plainSelect) {
    	
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(plainSelect);
    	SqlTree plainselectNode=currentNode;
    	
    	
        List<WithItem> withItemsList = plainSelect.getWithItemsList();
        if (withItemsList != null && !withItemsList.isEmpty()) {
        	currentNode=plainselectNode.addChild("WITH_ITEMS");
            for (WithItem withItem : withItemsList) {            	
                withItem.accept((SelectVisitor) this);
            }
        }
        if (plainSelect.getSelectItems() != null) {
        	currentNode=plainselectNode.addChild("SELECT_ITEMS");
            for (SelectItem<?> item : plainSelect.getSelectItems()) {            	
                item.accept(this);
            }
        }

        if (plainSelect.getFromItem() != null) {
        	currentNode=plainselectNode.addChild("FROM");
            plainSelect.getFromItem().accept(this);
        }

        if (plainSelect.getJoins() != null) {
        	
        	currentNode=plainselectNode.addChild("JOIN_LIST");
        	SqlTree joinListNode=currentNode;
        	
            for (Join join : plainSelect.getJoins()) {            	
            	          
            	currentNode=joinListNode.addChild("JOIN");
            	SqlTree joinNode=currentNode;
            	
                join.getFromItem().accept(this);
                //join.getRightItem().accept(this);
                
                currentNode=joinNode.addChild("JOIN_ON_EXP");
                
                for (Expression expression : join.getOnExpressions()) {
                    expression.accept(this);
                }
                
                currentNode=plainselectNode;
            }
        }
        if (plainSelect.getWhere() != null) {
        	
        	currentNode=plainselectNode.addChild("WHERE");
        	
            plainSelect.getWhere().accept(this);
        }

        if (plainSelect.getHaving() != null) {
        	
        	currentNode=plainselectNode.addChild("HAVING");
        	
            plainSelect.getHaving().accept(this);
        }

        if (plainSelect.getOracleHierarchical() != null) {
        	
        	currentNode=plainselectNode.addChild("HIERARCHICAL");
        	
            plainSelect.getOracleHierarchical().accept(this);
        }
        
        if(plainSelect.getOrderByElements()!=null) {
        	currentNode=plainselectNode.addChild("ORDER_BY");
        	
        	for(OrderByElement element : plainSelect.getOrderByElements()) {
        		element.getExpression().accept(this);        		
        	}
        	
        }
        
        currentNode=backupCurrentNode;
    }

    /**
     * Override to adapt the tableName generation (e.g. with / without schema).
     *
     * @param table
     * @return
     */
    

    @Override
    public void visit(Table tableName) {
    	SqlTree newNode = currentNode.addChild(tableName);
    	tables.add(newNode);    	
    }

    @Override
    public void visit(Addition addition) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(addition);
    	
        visitBinaryExpression(addition);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(AndExpression andExpression) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(andExpression);
   			
        visitBinaryExpression(andExpression);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Between between) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(between);
    	
        between.getLeftExpression().accept(this);
        between.getBetweenExpressionStart().accept(this);
        between.getBetweenExpressionEnd().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(OverlapsCondition overlapsCondition) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(overlapsCondition);
    	
        overlapsCondition.getLeft().accept(this);
        overlapsCondition.getRight().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Column tableColumn) {    	
    	SqlTree newNode = currentNode.addChild(tableColumn);
    	this.columns.add(newNode);
    }

    @Override
    public void visit(Division division) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(division);
    	
        visitBinaryExpression(division);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(IntegerDivision division) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(division);
    	
        visitBinaryExpression(division);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(DoubleValue doubleValue) {
    	currentNode.addChild(doubleValue);    	    	    	
    }

    @Override
    public void visit(EqualsTo equalsTo) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(equalsTo);
    	
        visitBinaryExpression(equalsTo);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Function function) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(function);   
    	
        ExpressionList exprList = function.getParameters();
        if (exprList != null && !(exprList.size()==1 && exprList.get(0) instanceof AllColumns) ) {        	
            visit(exprList);
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(GreaterThan greaterThan) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(greaterThan);
    	
        visitBinaryExpression(greaterThan);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(greaterThanEquals);
    	
        visitBinaryExpression(greaterThanEquals);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(InExpression inExpression) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(inExpression);
    	
    	    	
        inExpression.getLeftExpression().accept(this);        
        inExpression.getRightExpression().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(FullTextSearch fullTextSearch) {

    }

    @Override
    public void visit(SignedExpression signedExpression) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(signedExpression);
    	
        signedExpression.getExpression().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {    	
    	currentNode.addChild(isNullExpression);
    }

    @Override
    public void visit(IsBooleanExpression isBooleanExpression) {
    	currentNode.addChild(isBooleanExpression);
    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {
    	currentNode.addChild(jdbcParameter);
    }

    @Override
    public void visit(LikeExpression likeExpression) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(likeExpression);

        visitBinaryExpression(likeExpression);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(ExistsExpression existsExpression) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(existsExpression);

        existsExpression.getRightExpression().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(MemberOfExpression memberOfExpression) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(memberOfExpression);

        memberOfExpression.getLeftExpression().accept(this);
        memberOfExpression.getRightExpression().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(LongValue longValue) {
    	currentNode.addChild(longValue);
    }

    @Override
    public void visit(MinorThan minorThan) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(minorThan);
    	
        visitBinaryExpression(minorThan);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(minorThanEquals);
    	
        visitBinaryExpression(minorThanEquals);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Multiplication multiplication) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(multiplication);
    	
        visitBinaryExpression(multiplication);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(notEqualsTo);
    	
        visitBinaryExpression(notEqualsTo);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(DoubleAnd doubleAnd) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(doubleAnd);
    	
        visitBinaryExpression(doubleAnd);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Contains contains) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(contains);
    	
        visitBinaryExpression(contains);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(ContainedBy containedBy) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(containedBy);
    	
        visitBinaryExpression(containedBy);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(NullValue nullValue) {
    	currentNode.addChild(nullValue);
    }

    @Override
    public void visit(OrExpression orExpression) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(orExpression);
    	    	
        visitBinaryExpression(orExpression);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(XorExpression xorExpression) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(xorExpression);
    	
        visitBinaryExpression(xorExpression);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Parenthesis parenthesis) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(parenthesis);
    	
        parenthesis.getExpression().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(StringValue stringValue) {
    	currentNode.addChild(stringValue);
    }

    @Override
    public void visit(Subtraction subtraction) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(subtraction);
    	
        visitBinaryExpression(subtraction);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(NotExpression notExpr) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(notExpr);
    	
        notExpr.getExpression().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(BitwiseRightShift expr) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(expr);
    	
        visitBinaryExpression(expr);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(BitwiseLeftShift expr) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(expr);
    	
        visitBinaryExpression(expr);
        
        //currentNode=backupCurrentNode;
    }

    public void visitBinaryExpression(BinaryExpression binaryExpression) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(binaryExpression);
    	
        binaryExpression.getLeftExpression().accept(this);
        binaryExpression.getRightExpression().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(ExpressionList<?> expressionList) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(expressionList);
    	
        for (Expression expression : expressionList) {
            expression.accept(this);
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(DateValue dateValue) {
    	currentNode.addChild(dateValue);
    }

    @Override
    public void visit(TimestampValue timestampValue) {
    	currentNode.addChild(timestampValue);
    }

    @Override
    public void visit(TimeValue timeValue) {
    	currentNode.addChild(timeValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jsqlparser.expression.ExpressionVisitor#visit(net.sf.jsqlparser.expression.
     * CaseExpression)
     */
    @Override
    public void visit(CaseExpression caseExpression) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(caseExpression);
    	
    	
        if (caseExpression.getSwitchExpression() != null) {
            caseExpression.getSwitchExpression().accept(this);
        }
        if (caseExpression.getWhenClauses() != null) {
            for (WhenClause when : caseExpression.getWhenClauses()) {
                when.accept(this);
            }
        }
        if (caseExpression.getElseExpression() != null) {
            caseExpression.getElseExpression().accept(this);
        }
        
        currentNode=backupCurrentNode;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jsqlparser.expression.ExpressionVisitor#visit(net.sf.jsqlparser.expression.WhenClause)
     */
    @Override
    public void visit(WhenClause whenClause) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(whenClause);
    	
    	
        if (whenClause.getWhenExpression() != null) {
            whenClause.getWhenExpression().accept(this);
        }
        if (whenClause.getThenExpression() != null) {
            whenClause.getThenExpression().accept(this);
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(anyComparisonExpression);
    	
        anyComparisonExpression.getSelect().accept((ExpressionVisitor) this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Concat concat) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(concat);
    	
        visitBinaryExpression(concat);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Matches matches) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(matches);
    	
        visitBinaryExpression(matches);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(bitwiseAnd);
    	
        visitBinaryExpression(bitwiseAnd);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(bitwiseOr);
    	
        visitBinaryExpression(bitwiseOr);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(bitwiseXor);
    	
        visitBinaryExpression(bitwiseXor);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(CastExpression cast) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(cast);
    	
        cast.getLeftExpression().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Modulo modulo) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(modulo);
    	
        visitBinaryExpression(modulo);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(AnalyticExpression analytic) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(analytic);
    	
    	
    	
        if (analytic.getExpression() != null) {
            analytic.getExpression().accept(this);
        }
        if (analytic.getDefaultValue() != null) {
            analytic.getDefaultValue().accept(this);
        }
        if (analytic.getOffset() != null) {
            analytic.getOffset().accept(this);
        }
        if (analytic.getKeep() != null) {
            analytic.getKeep().accept(this);
        }
        if (analytic.getFuncOrderBy() != null) {
            for (OrderByElement element : analytic.getOrderByElements()) {
                element.getExpression().accept(this);
            }
        }

        if (analytic.getWindowElement() != null) {
            analytic.getWindowElement().getRange().getStart().getExpression().accept(this);
            analytic.getWindowElement().getRange().getEnd().getExpression().accept(this);
            analytic.getWindowElement().getOffset().getExpression().accept(this);
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(SetOperationList list) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(list);
    	
    	
    	
        List<WithItem> withItemsList = list.getWithItemsList();
        if (withItemsList != null && !withItemsList.isEmpty()) {
            for (WithItem withItem : withItemsList) {
                withItem.accept((SelectVisitor) this);
            }
        }
        for (Select selectBody : list.getSelects()) {
            selectBody.accept((SelectVisitor) this);
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(ExtractExpression eexpr) {
      	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(eexpr);
    	
    	
        if (eexpr.getExpression() != null) {
            eexpr.getExpression().accept(this);
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(LateralSubSelect lateralSubSelect) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(lateralSubSelect);
    	
    	
        lateralSubSelect.getSelect().accept((SelectVisitor) this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(TableStatement tableStatement) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(tableStatement);
    	
        tableStatement.getTable().accept(this);
        
        currentNode=backupCurrentNode;
    }

    /**
     * Initializes table names collector. Important is the usage of Column instances to find table
     * names. This is only allowed for expression parsing, where a better place for tablenames could
     * not be there. For complete statements only from items are used to avoid some alias as
     * tablenames.
     *
     * @param allowColumnProcessing
     */
    protected void init() {
     
    }

    @Override
    public void visit(IntervalExpression iexpr) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(iexpr);
    	
    	
        if (iexpr.getExpression() != null) {
            iexpr.getExpression().accept(this);
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(JdbcNamedParameter jdbcNamedParameter) {
    	currentNode.addChild(jdbcNamedParameter);
    }

    @Override
    public void visit(OracleHierarchicalExpression oexpr) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(oexpr);
    	
    	
        if (oexpr.getStartExpression() != null) {
            oexpr.getStartExpression().accept(this);
        }

        if (oexpr.getConnectExpression() != null) {
            oexpr.getConnectExpression().accept(this);
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(RegExpMatchOperator rexpr) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(rexpr);
    	
    	
        visitBinaryExpression(rexpr);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(JsonExpression jsonExpr) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(jsonExpr);
    	
        if (jsonExpr.getExpression() != null) {
            jsonExpr.getExpression().accept(this);
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(JsonOperator jsonExpr) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(jsonExpr);
    	
        visitBinaryExpression(jsonExpr);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(AllColumns allColumns) {    	
    	SqlTree newNode = currentNode.addChild(allColumns);
    	this.columns.add(newNode);
    }

    @Override
    public void visit(AllTableColumns allTableColumns) {
    	//currentNode.addChild(allTableColumns);
    }

    @Override
    public void visit(AllValue allValue) {
    	currentNode.addChild(allValue);
    }

    @Override
    public void visit(IsDistinctExpression isDistinctExpression) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(isDistinctExpression);
    	
        visitBinaryExpression(isDistinctExpression);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(SelectItem item) {    	
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(item);
    	
        item.getExpression().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(UserVariable var) {
    	currentNode.addChild(var);
    }

    @Override
    public void visit(NumericBind bind) {
    	currentNode.addChild(bind);

    }

    @Override
    public void visit(KeepExpression aexpr) {
    	currentNode.addChild(aexpr);
    }

    @Override
    public void visit(MySQLGroupConcat groupConcat) {
		currentNode.addChild(groupConcat);
    }

    @Override
    public void visit(Delete delete) {
    	
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(delete);
    	SqlTree deleteNode=currentNode;
    	
    	
    	currentNode=deleteNode.addChild("TABLE");
    	
        visit(delete.getTable());

        if (delete.getUsingList() != null) {
        	
        	currentNode=deleteNode.addChild("USING");
        	
            for (Table using : delete.getUsingList()) {
                visit(using);
            }
        }

        if (delete.getJoins() != null) {
        	
        	currentNode=deleteNode.addChild("JOINS");
        	
            for (Join join : delete.getJoins()) {
                join.getFromItem().accept(this);
                //join.getRightItem().accept(this);
                
                
                currentNode=currentNode.addChild("JOIN_ON_EXP");
                
                for (Expression expression : join.getOnExpressions()) {
                    expression.accept(this);
                }
            }
        }

        currentNode=deleteNode.addChild("WHERE");
        
        if (delete.getWhere() != null) {
            delete.getWhere().accept(this);
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Update update) {
    	
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(update);
    	SqlTree updateNode=currentNode;
    	
    	
    	currentNode=updateNode.addChild("TABLE");
    	
        visit(update.getTable());
        
        
        if (update.getWithItemsList() != null) {
        	
        	currentNode=updateNode.addChild("WITH_ITEM_LIST");
        	
            for (WithItem withItem : update.getWithItemsList()) {
                withItem.accept((SelectVisitor) this);
            }
        }

        if (update.getStartJoins() != null) {
        	
        	currentNode=updateNode.addChild("START_JOINS");
        	
            for (Join join : update.getStartJoins()) {
                join.getRightItem().accept(this);
            }
        }
        if (update.getExpressions() != null) {
        	
        	currentNode=updateNode.addChild("EXPRESSIONS");
        	
            for (Expression expression : update.getExpressions()) {
                expression.accept(this);
            }
        }

        if (update.getFromItem() != null) {
        	
        	currentNode=updateNode.addChild("FROM_ITEM");
        	
            update.getFromItem().accept(this);
        }

        if (update.getJoins() != null) {
        	
        	currentNode=updateNode.addChild("JOINS");
        	
            for (Join join : update.getJoins()) {
            	
            	currentNode=currentNode.addChild("JOIN");
            	
                join.getRightItem().accept(this);
                
                currentNode=currentNode.addChild("JOIN_ON_EXP");
                
                for (Expression expression : join.getOnExpressions()) {
                    expression.accept(this);
                }
            }
        }

        if (update.getWhere() != null) {
        	
        	currentNode=updateNode.addChild("WHERE");
        	
            update.getWhere().accept(this);
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Insert insert) {
    	
      	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(insert);
    	SqlTree insertNode=currentNode;
    	
    	currentNode=insertNode.addChild("TABLE");
    	
        visit(insert.getTable());
        
        if (insert.getWithItemsList() != null) {
        	
        	currentNode=insertNode.addChild("WITH_ITEM_LIST");
        	
            for (WithItem withItem : insert.getWithItemsList()) {
                withItem.accept((SelectVisitor) this);
            }
        }
        if (insert.getSelect() != null) {
        	
        	currentNode=insertNode.addChild("SELECT");
        	
            visit(insert.getSelect());
        }
        
        currentNode=backupCurrentNode;
    }

    public void visit(Analyze analyze) {
       	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(analyze);    	
    	    	
        visit(analyze.getTable());
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Drop drop) {
      	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(drop);    	
    	 
        visit(drop.getName());
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Truncate truncate) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(truncate);    	
    	 
        visit(truncate.getTable());
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(CreateIndex createIndex) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(CreateSchema aThis) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(CreateTable create) {
    	
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(create);
    	SqlTree createNode=currentNode;
    	
    	currentNode=createNode.addChild("TABLE");
    	
        visit(create.getTable());
        
        if (create.getSelect() != null) {
        	currentNode=createNode.addChild("SELECT");
            create.getSelect().accept((SelectVisitor) this);
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(CreateView createView) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(Alter alter) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(Statements stmts) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(Execute execute) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(SetStatement set) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(ResetStatement reset) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(ShowColumnsStatement set) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(ShowIndexStatement showIndex) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(RowConstructor<?> rowConstructor) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(rowConstructor);    	
    	
        for (Expression expr : rowConstructor) {
            expr.accept(this);
        }
        
    	currentNode=backupCurrentNode;
    }

    @Override
    public void visit(RowGetExpression rowGetExpression) {
       	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(rowGetExpression);    	
    	
        rowGetExpression.getExpression().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(HexValue hexValue) {
    	currentNode.addChild(hexValue);

    }

    @Override
    public void visit(Merge merge) {
    	
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(merge);
    	SqlTree mergeNode=currentNode;
    	
    	currentNode=mergeNode.addChild("TABLE");
    	
        visit(merge.getTable());
        
        
        if (merge.getWithItemsList() != null) {
        	
        	currentNode=mergeNode.addChild("WITH_ITEM_LIST");
        	
            for (WithItem withItem : merge.getWithItemsList()) {
                withItem.accept((SelectVisitor) this);
            }
        }

        currentNode=mergeNode.addChild("FROM_ITEM");
        
        if (merge.getFromItem() != null) {
            merge.getFromItem().accept(this);
        }
        
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(OracleHint hint) {
    	currentNode.addChild(hint);
    }

    @Override
    public void visit(TableFunction tableFunction) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(tableFunction);
    	
        visit(tableFunction.getFunction());
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(AlterView alterView) {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public void visit(RefreshMaterializedViewStatement materializedView) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(materializedView);
    	
        visit(materializedView.getView());
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(TimeKeyExpression timeKeyExpression) {
    	currentNode.addChild(timeKeyExpression);
    }

    @Override
    public void visit(DateTimeLiteralExpression literal) {
    	currentNode.addChild(literal);

    }

    @Override
    public void visit(Commit commit) {
    	currentNode.addChild(commit);

    }

    @Override
    public void visit(Upsert upsert) {
    	
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(upsert);
    	SqlTree upsertNode=currentNode;
    	
    	currentNode=upsertNode.addChild("TABLE");
    	
        visit(upsert.getTable());
        
        
        if (upsert.getExpressions() != null) {
        	currentNode=upsertNode.addChild("EXPRESSIONS");
            upsert.getExpressions().accept(this);
        }
        if (upsert.getSelect() != null) {
        	currentNode=upsertNode.addChild("SELECT");
            visit(upsert.getSelect());
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(UseStatement use) {
    	currentNode.addChild(use);
    }

    @Override
    public void visit(ParenthesedFromItem parenthesis) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(parenthesis);    	
    	
        parenthesis.getFromItem().accept(this);
        
    	currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Block block) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(block);
    	
        if (block.getStatements() != null) {
            visit(block.getStatements());
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Comment comment) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(comment);
    	
    	
        if (comment.getTable() != null) {
            visit(comment.getTable());
        }
        if (comment.getColumn() != null) {
            Table table = comment.getColumn().getTable();
            if (table != null) {
                visit(table);
            }
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(Values values) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(values);    	
    	
        values.getExpressions().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(DescribeStatement describe) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(describe);    	
    	
        describe.getTable().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(ExplainStatement explain) {
    	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(explain);
    	
    	
        if (explain.getStatement() != null) {
            explain.getStatement().accept((StatementVisitor) this);
        }
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(NextValExpression nextVal) {
    	currentNode.addChild(nextVal);
    }

    @Override
    public void visit(CollateExpression col) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(col);
    	
    	
        col.getLeftExpression().accept(this);
        
    	currentNode=backupCurrentNode;
    }

    @Override
    public void visit(ShowStatement aThis) {
    		currentNode.addChild(aThis);
    }

    @Override
    public void visit(SimilarToExpression expr) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(expr);
    	
        visitBinaryExpression(expr);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(DeclareStatement aThis) {
    	currentNode.addChild(aThis);
    }

    @Override
    public void visit(Grant grant) {
    	currentNode.addChild(grant);

    }

    @Override
    public void visit(ArrayExpression array) {
    	
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(array);

    	
        array.getObjExpression().accept(this);
        if (array.getStartIndexExpression() != null) {
            array.getIndexExpression().accept(this);
        }
        if (array.getStartIndexExpression() != null) {
            array.getStartIndexExpression().accept(this);
        }
        if (array.getStopIndexExpression() != null) {
            array.getStopIndexExpression().accept(this);
        }
        
    	currentNode=backupCurrentNode;
    }

    @Override
    public void visit(ArrayConstructor array) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(array);

    	
        for (Expression expression : array.getExpressions()) {
            expression.accept(this);
        }
        
    	currentNode=backupCurrentNode;
    }

    @Override
    public void visit(CreateSequence createSequence) {
        throw new UnsupportedOperationException(
                "Finding tables from CreateSequence is not supported");
    }

    @Override
    public void visit(AlterSequence alterSequence) {
        throw new UnsupportedOperationException(
                "Finding tables from AlterSequence is not supported");
    }

    @Override
    public void visit(CreateFunctionalStatement createFunctionalStatement) {
        throw new UnsupportedOperationException(
                "Finding tables from CreateFunctionalStatement is not supported");
    }

    @Override
    public void visit(ShowTablesStatement showTables) {
        throw new UnsupportedOperationException(
                "Finding tables from ShowTablesStatement is not supported");
    }

    @Override
    public void visit(TSQLLeftJoin tsqlLeftJoin) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(tsqlLeftJoin);

    	
        visitBinaryExpression(tsqlLeftJoin);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(TSQLRightJoin tsqlRightJoin) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(tsqlRightJoin);

        visitBinaryExpression(tsqlRightJoin);
        
        //currentNode=backupCurrentNode;
    }

    @Override
    public void visit(VariableAssignment var) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(var);

        var.getVariable().accept(this);
        var.getExpression().accept(this);
        
        currentNode=backupCurrentNode;
    }

    @Override
    public void visit(XMLSerializeExpr aThis) {
    	currentNode.addChild(aThis);
    }

    @Override
    public void visit(CreateSynonym createSynonym) {
        throwUnsupported(createSynonym);
    }

    private static <T> void throwUnsupported(T type) {
        throw new UnsupportedOperationException(String.format(
                "Finding tables from %s is not supported", type.getClass().getSimpleName()));
    }

    @Override
    public void visit(TimezoneExpression aThis) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(aThis);
    	
    	
        aThis.getLeftExpression().accept(this);
        
    	currentNode=backupCurrentNode;
    }

    @Override
    public void visit(SavepointStatement savepointStatement) {
    	currentNode.addChild(savepointStatement);
    }

    @Override
    public void visit(RollbackStatement rollbackStatement) {
    	currentNode.addChild(rollbackStatement);
    }

    @Override
    public void visit(AlterSession alterSession) {
    	currentNode.addChild(alterSession);
    }

    @Override
    public void visit(JsonAggregateFunction expression) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(expression);

    	
        Expression expr = expression.getExpression();
        if (expr != null) {
            expr.accept(this);
        }

        expr = expression.getFilterExpression();
        if (expr != null) {
            expr.accept(this);
        }
        
    	currentNode=backupCurrentNode;
    }

    @Override
    public void visit(JsonFunction expression) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(expression);

    	
        for (JsonFunctionExpression expr : expression.getExpressions()) {
            expr.getExpression().accept(this);
        }
        
    	currentNode=backupCurrentNode;
    }

    @Override
    public void visit(ConnectByRootOperator connectByRootOperator) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(connectByRootOperator);

    	
        connectByRootOperator.getColumn().accept(this);
        
    	currentNode=backupCurrentNode;
    }

    public void visit(IfElseStatement ifElseStatement) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(ifElseStatement);

    	
        ifElseStatement.getIfStatement().accept(this);
        if (ifElseStatement.getElseStatement() != null) {
            ifElseStatement.getElseStatement().accept(this);
        }
        
    	currentNode=backupCurrentNode;
    }

    public void visit(OracleNamedFunctionParameter oracleNamedFunctionParameter) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(oracleNamedFunctionParameter);

        oracleNamedFunctionParameter.getExpression().accept(this);
        
    	currentNode=backupCurrentNode;
    }

    @Override
    public void visit(RenameTableStatement renameTableStatement) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(renameTableStatement);

    	
        for (Map.Entry<Table, Table> e : renameTableStatement.getTableNames()) {
            e.getKey().accept(this);
            e.getValue().accept(this);
        }
        
    	currentNode=backupCurrentNode;
    }

    @Override
    public void visit(PurgeStatement purgeStatement) {
     	SqlTree backupCurrentNode=currentNode;    	
    	currentNode=backupCurrentNode.addChild(purgeStatement);

    	
        if (purgeStatement.getPurgeObjectType() == PurgeObjectType.TABLE) {
            ((Table) purgeStatement.getObject()).accept(this);
        }
        
    	currentNode=backupCurrentNode;
    }

    @Override
    public void visit(AlterSystemStatement alterSystemStatement) {
        // no tables involved in this statement
    	currentNode.addChild(alterSystemStatement);
    }

    @Override
    public void visit(UnsupportedStatement unsupportedStatement) {
        // no tables involved in this statement
    	currentNode.addChild(unsupportedStatement);
    }

    @Override
    public void visit(GeometryDistance geometryDistance) {
    	//SqlTree backupCurrentNode=currentNode;    	
    	//currentNode=backupCurrentNode.addChild(geometryDistance);

        visitBinaryExpression(geometryDistance);
        
        //currentNode=backupCurrentNode;
    }







}
