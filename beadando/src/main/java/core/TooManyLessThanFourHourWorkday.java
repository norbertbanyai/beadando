/**
 * 
 */
package core;

/**
 * @author bnorbi
 *
 */
public class TooManyLessThanFourHourWorkday extends Exception {
	private int days;

	/**
	 * @param days
	 */
	public TooManyLessThanFourHourWorkday(int days) {
		this.days = days;
	}
	
	public int getDayCount() {
		return days;
	}
	
	
}
