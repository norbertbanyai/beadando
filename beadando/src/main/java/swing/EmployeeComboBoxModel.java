/**
 * 
 */
package swing;

import java.util.List;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.ListDataListener;

import org.w3c.dom.views.AbstractView;

import dao.Employee;

/**
 * @author bnorbi
 * 
 */
class EmployeeComboBoxModel implements ComboBoxModel<Employee> {

	private List<Employee> emps;
	private Employee selected;
	private JDialog u;
	
	private MainWindow mw;
	
	public EmployeeComboBoxModel(JDialog parent, List<Employee> emps) {
		this.emps = emps;
		u = parent;
	}
	
	public EmployeeComboBoxModel(MainWindow parent,
			List<Employee> findAllEmployees) {
		this.emps = findAllEmployees;
		mw = parent;
	}
	@Override
	public void addListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Employee getElementAt(int arg0) {
		return emps.get(arg0);
	}

	@Override
	public int getSize() {
		return emps.size();
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getSelectedItem() {
		return this.selected;
	}

	@Override
	public void setSelectedItem(Object anItem) {
		this.selected = (Employee)anItem;
		if (u instanceof EmployeeUpdate) {
			((EmployeeUpdate)u).e = (Employee)anItem;
			((EmployeeUpdate)u).updateLabels(this.selected);
		} else if (u instanceof EmployeeDelete) {
			((EmployeeDelete)u).e = (Employee)anItem;
			((EmployeeDelete)u).updateLabels(this.selected);
		}
		if (mw == null) {
			return;
		}
		mw.e = (Employee)anItem;
		mw.updateYearLabels();
	}

}
