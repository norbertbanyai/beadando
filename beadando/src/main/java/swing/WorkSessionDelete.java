package swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dao.Employee;
import dao.EmployeeDAO;
import dao.EmployeeDAOFactory;
import dao.PersistentLayerException;
import dao.WorkSession;
import dao.WorkSessionDAO;
import dao.WorkSessionDAOFactory;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WorkSessionDelete extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPanel buttonPane;
	private JTextField textId;
	private JTextField textEmployeeId;
	private JTextField textDate;
	private JTextField textDuration;
	private JTextField textType;
	
	WorkSession ws;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WorkSessionDelete dialog = new WorkSessionDelete(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateLabels(WorkSession e) {
		ws = e;
		textId.setText(String.valueOf(e.getId()));
		textEmployeeId.setText(String.valueOf(e.getEmployee_id()));
		String date = e.getDate().toString();
		date = date.replace('-', '.');
		textDate.setText(date);
		textDuration.setText(e.getDuration()+"");
		switch (e.getType()) {
		case WORK:
			textType.setText("Work");
			break;
		case SICKNESS:
			textType.setText("Sick");
			break;
		case DAY_OFF:
			textType.setText("Day off");
			break;
		default:
			break;
		}
	}
	/**
	 * Create the dialog.
	 */
	public WorkSessionDelete(WorkSession selectedSession) {
		setTitle("Delete a session");
		setBounds(100, 100, 450, 500);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton okButton = new JButton("Delete");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e1) {
						WorkSessionDAOFactory wdaof = WorkSessionDAOFactory.newInstance();
						wdaof.setType(WorkSessionDAOFactory.Type.JDBC);
						WorkSessionDAO wdao = wdaof.newWorkSessionDAO();
						try {
							int answer = JOptionPane.showConfirmDialog(WorkSessionDelete.this, "Are you sure you want to delete the session: " + ws.toString());
							if (answer == JOptionPane.CANCEL_OPTION || answer == JOptionPane.NO_OPTION) {
								return;
							}
							wdao.deleteWorkSession(ws);
							JOptionPane.showMessageDialog(WorkSessionDelete.this, "Delete was successful");
							WorkSessionDelete.this.dispose();
						} catch(PersistentLayerException e2) {
							JOptionPane.showMessageDialog(WorkSessionDelete.this,
									e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
						WorkSessionDelete.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, 434, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, 434, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, 423, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addGap(5))
		);
		JLabel labelId = new JLabel("Id");
		
		textId = new JTextField();
		textId.setEditable(false);
		textId.setColumns(10);
		
		JLabel labelName = new JLabel("employee_id");
		
		textEmployeeId = new JTextField();
		textEmployeeId.setEditable(false);
		textEmployeeId.setColumns(10);
		
		JLabel labelHireDate = new JLabel("date");
		
		textDate = new JTextField();
		textDate.setEditable(false);
		textDate.setColumns(10);
		
		JLabel labelSalary = new JLabel("duration");
		
		textDuration = new JTextField();
		textDuration.setEditable(false);
		textDuration.setColumns(10);
		
		JLabel labelDepartment = new JLabel("type");
		
		textType = new JTextField();
		textType.setEditable(false);
		textType.setColumns(10);
		
		WorkSessionDAOFactory wdaof = WorkSessionDAOFactory.newInstance();
		wdaof.setType(WorkSessionDAOFactory.Type.JDBC);
		WorkSessionDAO wdao = wdaof.newWorkSessionDAO();
		
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		comboBox.setModel(new WorkSessionComboBoxModel(WorkSessionDelete.this, wdao.findAllWorkSession()));
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
						.addComponent(comboBox, Alignment.TRAILING, 0, 404, Short.MAX_VALUE)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
							.addGroup(gl_contentPanel.createSequentialGroup()
								.addComponent(labelDepartment)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(textType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
									.addComponent(labelId)
									.addComponent(labelName)
									.addComponent(labelHireDate)
									.addComponent(labelSalary))
								.addGap(58)
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
									.addComponent(textEmployeeId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(textId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(textDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(textDuration, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(51)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelId)
						.addComponent(textId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(28)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelName)
						.addComponent(textEmployeeId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelHireDate)
						.addComponent(textDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(27)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(labelSalary)
						.addComponent(textDuration, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(30)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelDepartment)
						.addComponent(textType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(119, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		getContentPane().setLayout(groupLayout);
	}
}
