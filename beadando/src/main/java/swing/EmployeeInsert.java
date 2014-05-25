package swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import java.awt.Dimension;

import javax.swing.JTextField;

import dao.Employee;
import dao.EmployeeDAO;
import dao.EmployeeDAOFactory;
import dao.ExistingEmployeeException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class EmployeeInsert extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textId;
	private JTextField textName;
	private JFormattedTextField textHireDate;
	private JTextField textSalary;
	private JTextField textDepartment;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EmployeeInsert dialog = new EmployeeInsert();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EmployeeInsert() {
		setTitle("Insert an employee");
		setAlwaysOnTop(true);
		setBounds(100, 100, 289, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel labelId = new JLabel("id");
		labelId.setSize(new Dimension(20, 10));
		labelId.setPreferredSize(new Dimension(14, 14));
		
		textId = new JTextField();
		textId.setToolTipText("must be a number(eg. 1 or 101)");
		textId.setColumns(10);
		
		JLabel labelName = new JLabel("name");
		
		textName = new JTextField();
		textName.setToolTipText("Must be a human name (eg Arnold Schwarzenegger)");
		textName.setColumns(10);
		
		JLabel labelHireDate = new JLabel("hire date");
		
		textHireDate = new JFormattedTextField(new SimpleDateFormat("yyyy.MM.dd"));
		textHireDate.setToolTipText("Must be formmatted like this YYYY.MM.DD");
		textHireDate.setColumns(10);
		
		JLabel labelSalary = new JLabel("salary");
		
		textSalary = new JTextField();
		textSalary.setToolTipText("Must be a number");
		textSalary.setColumns(10);
		
		JLabel labelDepartment = new JLabel("department");
		
		textDepartment = new JTextField();
		textDepartment.setToolTipText("A department name");
		textDepartment.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(labelId, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelName)
						.addComponent(labelHireDate)
						.addComponent(labelSalary)
						.addComponent(labelDepartment))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(textDepartment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textSalary, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textHireDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(350, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(24)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelId, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(textId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelName)
						.addComponent(textName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(33)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelHireDate)
						.addComponent(textHireDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(32)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelSalary)
						.addComponent(textSalary, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(30)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(labelDepartment)
						.addComponent(textDepartment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(125, Short.MAX_VALUE))
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
						int id;
						String name;
						Date date;
						BigDecimal salary;
						String department;
						try {
							id = Integer.valueOf(textId.getText());
							textId.setBackground(Color.white);
						} catch (NumberFormatException e) {
							textId.setBackground(Color.red);
							return;
						}
						if (textName.getText().length() > 0) {
							name = textName.getText();
							textName.setBackground(Color.white);
						} else {
							textName.setBackground(Color.red);
							return;
						}
						try {
							java.util.Date d = (java.util.Date)textHireDate.getValue();
							date = new Date(d.getTime());
							textHireDate.setBackground(Color.white);
						} catch(Exception e) {
							textHireDate.setBackground(Color.red);
							return;
						}
						
						try {
							salary = new BigDecimal(textSalary.getText());
							textSalary.setBackground(Color.white);
						} catch(NumberFormatException e) {
							textSalary.setBackground(Color.red);
							return;
						}
						if (textDepartment.getText().length() > 0) {
							department = textDepartment.getText();
							textDepartment.setBackground(Color.white);
						} else {
							textDepartment.setBackground(Color.red);
							return;
						}
						Employee e = new Employee(id, name, date, salary, department);
						EmployeeDAOFactory edaof = EmployeeDAOFactory.newInstance();
						edaof.setType(EmployeeDAOFactory.Type.JDBC);
						EmployeeDAO edao = edaof.newEmployeeDAO();
						try {
							edao.createEmployee(e);
							JOptionPane.showMessageDialog(EmployeeInsert.this, "Insert was successful");
							EmployeeInsert.this.dispose();
						} catch (ExistingEmployeeException e1) {
							JOptionPane.showMessageDialog(EmployeeInsert.this,
									"Existing employee with id = " + e1.getEmployeeId(), "Error", JOptionPane.ERROR_MESSAGE);
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
						EmployeeInsert.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
