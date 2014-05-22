package dao.impl;

import java.sql.Date;
import java.util.List;

import dao.ExistingWorkSessionException;
import dao.WorkSession;
import dao.WorkSession.SessionTypes;
import dao.WorkSessionDAO;
import dao.WorkSessionNotFoundException;

public class XMLWorkSessionDAOImpl implements WorkSessionDAO {

	@Override
	public List<WorkSession> findAllWorkSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkSession findWorkSessionById(int id)
			throws WorkSessionNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkSession> findWorkSessionsByEmployeeId(int employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkSession> findWorkSessionsByEmployeeName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkSession> findWorkSessionsByDate(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkSession> findWorkSessionsByType(SessionTypes type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createWorkSession(WorkSession ws)
			throws ExistingWorkSessionException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteWorkSession(WorkSession ws) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateWorkSession(WorkSession ws) {
		// TODO Auto-generated method stub

	}

}
