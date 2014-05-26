package core;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.Employee;
import dao.PersistentLayerException;
import dao.impl.ConnectionHelper;
import static core.Main.logger;

/**
 * Class that does the importing and exporting of the employees' table in the database into/from an XML file.
 * 
 * @see XMLHandler
 */
public class DataLoader {
	
	/**
	 * Deletes all the employees from the database and also purges the table.
	 */
	private static void deleteEmployeesFromDatabase() {
		try(Statement stmt = ConnectionHelper.getConnection().createStatement()) {
			stmt.executeUpdate("delete employees purge");
		} catch (SQLException | IOException e) {
			logger.error("error while purging employees", e);
			throw new PersistentLayerException(e);
		}
	}
	
	/**
	 * Removes the foreign key set on the database table: {@code worksessions} which references on the
	 * database table {@code employees} on {@code employee_id}. This is needed to be able to execute {@link #deleteEmployeesFromDatabase()}.
	 */
	private static void removeForeignKeyFromWorkSessions() {
		try(Statement stmt = ConnectionHelper.getConnection().createStatement()) {
			stmt.executeUpdate("alter table worksessions drop constraint ws_fk");
		} catch (SQLException | IOException e) {
			logger.error("error while removing foreign keys from worksessions", e);
			throw new PersistentLayerException(e);
		}
	}
	
	/**
	 * Adds the foreign key removed in {@link #removeForeignKeyFromWorkSessions()} to the database table {@code worksessions} references
	 * the database table {@code employees} on {@code employee_id}.
	 */
	private static void addForeignKeyToWorkSessions() {
		try(Statement stmt = ConnectionHelper.getConnection().createStatement()) {
			stmt.executeUpdate("alter table worksessions add constraint ws_fk foreign key (employee_id) references employees(employee_id)");
		} catch (SQLException | IOException e) {
			logger.error("error while adding foreign key to worksessions");
			throw new PersistentLayerException(e);
		}
	}
	
	/**
	 * Imports employees into the {@code employees} table from a specific XML file, and deletes the current content of database table {@code employees}.
	 * <p>Does not execute the import if the XML file is corrupt.</p>
	 * 
	 * @param xmlPath the path of the XML file we want to load the employees from
	 */
	public static void importEmployees(String xmlPath) {
		logger.info("loading employees from xml, path = " + xmlPath);
		List<Employee> list = XMLHandler.loadEmployeesFromXML(xmlPath);
		if (list != null) {
			logger.info("loaded employees from xml, path = " + xmlPath);
			try {
				logger.info("setting autocommit=false to prevent corruption in case of an exception");
				ConnectionHelper.getConnection().setAutoCommit(false);
				logger.info("setting autocommit=false was successful");
			} catch (SQLException | IOException e1) {
				logger.error("error while setting autocommit=false to import employees", e1);
				throw new PersistentLayerException(e1);
			}
			logger.info("removing foregin keys");
			DataLoader.removeForeignKeyFromWorkSessions();
			logger.info("removed foreign keys");
			logger.info("deleting current employees from the database");
			DataLoader.deleteEmployeesFromDatabase();
			logger.info("deleted employees from the database");
			try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("insert into employees values(?, ?, ?, ?, ?)")) {
				logger.info("loading employees into the database");
				for (Employee employee : list) {
					Employee.employeeToPreparedStatement(pstmt, employee);
					pstmt.executeUpdate();
				}
				logger.info("loaded " + list.size() + " employees successfully into the databse");
				logger.info("adding foreign key to worksessions");
				DataLoader.addForeignKeyToWorkSessions();
				logger.info("added foreign key to worksessions");
			} catch (SQLException | IOException e) {
				logger.error("something wrong happened while uploading employees to the database");
				try {
					logger.info("rolling back database");
					ConnectionHelper.getConnection().rollback();
					logger.info("rolled back the database");
				} catch (SQLException | IOException e1) {
					logger.error("error happened while rolling back the database", e1);
					throw new PersistentLayerException(e1);
				}
				throw new PersistentLayerException(e);
			}
			try {
				logger.info("commiting database changes after uploading employees");
				ConnectionHelper.getConnection().commit();
				logger.info("commit was successful");
				logger.info("setting autocommit back to true");
				ConnectionHelper.getConnection().setAutoCommit(true);
				logger.info("autocommit = true set was successful");
			} catch (SQLException | IOException e) {
				logger.error("error happened while trying to commit | setting autocommit = true", e);
				throw new PersistentLayerException(e);
			}
		}
	}
	
	/**
	 * Exports the content of the database table {@code employees} into an XML file.
	 * 
	 * @param xmlPath the path of the XML file we want to export the employees to
	 */
	public static void exportEmployees(String xmlPath) {
		List<Employee> list = new ArrayList<>();
		logger.info("loading employees to xml, path=" + xmlPath);
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select * from employees")) {
			try(ResultSet rset = pstmt.executeQuery()) {
				while (rset.next()) {
					list.add(new Employee(rset.getInt("employee_id"), rset.getString("name"),
							rset.getDate("hire_date"), rset.getBigDecimal("salary"), rset.getString("department_name")));
				}
			}
		} catch (SQLException | IOException e) {
			logger.error("error happened while retrieving employees from db to xml");
			throw new PersistentLayerException(e);
		}
		XMLHandler.loadEmployeesToXML(xmlPath, list);
		logger.info("successfully loaded saved employees to " + xmlPath);
	}
}
