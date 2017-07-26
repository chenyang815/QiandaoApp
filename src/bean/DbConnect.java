package bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnect {
	 private static String driverName="com.mysql.jdbc.Driver";
	    private static String userName="root";
	    private static String password="123456";
	    public static String dbName="qiandao";
	    public static Connection getDBconnect(){
	        String url1="jdbc:mysql://127.0.0.1:3306/"+dbName;
	        String url2="?useSSL=false&user="+userName+"&password="+password;
	        String url3="&useUnicode=true&characterEncoding=UTF-8";
	        String url=url1+url2+url3;
	        Connection connection=null;
	        try {
	            Class.forName(driverName);
	            connection= DriverManager.getConnection(url);
	            //return connection;
	        }catch (Exception e){
	            e.printStackTrace();
	        }
	        return connection;
	    }
	    public static void closeDb(Connection connection, PreparedStatement pstm, ResultSet rs){

	        try {

	            if (rs!=null) rs.close();
	            if (pstm!=null) pstm.close();
	            if (connection!=null)

	                connection.close();

	        } catch (SQLException e) {
	            e.printStackTrace();

	        }
	    }
}
