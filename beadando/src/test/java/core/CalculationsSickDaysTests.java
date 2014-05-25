package core;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import dao.AlreadyReachedFifteenSickDays;
import dao.AlreadyReachedThirtyDayOffs;
import dao.TooMuchWorkOnADayException;
import dao.WorkSession;
import dao.WorkSessionDAO;
import dao.WorkSession.SessionTypes;
import dao.impl.JDBCWorkSessionDAOImpl;

public class CalculationsSickDaysTests {
	static WorkSessionDAO wdao;

	@BeforeClass
	public static void prepareTests() {
		wdao = new JDBCWorkSessionDAOImpl();
	}

	@After
	public void afterTest() {
		List<WorkSession> list = wdao.findWorkSessionsByEmployeeId(100);
		for (WorkSession workSession : list) {
			wdao.deleteWorkSession(workSession);
		}
	}
	
	@Test
	public void testGetSickDaysOnAWeekByEmployeeId() {
		LocalDate date = new LocalDate(2012, 1, 2); // Monday

		WorkSession ws1 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "SICKNESS");
		try {
			wdao.createWorkSession(ws1);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}

		int sickDays = Calculations.getSickDaysOnAWeekByEmployeeId(100, new Date(
				date.toDate().getTime()));
		assertEquals(1, sickDays);

		date = date.plusDays(1); // Tuesday

		WorkSession ws2 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "SICKNESS");
		try {
			wdao.createWorkSession(ws2);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		sickDays = Calculations.getSickDaysOnAWeekByEmployeeId(100, new Date(date
				.toDate().getTime()));
		assertEquals(2, sickDays);

		date = date.plusDays(1); // Wednesday
		WorkSession ws3 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "WORK"); // we insert a workday
		try {
			wdao.createWorkSession(ws3);
			ws3.setType(SessionTypes.SICKNESS); // but we modify it to a sickday
			wdao.createWorkSession(ws3);
			sickDays = Calculations.getSickDaysOnAWeekByEmployeeId(100,
					ws3.getDate()); // should be 3
			assertEquals(3, sickDays);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}

		date = new LocalDate(2012, 1, 7); // Saturday
		WorkSession ws4 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "SICKNESS");
		try {
			wdao.createWorkSession(ws4);
			sickDays = Calculations.getSickDaysOnAWeekByEmployeeId(100,
					ws4.getDate());
			assertEquals(3, sickDays); // one cannot simply use a sickday at weekend
			
			date = date.plusDays(1); // Sunday
			ws4.setDate(new Date(date.toDate().getTime()));
			wdao.createWorkSession(ws4);
			sickDays = Calculations.getSickDaysOnAWeekByEmployeeId(100,
					ws4.getDate());
			assertEquals(3, sickDays);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
	}
	
	@Test
	public void testGetSickDaysInCurrentYearByEmployeeId() {
		LocalDate date = new LocalDate(2014, 1, 5); // Sunday
		WorkSession ws1 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "SICKNESS");
		try {
			wdao.createWorkSession(ws1);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		int sickDays = Calculations.getSickDaysInCurrentYearByEmployeeId(100);
		assertEquals(0, sickDays);
		
		date = new LocalDate(2014, 5, 2); // Friday
		WorkSession ws2 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "SICKNESS");
		try {
			wdao.createWorkSession(ws2);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		sickDays = Calculations.getSickDaysInCurrentYearByEmployeeId(100);
		assertEquals(1, sickDays);
		
		date = new LocalDate(2014, 5, 21); // Wednesday
		WorkSession ws3 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "SICKNESS");
		try {
			wdao.createWorkSession(ws3);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		sickDays = Calculations.getSickDaysInCurrentYearByEmployeeId(100);
		assertEquals(2, sickDays);
		
		date = new LocalDate(2014, 5, 21); // Wednesday
		WorkSession ws4 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "DAY_OFF");
		try {
			wdao.createWorkSession(ws4);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		sickDays = Calculations.getSickDaysInCurrentYearByEmployeeId(100);
		assertEquals(1, sickDays); //sickday < dayoff
		
		date = new LocalDate(2014, 5, 22); // Thursday
		WorkSession ws5 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "WORK");
		try {
			wdao.createWorkSession(ws5);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		sickDays = Calculations.getSickDaysInCurrentYearByEmployeeId(100);
		assertEquals(1, sickDays);
		
		date = new LocalDate(2014, 5, 22); // Thursday
		WorkSession ws6 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "SICKNESS");
		try {
			wdao.createWorkSession(ws6);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		sickDays = Calculations.getSickDaysInCurrentYearByEmployeeId(100);
		assertEquals(2, sickDays); //workday < sickday
		
	}

	@Test
	public void testGetSickDaysInAMonthByEmployeeId() {
		LocalDate date = new LocalDate(2012, 1, 2); // Monday

		WorkSession ws1 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "SICKNESS");
		try {
			wdao.createWorkSession(ws1);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}

		int sickDays = Calculations.getSickDaysInAMonthByEmployeeId(100, new Date(
				date.toDate().getTime()));
		assertEquals(1, sickDays);

		date = date.plusDays(1); // Tuesday

		WorkSession ws2 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "SICKNESS");
		try {
			wdao.createWorkSession(ws2);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		sickDays = Calculations.getSickDaysInAMonthByEmployeeId(100, new Date(date
				.toDate().getTime()));
		assertEquals(2, sickDays);

		date = date.plusDays(1); // Wednesday
		WorkSession ws3 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "WORK"); // we insert a workday
		try {
			wdao.createWorkSession(ws3);
			ws3.setType(SessionTypes.SICKNESS); // but we modify it to a sickday
			wdao.createWorkSession(ws3);
			sickDays = Calculations.getSickDaysInAMonthByEmployeeId(100,
					ws3.getDate()); // should be 3
			assertEquals(3, sickDays);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}

		date = new LocalDate(2012, 1, 7); // Saturday
		WorkSession ws4 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "SICKNESS");
		try {
			wdao.createWorkSession(ws4);
			sickDays = Calculations.getSickDaysInAMonthByEmployeeId(100,
					ws4.getDate());
			assertEquals(3, sickDays); // one cannot simply use a sickday at weekend
			
			date = date.plusDays(1); // Sunday
			ws4.setDate(new Date(date.toDate().getTime()));
			wdao.createWorkSession(ws4);
			sickDays = Calculations.getSickDaysInAMonthByEmployeeId(100,
					ws4.getDate());
			assertEquals(3, sickDays);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		
		date = new LocalDate(2012, 1, 29); // Sunday
		WorkSession ws5 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "SICKNESS");
		try {
			wdao.createWorkSession(ws5);
			sickDays = Calculations.getSickDaysInAMonthByEmployeeId(100,
					ws5.getDate());
			assertEquals(3, sickDays); // one cannot simply use a sickday at weekend
			
			date = date.plusDays(1); // Monday jan 30
			ws5.setDate(new Date(date.toDate().getTime()));
			wdao.createWorkSession(ws5);
			sickDays = Calculations.getSickDaysInAMonthByEmployeeId(100,
					ws5.getDate());
			assertEquals(4, sickDays);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
	}

}
