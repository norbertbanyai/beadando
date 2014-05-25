package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static core.Main.logger;

/**
 * Class for representing a work session.
 */
public class WorkSession {
	
	/**
	 * The types of the sessions.
	 */
	public enum SessionTypes {
		/**
		 * The employee worked in this session.
		 */
		WORK,
		/**
		 * The employee was sick in this session.
		 */
		SICKNESS,
		/**
		 * The employee had a day off in this session.
		 */
		DAY_OFF,
	}
	
	/**
	 * The id of the session.
	 */
	private int id;
	
	/**
	 * This session belongs to the {@code Employee} with the id <code>=</code> {@code employee_id}.
	 */
	private int employee_id;
	
	/**
	 * The date of the session.
	 */
	private Date date;
	
	/**
	 * The duration of the session.
	 * In hours(0..12).
	 */
	private short duration;
	
	/**
	 * The type of this session.
	 * @see SessionTypes
	 */
	private SessionTypes type;
	
	/**
	 * Constructor for the class {@code WorkSession}.
	 * 
	 * @param id the id of the {@code WorkSession}
	 * @param employee_id {@code WorkSession} belongs to the {@code Employee} with the id <code>=</code> {@code employee_id}
	 * @param date the date of the session
	 * @param duration the duration of the session
	 * @param type the type of the session given as a string, then converted into a {@link SessionTypes}
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
			logger.error("Unknown session type has been given to the constructor of WorkSession" + type.toUpperCase());
			break;
		}
	}

	/**
	 * Returns the session owner employee's id.
	 * 
	 * @return the session owner employee's id
	 */
	public int getEmployee_id() {
		return employee_id;
	}

	/**
	 * Sets the session owner's id.
	 * 
	 * @param employee_id the owner's id
	 */
	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}

	/**
	 * Returns the date of the session.
	 * 
	 * @return the date of the session
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date of the session.
	 * 
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Returns the {@code WorkSession}'s duration.
	 * 
	 * @return the duration of the {@code WorkSession}
	 */
	public short getDuration() {
		return duration;
	}

	/**
	 * Sets the duration of this {@code WorkSession}.
	 * 
	 * @param duration the duration to set
	 */
	public void setDuration(short duration) {
		this.duration = duration;
	}

	/**
	 * Returns this {@code WorkSession}'s type.
	 * 
	 * @return the type of the session
	 */
	public SessionTypes getType() {
		return type;
	}

	/**
	 * Sets this {@code WorkSession}'s type.
	 * 
	 * @param type the type to set
	 */
	public void setType(SessionTypes type) {
		this.type = type;
	}

	/**
	 * Returns the {@code WorkSession}'s id.
	 * 
	 * @return the id of the {@code WorkSession}
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the hash value of this object.
	 * 
	 * @return the hash value of this object
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/**
	 * Compares this {@code WorkSession} with with the specified object. Two {@code WorkSession} objects are
	 * considered equal if and only if their ids are equal.
	 * 
	 * @param obj the object to compare to
	 * @return <code>true</code> if the objects are equal, <code>false</code> otherwise
	 */
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

	/**
	 * Returns the string representation of this {@code WorkSession}.
	 * 
	 * @return the string representation of this {@code WorkSession} in the form
	 * 			<span><em>id=</em><code> id </code><em>employee </em><code>= employoo's name date type</code></span>
	 */
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
	
	/**
	 * Converts the given {@code WorkSession} into a form of {@code PreparedStatement}.
	 * @see WorkSessionDAO
	 * 
	 * @param pstmt the {@code PreparedStatement} we want to write the session's data into
	 * @param ws the session we want to convert into a {@code PreparedStatement}
	 * @throws SQLException if something happens during the conversion
	 */
	public static void workSessionToPreparedStatement(PreparedStatement pstmt, WorkSession ws) throws SQLException {
		pstmt.setInt(1, ws.employee_id);
		pstmt.setDate(2, ws.date);
		pstmt.setInt(3, ws.duration);
		pstmt.setString(4, ws.getType().toString());
	}
	
	
}
