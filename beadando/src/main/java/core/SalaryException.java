package core;

import java.math.BigDecimal;

/**
 * Exception thrown when calculating an employee's salary in a specific month and the employee breaks some of the rules declared in
 * the {@link Calculations#getSalaryInAMonthByEmployeeId(int, java.sql.Date)}.
 */
public class SalaryException extends Exception {
	
	/**
	 * The {@code Employee}'s salary.
	 */
	private BigDecimal salary;
	
	/**
	 * The broken rules' messages.
	 */
	private String msg;
	
	/**
	 * Constructor for the exception.
	 * 
	 * @param salary the {@code Employee}'s salary
	 * @param errors the broken rules' messages copied into one string
	 */
	public SalaryException(BigDecimal salary, String errors) {
		this.salary = salary;
		msg = errors;
	}
	
	/**
	 * Returns the {@code Employee}'s salary.
	 * 
	 * @return the {@code Employee}'s salary
	 */
	public BigDecimal getSalary() {
		return salary;
	}
	
	
	@Override
	/**
	 * Returns the error messages.
	 * 
	 * @return the error messages
	 */
	public String getMessage() {
		return msg;
	}

}
