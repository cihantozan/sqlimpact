import java.io.FileNotFoundException;
import java.sql.SQLException;

import net.sf.jsqlparser.JSQLParserException;
import sqlimpact.util.DBUtil;
import sqlimpact.util.SqlTreeUtil;

public class Deneme {
public static void main(String[] args) throws FileNotFoundException, JSQLParserException, ClassNotFoundException, SQLException {
		
		String sql="UPDATE user1.TABLE1 a\n"
				+ "SET a.b =\n"
				+ "(\n"
				+ "	SELECT b.COLUMN2 \n"
				+ "	FROM user1.TABLE2 b\n"
				+ "	WHERE b.COLUMN1 =a.a \n"
				+ ")\n"
				+ "WHERE a.b = 123";
		
		DBUtil dbutil=new DBUtil();
		//dbutil.getMetadataFromFile("/home/cihan/dbmetadata.csv");
		dbutil.getMetadataFromDB();
		
		SqlTreeUtil sqlTreeUtil=new SqlTreeUtil();
		sqlTreeUtil.parse(sql, "USER1", dbutil);
				
		
		System.out.println("finished");

	}
}
