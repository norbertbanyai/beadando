/**
 * 
 */
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

/**
 * @author bnorbi
 *
 */
public class DataLoader {
	
	private static void deleteEmployeesFromDatabase() {
		try(Statement stmt = ConnectionHelper.getConnection().createStatement()) {
			stmt.executeUpdate("delete employees purge");
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
	}
	
	private static void removeForeignKeyFromWorkSessions() {
		try(Statement stmt = ConnectionHelper.getConnection().createStatement()) {
			stmt.executeUpdate("alter table worksessions drop constraint ws_fk");
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
	}
	
	private static void addForeignKeyToWorkSessions() {
		try(Statement stmt = ConnectionHelper.getConnection().createStatement()) {
			stmt.executeUpdate("alter table worksessions add constraint ws_fk foreign key (employee_id) references employees(employee_id)");
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
	}
	
	public static void importEmployees(String xmlPath) {
		List<Employee> list = XMLHandler.loadEmployeesFromXML(xmlPath);
		if (list != null) {
			DataLoader.removeForeignKeyFromWorkSessions();
			DataLoader.deleteEmployeesFromDatabase();
			try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("insert into employees values(?, ?, ?, ?, ?)")) {
				for (Employee employee : list) {
					Employee.employeeToPreparedStatement(pstmt, employee);
					pstmt.executeUpdate();
				}
				DataLoader.addForeignKeyToWorkSessions();
			} catch (SQLException | IOException e) {
				throw new PersistentLayerException(e);
			}
		}
	}
	
	public static void exportEmployees(String xmlPath) {
		List<Employee> list = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select * from employees")) {
			try(ResultSet rset = pstmt.executeQuery()) {
				while (rset.next()) {
					list.add(new Employee(rset.getInt("employee_id"), rset.getString("name"),
							rset.getDate("hire_date"), rset.getBigDecimal("salary"), rset.getString("department_name")));
				}
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
		XMLHandler.loadEmployeesToXML(xmlPath, list);
	}

}
