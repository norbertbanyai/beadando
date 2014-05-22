/**
 * 
 */
package dao;

import java.util.List;

/**
 * @author bnorbi
 * 
 */
public interface EmployeeDAO {
	List<Employee> findAllEmployees();

	Employee findEmployeeById(int id) throws EmployeeNotFoundException;
	
	List<Employee> findEmployeesByName(String name);

	List<Employee> findEmployeesByDepartment(String depName);

	void createEmployee(Employee e) throws ExistingEmployeeException;
	
	void deleteEmployee(Employee e);
	
	void updateEmployee(Employee e);
}