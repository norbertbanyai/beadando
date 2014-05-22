package dao.impl;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.ExistingWorkSessionException;
import dao.PersistentLayerException;
import dao.TooMuchWorkOnADayException;
import dao.WorkSession;
import dao.WorkSession.SessionTypes;
import dao.WorkSessionDAO;
import dao.WorkSessionNotFoundException;

public class JDBCWorkSessionDAOImpl implements WorkSessionDAO {

	private static final String DELETE_WORKSESSION = "delete from worksessions where worksession_id = ?";
	private static final String CREATE_WORKSESSION = "insert into worksessions values(worksession_sequence.nextval, ?, ?, ?, ?)";
	private static final String FIND_ALL_WORKSESSION = "select worksession_id, employee_id, session_date, duration, type from worksessions ";
	private static final String FIND_WORKSESSION_BY_ID = FIND_ALL_WORKSESSION + "where worksession_id = ?";
	private static final String FIND_ALL_WORKSESSION_BY_EMPLOYEE_ID = FIND_ALL_WORKSESSION + "where employee_id = ?";
	private static final String FIND_ALL_WORKSESSION_BY_EMPLOYEE_NAME = FIND_ALL_WORKSESSION +
			"where employee_id in (select employee_id from employees where lower(name) like ?)";
	private static final String FIND_ALL_WORKSESSION_BY_DATE = FIND_ALL_WORKSESSION + "where session_date = ?";
	private static final String FIND_ALL_WORKSESSION_BY_TYPE = FIND_ALL_WORKSESSION + "where type = ?";

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
	public WorkSession findWorkSessionById(int id)
			throws WorkSessionNotFoundException {
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement(FIND_WORKSESSION_BY_ID)) {
			pstmt.setInt(1, id);
			try(ResultSet rset = pstmt.executeQuery()) {
				if (rset.next()) {
					return new WorkSession(rset.getInt("worksession_id"), rset.getInt("employee_id"),
							rset.getDate("session_date"), rset.getShort("duration"), rset.getString("type"));
				} else {
					throw new WorkSessionNotFoundException(id);
				}
			}
		} catch (SQLException | IOException e) {
			throw new PersistentLayerException(e);
		}
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
	public List<WorkSession> findWorkSessionsByEmployeeName(String name) {
		List<WorkSession> l = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement(FIND_ALL_WORKSESSION_BY_EMPLOYEE_NAME)) {
			pstmt.setString(1, "%" + name.toLowerCase() + "%");
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
	public List<WorkSession> findWorkSessionsByDate(Date date) {
		List<WorkSession> l = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement(FIND_ALL_WORKSESSION_BY_DATE)) {
			pstmt.setDate(1, date);
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
	public List<WorkSession> findWorkSessionsByType(SessionTypes type) {
		List<WorkSession> l = new ArrayList<>();
		try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement(FIND_ALL_WORKSESSION_BY_TYPE)) {
			pstmt.setString(1, type.toString());
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

//	@Override
//	public void createWorkSession(WorkSession ws)
//			throws ExistingWorkSessionException {
//		try {
//			new JDBCWorkSessionDAOImpl().findWorkSessionById(ws.getId());
//			throw new ExistingWorkSessionException(ws.getId());
//		} catch (WorkSessionNotFoundException e) {
//			try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement(CREATE_WORKSESSION)) {
//				WorkSession.workSessionToPreparedStatement(pstmt, ws);
//				pstmt.executeUpdate();
//			} catch (SQLException | IOException e1) {
//				throw new PersistentLayerException(e1);
//			}
//		}
//
//	}
	@Override
	public void createWorkSession(WorkSession ws) throws ExistingWorkSessionException, TooMuchWorkOnADayException {
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
	public void updateWorkSession(WorkSession ws) {
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
