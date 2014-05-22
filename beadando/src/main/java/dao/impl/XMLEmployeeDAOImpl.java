/**
 * 
 */
package dao.impl;

import java.util.List;

import dao.Employee;
import dao.EmployeeDAO;
import dao.EmployeeNotFoundException;
import dao.ExistingEmployeeException;

/**
 * @author bnorbi
 *
 */
public class XMLEmployeeDAOImpl implements EmployeeDAO {

	/* (non-Javadoc)
	 * @see dao.EmployeeDAO#findAllEmployees()
	 */
	@Override
	public List<Employee> findAllEmployees() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dao.EmployeeDAO#findEmployeeById(int)
	 */
	@Override
	public Employee findEmployeeById(int id) throws EmployeeNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dao.EmployeeDAO#findEmployeesByDepartment(java.lang.String)
	 */
	@Override
	public List<Employee> findEmployeesByDepartment(String depName) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dao.EmployeeDAO#createEmployee(dao.Employee)
	 */
	@Override
	public void createEmployee(Employee e) throws ExistingEmployeeException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Employee> findEmployeesByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteEmployee(Employee e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateEmployee(Employee e) {
		// TODO Auto-generated method stub
		
	}

}
