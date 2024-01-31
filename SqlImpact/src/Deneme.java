import java.io.FileNotFoundException;
import java.sql.SQLException;

import net.sf.jsqlparser.JSQLParserException;
import sqlimpact.util.DBUtil;
import sqlimpact.util.SqlTreeUtil;

public class Deneme {
public static void main(String[] args) throws FileNotFoundException, JSQLParserException, ClassNotFoundException, SQLException {
		
		String sql="select * from user1.table1";
		
		DBUtil dbutil=new DBUtil();
		dbutil.getMetadataFromFile("c:\\gecici\\dbmetadata.csv");
		System.out.println("metadata file read ok.");
		//dbutil.getMetadataFromDB();
		
		dbutil.getViewListFromFile("c:\\gecici\\views.csv");
		System.out.println("views file read ok.");
		
		SqlTreeUtil sqlTreeUtil=new SqlTreeUtil();
		sqlTreeUtil.parse(sql, "CDMKART", dbutil);
				
		
		System.out.println("finished");

	}
}
