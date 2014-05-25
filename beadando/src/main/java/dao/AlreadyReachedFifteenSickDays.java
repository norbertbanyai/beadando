package dao;

/**
 * Exception for indicating that an employee already reached his sick day limit per year.
 * @see WorkSessionDAO
 */
public class AlreadyReachedFifteenSickDays extends Exception {
	
	@Override
	public String getMessage() {
		return "This employee already had 15 sick days.";
	}
}
