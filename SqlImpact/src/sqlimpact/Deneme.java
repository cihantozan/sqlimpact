package sqlimpact;

import java.io.FileNotFoundException;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import sqlimpact.util.DBUtil;
import sqlimpact.util.SqlTreeUtil;


public class Deneme {

	public static void main(String[] args) throws JSQLParserException, FileNotFoundException {
		
		String sql="WITH t3 AS (select column1 c1,column2 c2 from table20)\n"
				+ "SELECT *\n"
				+ "FROM \n"
				+ "(\n"
				+ "	with t1 AS (SELECT DISTINCT COLUMN1,COLUMN2 FROM USER1.TABLE2)\n"
				+ "	SELECT q1 AS w1,q2 AS w2,q3 AS w3\n"
				+ "	FROM\n"
				+ "	(\n"
				+ "		with t2 AS (SELECT A FROM USER1.TABLE1,t3 WHERE a=c2 OR 1=1)\n"
				+ "		SELECT a as q1,column1 AS q2,column2 AS q3\n"
				+ "		FROM t2, t1\n"
				+ "	) a\n"
				+ "	WHERE NOT EXISTS (SELECT 1 FROM user2.table3 WHERE a=a.q3)	\n"
				+ "	AND (SELECT count(*) FROM user1.TABLE1 WHERE table1.a=a.q2 OR 1=1)>=1\n"
				+ ") \n"
				+ "order by tarih,(select qwerty from dual)";
		
		DBUtil dbUtil=new DBUtil();
		//dbUtil.getMetadata();
		dbUtil.getMetadataFromFile("/home/cihan/dbmetadata.csv");
		
		SqlTreeUtil sqlTreeUtil=new SqlTreeUtil();
		sqlTreeUtil.parse(sql, "USER1", dbUtil);
				
		
		System.out.println("finished");

	}

}
