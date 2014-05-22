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

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EmployeeDelete extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPanel buttonPane;
	private JTextField textId;
	private JTextField textName;
	private JTextField textHireDate;
	private JTextField textSalary;
	private JTextField textDepartment;
	
	Employee e;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EmployeeDelete dialog = new EmployeeDelete(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateLabels(Employee e) {
		textId.setText(String.valueOf(e.getId()));
		textName.setText(e.getName());
		textHireDate.setText(e.getHireDate().toString());
		textSalary.setText(e.getSalary().toString());
		textDepartment.setText(e.getDepartment());
	}
	/**
	 * Create the dialog.
	 */
	public EmployeeDelete(Employee selectedEmployee) {
		setTitle("Delete an employee");
		setBounds(100, 100, 450, 500);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton okButton = new JButton("Delete");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e1) {
						EmployeeDAOFactory edaof = EmployeeDAOFactory.newInstance();
						edaof.setType(EmployeeDAOFactory.Type.JDBC);
						EmployeeDAO edao = edaof.newEmployeeDAO();
						try {
							int answer = JOptionPane.showConfirmDialog(EmployeeDelete.this, "Are you sure you want to delete the employee: " + e.toString());
							if (answer == JOptionPane.CANCEL_OPTION || answer == JOptionPane.NO_OPTION) {
								return;
							}
							edao.deleteEmployee(e);
							JOptionPane.showMessageDialog(EmployeeDelete.this, "Delete was successful");
							EmployeeDelete.this.dispose();
						} catch(PersistentLayerException e2) {
							JOptionPane.showMessageDialog(EmployeeDelete.this,
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
						EmployeeDelete.this.dispose();
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
		
		JLabel labelName = new JLabel("Name");
		
		textName = new JTextField();
		textName.setEditable(false);
		textName.setColumns(10);
		
		JLabel labelHireDate = new JLabel("Hiredate");
		
		textHireDate = new JTextField();
		textHireDate.setEditable(false);
		textHireDate.setColumns(10);
		
		JLabel labelSalary = new JLabel("Salary");
		
		textSalary = new JTextField();
		textSalary.setEditable(false);
		textSalary.setColumns(10);
		
		JLabel labelDepartment = new JLabel("Department");
		
		textDepartment = new JTextField();
		textDepartment.setEditable(false);
		textDepartment.setColumns(10);
		
		EmployeeDAOFactory edaofs = EmployeeDAOFactory.newInstance();
		edaofs.setType(EmployeeDAOFactory.Type.JDBC);
		EmployeeDAO edaos = edaofs.newEmployeeDAO();
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new EmployeeComboBoxModel(EmployeeDelete.this, edaos.findAllEmployees()));
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
						.addComponent(comboBox, Alignment.TRAILING, 0, 404, Short.MAX_VALUE)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
							.addGroup(gl_contentPanel.createSequentialGroup()
								.addComponent(labelDepartment)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(textDepartment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
									.addComponent(labelId)
									.addComponent(labelName)
									.addComponent(labelHireDate)
									.addComponent(labelSalary))
								.addGap(58)
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
									.addComponent(textName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(textId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(textHireDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(textSalary, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
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
						.addComponent(textName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelHireDate)
						.addComponent(textHireDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(27)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(labelSalary)
						.addComponent(textSalary, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(30)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelDepartment)
						.addComponent(textDepartment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(119, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		getContentPane().setLayout(groupLayout);
	}
}
