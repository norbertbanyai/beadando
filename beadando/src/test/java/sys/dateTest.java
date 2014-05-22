package sys;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.joda.time.LocalDate;
import org.joda.time.MonthDay;
import org.joda.time.Months;
import org.junit.Test;

public class dateTest {

	@Test
	public void test() {
		String string = "2014.05.10";
		LocalDate ldate = new LocalDate(LocalDate.now());
		ldate = ldate.minusDays(ldate.getDayOfMonth() - 1);
		ldate = ldate.minusMonths(ldate.getMonthOfYear() -1 );
		System.out.println(ldate);
	}

}
