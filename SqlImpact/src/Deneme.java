import java.io.FileNotFoundException;

import net.sf.jsqlparser.JSQLParserException;
import sqlimpact.util.DBUtil;
import sqlimpact.util.SqlTreeUtil;

public class Deneme {
public static void main(String[] args) throws FileNotFoundException, JSQLParserException {
		
		String sql="select * from user1.table2 where column2='a' and column2>1";
		
		DBUtil dbutil=new DBUtil();
		dbutil.getMetadataFromFile("/home/cihan/dbmetadata.csv");
		
		SqlTreeUtil sqlTreeUtil=new SqlTreeUtil();
		sqlTreeUtil.parse(sql, "USER1", dbutil);
				
		
		System.out.println("finished");

	}
}
