/**
 * 
 */
package swing;

import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JDialog;
import javax.swing.event.ListDataListener;

import dao.WorkSession;

/**
 * @author bnorbi
 * 
 */
class WorkSessionComboBoxModel implements ComboBoxModel<WorkSession> {

	private List<WorkSession> sessions;
	private WorkSession selected;
	private JDialog parent;
	
	public WorkSessionComboBoxModel(JDialog parent, List<WorkSession> l) {
		this.sessions = l;
		this.parent = parent;
	}
	@Override
	public void addListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public WorkSession getElementAt(int arg0) {
		return sessions.get(arg0);
	}

	@Override
	public int getSize() {
		return sessions.size();
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
		this.selected = (WorkSession)anItem;
		if (parent instanceof WorkSessionUpdate) {
			((WorkSessionUpdate)parent).ws = (WorkSession)anItem;
			((WorkSessionUpdate)parent).updateLabels(this.selected);
		} else if (parent instanceof WorkSessionDelete) {
			((WorkSessionDelete)parent).ws = (WorkSession)anItem;
			((WorkSessionDelete)parent).updateLabels(this.selected);
		}
	}

}
