package dao;

/**
 * Exception for indicating that we tried to create an {@code Employee} with an {@code EmployeeDAO}, but an employee
 * with the given id already exists.
 */
public class ExistingEmployeeException extends Exception {
	/**
	 * The employee id, which is already in the data storage.
	 */
	private int employeeId;

	/**
	 * Constructor for the exception.
	 * Sets the {@link #employeeId} to the given employee id.
	 * 
	 * @param employeeId the employee id which already exists in the data storage
	 */
	public ExistingEmployeeException(int employeeId) {
		super();
		this.employeeId = employeeId;
	}

	/**
	 * Returns the employee id caused this exception.
	 * 
	 * @return the employee id caused this exception.
	 */
	public int getEmployeeId() {
		return employeeId;
	}
}
