package dao;

public class EmployeeNotFoundException extends Exception {

	private int employeeId;

	public EmployeeNotFoundException(int id) {
		this.employeeId = id;
	}

	public int getEmployeeId() {
		return employeeId;
	}
}
