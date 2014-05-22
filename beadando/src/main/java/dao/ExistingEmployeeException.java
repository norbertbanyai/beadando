package dao;

public class ExistingEmployeeException extends Exception {
	private int employeeId;

	/**
	 * @param employeeId
	 */
	public ExistingEmployeeException(int employeeId) {
		super();
		this.employeeId = employeeId;
	}

	/**
	 * @return the employeeId
	 */
	public int getEmployeeId() {
		return employeeId;
	}

	
}
