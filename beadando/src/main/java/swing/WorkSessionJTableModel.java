/**
 * 
 */
package swing;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import dao.Employee;
import dao.WorkSession;

/**
 * @author bnorbi
 *
 */
public class WorkSessionJTableModel extends AbstractTableModel {

	private String[] columnNames = new String[] {"id", "employee_id", "date", "duration (hours)", "type"};
	private List<WorkSession> list;
	
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
	
	public WorkSessionJTableModel(List<WorkSession> ws) {
		this.list = ws;
	}
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		WorkSession ws = list.get(rowIndex);
		Object o = new Object();
		switch (columnIndex) {
			case 0: o = ws.getId(); break;
			case 1: o = ws.getEmployee_id(); break;
			case 2: o = ws.getDate(); break;
			case 3: o = ws.getDuration(); break;
			case 4: o = ws.getType(); break;
			default:
				break;
		}
		return o;
	}
}
