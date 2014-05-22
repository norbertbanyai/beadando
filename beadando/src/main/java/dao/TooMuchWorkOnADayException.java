/**
 * 
 */
package dao;

/**
 * @author bnorbi
 *
 */
public class TooMuchWorkOnADayException extends Exception {
	private int workedHours;

	/**
	 * @param workedHours
	 */
	public TooMuchWorkOnADayException(int workedHours) {
		super();
		this.workedHours = workedHours;
	}
	
	public int getWorkedHours() {
		return workedHours;
	}
	@Override
	public String getMessage() {
		return "Cannot work more than 12 hours (you entered: " + workedHours + ")";
	}
}
