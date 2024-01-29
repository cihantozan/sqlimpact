package sqlimpact.util;

import java.util.ArrayList;
import java.util.List;

public class SqlTree {

	private SqlTree parent;
	private List<SqlTree> childList;	
	private Object data;
	private Object extraData;
	
	private SqlTreeListener sqlTreeListener; 
	
	public SqlTree getParent() {
		return parent;
	}
	public void setParent(SqlTree parent) {
		this.parent = parent;
	}
	public List<SqlTree> getChildList() {
		return childList;
	}
	public void setChildList(List<SqlTree> childList) {
		this.childList = childList;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public SqlTreeListener getSqlTreeListener() {
		return sqlTreeListener;
	}
	public void setSqlTreeListener(SqlTreeListener sqlTreeListener) {
		this.sqlTreeListener = sqlTreeListener;
	}	
	public Object getExtraData() {
		return extraData;
	}
	public void setExtraData(Object extraData) {
		this.extraData = extraData;
	}
	
	public SqlTree() {		
	}
	public SqlTree(SqlTree parent, Object data) {
		super();
		this.parent = parent;
		this.data = data;
	}
	
	
	public void addChild(SqlTree child)  {
		if(this.childList==null) this.childList=new ArrayList<SqlTree>();		
		this.childList.add(child);
		if(sqlTreeListener!=null) this.sqlTreeListener.onSqlTreeChildAdd(child);
	}
	public SqlTree addChild(Object data) {
		SqlTree child=new SqlTree(this, data);
		child.setSqlTreeListener(this.sqlTreeListener);
		this.addChild(child);
		return child;
	}
	
	
	@Override
	public String toString() {
		return "SqlTree [" + data + "]";
	}
	
	
	
}
