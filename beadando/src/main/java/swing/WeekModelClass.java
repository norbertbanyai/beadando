package swing;

import java.sql.Date;

import org.joda.time.LocalDate;

public class WeekModelClass {

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
	public WeekModelClass(Date date) {
		super();
		this.date = date;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 42;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof WeekModelClass)) {
			return false;
		}
		WeekModelClass wmc = (WeekModelClass)obj;
		LocalDate ldate = new LocalDate(date.getTime());
		LocalDate ldate2 = new LocalDate(wmc.date.getTime());
		if (ldate.getYear() == ldate2.getYear()) {
			if (ldate.getWeekOfWeekyear() == ldate2.getWeekOfWeekyear()) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		LocalDate ldate = new LocalDate(date.getTime());
		return ldate.getWeekOfWeekyear() + ". week";
	}
	
	
}
