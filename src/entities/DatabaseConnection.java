package entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//this class creates the connection to SQL dataBase
public class DatabaseConnection {

	public static final String url = "jdbc:mysql://cse.unl.edu/jreyesco";
	public static final String username = "jreyesco";
	public static final String password = "_tr4QQ";

	static public Connection getConnection()
	{
		try {	//creates jdbc Driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {		//Catches all errors when Driver instance not successfully created
			System.out.println("InstantiationException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		Connection conn = null;
		//Connects driver to SQL dataBase
		try {
			conn = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.username, DatabaseConnection.password);
		} catch (SQLException e) {				
			System.out.println("SQLException: ");	//error when connecting to dataBase
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return conn;
	}
}