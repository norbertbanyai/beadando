package dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import dao.AlreadyReachedFifteenSickDays;
import dao.AlreadyReachedThirtyDayOffs;
import dao.PersistentLayerException;
import dao.TooMuchWorkOnADayException;
import dao.WorkSession;
import dao.WorkSessionDAO;
import static core.Main.logger;

/**
 * A JDBC implementation of the {@link WorkSessionDAO} interface.
 */
public class JDBCWorkSessionDAOImpl implements WorkSessionDAO {

	/**
	 * String that is used by a {@code PreparedStatement} to delete all {@code WorkSession}s from the database by a
	 * specific <code>worksession_id</code>.
	 */
	private static final String DELETE_WORKSESSION = "delete from worksessions where worksession_id = ?";
	
	/**
	 * String that is used by a {@code PreparedStatement} to insert a {@code WorkSession} into the database.
	 */
	private static final String CREATE_WORKSESSION = "insert into worksessions values(worksession_sequence.nextval, ?, ?, ?, ?)";
	
	/**
	 * String that is used by a {@code Statement} to find all {@code WorkSession} in the database.
	 */
	private static final String FIND_ALL_WORKSESSION = "select worksession_id, employee_id, session_date, duration, type from worksessions ";
	
	/**
	 * String that expands the {@link #FIND_ALL_WORKSESSION} to be used by a {@code PreparedStatement} to query a {@code WorkSession}
	 * by a specific <code>worksession_id</code>.
	 */
	private static final String FIND_WORKSESSION_BY_ID = FIND_ALL_WORKSESSION + "where worksession_id = ?";
	
	/**
	 * String that expands the {@link #FIND_ALL_WORKSESSION} to be used by a {@code PreparedStatement} to query all {@code WorkSession}
	 * where the <code>employee_id</code> equals with a specific <code>employee_id</code>.
	 */
	private static final String FIND_ALL_WORKSESSION_BY_EMPLOYEE_ID = FIND_ALL_WORKSESSION + "where employee_id = ?";

	@Override
	public List<WorkSession> findAllWorkSession() {
		List<WorkSession> l = new ArrayList<>();
		try(Statement stmt = ConnectionHelper.getConnection().createStatement();
				ResultSet rset = stmt.executeQuery(FIND_ALL_WORKSESSION)) {
			while (rset.next()) {
				l.add(new WorkSession(rset.getInt("worksession_id"), rset.getInt("employee_id"), rset.getDate("session_date"), rset.getShort("duration"), rset.getString("type")));
			}
			
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
		return l;
	}

	@Override
	public List<WorkSession> findWorkSessionsByEmployeeId(int employeeId) {
		List<WorkSession> l = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement(FIND_ALL_WORKSESSION_BY_EMPLOYEE_ID)) {
			pstmt.setInt(1, employeeId);
			try(ResultSet rset = pstmt.executeQuery()) {
				while (rset.next()) {
					l.add(new WorkSession(rset.getInt("worksession_id"), rset.getInt("employee_id"),
							rset.getDate("session_date"), rset.getShort("duration"), rset.getString("type")));
				}
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
		return l;
	}
	
	@Override
	public void createWorkSession(WorkSession ws) throws TooMuchWorkOnADayException, AlreadyReachedThirtyDayOffs, AlreadyReachedFifteenSickDays {
		if (ws.getType().equals(WorkSession.SessionTypes.SICKNESS) || ws.getType().equals(WorkSession.SessionTypes.DAY_OFF)) {
			List<WorkSession> sickDaysTemp = new ArrayList<>();
			List<WorkSession> dayOffsTemp = new ArrayList<>();
			try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select session_date, type from worksessions"
					+ " where employee_id = ? and to_char(session_date, 'YYYY') = to_char(?, 'YYYY') group by session_date, type")) {
				pstmt.setInt(1, ws.getEmployee_id());
				pstmt.setDate(2, ws.getDate());
				try(ResultSet rset = pstmt.executeQuery()) {
					int temp_id = 1;
					while (rset.next()) {
						WorkSession ws2 = new WorkSession(temp_id++, ws.getEmployee_id(), rset.getDate("session_date"), (short)8, rset.getString("type"));
						switch (ws2.getType()) {
						case SICKNESS:
							sickDaysTemp.add(ws2);
							break;
						case DAY_OFF:
							dayOffsTemp.add(ws2);
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
			
			for (WorkSession dayOff : dayOffsTemp) {
				LocalDate ldate = new LocalDate(dayOff.getDate());
				if (ldate.getDayOfWeek() < 6) {
					dayOffs.add(dayOff);
				}
			}
			if (dayOffs.size() >= 30) {
				throw new AlreadyReachedThirtyDayOffs();
			}
			if (sickDays.size() >= 15) {
				throw new AlreadyReachedFifteenSickDays();
			}
		}
		
		if (ws.getType().equals(WorkSession.SessionTypes.WORK)) {
			try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select sum(duration) altogether from worksessions where employee_id = ? and session_date = ? and type = 'WORK'")) {
				pstmt.setInt(1, ws.getEmployee_id());
				pstmt.setDate(2, ws.getDate());
				try(ResultSet rset = pstmt.executeQuery()) {
					if (rset.next()) {
						int sum = rset.getInt("altogether") + ws.getDuration();
						if (sum > 12) {
							throw new TooMuchWorkOnADayException(sum);
						}
					}
				}
			} catch (SQLException | IOException e) {
				throw new PersistentLayerException(e);
			}
		}
		
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement(CREATE_WORKSESSION)) {
			WorkSession.workSessionToPreparedStatement(pstmt, ws);
			pstmt.executeUpdate();
		} catch (SQLException | IOException e1) {
			throw new PersistentLayerException(e1);
		}
	}

	@Override
	public void deleteWorkSession(WorkSession ws) {
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement(DELETE_WORKSESSION)) {
			pstmt.setInt(1, ws.getId());
			pstmt.executeUpdate();
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}

	}

	@Override
	public void updateWorkSession(WorkSession ws) throws AlreadyReachedThirtyDayOffs, AlreadyReachedFifteenSickDays, TooMuchWorkOnADayException {
		if (ws.getType().equals(WorkSession.SessionTypes.DAY_OFF) || ws.getType().equals(WorkSession.SessionTypes.SICKNESS)) {
			List<WorkSession> sickDaysTemp = new ArrayList<>();
			List<WorkSession> dayOffsTemp = new ArrayList<>();
			try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select session_date, type from worksessions"
					+ " where employee_id = ? and to_char(session_date, 'YYYY') = to_char(?, 'YYYY') group by session_date, type")) {
				pstmt.setInt(1, ws.getEmployee_id());
				pstmt.setDate(2, ws.getDate());
				try(ResultSet rset = pstmt.executeQuery()) {
					int temp_id = 1;
					while (rset.next()) {
						WorkSession ws2 = new WorkSession(temp_id++, ws.getEmployee_id(), rset.getDate("session_date"), (short)8, rset.getString("type"));
						switch (ws2.getType()) {
						case SICKNESS:
							sickDaysTemp.add(ws2);
							break;
						case DAY_OFF:
							dayOffsTemp.add(ws2);
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
			
			for (WorkSession dayOff : dayOffsTemp) {
				LocalDate ldate = new LocalDate(dayOff.getDate());
				if (ldate.getDayOfWeek() < 6) {
					dayOffs.add(dayOff);
				}
			}
			if (dayOffs.size() >= 30) {
				throw new AlreadyReachedThirtyDayOffs();
			}
			if (sickDays.size() >= 15) {
				throw new AlreadyReachedFifteenSickDays();
			}
		}
		
		if (ws.getType().equals(WorkSession.SessionTypes.WORK)) {
			
			try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select sum(duration) altogether from worksessions where employee_id = ? and session_date = ? and type = 'WORK'")) {
				pstmt.setInt(1, ws.getEmployee_id());
				pstmt.setDate(2, ws.getDate());
				try(ResultSet rset = pstmt.executeQuery()) {
					if (rset.next()) {
						int sum = rset.getInt("altogether") + ws.getDuration();
						if (sum > 12) {
							throw new TooMuchWorkOnADayException(sum);
						}
					}
				}
			} catch (SQLException | IOException e) {
				throw new PersistentLayerException(e);
			}
		}
		
		
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement(FIND_WORKSESSION_BY_ID, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
			pstmt.setInt(1, ws.getId());
			try(ResultSet rset = pstmt.executeQuery()) {
				if (rset.next()) {
					rset.updateInt("employee_id", ws.getEmployee_id());
					rset.updateDate("session_date", ws.getDate());
					rset.updateShort("duration", ws.getDuration());
					rset.updateString("type", ws.getType().toString());
					rset.updateRow();
				}
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}

	}

}
