package core;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.LocalDate;

import dao.Employee;
import dao.PersistentLayerException;
import dao.WorkSession;
import dao.impl.ConnectionHelper;
import static core.Main.logger;

/**
 * Class implementing the main algorithms used in this program, to calculate sick days, days taken off, worked days, and even salary
 * from several viewpoints (yearly, monthly, weekly).
 * 
 * @see Employee
 * @see WorkSession
 */
public class Calculations {
	/**
	 * String that is used in a {@code PreparedStatement}, selecting {@code WorkSession}s by a specific {@code employee_id} in a specific {@code Date}'s year and month.
	 */
	private static final String findWorkSessionsInAMonthWithATypeByEmployeeId =
			"select session_date, type, sum(duration) duration "
			+ "from worksessions "
			+ "where employee_id = ? and "
			+ "to_char(session_date, 'YYYY.MM') = to_char(?, 'YYYY.MM') "
			+ "group by session_date, type";
	
	/**
	 * Returns a specific employee's free days on a specific week.
	 * <p>Saturday and Sunday cannot be day off.</p>
	 * <p>Work day is overwritten by the day off, sick days are also, <b>unless it's weekend, then it will not count as neither work day/sick day nor day off</b></p>
	 * 
	 * @param id the employee's id
	 * @param date the date that provides the week (can be any day on that week) we want to analyze
	 * @return the number of free days
	 */
	public static int getDayOffsOnAWeekByEmployeeId(int id, Date date) {
		LocalDate startDate = new LocalDate(date);
		int dayOfWeek = startDate.getDayOfWeek();
		startDate = startDate.minusDays(dayOfWeek -1);
		LocalDate endDate = startDate.plusDays(6);
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
			logger.error("Error while getting dayoffs on a week by employeeid", e);
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
	
	/**
	 * Returns a specific employee's sick days on a specific week.
	 * <p>Saturday and Sunday cannot be sick day.</p>
	 * <p>Work day is overwritten by a sick day, sick days are overwritten by free days, <b>unless it's weekend, then it will not count as neither work day/sick day nor day off</b></p>
	 * 
	 * @param id the employee's id
	 * @param date the date that provides the week (can be any day on that week) we want to analyze
	 * @return the number of sick days
	 */
	public static int getSickDaysOnAWeekByEmployeeId(int id, Date date) {
		LocalDate startDate = new LocalDate(date);
		int dayOfWeek = startDate.getDayOfWeek();
		startDate = startDate.minusDays(dayOfWeek -1);
		LocalDate endDate = startDate.plusDays(6);
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
			logger.error("Error while retrieving sickdays on a week by employeeid", e);
			throw new PersistentLayerException(e);
		}
		List<WorkSession> sickDays = new ArrayList<>();
		
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
	
	/**
	 * Returns an employee's worked hours on a specific week.
	 * 
	 * @param id the employee's id
	 * @param date the date that provides the week (can be any day on that week) we want to analyze
	 * @return the number of hours worked on that week
	 */
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
			logger.error("Error while retrieving worked hours on a week by employeeid", e);
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
	
	/**
	 * Returns an employee's worked hours in a specific month.
	 * 
	 * @param id the employee's id
	 * @param date the date that provides the month (can be any day in that month) we want to analyze
	 * @return the number of hours worked in that month
	 */
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
			logger.error("Error while retrieving worked hours in a month by employeeid", e1);
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
	
	/**
	 * Returns a specific employee's sick days in the current year.
	 * <p>Work days are overwritten by sick days unless it's weekend, then it's not counted for anywhere.</p>
	 * <p>Sick days are overwritten by free days unless it's weekend, then it's not counted for anywhere.</p>
	 * 
	 * @param id the employee's id we want to analyze
	 * @return the number of sick days in the current year (only weekdays)
	 */
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
			logger.error("Error while retrieving sick days in current year by employeeid", e);
			throw new PersistentLayerException(e);
		}
		
		List<WorkSession> sickDays = new ArrayList<>();
		
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
	
	/**
	 * Returns a specific employee's sick days in a specific month.
	 * <p>Work days are overwritten by sick days unless it's weekend, then it's not counted for anywhere.</p>
	 * <p>Sick days are overwritten by free days unless it's weekend, then it's not counted for anywhere.</p>
	 * 
	 * @param id the employee's id
	 * @param date the month (can be any of the month's day) we want to analyze
	 * @return the number of the employee's sick days (only weekdays)
	 */
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
			logger.error("Error while retrieving sick days in a month by employeeid", e);
			throw new PersistentLayerException(e);
		}
		
		List<WorkSession> sickDays = new ArrayList<>();
		
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
	
	/**
	 * Returns a specific employee's free days in the current year.
	 * <p>Work days are overwritten by sick days unless it's weekend, then it's not counted for anywhere.</p>
	 * <p>Sick days are overwritten by free days unless it's weekend, then it's not counted for anywhere.</p>
	 * 
	 * @param id the employee's id we want to analyze
	 * @return the number of free days in the current year (only weekdays)
	 */
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
			logger.error("Error while retrieving day offs in current year by employeeid", e);
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
	
	/**
	 * Returns a specific employee's free days in a specific month.
	 * <p>Work days are overwritten by sick days unless it's weekend, then it's not counted for anywhere.</p>
	 * <p>Sick days are overwritten by free days unless it's weekend, then it's not counted for anywhere.</p>
	 * 
	 * @param id the employee's id
	 * @param date the month (can be any of the month's day) we want to analyze
	 * @return the number of the employee's days taken off (only weekdays)
	 */
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
			logger.error("Error while retrieving dayoffs in a month by employeeid", e);
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
	
	/**
	 * Returns the salary earned by the specific employee in a specific month.
	 * <p>Work days are overwritten by sick days.</p>
	 * <p>Sick days are overwritten by free days.</p>
	 * <p>If a sick day is on weekend then it does not get into the final salary.</p>
	 * <p>If a day off is on weekend then it does not get into the final salary.</p>
	 * <p>Salary is retrieved from the database. That's the base salary.</p>
	 * <p>Base salary is for 20 worked days, with 8 hours per day.</p>
	 * <p>Sick days, and days taken off have the same hourly rate as worked days.</p>
	 * <p>Sick days, and days taken off are counted with 8 hours per day.</p>
	 * <p>If a person does overtime (over 8 hours per day), then the plus hours' rate has a 1.5x multiplier.</p>
	 * <p>If a person works on Saturday, then the hour rate has 1.5x multiplier. Overtime is not counted on these days.</p>
	 * <p>If a person works on Sunday, then the hour rate has 2x multiplier. Overtime is not counted on these days.</p>
	 * <p>If a person starts working on a weekend day, and then he gets sick or decides to go home(take a day off), then the
	 * day is not counted as work day, neither sick day nor free day.</p>
	 * <p><b>Rules</b></p>
	 * <ul>
	 * <li>A person should work more than 4 hours per every work day on weekdays. After 10 work day with less equals than 4 hours per work day,
	 * the program throws an exception which indicates this.</li>
	 * <li>A person should work more than 40 hours per week. If he doesn't apply to this expectations an exception is thrown, which indicates this.</li>
	 * <li>A person should work more than 160 hours per month. If he doesn't apply to this expectations an exception is thrown, which indicates this.</li>
	 * </ul>
	 * <p>{@code SalaryException} collects these rule breaks. If any of these rules are broken, the exception will be thrown. This exception will
	 * contain the salary, and the error messages.</p>
	 * @see SalaryException
	 * 
	 * @param id the employee's id
	 * @param date the date of the month (can be given any of the month's days) we want to analyze
	 * @return the employee's salary in the specific month
	 * @throws SalaryException if the rules above are broken
	 */
	public static BigDecimal getSalaryInAMonthByEmployeeId(int id, Date date) throws SalaryException {
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
			logger.error("Error while getting worksessions for calculating salary", e);
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
		StringBuilder sb = new StringBuilder();
		int lessThanFourHourWorkDayCount = 0;
		for (WorkSession workDay : workDays) {
			LocalDate ldate = new LocalDate(workDay.getDate());
			if (workDay.getDuration() <= 4 && ldate.getDayOfWeek() < 6) { //worked less than 4 hour on a weekday
				lessThanFourHourWorkDayCount++;
			}
		}
		if (lessThanFourHourWorkDayCount > 10) {
			sb = sb.append("Day expectation (4h/Day) BROKEN\n");
		}

		Map<Integer, Integer> map = new HashMap<>();
		
		LocalDate startOfMonth = new LocalDate(date.getTime());
		startOfMonth = startOfMonth.minusDays(startOfMonth.getDayOfMonth() - 1);
		LocalDate endOfMonth = startOfMonth.plusMonths(1);
		endOfMonth = endOfMonth.minusDays(1);
		int startWeek = startOfMonth.getWeekOfWeekyear();
		int endWeek = endOfMonth.getWeekOfWeekyear();
		while (startWeek <= endWeek) {
			map.put(startWeek, 0);
			startWeek++;
		}
		
		for (WorkSession workDay : workDays) {
			LocalDate ldate = new LocalDate(workDay.getDate());
			int weekWorkHours = getWorkedHoursOnAWeekByEmployeeId(workDay.getEmployee_id(), workDay.getDate());
			map.put(ldate.getWeekOfWeekyear(), weekWorkHours);
		}
		boolean XWorkHourPerWeekWarn = false;
		int count = 0;
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			if (entry.getValue() < 40) {
				XWorkHourPerWeekWarn = true;
				count++;
			}
		}
		if (XWorkHourPerWeekWarn) {
			sb = sb.append("Week expectation (40h/Week) BROKEN\n");
		}
		
		int sumWorkedHours = 0;
		for (WorkSession workDay : workDays) {
			sumWorkedHours += workDay.getDuration();
		}
		if (sumWorkedHours < 160) {
			sb = sb.append("Month expectation (160h/Month) BROKEN\n");
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
			logger.error("error while retrieving baseSalary in calculating salary", e);
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
					salary = salary.add(baseSalary.multiply(new BigDecimal(8))); // normal hours
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
		try {
			return salary;
		} finally {
			if (sb.length() > 0) {
				logger.info("id=" + id + ", employee has problems with expectations: \n" + sb.toString());
				throw new SalaryException(salary, sb.toString());
			}
		}
		
	}
	
	/**
	 * Returns the number of specific employee's worked days in the current year.
	 * <p>Work days are overwritten by sick days.</p>
	 * <p>Sick days are overwritten by days taken off.</p>
	 * 
	 * @param id the employee's id
	 * @return the number of worked days
	 */
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
			logger.error("error while retrieving worked days in current year", e);
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
		return workDays.size();
	}
}
