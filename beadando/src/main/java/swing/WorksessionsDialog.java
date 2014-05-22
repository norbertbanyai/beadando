package swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;

import dao.Employee;
import dao.EmployeeDAO;
import dao.EmployeeDAOFactory;
import dao.WorkSession;
import dao.WorkSessionDAO;
import dao.WorkSessionDAOFactory;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class WorksessionsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private List<WorkSession> list;
	private JButton okButton;
	private JButton btnUpdateSelectedRow;
	private JButton btnDeleteSelectedRow;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WorksessionsDialog dialog = new WorksessionsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WorksessionsDialog() {
		setBounds(100, 100, 606, 376);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			table = new JTable();
			WorkSessionDAOFactory wdaof = WorkSessionDAOFactory.newInstance();
			wdaof.setType(WorkSessionDAOFactory.Type.JDBC);
			WorkSessionDAO wdao = wdaof.newWorkSessionDAO();
			list = wdao.findAllWorkSession();
			table.setModel(new WorkSessionJTableModel(list));
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
						WorkSessionDAOFactory wdaof = WorkSessionDAOFactory.newInstance();
						wdaof.setType(WorkSessionDAOFactory.Type.JDBC);
						WorkSessionDAO wdao = wdaof.newWorkSessionDAO();
						list = wdao.findAllWorkSession();
						table.setModel(new WorkSessionJTableModel(list));
					}
				});
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
			}
			
			JButton btnNewButton = new JButton("Close");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WorksessionsDialog.this.dispose();
				}
			});
			btnUpdateSelectedRow = new JButton("Update selected row");
			btnUpdateSelectedRow.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (table.getSelectedRow() < 0) {
						return;
					}
					new WorkSessionUpdate(list.get(table.getSelectedRow())).setVisible(true);
				}
			});
			btnDeleteSelectedRow = new JButton("Delete selected row");
			btnDeleteSelectedRow.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (table.getSelectedRow() < 0) {
						return;
					}
					new WorkSessionDelete(list.get(table.getSelectedRow())).setVisible(true);
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
