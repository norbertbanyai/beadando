package core;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;
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

public class CalculationsWorkTests {
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
	public void testGetWorkedHoursOnAWeekByEmployeeId() {
		LocalDate date = new LocalDate(2012, 1, 2); // Monday

		WorkSession ws1 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "WORK");
		try {
			wdao.createWorkSession(ws1);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		int workedHours = Calculations.getWorkedHoursOnAWeekByEmployeeId(100, ws1.getDate());
		assertEquals(8, workedHours);
		WorkSession ws2 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "WORK");
		try {
			wdao.createWorkSession(ws1);
			fail(":(");
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			
		}
		workedHours = Calculations.getWorkedHoursOnAWeekByEmployeeId(100, ws2.getDate());
		assertEquals(8, workedHours);
		
		WorkSession ws3 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 4, "WORK");
		try {
			wdao.createWorkSession(ws3);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedHours = Calculations.getWorkedHoursOnAWeekByEmployeeId(100, ws3.getDate());
		assertEquals(12, workedHours);
		
		date = date.plusDays(1); //Tuesday
		WorkSession ws4 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "WORK");
		try {
			wdao.createWorkSession(ws4);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedHours = Calculations.getWorkedHoursOnAWeekByEmployeeId(100, ws4.getDate());
		assertEquals(20, workedHours);
		
		WorkSession ws5 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "SICKNESS"); //let's insert a sickday to tuesday
		
		try {
			wdao.createWorkSession(ws5);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedHours = Calculations.getWorkedHoursOnAWeekByEmployeeId(100, ws4.getDate());
		assertEquals(12, workedHours); //tuesday doesn't count as workday since we inserted a sickday to it
		
		
		date = date.plusDays(1); // wednesday
		WorkSession wstemp = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "WORK"); //let's insert a work to wednesday
		
		WorkSession ws6 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "DAY_OFF"); //let's insert a dayoff to wednesday
		
		try {
			wdao.createWorkSession(wstemp);
			workedHours = Calculations.getWorkedHoursOnAWeekByEmployeeId(100, ws4.getDate());
			assertEquals(20, workedHours);
			wdao.createWorkSession(ws6);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedHours = Calculations.getWorkedHoursOnAWeekByEmployeeId(100, ws4.getDate());
		assertEquals(12, workedHours); //wednesday doesn't count as workday since we inserted a dayoff to it
		
		date = new LocalDate(2012,1,7); //saturday
		WorkSession ws7 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "WORK");
		WorkSession ws8 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "DAY_OFF");
		
		try {
			wdao.createWorkSession(ws7);
			workedHours = Calculations.getWorkedHoursOnAWeekByEmployeeId(100, ws4.getDate());
			assertEquals(20, workedHours);
			wdao.createWorkSession(ws8);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedHours = Calculations.getWorkedHoursOnAWeekByEmployeeId(100, ws4.getDate());
		assertEquals(12, workedHours); // saturday doesn't count as workday: he entered work on saturday but left work cuz day off
		// what happens? nobody pays for the stupid employee bye bye
		
	}

	@Test
	public void testGetWorkedHoursInAMonthByEmployeeId() {
		LocalDate date = new LocalDate(2012, 1, 1); // Sunday
		WorkSession ws1 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "WORK");
		try {
			wdao.createWorkSession(ws1);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		int workedHours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws1.getDate());
		assertEquals(8, workedHours);
		
		date = new LocalDate(2012, 1, 31);
		WorkSession ws2 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "WORK");
		try {
			wdao.createWorkSession(ws2);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedHours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws1.getDate());
		assertEquals(16, workedHours);
		
		date = new LocalDate(2011, 12, 31);
		WorkSession ws3 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "WORK");
		try {
			wdao.createWorkSession(ws3);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedHours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws1.getDate());
		assertEquals(16, workedHours);
		
		date = new LocalDate(2012, 1, 15);
		WorkSession ws4 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "WORK");
		try {
			wdao.createWorkSession(ws4);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedHours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws1.getDate());
		assertEquals(24, workedHours);
		
		date = new LocalDate(2012, 1, 16);
		WorkSession ws5 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "SICKNESS");
		try {
			wdao.createWorkSession(ws5);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedHours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws1.getDate());
		assertEquals(24, workedHours);
		
		date = new LocalDate(2012, 1, 17);
		WorkSession ws6 = new WorkSession(1, 100, new Date(date.toDate()
				.getTime()), (short) 8, "DAY_OFF");
		try {
			wdao.createWorkSession(ws6);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedHours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws1.getDate());
		assertEquals(24, workedHours);
	}
	
	@Test
	public void testGetAllWorkedDayCurrentYearByEmployeeId() {
		int workedDays;
		LocalDate date = new LocalDate(2014, 1, 1);
		WorkSession ws1 = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws1);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedDays = Calculations.getAllWorkedDayCurrentYearByEmployeeId(100);
		assertEquals(1, workedDays);
		
		ws1.setDuration((short)4);
		try {
			wdao.createWorkSession(ws1);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e1) {
			fail(":(");
		}
		workedDays = Calculations.getAllWorkedDayCurrentYearByEmployeeId(100);
		assertEquals(1, workedDays);
		
		date = new LocalDate(2014, 12, 31); //wednesday
		WorkSession ws2 = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws2);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedDays = Calculations.getAllWorkedDayCurrentYearByEmployeeId(100);
		assertEquals(2, workedDays);
		
		ws2.setType(SessionTypes.SICKNESS);
		try {
			wdao.createWorkSession(ws2);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedDays = Calculations.getAllWorkedDayCurrentYearByEmployeeId(100);
		assertEquals(1, workedDays);
		
		date = new LocalDate(2014, 7, 25); //friday
		WorkSession ws3 = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws3);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedDays = Calculations.getAllWorkedDayCurrentYearByEmployeeId(100);
		assertEquals(2, workedDays);
		
		ws3.setType(SessionTypes.DAY_OFF);
		try {
			wdao.createWorkSession(ws3);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedDays = Calculations.getAllWorkedDayCurrentYearByEmployeeId(100);
		assertEquals(1, workedDays);
		
		date = date.plusDays(1); //saturday
		WorkSession ws4 = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws4);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedDays = Calculations.getAllWorkedDayCurrentYearByEmployeeId(100);
		assertEquals(2, workedDays);
		
		ws4.setType(SessionTypes.SICKNESS);
		try {
			wdao.createWorkSession(ws4);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedDays = Calculations.getAllWorkedDayCurrentYearByEmployeeId(100);
		assertEquals(1, workedDays);
		
		date = date.plusDays(1);//sunday
		WorkSession ws5 = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws5);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedDays = Calculations.getAllWorkedDayCurrentYearByEmployeeId(100);
		assertEquals(2, workedDays);
		
		ws5.setType(SessionTypes.DAY_OFF);
		try {
			wdao.createWorkSession(ws5);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		workedDays = Calculations.getAllWorkedDayCurrentYearByEmployeeId(100);
		assertEquals(1, workedDays);
	}

}
