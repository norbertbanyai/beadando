package dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class for representing an employee.
 */
public class Employee {
	/**
	 * The employee's unique ID.
	 */
	private int id;
	
	/**
	 * The employee's name.
	 */
	private String name;
	
	/**
	 * The employee's hire date.
	 */
	private Date hireDate;
	
	/**
	 * The employee's base salary.
	 * Counted as 20 days per month, and 8 hours worked per day.
	 */
	private BigDecimal salary;
	
	/**
	 * The name of the employee's department.
	 */
	private String department;
	
	/**
	 * Constructor for the {@code Employee} class.
	 * 
	 * @param id the employee's id
	 * @param name the employee's name
	 * @param hireDate the employee's hire date
	 * @param salary the employee's salary
	 * @param department the name of the employee's department
	 */
	public Employee(int id, String name, Date hireDate, BigDecimal salary,
			String department) {
		super();
		this.id = id;
		this.name = name;
		this.hireDate = hireDate;
		this.salary = salary;
		this.department = department;
	}


	/**
	 * Returns the employee's name.
	 * 
	 * @return the employee's name
	 */
	public String getName() {
		return name;
	}


	/**
	 * Sets the employee's name.
	 * 
	 * @param name the name to set to the employee
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Returns the employee's hire date.
	 * 
	 * @return the employee's hire date
	 */
	public Date getHireDate() {
		return hireDate;
	}


	/**
	 * Sets the employee's hire date.
	 * 
	 * @param hireDate the hire date to set to the employee
	 */
	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}


	/**
	 * Returns the employee's salary.
	 * 
	 * @return the employee's salary
	 */
	public BigDecimal getSalary() {
		return salary;
	}


	/**
	 * Sets the employee's salary.
	 * 
	 * @param salary the salary to set to the employee
	 */
	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}


	/**
	 * Returns the name of the employee's department.
	 * 
	 * @return the name of the employee's department.
	 */
	public String getDepartment() {
		return department;
	}


	/**
	 * Sets the name of the employee's department.
	 * 
	 * @param department the name to set to the employee's department
	 */
	public void setDepartment(String department) {
		this.department = department;
	}


	/**
	 * Returns the employee's id.
	 * 
	 * @return the employee's id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns a hash code value for this object.
	 * 
	 * @return a hash code value for this object.
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}


	/**
	 * Compares this {@code Employee} with with the specified object. Two {@code Employee} objects are
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
		if (!(obj instanceof Employee)) {
			return false;
		}
		Employee other = (Employee) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the string representation of this employee.
	 * 
	 * @return the string representation of this employee in the form
	 * 			<span><em>id</em><code> </code><em>name</em></span>
	 */
	@Override
	public String toString() {
		return id + " " + name;
	}

	/**
	 * Converts the given {@code Employee} into a form of {@code PreparedStatement}.
	 * @see EmployeeDAO
	 * 
	 * @param pstmt the {@code PreparedStatement} we want to write the employee's data into
	 * @param e the employee we want to convert into a {@code PreparedStatement}
	 * @throws SQLException if something happens during the conversion
	 */
	public static void employeeToPreparedStatement(PreparedStatement pstmt, Employee e) throws SQLException {
		pstmt.setInt(1, e.id);
		pstmt.setString(2, e.name);
		pstmt.setDate(3, e.hireDate);
		pstmt.setBigDecimal(4, e.salary);
		pstmt.setString(5, e.department);
	}

}