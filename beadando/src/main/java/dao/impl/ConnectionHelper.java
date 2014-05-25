package dao.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import static core.Main.logger;

/**
 * Class that provides the connection with the database.
 */
public class ConnectionHelper {
	
	/**
	 * The static variable that stores the {@code Connection}.
	 */
	private static Connection conn;
	
	/**
	 * Returns a new {@code Connection} if the {@link #conn} is null and {@link #conn} will be equal with this {@code Connection}.
	 * If the {@link #conn} is not null then returns {@link #conn}.
	 * <p>Reads the connections informations from the property file: <code>dbconnection.properties</code>.</p>
	 * 
	 * @return {@link #conn} will be returned if it is not null, if it is then we create a new {@code Connection}, {@link #conn}
	 * will be equal to this object, and finally returns {@link #conn}
	 * @throws IOException is thrown if something unexpected occurs when we try to build up the connection with the database
	 * @throws SQLException is thrown if an SQL query goes wrong
	 */
	public static Connection getConnection() throws IOException, SQLException {
		if (conn == null) {
			Properties p = new Properties();
			p.load(p.getClass().getResourceAsStream("/dbconnection.properties"));
			conn = DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"), 
																	 p.getProperty("password"));
		}
		return conn;
	}
}
