/**
 * 
 */
package core;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import dao.Employee;
import dao.PersistentLayerException;
import dao.WorkSession;
import dao.impl.ConnectionHelper;

/**
 * @author bnorbi
 *
 */
public class Calculations {
	private static final String findWorkSessionsInAMonthWithATypeByEmployeeId =
			"select session_date, type, sum(duration) duration "
			+ "from worksessions "
			+ "where employee_id = ? and "
			+ "to_char(session_date, 'YYYY.MM') = to_char(?, 'YYYY.MM') "
			+ "group by session_date, type";

	public static int getAllWorkedHourByEmployeeId(int id) {
		List<WorkSession> workDaysTemp = new ArrayList<>();
		List<WorkSession> sickDaysTemp = new ArrayList<>();
		List<WorkSession> dayOffsTemp = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select session_date, type, sum(duration) duration from worksessions"
				+ " where employee_id = ? group by session_date, type")) {
			pstmt.setInt(1, id);
			try(ResultSet rset = pstmt.executeQuery()) {
				int temp_id = 1;
				while (rset.next()) {
					WorkSession ws = new WorkSession(temp_id++, id, rset.getDate("session_date"), (short)rset.getInt("duration"), rset.getString("type"));
					switch (ws.getType()) {
					case WORK:
						workDaysTemp.add(ws);
						break;
					case SICKNESS:
						sickDaysTemp.add(ws);
						break;
					case DAY_OFF:
						dayOffsTemp.add(ws);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
		List<WorkSession> workDays = new ArrayList<>();
		
		for (WorkSession workDay : workDaysTemp) {
			boolean contains = false;
			for (WorkSession sickDay : sickDaysTemp) {
				if (workDay.getDate().equals(sickDay.getDate())) {
					contains = true;
				}
			}
			for (WorkSession dayOff : dayOffsTemp) {
				if (workDay.getDate().equals(dayOff.getDate())) {
					contains = true;
				}
			}
			if (!contains) {
				workDays.add(workDay);
			}
		}
		int sum = 0;
		for (WorkSession workSession : workDays) {
			sum += workSession.getDuration();
		}
		return sum;
	}
	public static int getAllWorkedHourByEmployee(Employee e) {
		List<WorkSession> workDaysTemp = new ArrayList<>();
		List<WorkSession> sickDaysTemp = new ArrayList<>();
		List<WorkSession> dayOffsTemp = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select session_date, type, sum(duration) duration from worksessions"
				+ " where employee_id = ? group by session_date, type")) {
			pstmt.setInt(1, e.getId());
			try(ResultSet rset = pstmt.executeQuery()) {
				int temp_id = 1;
				while (rset.next()) {
					WorkSession ws = new WorkSession(temp_id++, e.getId(), rset.getDate("session_date"), (short)rset.getInt("duration"), rset.getString("type"));
					switch (ws.getType()) {
					case WORK:
						workDaysTemp.add(ws);
						break;
					case SICKNESS:
						sickDaysTemp.add(ws);
						break;
					case DAY_OFF:
						dayOffsTemp.add(ws);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException | IOException e1) {
			throw new PersistentLayerException(e1);
		}
		List<WorkSession> workDays = new ArrayList<>();
		
		for (WorkSession workDay : workDaysTemp) {
			boolean contains = false;
			for (WorkSession sickDay : sickDaysTemp) {
				if (workDay.getDate().equals(sickDay.getDate())) {
					contains = true;
				}
			}
			for (WorkSession dayOff : dayOffsTemp) {
				if (workDay.getDate().equals(dayOff.getDate())) {
					contains = true;
				}
			}
			if (!contains) {
				workDays.add(workDay);
			}
		}
		int sum = 0;
		for (WorkSession workSession : workDays) {
			sum += workSession.getDuration();
		}
		return sum;
	}
	
	public static int getWorkedHoursOnADayByEmployeeId(int id, Date date) {
		List<WorkSession> workDaysTemp = new ArrayList<>();
		List<WorkSession> sickDaysTemp = new ArrayList<>();
		List<WorkSession> dayOffsTemp = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select type, sum(duration) duration from worksessions"
				+ " where employee_id = ? and session_date = ? group by type")) {
			pstmt.setInt(1, id);
			pstmt.setDate(2, date);
			try(ResultSet rset = pstmt.executeQuery()) {
				int temp_id = 1;
				while (rset.next()) {
					WorkSession ws = new WorkSession(temp_id++, id, date, (short)rset.getInt("duration"), rset.getString("type"));
					switch (ws.getType()) {
					case WORK:
						workDaysTemp.add(ws);
						break;
					case SICKNESS:
						sickDaysTemp.add(ws);
						break;
					case DAY_OFF:
						dayOffsTemp.add(ws);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
		List<WorkSession> workDays = new ArrayList<>();
		
		for (WorkSession workDay : workDaysTemp) {
			boolean contains = false;
			for (WorkSession sickDay : sickDaysTemp) {
				if (workDay.getDate().equals(sickDay.getDate())) {
					contains = true;
				}
			}
			for (WorkSession dayOff : dayOffsTemp) {
				if (workDay.getDate().equals(dayOff.getDate())) {
					contains = true;
				}
			}
			if (!contains) {
				workDays.add(workDay);
			}
		}
		int sum = 0;
		for (WorkSession workSession : workDays) {
			sum += workSession.getDuration();
		}
		return sum;
	}
	
	public static int getWorkedHoursOnADayByEmployee(Employee e, Date date) {
		List<WorkSession> workDaysTemp = new ArrayList<>();
		List<WorkSession> sickDaysTemp = new ArrayList<>();
		List<WorkSession> dayOffsTemp = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select type, sum(duration) duration from worksessions"
				+ " where employee_id = ? and session_date = ? group by type")) {
			pstmt.setInt(1, e.getId());
			pstmt.setDate(2, date);
			try(ResultSet rset = pstmt.executeQuery()) {
				int temp_id = 1;
				while (rset.next()) {
					WorkSession ws = new WorkSession(temp_id++, e.getId(), rset.getDate("session_date"), (short)rset.getInt("duration"), rset.getString("type"));
					switch (ws.getType()) {
					case WORK:
						workDaysTemp.add(ws);
						break;
					case SICKNESS:
						sickDaysTemp.add(ws);
						break;
					case DAY_OFF:
						dayOffsTemp.add(ws);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException | IOException e1) {
			throw new PersistentLayerException(e1);
		}
		List<WorkSession> workDays = new ArrayList<>();
		
		for (WorkSession workDay : workDaysTemp) {
			boolean contains = false;
			for (WorkSession sickDay : sickDaysTemp) {
				if (workDay.getDate().equals(sickDay.getDate())) {
					contains = true;
				}
			}
			for (WorkSession dayOff : dayOffsTemp) {
				if (workDay.getDate().equals(dayOff.getDate())) {
					contains = true;
				}
			}
			if (!contains) {
				workDays.add(workDay);
			}
		}
		int sum = 0;
		for (WorkSession workSession : workDays) {
			sum += workSession.getDuration();
		}
		return sum;
	}
	
	public static int getWorkedHoursOnAWeekByEmployeeId(int id, Date date) {
		LocalDate startDate = new LocalDate(date);
		int dayOfWeek = startDate.getDayOfWeek();
		startDate = startDate.minusDays(dayOfWeek -1);
		LocalDate endDate = startDate.plusDays(6);
		List<WorkSession> workDaysTemp = new ArrayList<>();
		List<WorkSession> sickDaysTemp = new ArrayList<>();
		List<WorkSession> dayOffsTemp = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select session_date, type, sum(duration) duration from worksessions"
				+ " where employee_id = ? and session_date between ? and ? group by session_date, type")) {
			pstmt.setInt(1, id);
			pstmt.setDate(2, new Date(startDate.toDate().getTime()));
			pstmt.setDate(3, new Date(endDate.toDate().getTime()));
			try(ResultSet rset = pstmt.executeQuery()) {
				int temp_id = 1;
				while (rset.next()) {
					WorkSession ws = new WorkSession(temp_id++, id, rset.getDate("session_date"), (short)rset.getInt("duration"), rset.getString("type"));
					switch (ws.getType()) {
					case WORK:
						workDaysTemp.add(ws);
						break;
					case SICKNESS:
						sickDaysTemp.add(ws);
						break;
					case DAY_OFF:
						dayOffsTemp.add(ws);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
		List<WorkSession> workDays = new ArrayList<>();
		
		for (WorkSession workDay : workDaysTemp) {
			boolean contains = false;
			for (WorkSession sickDay : sickDaysTemp) {
				if (workDay.getDate().equals(sickDay.getDate())) {
					contains = true;
				}
			}
			for (WorkSession dayOff : dayOffsTemp) {
				if (workDay.getDate().equals(dayOff.getDate())) {
					contains = true;
				}
			}
			if (!contains) {
				workDays.add(workDay);
			}
		}
		int sum = 0;
		for (WorkSession workSession : workDays) {
			sum += workSession.getDuration();
		}
		return sum;
	}
	
	public static int getWorkedHoursOnAWeekByEmployee(Employee e, Date date) {
		LocalDate startDate = new LocalDate(date);
		int dayOfWeek = startDate.getDayOfWeek();
		startDate = startDate.minusDays(dayOfWeek -1);
		LocalDate endDate = startDate.plusDays(6);
		List<WorkSession> workDaysTemp = new ArrayList<>();
		List<WorkSession> sickDaysTemp = new ArrayList<>();
		List<WorkSession> dayOffsTemp = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select session_date, type, sum(duration) duration from worksessions"
				+ " where employee_id = ? and session_date between ? and ? group by session_date, type")) {
			pstmt.setInt(1, e.getId());
			pstmt.setDate(2, new Date(startDate.toDate().getTime()));
			pstmt.setDate(3, new Date(endDate.toDate().getTime()));
			try(ResultSet rset = pstmt.executeQuery()) {
				int temp_id = 1;
				while (rset.next()) {
					WorkSession ws = new WorkSession(temp_id++, e.getId(), rset.getDate("session_date"), (short)rset.getInt("duration"), rset.getString("type"));
					switch (ws.getType()) {
					case WORK:
						workDaysTemp.add(ws);
						break;
					case SICKNESS:
						sickDaysTemp.add(ws);
						break;
					case DAY_OFF:
						dayOffsTemp.add(ws);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException | IOException e1) {
			throw new PersistentLayerException(e1);
		}
		List<WorkSession> workDays = new ArrayList<>();
		
		for (WorkSession workDay : workDaysTemp) {
			boolean contains = false;
			for (WorkSession sickDay : sickDaysTemp) {
				if (workDay.getDate().equals(sickDay.getDate())) {
					contains = true;
				}
			}
			for (WorkSession dayOff : dayOffsTemp) {
				if (workDay.getDate().equals(dayOff.getDate())) {
					contains = true;
				}
			}
			if (!contains) {
				workDays.add(workDay);
			}
		}
		int sum = 0;
		for (WorkSession workSession : workDays) {
			sum += workSession.getDuration();
		}
		return sum;
	}
	
	public static int getWorkedHoursInAMonthByEmployeeId(int id, Date date) {
		LocalDate startOfMonth = new LocalDate(date);
		startOfMonth = startOfMonth.minusDays(startOfMonth.getDayOfMonth() - 1);
		LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
		List<WorkSession> workDaysTemp = new ArrayList<>();
		List<WorkSession> sickDaysTemp = new ArrayList<>();
		List<WorkSession> dayOffsTemp = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select session_date, type, sum(duration) duration from worksessions"
				+ " where employee_id = ? and session_date between ? and ? group by session_date, type")) {
			pstmt.setInt(1, id);
			pstmt.setDate(2, new Date(startOfMonth.toDate().getTime()));
			pstmt.setDate(3, new Date(endOfMonth.toDate().getTime()));
			try(ResultSet rset = pstmt.executeQuery()) {
				int temp_id = 1;
				while (rset.next()) {
					WorkSession ws = new WorkSession(temp_id++, id, rset.getDate("session_date"), (short)rset.getInt("duration"), rset.getString("type"));
					switch (ws.getType()) {
					case WORK:
						workDaysTemp.add(ws);
						break;
					case SICKNESS:
						sickDaysTemp.add(ws);
						break;
					case DAY_OFF:
						dayOffsTemp.add(ws);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException | IOException e1) {
			throw new PersistentLayerException(e1);
		}
		List<WorkSession> workDays = new ArrayList<>();
		
		for (WorkSession workDay : workDaysTemp) {
			boolean contains = false;
			for (WorkSession sickDay : sickDaysTemp) {
				if (workDay.getDate().equals(sickDay.getDate())) {
					contains = true;
				}
			}
			for (WorkSession dayOff : dayOffsTemp) {
				if (workDay.getDate().equals(dayOff.getDate())) {
					contains = true;
				}
			}
			if (!contains) {
				workDays.add(workDay);
			}
		}
		int sum = 0;
		for (WorkSession workSession : workDays) {
			sum += workSession.getDuration();
		}
		return sum;
	}
	
	public static int getWorkedHoursInAMonthByEmployee(Employee e, Date date) {
		LocalDate startOfMonth = new LocalDate(date);
		startOfMonth = startOfMonth.minusDays(startOfMonth.getDayOfMonth() - 1);
		LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
		List<WorkSession> workDaysTemp = new ArrayList<>();
		List<WorkSession> sickDaysTemp = new ArrayList<>();
		List<WorkSession> dayOffsTemp = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select session_date, type, sum(duration) duration from worksessions"
				+ " where employee_id = ? and session_date between ? and ? group by session_date, type")) {
			pstmt.setInt(1, e.getId());
			pstmt.setDate(2, new Date(startOfMonth.toDate().getTime()));
			pstmt.setDate(3, new Date(endOfMonth.toDate().getTime()));
			try(ResultSet rset = pstmt.executeQuery()) {
				int temp_id = 1;
				while (rset.next()) {
					WorkSession ws = new WorkSession(temp_id++, e.getId(), rset.getDate("session_date"), (short)rset.getInt("duration"), rset.getString("type"));
					switch (ws.getType()) {
					case WORK:
						workDaysTemp.add(ws);
						break;
					case SICKNESS:
						sickDaysTemp.add(ws);
						break;
					case DAY_OFF:
						dayOffsTemp.add(ws);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException | IOException e1) {
			throw new PersistentLayerException(e1);
		}
		List<WorkSession> workDays = new ArrayList<>();
		
		for (WorkSession workDay : workDaysTemp) {
			boolean contains = false;
			for (WorkSession sickDay : sickDaysTemp) {
				if (workDay.getDate().equals(sickDay.getDate())) {
					contains = true;
				}
			}
			for (WorkSession dayOff : dayOffsTemp) {
				if (workDay.getDate().equals(dayOff.getDate())) {
					contains = true;
				}
			}
			if (!contains) {
				workDays.add(workDay);
			}
		}
		int sum = 0;
		for (WorkSession workSession : workDays) {
			sum += workSession.getDuration();
		}
		return sum;
	}
	
	public static int getSickDaysInCurrentYearByEmployeeId(int id) {
		List<WorkSession> sickDaysTemp = new ArrayList<>();
		List<WorkSession> dayOffsTemp = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select session_date, type from worksessions"
				+ " where employee_id = ? and to_char(session_date, 'YYYY') = to_char(sysdate, 'YYYY') group by session_date, type")) {
			pstmt.setInt(1, id);
			try(ResultSet rset = pstmt.executeQuery()) {
				int temp_id = 1;
				while (rset.next()) {
					WorkSession ws = new WorkSession(temp_id++, id, rset.getDate("session_date"), (short)8, rset.getString("type"));
					switch (ws.getType()) {
					case SICKNESS:
						sickDaysTemp.add(ws);
						break;
					case DAY_OFF:
						dayOffsTemp.add(ws);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
		
		List<WorkSession> sickDays = new ArrayList<>();
		List<WorkSession> dayOffs = new ArrayList<>();
		
		for (WorkSession sickDay : sickDaysTemp) {
			boolean contains = false;
			for (WorkSession dayOff : dayOffsTemp) {
				if (sickDay.getDate().equals(dayOff.getDate())) {
					contains = true;
				}
			}
			LocalDate ldate = new LocalDate(sickDay.getDate());
			if (!contains && ldate.getDayOfWeek() < 6) {
				sickDays.add(sickDay);
			}
		}
		return sickDays.size();
	}
	
	public static int getSickDaysInAMonthByEmployeeId(int id, Date date) {
		List<WorkSession> sickDaysTemp = new ArrayList<>();
		List<WorkSession> dayOffsTemp = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select session_date, type from worksessions"
				+ " where employee_id = ? and to_char(session_date, 'YYYY.MM') = to_char(?, 'YYYY.MM') group by session_date, type")) {
			pstmt.setInt(1, id);
			pstmt.setDate(2, date);
			try(ResultSet rset = pstmt.executeQuery()) {
				int temp_id = 1;
				while (rset.next()) {
					WorkSession ws = new WorkSession(temp_id++, id, rset.getDate("session_date"), (short)8, rset.getString("type"));
					switch (ws.getType()) {
					case SICKNESS:
						sickDaysTemp.add(ws);
						break;
					case DAY_OFF:
						dayOffsTemp.add(ws);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
		
		List<WorkSession> sickDays = new ArrayList<>();
		List<WorkSession> dayOffs = new ArrayList<>();
		
		for (WorkSession sickDay : sickDaysTemp) {
			boolean contains = false;
			for (WorkSession dayOff : dayOffsTemp) {
				if (sickDay.getDate().equals(dayOff.getDate())) {
					contains = true;
				}
			}
			LocalDate ldate = new LocalDate(sickDay.getDate());
			if (!contains && ldate.getDayOfWeek() < 6) {
				sickDays.add(sickDay);
			}
		}
		return sickDays.size();
	}
	
	public static int getDayOffsInCurrentYearByEmployeeId(int id) {
		List<WorkSession> dayOffsTemp = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select session_date, type from worksessions"
				+ " where employee_id = ? and to_char(session_date, 'YYYY') = to_char(sysdate, 'YYYY') group by session_date, type")) {
			pstmt.setInt(1, id);
			try(ResultSet rset = pstmt.executeQuery()) {
				int temp_id = 1;
				while (rset.next()) {
					WorkSession ws = new WorkSession(temp_id++, id, rset.getDate("session_date"), (short)8, rset.getString("type"));
					switch (ws.getType()) {
					case DAY_OFF:
						dayOffsTemp.add(ws);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
		
		List<WorkSession> dayOffs = new ArrayList<>();
		
		for (WorkSession dayOff : dayOffsTemp) {
			LocalDate ldate = new LocalDate(dayOff.getDate());
			if (ldate.getDayOfWeek() < 6) {
				dayOffs.add(dayOff);
			}
		}
		return dayOffs.size();
	}
	
	public static int getDayOffsInAMonthByEmployeeId(int id, Date date) {
		List<WorkSession> dayOffsTemp = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select session_date, type from worksessions"
				+ " where employee_id = ? and to_char(session_date, 'YYYY.MM') = to_char(?, 'YYYY.MM') group by session_date, type")) {
			pstmt.setInt(1, id);
			pstmt.setDate(2, date);
			try(ResultSet rset = pstmt.executeQuery()) {
				int temp_id = 1;
				while (rset.next()) {
					WorkSession ws = new WorkSession(temp_id++, id, rset.getDate("session_date"), (short)8, rset.getString("type"));
					switch (ws.getType()) {
					case DAY_OFF:
						dayOffsTemp.add(ws);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
		
		List<WorkSession> dayOffs = new ArrayList<>();
		
		for (WorkSession dayOff : dayOffsTemp) {
			LocalDate ldate = new LocalDate(dayOff.getDate());
			if (ldate.getDayOfWeek() < 6) {
				dayOffs.add(dayOff);
			}
		}
		return dayOffs.size();
	}
	
	public static BigDecimal getSalaryInAMonthByEmployeeId(int id, Date date) throws TooManyLessThanFourHourWorkday {
		List<WorkSession> workDaysTemp = new ArrayList<>();
		List<WorkSession> sickDaysTemp = new ArrayList<>();
		List<WorkSession> dayOffsTemp = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement(findWorkSessionsInAMonthWithATypeByEmployeeId)) {
			pstmt.setInt(1, id);
			pstmt.setDate(2, date);
			try(ResultSet rset = pstmt.executeQuery()) {
				int temp_id = 1;
				while (rset.next()) {
					WorkSession ws = new WorkSession(temp_id++, id, rset.getDate("session_date"), (short)rset.getInt("duration"), rset.getString("type"));
					switch (ws.getType()) {
					case WORK:
						workDaysTemp.add(ws);
						break;
					case SICKNESS:
						sickDaysTemp.add(ws);
						break;
					case DAY_OFF:
						dayOffsTemp.add(ws);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
		List<WorkSession> workDays = new ArrayList<>();
		List<WorkSession> sickDays = new ArrayList<>();
		List<WorkSession> dayOffs = new ArrayList<>();
		
		for (WorkSession workDay : workDaysTemp) {
			boolean contains = false;
			for (WorkSession sickDay : sickDaysTemp) {
				if (workDay.getDate().equals(sickDay.getDate())) {
					contains = true;
				}
			}
			for (WorkSession dayOff : dayOffsTemp) {
				if (workDay.getDate().equals(dayOff.getDate())) {
					contains = true;
				}
			}
			if (!contains) {
				workDays.add(workDay);
			}
		}
		for (WorkSession sickDay : sickDaysTemp) {
			boolean contains = false;
			for (WorkSession dayOff : dayOffsTemp) {
				if (sickDay.getDate().equals(dayOff.getDate())) {
					contains = true;
				}
			}
			LocalDate ldate = new LocalDate(sickDay.getDate());
			if (!contains && ldate.getDayOfWeek() < 6) {
				sickDays.add(sickDay);
			}
		}
		for (WorkSession dayOff : dayOffsTemp) {
			LocalDate ldate = new LocalDate(dayOff.getDate());
			if (ldate.getDayOfWeek() < 6) {
				dayOffs.add(dayOff);
			}
		}
		int lessThanFourHourWorkDayCount = 0;
		for (WorkSession workDay : workDays) {
			LocalDate ldate = new LocalDate(workDay.getDate());
			if (workDay.getDuration() <= 4 && ldate.getDayOfWeek() < 6) { //worked less than 4 hour on a weekday
				lessThanFourHourWorkDayCount++;
			}
		}
		if (lessThanFourHourWorkDayCount > 10) {
			throw new TooManyLessThanFourHourWorkday(lessThanFourHourWorkDayCount);
		}
		
		
		BigDecimal baseSalary = new BigDecimal(0);
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select salary from employees where employee_id = ?")) {
			pstmt.setInt(1, id);
			try(ResultSet rset = pstmt.executeQuery()) {
				if (rset.next()) {
					baseSalary = rset.getBigDecimal("salary");
				}
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
		
		baseSalary = baseSalary.divide(new BigDecimal(20)).divide(new BigDecimal(8));
		//sickday's count * 8 hour
		BigDecimal salary = baseSalary.multiply(new BigDecimal(8)).multiply(new BigDecimal(sickDays.size()));
		//dayoff's count * 8 hour
		salary = salary.add(baseSalary.multiply(new BigDecimal(8)).multiply(new BigDecimal(dayOffs.size())));
		for (WorkSession workSession : workDays) {
			LocalDate ldate = new LocalDate(workSession.getDate());
			if (ldate.getDayOfWeek() < 6) { //weekdays
				if (workSession.getDuration() <=8) { //no plus hours
					salary = salary.add(baseSalary.multiply(new BigDecimal(workSession.getDuration())));
				} else { //plus hours
					short plusHours = (short)(workSession.getDuration() - 8);
					salary = salary.add(baseSalary.multiply(new BigDecimal(workSession.getDuration()))); // normal hours
					salary = salary.add( baseSalary.multiply(new BigDecimal(1.5)).multiply(new BigDecimal(plusHours)) ); // plus hours
				}
			} else { //saturday & sunday
				if (ldate.getDayOfWeek() == 6) { //saturday 1.5x multiplier
					salary = salary.add(baseSalary.multiply(new BigDecimal(1.5)).multiply(new BigDecimal(workSession.getDuration())));
				} else if (ldate.getDayOfWeek() == 7) { //sunday 2x multiplier
					salary = salary.add(baseSalary.multiply(new BigDecimal(2)).multiply(new BigDecimal(workSession.getDuration())));
				}
			}
		}
		
		return salary;
		
	}
	public static int getAllWorkedDayCurrentYearByEmployeeId(int id) {
		List<WorkSession> workDaysTemp = new ArrayList<>();
		List<WorkSession> sickDaysTemp = new ArrayList<>();
		List<WorkSession> dayOffsTemp = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select session_date, type from worksessions"
				+ " where employee_id = ? and to_char(session_date, 'YYYY') = to_char(sysdate, 'YYYY') group by session_date, type")) {
			pstmt.setInt(1, id);
			try(ResultSet rset = pstmt.executeQuery()) {
				int temp_id = 1;
				while (rset.next()) {
					WorkSession ws = new WorkSession(temp_id++, id, rset.getDate("session_date"), (short)8, rset.getString("type"));
					switch (ws.getType()) {
					case WORK:
						workDaysTemp.add(ws);
						break;
					case SICKNESS:
						sickDaysTemp.add(ws);
						break;
					case DAY_OFF:
						dayOffsTemp.add(ws);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
		List<WorkSession> workDays = new ArrayList<>();
		List<WorkSession> sickDays = new ArrayList<>();
		List<WorkSession> dayOffs = new ArrayList<>();
		
		for (WorkSession workDay : workDaysTemp) {
			boolean contains = false;
			for (WorkSession sickDay : sickDaysTemp) {
				if (workDay.getDate().equals(sickDay.getDate())) {
					contains = true;
				}
			}
			for (WorkSession dayOff : dayOffsTemp) {
				if (workDay.getDate().equals(dayOff.getDate())) {
					contains = true;
				}
			}
			if (!contains) {
				workDays.add(workDay);
			}
		}
		return workDays.size();
	}
}
