package swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import javax.swing.JTextField;

import dao.AlreadyReachedFifteenSickDays;
import dao.AlreadyReachedThirtyDayOffs;
import dao.Employee;
import dao.EmployeeDAO;
import dao.EmployeeDAOFactory;
import dao.EmployeeNotFoundException;
import dao.TooMuchWorkOnADayException;
import dao.WorkSession;
import dao.WorkSessionDAO;
import dao.WorkSessionDAOFactory;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import static core.Main.logger;

public class WorkSessionUpdate extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textEmployeeId;
	private JTextField textDuration;
	private JFormattedTextField textDate;
	WorkSession ws;
	
	private ButtonGroup buttonGroup;
	private JRadioButton rdbtnWork;
	private JRadioButton rdbtnSick;
	private JRadioButton rdbtnDayOff;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WorkSessionUpdate dialog = new WorkSessionUpdate(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			logger.error("Error happened in WorkSessionUpdate.Main()", e);
		}
	}
	
	public void updateLabels(WorkSession e) {
		ws = e;
		textEmployeeId.setText(String.valueOf(e.getEmployee_id()));
		String date = e.getDate().toString();
		date = date.replace('-', '.');
		textDate.setText(date);
		try {
			textDate.commitEdit();
			textDate.setBackground(Color.white);
		} catch (ParseException e1) {
			textDate.setBackground(Color.red);
		}
		textDuration.setText(e.getDuration()+"");
		switch (e.getType()) {
		case WORK:
			rdbtnWork.setSelected(true);
			break;
		case SICKNESS:
			rdbtnSick.setSelected(true);
			break;
		case DAY_OFF:
			rdbtnDayOff.setSelected(true);
			break;
		default:
			break;
		}
	}

	/**
	 * Create the dialog.
	 */
	public WorkSessionUpdate(WorkSession selectedSession) {
		setTitle("Update a worksession");
		setAlwaysOnTop(true);
		setBounds(100, 100, 434, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel labelName = new JLabel("employee_id");
		
		textEmployeeId = new JTextField();
		textEmployeeId.setToolTipText("Must be an existing employee id(eg 1)");
		textEmployeeId.setText("");
		textEmployeeId.setColumns(10);
		
		JLabel labelSalary = new JLabel("duration");
		
		textDuration = new JTextField();
		textDuration.setToolTipText("Must be a number(in hours)");
		textDuration.setColumns(10);
		
		JLabel labelDepartment = new JLabel("type");
		
		WorkSessionDAOFactory wdaof = WorkSessionDAOFactory.newInstance();
		wdaof.setType(WorkSessionDAOFactory.Type.JDBC);
		WorkSessionDAO wdao = wdaof.newWorkSessionDAO();
		
		
		
		
		JLabel lblDate = new JLabel("date");
		
		textDate = new JFormattedTextField(new SimpleDateFormat("yyyy.MM.dd"));
		textDate.setToolTipText("Must be formmatted like this YYYY.MM.DD");
		textDate.setColumns(10);
		
		rdbtnWork = new JRadioButton("Work");
		rdbtnWork.setSelected(true);
		
		rdbtnSick = new JRadioButton("Sick");
		
		rdbtnDayOff = new JRadioButton("Day off");
		buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnWork);
		buttonGroup.add(rdbtnSick);
		buttonGroup.add(rdbtnDayOff);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		comboBox.setModel(new WorkSessionComboBoxModel(WorkSessionUpdate.this, wdao.findAllWorkSession()));
		if (selectedSession != null) {
			ws = selectedSession;
			updateLabels(ws);
			comboBox.setSelectedItem(ws);
		}
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(comboBox, 0, 388, Short.MAX_VALUE)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(labelName)
								.addComponent(labelSalary)
								.addComponent(lblDate)
								.addComponent(labelDepartment))
							.addGap(22)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(rdbtnSick)
								.addComponent(rdbtnWork)
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
									.addComponent(textDuration)
									.addComponent(textEmployeeId)
									.addComponent(textDate))
								.addComponent(rdbtnDayOff))))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(31)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelName)
						.addComponent(textEmployeeId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(31)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDate)
						.addComponent(textDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(39)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelSalary)
						.addComponent(textDuration, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(rdbtnWork)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnSick)
						.addComponent(labelDepartment))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnDayOff)
					.addContainerGap(89, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Update");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (ws == null) {
							return;
						}
						int emp_id;
						Date date;
						int duration;
						String type;
						
						try {
							emp_id = Integer.valueOf(textEmployeeId.getText());
							textEmployeeId.setBackground(Color.white);
						} catch (NumberFormatException e) {
							textEmployeeId.setBackground(Color.red);
							return;
						}
						EmployeeDAOFactory edaof = EmployeeDAOFactory.newInstance();
						edaof.setType(EmployeeDAOFactory.Type.JDBC);
						EmployeeDAO edao = edaof.newEmployeeDAO();
						try {
							Employee emp = edao.findEmployeeById(emp_id);
							textEmployeeId.setBackground(Color.white);
						} catch (EmployeeNotFoundException e1) {
							textEmployeeId.setBackground(Color.red);
							JOptionPane.showMessageDialog(WorkSessionUpdate.this,
									"Employee not found with id = " + e1.getEmployeeId(), "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						try {
							java.util.Date d = (java.util.Date)textDate.getValue();
							date = new Date(d.getTime());
							textDate.setBackground(Color.white);
						} catch(Exception e) {
							textDate.setBackground(Color.red);
							return;
						}
						
						try {
							duration = Integer.valueOf(textDuration.getText());
							textDuration.setBackground(Color.white);
						} catch (NumberFormatException e) {
							textDuration.setBackground(Color.red);
							return;
						}
						
						if (rdbtnWork.isSelected()) {
							type = "WORK";
						} else if (rdbtnSick.isSelected()) {
							type = "SICKNESS";
						} else if (rdbtnDayOff.isSelected()) {
							type = "DAY_OFF";
						} else {
							return;
						}
						
						WorkSession w = new WorkSession(ws.getId(), emp_id, date, (short)duration, type);
						
						WorkSessionDAOFactory wdaof = WorkSessionDAOFactory.newInstance();
						wdaof.setType(WorkSessionDAOFactory.Type.JDBC);
						WorkSessionDAO wdao = wdaof.newWorkSessionDAO();
						try {
							wdao.updateWorkSession(w);
							JOptionPane.showMessageDialog(WorkSessionUpdate.this, "Update was successful");
							WorkSessionUpdate.this.dispose();
						} catch (AlreadyReachedThirtyDayOffs
								| AlreadyReachedFifteenSickDays
								| TooMuchWorkOnADayException e) {
							JOptionPane.showMessageDialog(WorkSessionUpdate.this,
									e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						WorkSessionUpdate.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
