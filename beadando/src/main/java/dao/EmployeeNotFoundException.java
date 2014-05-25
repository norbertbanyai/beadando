package dao;

/**
 * Exception to indicate that we tried to retrieve an {@code Employee} with a non-existing id.
 */
public class EmployeeNotFoundException extends Exception {

	/**
	 * The non-existing id.
	 */
	private int employeeId;

	/**
	 * Constructor for the exception.
	 * Copies the incorrect id into {@link #employeeId}
	 * 
	 * @param id the non-existing employee id
	 */
	public EmployeeNotFoundException(int id) {
		this.employeeId = id;
	}

	/**
	 * Returns the employee id which caused this exception.
	 * 
	 * @return the employee id which caused this exception.
	 */
	public int getEmployeeId() {
		return employeeId;
	}
}
