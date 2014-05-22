package core;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.joda.time.LocalDate;

import dao.Employee;
import dao.EmployeeDAO;
import dao.EmployeeDAOFactory;
import dao.EmployeeNotFoundException;
import dao.ExistingEmployeeException;
import dao.ExistingWorkSessionException;
import dao.PersistentLayerException;
import dao.TooMuchWorkOnADayException;
import dao.WorkSession;
import dao.WorkSessionDAO;
import dao.WorkSessionDAOFactory;
import dao.WorkSessionNotFoundException;
import dao.WorkSession.SessionTypes;

public class Main {

	public static void main(String[] args) {
		
		EmployeeDAOFactory edaof = EmployeeDAOFactory.newInstance();
		edaof.setType(EmployeeDAOFactory.Type.JDBC);
		EmployeeDAO edao = edaof.newEmployeeDAO();
		Employee e = new Employee(1, "Kiss Béla", new Date(new LocalDate(2010, 2, 11).toDate().getTime()), new BigDecimal("10000"), "IT");
//		edao.updateEmployee(e);
		int sum = Calculations.getDayOffsInCurrentYearByEmployeeId(1);
		System.out.println(Calculations.getSickDaysInCurrentYearByEmployeeId(1));
		System.out.println(sum);
		
		DataLoader.importEmployees("employees.xml");
	}

}
