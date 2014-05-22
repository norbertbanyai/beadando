/**
 * 
 */
package dao;

import dao.impl.JDBCWorkSessionDAOImpl;
import dao.impl.XMLWorkSessionDAOImpl;


/**
 * @author bnorbi
 *
 */
public class WorkSessionDAOFactory {
	public enum Type {
		JDBC,
		XML
	}
	private Type type = Type.JDBC;
	public WorkSessionDAO newWorkSessionDAO() {
		if (Type.JDBC.equals(type)) {
			return new JDBCWorkSessionDAOImpl();
		}
		return new XMLWorkSessionDAOImpl();
	}
	public void setType(Type t) {
		type = t;
	}
	public static WorkSessionDAOFactory newInstance() {
		return new WorkSessionDAOFactory();
	}
}
