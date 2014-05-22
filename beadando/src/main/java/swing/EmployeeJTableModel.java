/**
 * 
 */
package swing;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import dao.Employee;

/**
 * @author bnorbi
 *
 */
public class EmployeeJTableModel extends AbstractTableModel{
	private String[] columnNames = new String[] {"id", "Name", "Hire date", "Salary", "Department"};
	private List<Employee> emps;
	
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	public String[] getColumnNames() {
		return columnNames;
	}
	@Override
	public String getColumnName(int arg0) {
		return columnNames[arg0];
	}
	
	public EmployeeJTableModel(List<Employee> emps) {
		this.emps = emps;
	}
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return emps.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Employee e = emps.get(rowIndex);
		Object o = new Object();
		switch (columnIndex) {
			case 0: o = e.getId(); break;
			case 1: o = e.getName(); break;
			case 2: o = e.getHireDate(); break;
			case 3: o = e.getSalary(); break;
			case 4: o = e.getDepartment(); break;
			default:
				break;
		}
		return o;
	}

	
}
