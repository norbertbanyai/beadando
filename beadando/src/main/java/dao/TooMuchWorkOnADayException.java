package dao;

/**
 * Exception for indicating that the user tried to enter too much work on a day to an employee.
 * An employee cannot get beyond the 12 hours<code>/</code> day limit.
 */
public class TooMuchWorkOnADayException extends Exception {
	
	/**
	 * The hours worked <code>+</code> the hours the user entered.
	 */
	private int workedHours;

	/**
	 * Constructor for the exception.
	 * Sets the {@link #workedHours}.
	 * 
	 * @param workedHours the number of worked hours the user tried to enter
	 */
	public TooMuchWorkOnADayException(int workedHours) {
		super();
		this.workedHours = workedHours;
	}
	
	/**
	 * Returns the {@link #workedHours}.
	 * 
	 * @return the worked hours the user entered.
	 */
	public int getWorkedHours() {
		return workedHours;
	}
	
	/**
	 * Returns the string representation of the exception.
	 * 
	 * @return the string representation of the exception in the form
	 * 			<span><em>Cannot work more than 12 hours (you entered: </em><code>workedHours</code></span>
	 */
	public String getMessage() {
		return "Cannot work more than 12 hours (you entered: " + workedHours + ")";
	}
}
