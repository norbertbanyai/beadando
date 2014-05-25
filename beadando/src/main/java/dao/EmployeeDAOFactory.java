package dao;

import dao.impl.JDBCEmployeeDAOImpl;

/**
 * Class which allows us to configure what kind of {@link EmployeeDAO} implementation we want to use.
 * At the moment only the JDBC implementation exists, because I did not need the XML implementation during this project.
 * @see JDBCEmployeeDAOImpl
 */
public class EmployeeDAOFactory {
	
	/**
	 * The implementation species we can use.
	 * <b>XML is not implemented.</b>
	 */
	public enum Type {
		/**
		 * JDBC type.
		 */
		JDBC,
		/**
		 * XML type.
		 */
		XML
	}
	
	/**
	 * This variable stores what kind of implementation we want to use.
	 */
	private Type type = Type.JDBC;
	
	/**
	 * Returns a new {@code EmployeeDAO} which fits the expectations.
	 * 
	 * @return a new {@code EmployeeDAO} which fits the expectations
	 */
	public EmployeeDAO newEmployeeDAO() {
		if (Type.JDBC.equals(type)) {
			return new JDBCEmployeeDAOImpl();
		}
		return new JDBCEmployeeDAOImpl();
	}
	
	/**
	 * Sets the type configuration for this factory.
	 * @see Type
	 * 
	 * @param t the type we want to use can be <code>JDBC</code> or <code>XML</code>
	 */
	public void setType(Type t) {
		type = t;
	}
	
	/**
	 * Returns a new {@code EmployeeDAOFactory} instance.
	 * 
	 * @return a new {@code EmployeeDAOFactory} instance
	 */
	public static EmployeeDAOFactory newInstance() {
		return new EmployeeDAOFactory();
	}
}
