package swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;


import javax.swing.JTextField;

import dao.Employee;
import dao.EmployeeDAO;
import dao.EmployeeDAOFactory;
import dao.PersistentLayerException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.JComboBox;
import static core.Main.logger;

public class EmployeeUpdate extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textName;
	private JTextField textSalary;
	private JTextField textDepartment;
	Employee e;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EmployeeUpdate dialog = new EmployeeUpdate(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e1) {
			logger.error("Error happened in EmployeeUpdate.Main()", e1);
		}
	}
	
	public void updateLabels(Employee e) {
		textName.setText(e.getName());
		textSalary.setText(e.getSalary().toString());
		textDepartment.setText(e.getDepartment());
	}

	/**
	 * Create the dialog.
	 */
	public EmployeeUpdate(Employee selectedEmployee) {
		setTitle("Update an employee");
		setAlwaysOnTop(true);
		setBounds(100, 100, 289, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel labelName = new JLabel("name");
		
		textName = new JTextField();
		textName.setToolTipText("Must be a human name (eg Arnold Schwarzenegger)");
		textName.setColumns(10);
		
		JLabel labelSalary = new JLabel("salary");
		
		textSalary = new JTextField();
		textSalary.setToolTipText("Must be a number");
		textSalary.setColumns(10);
		
		JLabel labelDepartment = new JLabel("department");
		
		textDepartment = new JTextField();
		textDepartment.setToolTipText("A department name");
		textDepartment.setColumns(10);
		
		EmployeeDAOFactory edaofs = EmployeeDAOFactory.newInstance();
		edaofs.setType(EmployeeDAOFactory.Type.JDBC);
		EmployeeDAO edaos = edaofs.newEmployeeDAO();
		
		
		
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		comboBox.setModel(new EmployeeComboBoxModel(EmployeeUpdate.this, edaos.findAllEmployees()));
		if (selectedEmployee != null) {
			e = selectedEmployee;
			updateLabels(e);
			comboBox.setSelectedItem(e);
		}
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(comboBox, 0, 243, Short.MAX_VALUE)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(labelName)
								.addComponent(labelSalary)
								.addComponent(labelDepartment))
							.addGap(22)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(textDepartment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textSalary, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
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
						.addComponent(textName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelSalary)
						.addComponent(textSalary, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(31)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelDepartment)
						.addComponent(textDepartment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(192, Short.MAX_VALUE))
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
						if (e == null) {
							return;
						}
						BigDecimal salary;
						
						if (textName.getText().length() > 0) {
							e.setName(textName.getText());
							textName.setBackground(Color.white);
						} else {
							textName.setBackground(Color.red);
							return;
						}
						
						try {
							salary = new BigDecimal(textSalary.getText());
							e.setSalary(salary);
							textSalary.setBackground(Color.white);
						} catch(NumberFormatException e) {
							textSalary.setBackground(Color.red);
							return;
						}
						if (textDepartment.getText().length() > 0) {
							e.setDepartment(textDepartment.getText());
							textDepartment.setBackground(Color.white);
						} else {
							textDepartment.setBackground(Color.red);
							return;
						}
						EmployeeDAOFactory edaof = EmployeeDAOFactory.newInstance();
						edaof.setType(EmployeeDAOFactory.Type.JDBC);
						EmployeeDAO edao = edaof.newEmployeeDAO();
						try {
							edao.updateEmployee(e);
							JOptionPane.showMessageDialog(EmployeeUpdate.this, "Update was successful");
							EmployeeUpdate.this.dispose();
						} catch(PersistentLayerException e) {
							logger.error("Error happened while trying to update an employee in EmployeeUpdate", e);
							JOptionPane.showMessageDialog(EmployeeUpdate.this,
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
						EmployeeUpdate.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
