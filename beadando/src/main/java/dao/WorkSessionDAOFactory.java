package dao;

import dao.impl.JDBCWorkSessionDAOImpl;

/**
 * Class which allows us to configure what kind of {@link WorkSessionDAO} implementation we want to use.
 * At the moment only the JDBC implementation exists, because I did not need the XML implementation during this project.
 * @see JDBCWorkSessionDAOImpl
 */
public class WorkSessionDAOFactory {
	
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
	 * Returns a new {@code WorkSessionDAO} which fits the expectations.
	 * 
	 * @return a new {@code WorkSessionDAO} which fits the expectations
	 */
	public WorkSessionDAO newWorkSessionDAO() {
		if (Type.JDBC.equals(type)) {
			return new JDBCWorkSessionDAOImpl();
		}
		return new JDBCWorkSessionDAOImpl();
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
	 * Returns a new {@code WorkSessionDAOFactory} instance.
	 * 
	 * @return a new {@code WorkSessionDAOFactory} instance
	 */
	public static WorkSessionDAOFactory newInstance() {
		return new WorkSessionDAOFactory();
	}
}
