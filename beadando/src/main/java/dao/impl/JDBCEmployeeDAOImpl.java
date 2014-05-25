package dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import dao.Employee;
import dao.EmployeeDAO;
import dao.EmployeeNotFoundException;
import dao.ExistingEmployeeException;
import dao.PersistentLayerException;
import static core.Main.logger;

/**
 * A JDBC implementation of the {@link EmployeeDAO} interface.
 */
public class JDBCEmployeeDAOImpl implements EmployeeDAO{
	
	/**
	 * String that is used by a {@code PreparedStatement} to insert an {@code Employee} into the database.
	 */
	private static final String INSERT_INTO_EMPLOYEES = "insert into employees values(?, ?, ?, ?, ?)";
	
	/**
	 * String that is used by a {@code Statement} to select all {@code Employee} from the database.
	 */
	private static final String FIND_ALL_EMPLOYEES = "select employee_id, name, hire_date, salary, department_name from employees ";
	
	/**
	 * String that is used by a {@code PreparedStatement} also expands the {@link #FIND_ALL_EMPLOYEES} to search by a specific employee_id.
	 */
	private static final String FIND_EMPLOYEE_BY_ID = FIND_ALL_EMPLOYEES + "where employee_id = ?";


	@Override
	public List<Employee> findAllEmployees() {
		List<Employee> l = new ArrayList<>();
		try(Statement stmt = ConnectionHelper.getConnection().createStatement();
				ResultSet rset = stmt.executeQuery(FIND_ALL_EMPLOYEES)) {
			while (rset.next()) {
				l.add(new Employee(rset.getInt("employee_id"), rset.getString("name"),
						rset.getDate("hire_date"), rset.getBigDecimal("salary"), rset.getString("department_name")));
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
		return l;
	}

	@Override
	public Employee findEmployeeById(int id) throws EmployeeNotFoundException {
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement(FIND_EMPLOYEE_BY_ID)) {
			pstmt.setInt(1, id);
			try(ResultSet rset = pstmt.executeQuery()) {
				if (rset.next()) {
					return new Employee(rset.getInt("employee_id"), rset.getString("name"),
							rset.getDate("hire_date"), rset.getBigDecimal("salary"), rset.getString("department_name"));
				} else {
					throw new EmployeeNotFoundException(id);
				}
			}
			
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
	}

	@Override
	public void createEmployee(Employee e) throws ExistingEmployeeException {
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement(FIND_EMPLOYEE_BY_ID)) {
			pstmt.setInt(1, e.getId());
			try(ResultSet rset = pstmt.executeQuery()) {
				if (rset.next()) {
					throw new ExistingEmployeeException(e.getId());
				}
			}
		} catch (SQLException | IOException e1) {
			throw new PersistentLayerException(e1);
		}
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement(INSERT_INTO_EMPLOYEES)) {
			Employee.employeeToPreparedStatement(pstmt, e);
			pstmt.executeUpdate();
		} catch (SQLException | IOException e1) {
			throw new PersistentLayerException(e1);
		}
	}

	@Override
	public void deleteEmployee(Employee e) {
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("delete from employees where employee_id = ?")) {
			pstmt.setInt(1, e.getId());
			pstmt.executeUpdate();
		} catch (SQLException | IOException e1) {
			throw new PersistentLayerException(e1);
		}
		
	}

	@Override
	public void updateEmployee(Employee e) {
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().
				prepareStatement(FIND_EMPLOYEE_BY_ID, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
			pstmt.setInt(1, e.getId());
			try(ResultSet rset = pstmt.executeQuery()) {
				if (rset.next()) {
					rset.updateString("name", e.getName());
					rset.updateDate("hire_date", e.getHireDate());
					rset.updateBigDecimal("salary", e.getSalary());
					rset.updateString("department_name", e.getDepartment());
					rset.updateRow();
				}
			}
		} catch (SQLException | IOException e1) {
			throw new PersistentLayerException(e1);
		}
	}

}
