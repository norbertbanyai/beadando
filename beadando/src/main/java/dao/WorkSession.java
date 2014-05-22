package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class WorkSession {
	
	public enum SessionTypes {
		WORK,
		SICKNESS,
		DAY_OFF,
		NOT_ALLOWED_TO_WORK //cuz of sickness or on day offs
	}
	
	private int id;
	private int employee_id;
	private Date date;
	private short duration;
	private SessionTypes type;
	
	/**
	 * @param id
	 * @param employee_id
	 * @param date
	 * @param duration
	 * @param type
	 */
	public WorkSession(int id, int employee_id, Date date, short duration,
			String type) {
		super();
		this.id = id;
		this.employee_id = employee_id;
		this.date = date;
		this.duration = duration;
		switch (type.toUpperCase()) {
		case "WORK":
			this.type = SessionTypes.WORK;
			break;
		case "SICKNESS":
			this.type = SessionTypes.SICKNESS;
			break;
		case "DAY_OFF":
			this.type = SessionTypes.DAY_OFF;
			break;
		default:
			this.type = SessionTypes.NOT_ALLOWED_TO_WORK;
			break;
		}
	}

	/**
	 * @return the employee_id
	 */
	public int getEmployee_id() {
		return employee_id;
	}

	/**
	 * @param employee_id the employee_id to set
	 */
	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the duration
	 */
	public short getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(short duration) {
		this.duration = duration;
	}

	/**
	 * @return the type
	 */
	public SessionTypes getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(SessionTypes type) {
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof WorkSession)) {
			return false;
		}
		WorkSession other = (WorkSession) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		EmployeeDAOFactory edaof = EmployeeDAOFactory.newInstance();
		edaof.setType(EmployeeDAOFactory.Type.JDBC);
		EmployeeDAO edao = edaof.newEmployeeDAO();
		Employee e;
		try {
			e = edao.findEmployeeById(employee_id);
		} catch (EmployeeNotFoundException e1) {
			throw new PersistentLayerException("Employee not found", e1);
		}
		return "id="+id + " employee = " + e.getName() + " " + date + " " + type;
	}
	
	public static void workSessionToPreparedStatement(PreparedStatement pstmt, WorkSession ws) throws SQLException {
		pstmt.setInt(1, ws.employee_id);
		pstmt.setDate(2, ws.date);
		pstmt.setInt(3, ws.duration);
		pstmt.setString(4, ws.getType().toString());
	}
	
	
}
