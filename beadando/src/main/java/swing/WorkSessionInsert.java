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
import java.text.SimpleDateFormat;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JRadioButton;
import static core.Main.logger;

public class WorkSessionInsert extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textEmployeeId;
	private JFormattedTextField textDate;
	private JTextField textDuration;
	private ButtonGroup buttonGroup;
	private JRadioButton rdbtnWork;
	
	private JRadioButton rdbtnSick;
	
	private JRadioButton rdbtnDayOff;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WorkSessionInsert dialog = new WorkSessionInsert();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			logger.error("Error happened in WorkSessionInsert.Main()", e);
		}
	}

	/**
	 * Create the dialog.
	 */
	public WorkSessionInsert() {
		setTitle("Insert a worksession");
		setAlwaysOnTop(true);
		setBounds(100, 100, 289, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel labelName = new JLabel("employee_id");
		
		textEmployeeId = new JTextField();
		textEmployeeId.setToolTipText("Must be an existing employee_id");
//		textEmployeeId.setText("name");
		textEmployeeId.setColumns(10);
		
		JLabel labelHireDate = new JLabel("date");
		
		textDate = new JFormattedTextField(new SimpleDateFormat("yyyy.MM.dd"));
		textDate.setToolTipText("Must be formmatted like this YYYY.MM.DD");
		textDate.setColumns(10);
		
		JLabel labelSalary = new JLabel("duration");
		
		textDuration = new JTextField();
		textDuration.setToolTipText("Must be a number in hours(1..24)");
		textDuration.setColumns(10);
		
		JLabel labelDepartment = new JLabel("type");
		
		rdbtnWork = new JRadioButton("Work");
		rdbtnWork.setSelected(true);
		
		rdbtnSick = new JRadioButton("Sick");
		
		rdbtnDayOff = new JRadioButton("Day off");
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnWork);
		buttonGroup.add(rdbtnSick);
		buttonGroup.add(rdbtnDayOff);
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(labelHireDate)
						.addComponent(labelName)
						.addComponent(labelSalary)
						.addComponent(labelDepartment))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnDayOff)
						.addComponent(rdbtnSick)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
							.addComponent(textDate)
							.addComponent(textEmployeeId)
							.addComponent(textDuration))
						.addComponent(rdbtnWork))
					.addContainerGap(97, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(37)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textEmployeeId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelName))
					.addGap(31)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelHireDate))
					.addGap(32)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelSalary)
						.addComponent(textDuration, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addComponent(rdbtnWork)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnSick)
						.addComponent(labelDepartment))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnDayOff)
					.addContainerGap(110, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Insert");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
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
							JOptionPane.showMessageDialog(WorkSessionInsert.this,
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
						WorkSessionDAOFactory wdaof = WorkSessionDAOFactory.newInstance();
						wdaof.setType(WorkSessionDAOFactory.Type.JDBC);
						WorkSessionDAO wdao = wdaof.newWorkSessionDAO();
						WorkSession ws = new WorkSession(1, emp_id, date, (short)duration, type);
						try {
							wdao.createWorkSession(ws);
							JOptionPane.showMessageDialog(WorkSessionInsert.this, "Insert was successful");
							WorkSessionInsert.this.dispose();
						} catch (TooMuchWorkOnADayException e) {
							JOptionPane.showMessageDialog(WorkSessionInsert.this,
									e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						} catch (AlreadyReachedThirtyDayOffs e) {
							JOptionPane.showMessageDialog(WorkSessionInsert.this,
									e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						} catch (AlreadyReachedFifteenSickDays e) {
							JOptionPane.showMessageDialog(WorkSessionInsert.this,
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
					public void actionPerformed(ActionEvent e) {
						WorkSessionInsert.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
