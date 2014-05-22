package dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.joda.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Random;
//import java.util.UUID;
//
//import org.joda.time.DateTime;
//
//import core.Calendar;

public class Employee {
	private int id;
	private String name;
	private Date hireDate;
	private BigDecimal salary;
	private String department;

	public Employee(String name, int age, int salary) {
		this(1, name, new Date(new LocalDate(2010, 3, 7).toDate().getTime()), new BigDecimal(salary), "IT");
	}
	
	
	/**
	 * @param id
	 * @param name
	 * @param hireDate
	 * @param salary
	 * @param department
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the hireDate
	 */
	public Date getHireDate() {
		return hireDate;
	}


	/**
	 * @param hireDate the hireDate to set
	 */
	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}


	/**
	 * @return the salary
	 */
	public BigDecimal getSalary() {
		return salary;
	}


	/**
	 * @param salary the salary to set
	 */
	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}


	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}


	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
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
		if (!(obj instanceof Employee)) {
			return false;
		}
		Employee other = (Employee) obj;
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
		return id + " " + name;
	}

	
	public static void employeeToPreparedStatement(PreparedStatement pstmt, Employee e) throws SQLException {
		pstmt.setInt(1, e.id);
		pstmt.setString(2, e.name);
		pstmt.setDate(3, e.hireDate);
		pstmt.setBigDecimal(4, e.salary);
		pstmt.setString(5, e.department);
	}

}