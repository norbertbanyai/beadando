package swing;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import org.joda.time.LocalDate;

public class WeekComboBoxModel implements ComboBoxModel<WeekModelClass> {

	List<WeekModelClass> list;
	WeekModelClass selected;
	MainWindow mw;
	
	public WeekComboBoxModel(MainWindow mw) {
		list = new ArrayList<>();
		this.mw = mw;
		if (mw.e == null || mw.month == null) {
			throw new IllegalArgumentException();
		}
		LocalDate start = new LocalDate(mw.month.getDate().getTime());
		start = start.minusDays(start.getDayOfMonth() - 1);
		LocalDate end = start.plusMonths(1);
		int endWeek = end.getWeekOfWeekyear();
		while (start.getWeekOfWeekyear() <= endWeek) {
			list.add(new WeekModelClass(new Date(start.toDate().getTime())));
			start = start.plusWeeks(1);
		}
	}
	
	@Override
	public void addListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WeekModelClass getElementAt(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getSelectedItem() {
		return selected;
	}

	@Override
	public void setSelectedItem(Object arg0) {
		selected = (WeekModelClass)arg0;
		mw.week = selected;
		mw.updateWeekLabels();
		
	}

}
