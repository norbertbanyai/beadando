package dao;

import dao.impl.JDBCEmployeeDAOImpl;
import dao.impl.XMLEmployeeDAOImpl;

public class EmployeeDAOFactory {
	public enum Type {
		JDBC,
		XML
	}
	private Type type = Type.JDBC;
	
	public EmployeeDAO newEmployeeDAO() {
		if (Type.JDBC.equals(type)) {
			return new JDBCEmployeeDAOImpl();
		}
		return new XMLEmployeeDAOImpl();
	}
	public void setType(Type t) {
		type = t;
	}
	public static EmployeeDAOFactory newInstance() {
		return new EmployeeDAOFactory();
	}
}
