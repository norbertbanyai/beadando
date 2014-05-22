package swing;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import org.joda.time.LocalDate;

public class MonthComboBoxModel implements ComboBoxModel<MonthModelClass> {

	List<MonthModelClass> list;
	MonthModelClass selected;
	MainWindow mw;
	
	public MonthComboBoxModel(MainWindow mw) {
		list = new ArrayList<>();
		this.mw = mw;
		LocalDate ldate = new LocalDate(LocalDate.now());
		ldate = ldate.minusDays(ldate.getDayOfMonth() - 1);
		ldate = ldate.minusMonths(ldate.getMonthOfYear() -1 );
		ldate = ldate.minusYears(1);
		for (int i = 0; i < 12; i++) {
			LocalDate date = ldate.plusMonths(i);
			list.add(new MonthModelClass(new Date(date.toDate().getTime())));
		}
		ldate = ldate.plusYears(1);
		for (int i = 0; i < 12; i++) {
			LocalDate date = ldate.plusMonths(i);
			list.add(new MonthModelClass(new Date(date.toDate().getTime())));
		}
	}
	@Override
	public void addListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MonthModelClass getElementAt(int index) {
		return list.get(index);
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getSelectedItem() {
		return selected;
	}

	@Override
	public void setSelectedItem(Object anItem) {
		selected = (MonthModelClass)anItem;
		mw.month = selected;
		mw.updateMonthLabels();
	}

}
