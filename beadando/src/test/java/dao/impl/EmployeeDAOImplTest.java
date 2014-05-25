package dao.impl;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Date;

import org.joda.time.LocalDate;
import org.junit.Test;

import dao.Employee;
import dao.EmployeeDAO;
import dao.EmployeeNotFoundException;
import dao.ExistingEmployeeException;

public class EmployeeDAOImplTest {

	@Test
	public void testFindEmployeeById() {
		// 100 = Steven King
		EmployeeDAO edao = new JDBCEmployeeDAOImpl();
		try {
			Employee e = edao.findEmployeeById(100);
			assertNotNull(e);
			assertEquals("Steven King", e.getName());
		} catch (EmployeeNotFoundException e) {
			fail(":(");
		}
	}
	
	@Test
	public void testFindEmployeeByIdNonExistingIdTest() {
		EmployeeDAO edao = new JDBCEmployeeDAOImpl();
		try {
			Employee e = edao.findEmployeeById(-1);
			fail("EmployeeNotFoundException should have been thrown");
		} catch (EmployeeNotFoundException e) {
			
		}
	}

	@Test
	public void testCreateEmployee() {
		EmployeeDAO edao = new JDBCEmployeeDAOImpl();
		Employee e = new Employee(9999, "Teszt Elek", new Date(new LocalDate(2014,1,3).toDate().getTime()), new BigDecimal("7000"), "TEST");
		try {
			edao.createEmployee(e);
			Employee e2 = edao.findEmployeeById(e.getId());
			assertEquals(e.getName(), e2.getName());
			assertEquals(e.getHireDate(), e2.getHireDate());
			assertEquals(e.getSalary(), e2.getSalary());
			assertEquals(e.getDepartment(), e2.getDepartment());
			edao.deleteEmployee(e2);
		} catch (ExistingEmployeeException e1) {
			fail("should be existing :(");
		} catch (EmployeeNotFoundException e1) {
			fail("this souldn't be happening :(");
		}
	}
	
	@Test
	public void testCreateEmployeeWithExistingId() {
		EmployeeDAO edao = new JDBCEmployeeDAOImpl();
		try {
			Employee e = edao.findEmployeeById(100); //Steven King
			edao.createEmployee(e);
			fail("Steven King with id 100 must be already there!");
		} catch (EmployeeNotFoundException e) {
			fail("Steven King must be there");
		} catch (ExistingEmployeeException e1) {
			
		}
	}

	@Test
	public void testUpdateEmployee() {
		EmployeeDAO edao = new JDBCEmployeeDAOImpl();
		Employee e = new Employee(9999, "Teszt Elek", new Date(new LocalDate(2014,1,3).toDate().getTime()), new BigDecimal("7000"), "TEST");
		try {
			edao.createEmployee(e);
			Employee origi = edao.findEmployeeById(e.getId());
			e.setDepartment("NOT A TEST");
			e.setHireDate(new Date(new LocalDate(2014,5,1).toDate().getTime()));
			e.setSalary(new BigDecimal("77"));
			edao.updateEmployee(e);
			Employee e2 = edao.findEmployeeById(e.getId());
			assertNotNull(e2);
			assertNotEquals(origi.getHireDate(), e2.getHireDate());
			assertEquals(e.getHireDate(), e2.getHireDate());
			
			assertNotEquals(origi.getDepartment(), e2.getDepartment());
			assertEquals(e.getDepartment(), e2.getDepartment());
			
			assertNotEquals(origi.getSalary(), e2.getSalary());
			assertEquals(e.getSalary(), e2.getSalary());
			
			edao.deleteEmployee(e);
		} catch (ExistingEmployeeException e1) {
			fail("He shouldn't be there");
		} catch (EmployeeNotFoundException e1) {
			fail("He must be there!");
		}
	}

}
