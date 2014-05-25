package dao.impl;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Test;

import core.Calculations;
import dao.AlreadyReachedFifteenSickDays;
import dao.AlreadyReachedThirtyDayOffs;
import dao.TooMuchWorkOnADayException;
import dao.WorkSession;
import dao.WorkSessionDAO;
import dao.WorkSession.SessionTypes;

public class WorkSessionDAOImplTest {
	static WorkSessionDAO wdao;
	
	@BeforeClass
	public static void prepareTests() {
		wdao = new JDBCWorkSessionDAOImpl();
	}

	@Test
	public void testFindWorkSessionsByEmployeeId() {
		WorkSession ws1 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws1);
			List<WorkSession> wlist = wdao.findWorkSessionsByEmployeeId(100);
			assertEquals(1, wlist.size());
			assertEquals(ws1.getDate(), wlist.get(0).getDate());
			assertEquals(ws1.getDuration(), wlist.get(0).getDuration());
			assertEquals(WorkSession.SessionTypes.WORK, wlist.get(0).getType());
			wdao.deleteWorkSession(wlist.get(0));
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
	}
	
	@Test
	public void testFindWorkSessionsByEmployeeId2() {
		WorkSession ws1 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)8, "WORK");
		WorkSession ws2 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,2).toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws1);
			wdao.createWorkSession(ws2);
			List<WorkSession> wlist = wdao.findWorkSessionsByEmployeeId(100);
			assertEquals(2, wlist.size());
			assertEquals(ws1.getDate(), wlist.get(0).getDate());
			assertEquals(ws1.getDuration(), wlist.get(0).getDuration());
			assertEquals(WorkSession.SessionTypes.WORK, wlist.get(0).getType());
			
			assertEquals(ws2.getDate(), wlist.get(1).getDate());
			assertEquals(ws2.getDuration(), wlist.get(1).getDuration());
			assertEquals(WorkSession.SessionTypes.WORK, wlist.get(1).getType());
			
			wdao.deleteWorkSession(wlist.get(0));
			wdao.deleteWorkSession(wlist.get(1));
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
	}

	@Test
	public void testCreateWorkSession() {
		WorkSession ws1 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)8, "WORK");
		WorkSession ws2 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,2).toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws1);
			wdao.createWorkSession(ws2);
			List<WorkSession> wlist = wdao.findWorkSessionsByEmployeeId(100);
			assertEquals(2, wlist.size());
			assertEquals(ws1.getDate(), wlist.get(0).getDate());
			assertEquals(ws1.getDuration(), wlist.get(0).getDuration());
			assertEquals(WorkSession.SessionTypes.WORK, wlist.get(0).getType());
			
			assertEquals(ws2.getDate(), wlist.get(1).getDate());
			assertEquals(ws2.getDuration(), wlist.get(1).getDuration());
			assertEquals(WorkSession.SessionTypes.WORK, wlist.get(1).getType());
			
			wdao.deleteWorkSession(wlist.get(0));
			wdao.deleteWorkSession(wlist.get(1));
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
	}
	
	@Test
	public void testCreateWorkSessionTooMuchWorkOnADay() {
		WorkSession ws1 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)8, "WORK");
		WorkSession ws2 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)4, "WORK");
		WorkSession ws3 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)1, "WORK");
		try {
			wdao.createWorkSession(ws1);
			wdao.createWorkSession(ws2);//fine just 12 hours
			wdao.createWorkSession(ws3);
			fail("should get here");
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			List<WorkSession> wlist = wdao.findWorkSessionsByEmployeeId(100);
			for (WorkSession workSession : wlist) {
				wdao.deleteWorkSession(workSession);
			}
			assertTrue(e instanceof TooMuchWorkOnADayException);
		}
	}
	
	@Test
	public void testCreateWorkSessionAddDayOffToADay() {
		WorkSession ws1 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)8, "WORK");
		WorkSession ws2 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)4, "WORK");
		WorkSession ws3 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)2, "DAY_OFF");
		try {
			wdao.createWorkSession(ws1);
			wdao.createWorkSession(ws2);//fine just 12 hours
			wdao.createWorkSession(ws3);//still fine
			List<WorkSession> wlist = wdao.findWorkSessionsByEmployeeId(100);
			for (WorkSession workSession : wlist) {
				wdao.deleteWorkSession(workSession);
			}
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
	}
	
	@Test
	public void testCreateWorkSessionAddSickDayToADay() {
		WorkSession ws1 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)8, "WORK");
		WorkSession ws2 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)4, "WORK");
		WorkSession ws3 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)2, "SICKNESS");
		try {
			wdao.createWorkSession(ws1);
			wdao.createWorkSession(ws2);//fine just 12 hours
			wdao.createWorkSession(ws3);//still fine
			List<WorkSession> wlist = wdao.findWorkSessionsByEmployeeId(100);
			for (WorkSession workSession : wlist) {
				wdao.deleteWorkSession(workSession);
			}
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
	}
	
	@Test
	public void testCreateWorkSessionAlreadyReachedThirtyDaysOfssExcpTest() {
		LocalDate date = new LocalDate(2012,1,1);
		int count = 0;
		while(count < 30) {
			WorkSession w = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "DAY_OFF");
			if (date.getDayOfWeek() < 6) {
				try {
					wdao.createWorkSession(w);
					count++;
				} catch (TooMuchWorkOnADayException
						| AlreadyReachedThirtyDayOffs
						| AlreadyReachedFifteenSickDays e) {
					System.out.println(e.getMessage());
				}
			}
			date = date.plusDays(1);
		}

		List<WorkSession> list = wdao.findWorkSessionsByEmployeeId(100);
		
		assertEquals(30, list.size());
		while(date.getDayOfWeek() > 5) {
			date = date.plusDays(1);
		}
		WorkSession w = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "DAY_OFF");
		try {
			wdao.createWorkSession(w);
			fail(":(");
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			for (WorkSession workSession : list) {
				wdao.deleteWorkSession(workSession);
			}
			assertTrue(e instanceof AlreadyReachedThirtyDayOffs);
		}
		
	}
	
	@Test
	public void testCreateWorkSessionAlreadyReachedFifteenSickDayExcTest() {
		LocalDate date = new LocalDate(2012,1,1);
		int count = 0;
		while(count < 15) {
			WorkSession w = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "SICKNESS");
			if (date.getDayOfWeek() < 6) {
				try {
					wdao.createWorkSession(w);
					count++;
				} catch (TooMuchWorkOnADayException
						| AlreadyReachedThirtyDayOffs
						| AlreadyReachedFifteenSickDays e) {
					System.out.println(e.getMessage());
				}
			}
			date = date.plusDays(1);
		}

		List<WorkSession> list = wdao.findWorkSessionsByEmployeeId(100);
		
		assertEquals(15, list.size());
		while(date.getDayOfWeek() > 5) {
			date = date.plusDays(1);
		}
		WorkSession w = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "SICKNESS"); //16th
		try {
			wdao.createWorkSession(w);
			fail(":(");
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			for (WorkSession workSession : list) {
				wdao.deleteWorkSession(workSession);
			}
			assertTrue(e instanceof AlreadyReachedFifteenSickDays);
		}
		
	}

	@Test
	public void testDeleteWorkSession() {
		WorkSession ws1 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws1);
			List<WorkSession> list = wdao.findWorkSessionsByEmployeeId(100);
			assertEquals(1, list.size());
			wdao.deleteWorkSession(list.get(0));
			list = wdao.findWorkSessionsByEmployeeId(100);
			assertEquals(0, list.size());
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		
	}

	@Test
	public void testUpdateWorkSession() {
		WorkSession ws1 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws1);
			List<WorkSession> list = wdao.findWorkSessionsByEmployeeId(100);
			assertEquals(1, list.size());
			WorkSession w = list.get(0);
			w.setType(SessionTypes.SICKNESS);
			wdao.updateWorkSession(w);
			
			list = wdao.findWorkSessionsByEmployeeId(100);
			assertEquals(1, list.size());
			WorkSession w2 = list.get(0);
			for (WorkSession workSession : list) {
				wdao.deleteWorkSession(workSession);
			}
			
			assertEquals(SessionTypes.SICKNESS, w2.getType());
			assertEquals(w.getDate(), w2.getDate());
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
	}
	
	@Test
	public void testUpdateWorkSessionAlreadyReachedThirtyDaysOfssExcpTest() {
		LocalDate date = new LocalDate(2012,1,1);
		int count = 0;
		while(count < 30) {
			WorkSession w = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "DAY_OFF");
			if (date.getDayOfWeek() < 6) {
				try {
					wdao.createWorkSession(w);
					count++;
				} catch (TooMuchWorkOnADayException
						| AlreadyReachedThirtyDayOffs
						| AlreadyReachedFifteenSickDays e) {
					System.out.println(e.getMessage());
				}
			}
			date = date.plusDays(1);
		}

		List<WorkSession> list = wdao.findWorkSessionsByEmployeeId(100);
		
		assertEquals(30, list.size());
		while(date.getDayOfWeek() > 5) {
			date = date.plusDays(1);
		}
		WorkSession w = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(w);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		list = wdao.findWorkSessionsByEmployeeId(100);
		for (WorkSession workSession : list) {
			if (workSession.getType().equals(SessionTypes.WORK)) {
				workSession.setType(SessionTypes.DAY_OFF);
				try {
					wdao.updateWorkSession(workSession);
				} catch (AlreadyReachedThirtyDayOffs
						| AlreadyReachedFifteenSickDays
						| TooMuchWorkOnADayException e) {
					assertTrue(e instanceof AlreadyReachedThirtyDayOffs);
				}
				break;
			}
		}
		for (WorkSession workSession : list) {
			wdao.deleteWorkSession(workSession);
		}
		
	}
	
	@Test
	public void testUpdateWorkSessionAlreadyReachedFifteenSickDaysExcpTest() {
		LocalDate date = new LocalDate(2012,1,1);
		int count = 0;
		while(count < 15) {
			WorkSession w = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "SICKNESS");
			if (date.getDayOfWeek() < 6) {
				try {
					wdao.createWorkSession(w);
					count++;
				} catch (TooMuchWorkOnADayException
						| AlreadyReachedThirtyDayOffs
						| AlreadyReachedFifteenSickDays e) {
					System.out.println(e.getMessage());
				}
			}
			date = date.plusDays(1);
		}

		List<WorkSession> list = wdao.findWorkSessionsByEmployeeId(100);
		
		assertEquals(15, list.size());
		while(date.getDayOfWeek() > 5) {
			date = date.plusDays(1);
		}
		WorkSession w = new WorkSession(1, 100, new Date(date.toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(w);
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
			fail(":(");
		}
		list = wdao.findWorkSessionsByEmployeeId(100);
		for (WorkSession workSession : list) {
			if (workSession.getType().equals(SessionTypes.WORK)) {
				workSession.setType(SessionTypes.SICKNESS);
				try {
					wdao.updateWorkSession(workSession);
				} catch (AlreadyReachedThirtyDayOffs
						| AlreadyReachedFifteenSickDays
						| TooMuchWorkOnADayException e) {
					assertTrue(e instanceof AlreadyReachedFifteenSickDays);
				}
				break;
			}
		}
		for (WorkSession workSession : list) {
			wdao.deleteWorkSession(workSession);
		}
		
	}
	
	@Test
	public void testUpdateWorkSessionTooMuchWorkOnADayExcTest() {
		WorkSession ws1 = new WorkSession(1, 100, new Date(new LocalDate(2012,1,1).toDate().getTime()), (short)8, "WORK");
		try {
			wdao.createWorkSession(ws1);
			List<WorkSession> list = wdao.findWorkSessionsByEmployeeId(100);
			assertEquals(1, list.size());
			WorkSession w = list.get(0);
			w.setDuration((short)13);
			wdao.updateWorkSession(w);
			fail(":(");
		} catch (TooMuchWorkOnADayException | AlreadyReachedThirtyDayOffs
				| AlreadyReachedFifteenSickDays e) {
				List<WorkSession> wlist = wdao.findWorkSessionsByEmployeeId(100);
				for (WorkSession workSession : wlist) {
					wdao.deleteWorkSession(workSession);
				}
				assertTrue(e instanceof TooMuchWorkOnADayException);
			}
	}
}
