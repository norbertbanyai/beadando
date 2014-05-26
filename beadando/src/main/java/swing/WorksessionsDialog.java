package swing;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;

import dao.Employee;
import dao.PersistentLayerException;
import dao.WorkSession;
import dao.WorkSessionDAO;
import dao.WorkSessionDAOFactory;
import dao.impl.ConnectionHelper;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.joda.time.LocalDate;
import static core.Main.logger;

public class WorksessionsDialog extends JDialog {

	enum Type {
		YEAR,
		YEARMONTH,
		WEEK,
		DEFAULT
	}
	private Employee employee;
	private List<WorkSession> list;
	private Type type;
	private Date date;
	
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JButton okButton;
	private JButton btnUpdateSelectedRow;
	private JButton btnDeleteSelectedRow;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WorksessionsDialog dialog = new WorksessionsDialog(null, Type.DEFAULT, null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			logger.error("Error happened in WorksessionsDialog.Main()", e);
		}
	}

	/**
	 * Create the dialog.
	 */
	public WorksessionsDialog(List<WorkSession> l, Type type, Employee emp, Date date) {
		setBounds(100, 100, 606, 376);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			this.type = type;
			this.employee = emp;
			this.date = date;
			table = new JTable();
			switch (type) {
			case YEAR:
				List<WorkSession> listTemp = new ArrayList<>();
				try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select * from worksessions where to_char(session_date, 'YYYY') = to_char(sysdate, 'YYYY') and employee_id = ?")) {
					pstmt.setInt(1, emp.getId());
					try(ResultSet rset = pstmt.executeQuery()) {
						while (rset.next()) {
							listTemp.add(new WorkSession(rset.getInt("worksession_id"), rset.getInt("employee_id"),
							rset.getDate("session_date"), rset.getShort("duration"), rset.getString("type")));
						}
					}
				} catch (SQLException | IOException e) {
					logger.error("Error happened in WorksessionsDialog: case YEAR", e);
					throw new PersistentLayerException(e);
				}
				list = listTemp;
				break;
			case YEARMONTH:
				List<WorkSession> listTemp2 = new ArrayList<>();
				try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select * from worksessions where to_char(session_date, 'YYYY.MM') = to_char(?, 'YYYY.MM') and employee_id = ?")) {
					pstmt.setDate(1, this.date);
					pstmt.setInt(2, emp.getId());
					try(ResultSet rset = pstmt.executeQuery()) {
						while (rset.next()) {
							listTemp2.add(new WorkSession(rset.getInt("worksession_id"), rset.getInt("employee_id"),
							rset.getDate("session_date"), rset.getShort("duration"), rset.getString("type")));
						}
					}
				} catch (SQLException | IOException e) {
					logger.error("Error happened in WorksessionsDialog: case YEARMONTH", e);
					throw new PersistentLayerException(e);
				}
				list = listTemp2;
				break;
			case WEEK:
				List<WorkSession> listTemp3 = new ArrayList<>();
				try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select * from worksessions where session_date between ? and ? and employee_id = ?")) {
					LocalDate start = new LocalDate(this.date);
					start = start.minusDays(start.getDayOfWeek() - 1);
					LocalDate end = start.plusWeeks(1);
					end = end.minusDays(1);
					pstmt.setDate(1, new Date(start.toDate().getTime()));
					pstmt.setDate(2, new Date(end.toDate().getTime()));
					pstmt.setInt(3, emp.getId());
					try(ResultSet rset = pstmt.executeQuery()) {
						while (rset.next()) {
							listTemp3.add(new WorkSession(rset.getInt("worksession_id"), rset.getInt("employee_id"),
							rset.getDate("session_date"), rset.getShort("duration"), rset.getString("type")));
						}
					}
				} catch (SQLException | IOException e) {
					logger.error("Error happened in WorksessionsDialog: case WEEK", e);
					throw new PersistentLayerException(e);
				}
				list = listTemp3;
				break;
			case DEFAULT:
				break;

			default:
				break;
			}
			if (type.equals(Type.DEFAULT)) {
				WorkSessionDAOFactory wdaof = WorkSessionDAOFactory.newInstance();
				wdaof.setType(WorkSessionDAOFactory.Type.JDBC);
				WorkSessionDAO wdao = wdaof.newWorkSessionDAO();
				list = wdao.findAllWorkSession();
				table.setModel(new WorkSessionJTableModel(list));
				contentPanel.add(table, BorderLayout.CENTER);
				contentPanel.add(new JScrollPane(table));
			} else {
				table.setModel(new WorkSessionJTableModel(list));
				contentPanel.add(table, BorderLayout.CENTER);
				contentPanel.add(new JScrollPane(table));
				okButton = new JButton("Refresh");
			}
			
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				if (okButton == null) {
					okButton = new JButton("Refresh");
				}
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						switch (WorksessionsDialog.this.type) {
						case YEAR:
							List<WorkSession> listTemp = new ArrayList<>();
							try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select * from worksessions where to_char(session_date, 'YYYY') = to_char(sysdate, 'YYYY') and employee_id = ?")) {
								pstmt.setInt(1, WorksessionsDialog.this.employee.getId());
								try(ResultSet rset = pstmt.executeQuery()) {
									while (rset.next()) {
										listTemp.add(new WorkSession(rset.getInt("worksession_id"), rset.getInt("employee_id"),
										rset.getDate("session_date"), rset.getShort("duration"), rset.getString("type")));
									}
								}
							} catch (SQLException | IOException e) {
								logger.error("Error happened while trying to refresh the table in case YEAR", e);
								throw new PersistentLayerException(e);
							}
							list = listTemp;
							break;
						case YEARMONTH:
							List<WorkSession> listTemp2 = new ArrayList<>();
							try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select * from worksessions where to_char(session_date, 'YYYY.MM') = to_char(?, 'YYYY.MM') and employee_id = ?")) {
								pstmt.setDate(1, WorksessionsDialog.this.date);
								pstmt.setInt(2, WorksessionsDialog.this.employee.getId());
								try(ResultSet rset = pstmt.executeQuery()) {
									while (rset.next()) {
										listTemp2.add(new WorkSession(rset.getInt("worksession_id"), rset.getInt("employee_id"),
										rset.getDate("session_date"), rset.getShort("duration"), rset.getString("type")));
									}
								}
							} catch (SQLException | IOException e) {
								logger.error("Error happened while trying to refresh the table in case YEARMONTH", e);
								throw new PersistentLayerException(e);
							}
							list = listTemp2;
							break;
						case WEEK:
							List<WorkSession> listTemp3 = new ArrayList<>();
							try(PreparedStatement pstmt = ConnectionHelper.getConnection().prepareStatement("select * from worksessions where session_date between ? and ? and employee_id = ?")) {
								LocalDate start = new LocalDate(WorksessionsDialog.this.date);
								start = start.minusDays(start.getDayOfWeek() - 1);
								LocalDate end = start.plusWeeks(1);
								end = end.minusDays(1);
								pstmt.setDate(1, new Date(start.toDate().getTime()));
								pstmt.setDate(2, new Date(end.toDate().getTime()));
								pstmt.setInt(3, WorksessionsDialog.this.employee.getId());
								try(ResultSet rset = pstmt.executeQuery()) {
									while (rset.next()) {
										listTemp3.add(new WorkSession(rset.getInt("worksession_id"), rset.getInt("employee_id"),
										rset.getDate("session_date"), rset.getShort("duration"), rset.getString("type")));
									}
								}
							} catch (SQLException | IOException e) {
								logger.error("Error happened while trying to refresh the table in case WEEK", e);
								throw new PersistentLayerException(e);
							}
							list = listTemp3;
							break;
						case DEFAULT:
							break;

						default:
							break;
						}
						if (WorksessionsDialog.this.type.equals(Type.DEFAULT)) {
							WorkSessionDAOFactory wdaof = WorkSessionDAOFactory.newInstance();
							wdaof.setType(WorkSessionDAOFactory.Type.JDBC);
							WorkSessionDAO wdao = wdaof.newWorkSessionDAO();
							list = wdao.findAllWorkSession();
							table.setModel(new WorkSessionJTableModel(list));
						} else {
							table.setModel(new WorkSessionJTableModel(list));
						}
						
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
