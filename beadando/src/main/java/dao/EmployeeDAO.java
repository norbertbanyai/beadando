package dao;

import java.util.List;

/**
 * Interface for the operations we want to execute with {@link Employee}s.
 * @see EmployeeDAOFactory
 */
public interface EmployeeDAO {
	/**
	 * Returns all the employees we found.
	 * 
	 * @return a list of all employees we found
	 */
	List<Employee> findAllEmployees();

	/**
	 * Finds an {@code Employee} by a specific id.
	 * If the id isn't binded to an employee, it throws an {@code EmployeeNotFoundException}.
	 * @see EmployeeNotFoundException
	 * 
	 * @param id the employee's id
	 * @return an {@link Employee} object representing the {@code Employee}'s data
	 * @throws EmployeeNotFoundException if we didn't find any {@code Employee} with the given id
	 */
	Employee findEmployeeById(int id) throws EmployeeNotFoundException;

	/**
	 * Creates an {@code Employee} from the given {@code e} {@code Employee} object.
	 * {@code ExistingEmployeeException} is thrown if there is already an {@code Employee} with
	 * the parameter {@code Employee e}'s id.
	 * 
	 * @param e the {@code Employee} we want to represent.
	 * @throws ExistingEmployeeException if there is already an employee with the parameter {@code Employee e}'s id
	 */
	void createEmployee(Employee e) throws ExistingEmployeeException;
	
	/**
	 * Deletes an {@code Employee} by the {@code Employee e} object's id value.
	 * 
	 * @param e the {@code Employee} we want to delete
	 */
	void deleteEmployee(Employee e);
	
	/**
	 * Updates an {@code Employee} by the {@code Employee e} object's id value.
	 * 
	 * @param e the {@code Employee} we want to update
	 */
	void updateEmployee(Employee e);
}