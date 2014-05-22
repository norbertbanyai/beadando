package swing;

import java.sql.Date;
import java.util.Locale;

import org.joda.time.LocalDate;

public class MonthModelClass {
	private Date date;

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @param date
	 */
	public MonthModelClass(Date date) {
		super();
		this.date = date;
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MonthModelClass)) {
			return false;
		}
		MonthModelClass mdc = (MonthModelClass)obj;
		LocalDate ldate = new LocalDate(date.getTime());
		LocalDate ldate2 = new LocalDate(mdc.date.getTime());
		if (ldate.getYear() == ldate2.getYear() && ldate.getMonthOfYear() == ldate2.getMonthOfYear()) {
			return true;
		}
		return false;
	}
	@Override
	public int hashCode() {
		return 42;
	}
	@Override
	public String toString() {
		LocalDate ldate = new LocalDate(date.getTime());
		return ldate.getYear() + ". " + ldate.monthOfYear().getAsShortText(Locale.US);
	}

}
