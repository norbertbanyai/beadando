package core;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import dao.AlreadyReachedFifteenSickDays;
import dao.AlreadyReachedThirtyDayOffs;
import dao.Employee;
import dao.EmployeeDAO;
import dao.EmployeeNotFoundException;
import dao.TooMuchWorkOnADayException;
import dao.WorkSession;
import dao.WorkSessionDAO;
import dao.WorkSession.SessionTypes;
import dao.impl.JDBCEmployeeDAOImpl;
import dao.impl.JDBCWorkSessionDAOImpl;

public class CalculationsSalaryTest {
	static WorkSessionDAO wdao;
	static EmployeeDAO edao;
	static BigDecimal originalSalary;

	@BeforeClass
	public static void prepareTests() {
		wdao = new JDBCWorkSessionDAOImpl();
		edao = new JDBCEmployeeDAOImpl();
		Employee e = null;
		try {
			e = edao.findEmployeeById(100);
		} catch (EmployeeNotFoundException e1) {
			fail(":(");
		}
		originalSalary = e.getSalary();
		
		e.setSalary(new BigDecimal(10000));
		/* 10000 for 20 days with 8 hour per day
		 * 500/day
		 * 62,5/h
		 * 1.5x for saturdays
		 * 2x for sundays
		 */
		edao.updateEmployee(e);
	}

	@After
	public void afterTest() {
		List<WorkSession> list = wdao.findWorkSessionsByEmployeeId(100);
		for (WorkSession workSession : list) {
			wdao.deleteWorkSession(workSession);
		}
		Employee e = null;
		try {
			e = edao.findEmployeeById(100);
		} catch (EmployeeNotFoundException e1) {
			fail(":(");
		}
		e.setSalary(originalSalary);
		edao.updateEmployee(e);
	}
	
	@Test
	public void testGetSalaryInAMonthByEmployeeId() {
		LocalDate date = new LocalDate(2012, 1, 2); //monday
		WorkSession ws1 = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws1);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		BigDecimal salary = null;
		try {
			salary = Calculations.getSalaryInAMonthByEmployeeId(100, ws1.getDate());
		} catch (SalaryException e) {
			salary = e.getSalary();
		}
		//8 normal hour * 62,5
		assertEquals(new BigDecimal("500").doubleValue(), salary.doubleValue(), 0.000001);
		
		ws1.setDuration((short)4);
		try {
			wdao.createWorkSession(ws1);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		
		try {
			salary = Calculations.getSalaryInAMonthByEmployeeId(100, ws1.getDate());
		} catch (SalaryException e) {
			salary = e.getSalary();
		}
		//8 normal hour * 62,5 + 4 plushour * (62,5 * 1,5)
		assertEquals(new BigDecimal("875").doubleValue(), salary.doubleValue(), 0.000001);
		
		date = date.plusDays(1); //tuesday
		WorkSession ws2 = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws2);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		try {
			salary = Calculations.getSalaryInAMonthByEmployeeId(100, ws1.getDate());
		} catch (SalaryException e) {
			salary = e.getSalary();
		}
		// 875 so far + 500 = (62,5*8h) = 1375
		assertEquals(new BigDecimal("1375").doubleValue(), salary.doubleValue(), 0.000001);
		
		ws2.setType(SessionTypes.SICKNESS); //overriding tuesday's work with a sickday | sickday > workday
		try {
			wdao.createWorkSession(ws2);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		try {
			salary = Calculations.getSalaryInAMonthByEmployeeId(100, ws1.getDate());
		} catch (SalaryException e) {
			salary = e.getSalary();
		}
		int workhours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws2.getDate());
		assertEquals(12, workhours);
		assertEquals(new BigDecimal("1375").doubleValue(), salary.doubleValue(), 0.000001);
		
		date = date.plusDays(1); //wednesday
		WorkSession ws3 = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws3);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		try {
			salary = Calculations.getSalaryInAMonthByEmployeeId(100, ws1.getDate());
		} catch (SalaryException e) {
			salary = e.getSalary();
		}
		workhours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws2.getDate());
		assertEquals(20, workhours);
		assertEquals(new BigDecimal("1875").doubleValue(), salary.doubleValue(), 0.000001);
		
		ws3.setType(SessionTypes.DAY_OFF); //overriding wednesday's work with a dayoff | dayoff > workday
		try {
			wdao.createWorkSession(ws3);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		try {
			salary = Calculations.getSalaryInAMonthByEmployeeId(100, ws1.getDate());
		} catch (SalaryException e) {
			salary = e.getSalary();
		}
		workhours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws2.getDate());
		assertEquals(12, workhours);
		assertEquals(new BigDecimal("1875").doubleValue(), salary.doubleValue(), 0.000001);
		
		date = new LocalDate(2012, 1, 7); //saturday
		WorkSession ws4 = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws4);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		try {
			salary = Calculations.getSalaryInAMonthByEmployeeId(100, ws1.getDate());
		} catch (SalaryException e) {
			salary = e.getSalary();
		}
		workhours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws2.getDate());
		assertEquals(20, workhours);
		//this week so far: 1875, he went in on saturday(1.5x) with 8h = 750+
		assertEquals(new BigDecimal("2625").doubleValue(), salary.doubleValue(), 0.000001);
		
		ws4.setType(SessionTypes.SICKNESS);
		try {
			wdao.createWorkSession(ws4);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		try {
			salary = Calculations.getSalaryInAMonthByEmployeeId(100, ws1.getDate());
		} catch (SalaryException e) {
			salary = e.getSalary();
		}
		workhours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws2.getDate());
		assertEquals(12, workhours);
		//this week so far: 1875, he went in on saturday(1.5x) with 8h = 750+, he went home cuz of sickness-> no money for today :(
		assertEquals(new BigDecimal("1875").doubleValue(), salary.doubleValue(), 0.000001);
		
		date = date.plusDays(1); //sunday
		WorkSession ws5 = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws5);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		try {
			salary = Calculations.getSalaryInAMonthByEmployeeId(100, ws1.getDate());
		} catch (SalaryException e) {
			salary = e.getSalary();
		}
		workhours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws2.getDate());
		assertEquals(20, workhours);
		//this week so far: 1875, he went in on sunday(2x) with 8h = 1000+
		assertEquals(new BigDecimal("2875").doubleValue(), salary.doubleValue(), 0.000001);
		
		ws5.setType(SessionTypes.DAY_OFF);
		try {
			wdao.createWorkSession(ws5);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		try {
			salary = Calculations.getSalaryInAMonthByEmployeeId(100, ws1.getDate());
		} catch (SalaryException e) {
			salary = e.getSalary();
		}
		workhours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws2.getDate());
		assertEquals(12, workhours);
		//this week so far: 1875, he went in on sunday(2x) with 8h = 1000+ but he went home cuz he didn't want to work on sunday -> no money for that day :(
		assertEquals(new BigDecimal("1875").doubleValue(), salary.doubleValue(), 0.000001);
		
		date = new LocalDate(2012, 1, 14); //saturday
		WorkSession ws6 = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "SICKNESS");
		try {
			wdao.createWorkSession(ws6);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		try {
			salary = Calculations.getSalaryInAMonthByEmployeeId(100, ws1.getDate());
		} catch (SalaryException e) {
			salary = e.getSalary();
		}
		workhours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws2.getDate());
		assertEquals(12, workhours);
		// 1875 so far, he tried tried to insert a sickday on saturday, but it has no effect to money :(
		assertEquals(new BigDecimal("1875").doubleValue(), salary.doubleValue(), 0.000001);
		
		date = date.plusDays(1); //sunday
		WorkSession ws7 = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "DAY_OFF");
		try {
			wdao.createWorkSession(ws7);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		try {
			salary = Calculations.getSalaryInAMonthByEmployeeId(100, ws1.getDate());
		} catch (SalaryException e) {
			salary = e.getSalary();
		}
		workhours = Calculations.getWorkedHoursInAMonthByEmployeeId(100, ws2.getDate());
		assertEquals(12, workhours);
		// 1875 so far, he tried tried to insert a dayoff to sunday, but it has no effect
		assertEquals(new BigDecimal("1875").doubleValue(), salary.doubleValue(), 0.000001);
	}

}
