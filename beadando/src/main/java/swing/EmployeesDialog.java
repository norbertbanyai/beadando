package swing;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;

import dao.Employee;
import dao.EmployeeDAO;
import dao.EmployeeDAOFactory;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import static core.Main.logger;

public class EmployeesDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private List<Employee> list;
	private JButton okButton;
	private JButton btnUpdateSelectedRow;
	private JButton btnDeleteSelectedRow;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EmployeesDialog dialog = new EmployeesDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			logger.error("Error happened in EmployeesDialog.Main()", e);
		}
	}

	/**
	 * Create the dialog.
	 */
	public EmployeesDialog() {
		setBounds(100, 100, 606, 376);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			table = new JTable();
			EmployeeDAOFactory edaof = EmployeeDAOFactory.newInstance();
			edaof.setType(EmployeeDAOFactory.Type.JDBC);
			EmployeeDAO edao = edaof.newEmployeeDAO();
			list = edao.findAllEmployees();
			table.setModel(new EmployeeJTableModel(list));
			contentPanel.add(table, BorderLayout.CENTER);
			contentPanel.add(new JScrollPane(table));
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Refresh");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						EmployeeDAOFactory edaof = EmployeeDAOFactory.newInstance();
						edaof.setType(EmployeeDAOFactory.Type.JDBC);
						EmployeeDAO edao = edaof.newEmployeeDAO();
						list = edao.findAllEmployees();
						table.setModel(new EmployeeJTableModel(list));
					}
				});
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
			}
			
			JButton btnNewButton = new JButton("Close");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					EmployeesDialog.this.dispose();
				}
			});
			btnUpdateSelectedRow = new JButton("Update selected row");
			btnUpdateSelectedRow.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (table.getSelectedRow() < 0) {
						return;
					}
					new EmployeeUpdate(list.get(table.getSelectedRow())).setVisible(true);
				}
			});
			btnDeleteSelectedRow = new JButton("Delete selected row");
			btnDeleteSelectedRow.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (table.getSelectedRow() < 0) {
						return;
					}
					new EmployeeDelete(list.get(table.getSelectedRow())).setVisible(true);
				}
			});
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addContainerGap()
						.addComponent(okButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
						.addGap(38)
						.addComponent(btnUpdateSelectedRow)
						.addGap(26)
						.addComponent(btnDeleteSelectedRow)
						.addPreferredGap(ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
						.addComponent(btnNewButton)
						.addContainerGap())
			);
			gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnNewButton)
							.addComponent(okButton)
							.addComponent(btnUpdateSelectedRow)
							.addComponent(btnDeleteSelectedRow))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			);
			buttonPane.setLayout(gl_buttonPane);
		}
	}
}
