/**
 * 
 */
package dao;

import java.sql.Date;
import java.util.List;

/**
 * @author bnorbi
 * 
 */
public interface WorkSessionDAO {
	public List<WorkSession> findAllWorkSession();

	public WorkSession findWorkSessionById(int id)
			throws WorkSessionNotFoundException;

	public List<WorkSession> findWorkSessionsByEmployeeId(int employeeId);

	public List<WorkSession> findWorkSessionsByEmployeeName(String name);

	public List<WorkSession> findWorkSessionsByDate(Date date);

	public List<WorkSession> findWorkSessionsByType(
			WorkSession.SessionTypes type);

	public void createWorkSession(WorkSession ws)
			throws ExistingWorkSessionException, TooMuchWorkOnADayException;

	public void deleteWorkSession(WorkSession ws);

	public void updateWorkSession(WorkSession ws);
}
