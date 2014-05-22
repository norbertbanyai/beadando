package dao.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionHelper {
private static Connection conn;
	
	public static Connection getConnection() throws IOException, SQLException {
		if (conn == null) {
			Properties p = new Properties();
			p.load(p.getClass().getResourceAsStream("/dbconnection.properties"));
			conn = DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"), 
																	 p.getProperty("password"));
		}
		return conn;
	}
	
	public static Connection getConnection(String username, String password) throws IOException, SQLException {
		if (conn == null) {
			Properties p = new Properties();
			p.load(p.getClass().getResourceAsStream("/resources/dbconnection.properties"));
			conn = DriverManager.getConnection(p.getProperty("url"), username, password);
		}
		return conn;
	}
}
