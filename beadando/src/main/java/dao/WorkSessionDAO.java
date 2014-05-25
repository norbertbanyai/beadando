package dao;

import java.util.List;

import dao.WorkSession.SessionTypes;

/**
 * Interface for the operations we want to execute with {@link WorkSession}s.
 * @see WorkSessionDAOFactory
 */
public interface WorkSessionDAO {
	
	/**
	 * Returns the list of all the {@code WorkSession}s we found.
	 * 
	 * @return a list of all the {@code WorkSession} we found
	 */
	public List<WorkSession> findAllWorkSession();

	/**
	 * Finds all {@code WorkSession} by a specific {@link Employee}'s id.
	 * 
	 * @param employeeId the {@code Employee}'s id
	 * @return list of the {@code WorkSession}s we found
	 */
	public List<WorkSession> findWorkSessionsByEmployeeId(int employeeId);

	/**
	 * Creates a {@code WorkSession}.
	 * 
	 * @param ws the {@code WorkSession} we want to create
	 * @throws TooMuchWorkOnADayException is thrown if the {@link SessionTypes} is {@link SessionTypes#WORK} and the employee with this
	 * {@code WorkSession}'s duration breaks the day limit of work (12)
	 * @throws AlreadyReachedThirtyDayOffs is thrown if the {@link SessionTypes} is {@link SessionTypes#DAY_OFF} and the employee
	 * already reached his day off limit in the year
	 * @throws AlreadyReachedFifteenSickDays is thrown if the {@link SessionTypes} is {@link SessionTypes#SICKNESS} and the employee
	 * already reached his sick day limit in the year
	 */
	public void createWorkSession(WorkSession ws)
			throws TooMuchWorkOnADayException, AlreadyReachedThirtyDayOffs, AlreadyReachedFifteenSickDays;

	/**
	 * Deletes a {@code WorkSession} by the given {@code WorkSession}'s id.
	 * 
	 * @param ws the {@code WorkSession} we want to delete
	 */
	public void deleteWorkSession(WorkSession ws);

	/**
	 * Updates a {@code WorkSession} by the given {@code WorkSession}'s id.
	 * 
	 * @param ws the {@code WorkSession} we want to update
	 * @throws AlreadyReachedThirtyDayOffs is thrown if the {@link SessionTypes} is {@link SessionTypes#DAY_OFF} and the employee
	 * already reached his day off limit in the year
	 * @throws AlreadyReachedFifteenSickDays is thrown if the {@link SessionTypes} is {@link SessionTypes#SICKNESS} and the employee
	 * already reached his sick day limit in the year
	 * @throws TooMuchWorkOnADayException is thrown if the {@link SessionTypes} is {@link SessionTypes#WORK} and the employee with this
	 * {@code WorkSession}'s duration breaks the day limit of work (12)
	 */
	public void updateWorkSession(WorkSession ws)
			throws AlreadyReachedThirtyDayOffs, AlreadyReachedFifteenSickDays, TooMuchWorkOnADayException;
}
