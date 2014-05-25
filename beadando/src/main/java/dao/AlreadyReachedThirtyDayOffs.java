package dao;

/**
 * Exception for indicating that an employee already reached his free day limit per year.
 * @see WorkSessionDAO
 */
public class AlreadyReachedThirtyDayOffs extends Exception {

	@Override
	public String getMessage() {
		return "This employee already had 30 dayoffs.";
	}
}
