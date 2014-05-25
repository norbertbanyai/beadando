package swing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTable;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;

import dao.Employee;
import dao.EmployeeDAO;
import dao.EmployeeDAOFactory;
import dao.PersistentLayerException;
import dao.WorkSession;
import dao.impl.ConnectionHelper;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import core.Calculations;
import core.SalaryException;

import java.awt.Color;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;

import org.joda.time.LocalDate;

import swing.WorksessionsDialog.Type;
import javax.swing.JTextArea;

public class MainWindow {

	private JFrame mainFrame;

	Employee e;
	MonthModelClass month;
	WeekModelClass week;
	private JTextField textYearWorkedDays;
	private JTextField textYearSickDays;
	private JTextField textYearDayOffs;
	private JTextField textMonthWorkedHours;
	private JTextField textMonthSickDays;
	private JTextField textMonthDayOffs;
	private JComboBox comboBoxWeek;
	private JComboBox comboBoxMonth;
	private JTextField textWorkedHoursWeek;
	private JTextField textSickDaysWeek;
	private JTextField textDaysOffsWeek;
	private JButton btnYearWorkSessions;
	private JButton btnMonthWorkSessions;
	private JButton btnWeekWorkSessions;
	private JButton btnSalary;
	private JTextField textSalary;
	private JTextArea textProblems;

	public void updateYearLabels() {
		if (e == null) {
			return;
		}
		textYearWorkedDays.setText(String.valueOf(Calculations
				.getAllWorkedDayCurrentYearByEmployeeId(e.getId())));
		textYearSickDays.setText(String.valueOf(Calculations
				.getSickDaysInCurrentYearByEmployeeId(e.getId())));
		textYearDayOffs.setText(String.valueOf(Calculations
				.getDayOffsInCurrentYearByEmployeeId(e.getId())));
		btnYearWorkSessions.setEnabled(true);
		comboBoxMonth.setEnabled(true);
		updateMonthLabels();

	}

	public void updateMonthLabels() {
		if (e == null || month == null) {
			return;
		}
		Date date = month.getDate();
		textMonthWorkedHours.setText(String.valueOf(Calculations
				.getWorkedHoursInAMonthByEmployeeId(e.getId(), date)));
		textMonthSickDays.setText(String.valueOf(Calculations
				.getSickDaysInAMonthByEmployeeId(e.getId(), date)));
		textMonthDayOffs.setText(String.valueOf(Calculations
				.getDayOffsInAMonthByEmployeeId(e.getId(), date)));

		btnMonthWorkSessions.setEnabled(true);
		btnSalary.setEnabled(true);
		if (textSalary != null) {
			textSalary.setText("");
		}

		textWorkedHoursWeek.setText("");
		textSickDaysWeek.setText("");
		textDaysOffsWeek.setText("");
		btnWeekWorkSessions.setEnabled(false);
		comboBoxWeek.setEnabled(true);
		comboBoxWeek.setModel(new WeekComboBoxModel(MainWindow.this));
	}

	public void updateWeekLabels() {
		if (e == null || month == null || week == null) {
			return;
		}
		Date date = week.getDate();
		textWorkedHoursWeek.setText(String.valueOf(Calculations
				.getWorkedHoursOnAWeekByEmployeeId(e.getId(), date)));
		textSickDaysWeek.setText(String.valueOf(Calculations
				.getSickDaysOnAWeekByEmployeeId(e.getId(), date)));
		textDaysOffsWeek.setText(String.valueOf(Calculations
				.getDayOffsOnAWeekByEmployeeId(e.getId(), date)));
		btnWeekWorkSessions.setEnabled(true);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.setFont(new Font("SansSerif", Font.PLAIN, 12));
		mainFrame.setTitle("Session Handler");
		mainFrame.setBounds(500, 200, 450, 300);
		mainFrame.setSize(605, 431);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenu mnEmployee = new JMenu("Employee");
		mnEdit.add(mnEmployee);

		JMenuItem mntmInsert = new JMenuItem("Insert...");
		mntmInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new EmployeeInsert().setVisible(true);
			}
		});
		mnEmployee.add(mntmInsert);

		JMenuItem mntmUpdate = new JMenuItem("Update...");
		mntmUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new EmployeeUpdate(null).setVisible(true);
			}
		});
		mnEmployee.add(mntmUpdate);

		JMenuItem mntmDelete = new JMenuItem("Delete...");
		mntmDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new EmployeeDelete(null).setVisible(true);
			}
		});
		mnEmployee.add(mntmDelete);

		JMenu mnWorksession = new JMenu("Worksession");
		mnEdit.add(mnWorksession);

		JMenuItem mntmInsert_1 = new JMenuItem("Insert...");
		mntmInsert_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new WorkSessionInsert().setVisible(true);
			}
		});
		mnWorksession.add(mntmInsert_1);

		JMenuItem mntmUpdate_1 = new JMenuItem("Update...");
		mntmUpdate_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new WorkSessionUpdate(null).setVisible(true);
			}
		});
		mnWorksession.add(mntmUpdate_1);

		JMenuItem mntmDelete_1 = new JMenuItem("Delete");
		mntmDelete_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new WorkSessionDelete(null).setVisible(true);
			}
		});
		mnWorksession.add(mntmDelete_1);

		JMenu mnView = new JMenu("Show");
		menuBar.add(mnView);

		JMenuItem mntmEmployees = new JMenuItem("Employees");
		mntmEmployees.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new EmployeesDialog().setVisible(true);
			}
		});
		mnView.add(mntmEmployees);

		JMenuItem mntmWorksessions = new JMenuItem("Worksessions");
		mntmWorksessions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new WorksessionsDialog(null, Type.DEFAULT, null, null)
						.setVisible(true);
			}
		});
		mnView.add(mntmWorksessions);

		JMenu mnImportExport = new JMenu("Import/Export");
		menuBar.add(mnImportExport);

		JMenuItem mntmImportEmployees = new JMenuItem("Import employees");
		mntmImportEmployees.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Import().setVisible(true);
			}
		});
		mnImportExport.add(mntmImportEmployees);

		JMenuItem mntmExportEmployees = new JMenuItem("Export employees");
		mntmExportEmployees.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Export().setVisible(true);
			}
		});
		mnImportExport.add(mntmExportEmployees);

		EmployeeDAOFactory edaofs = EmployeeDAOFactory.newInstance();
		edaofs.setType(EmployeeDAOFactory.Type.JDBC);
		EmployeeDAO edaos = edaofs.newEmployeeDAO();

		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(107, 11, 405, 20);
		comboBox.setModel(new EmployeeComboBoxModel(MainWindow.this, edaos
				.findAllEmployees()));

		JLabel lblWorkedDaysThis = new JLabel("Worked days");
		lblWorkedDaysThis.setBounds(10, 90, 90, 14);

		textYearWorkedDays = new JTextField();
		textYearWorkedDays.setBounds(118, 87, 49, 20);
		textYearWorkedDays.setForeground(new Color(0, 122, 255));
		textYearWorkedDays.setEditable(false);
		textYearWorkedDays.setColumns(10);

		JLabel lblSickDaysThis = new JLabel("Sick days");
		lblSickDaysThis.setBounds(220, 90, 64, 14);

		textYearSickDays = new JTextField();
		textYearSickDays.setBounds(308, 87, 54, 20);
		textYearSickDays.setForeground(new Color(0, 122, 255));
		textYearSickDays.setEditable(false);
		textYearSickDays.setColumns(10);

		textYearDayOffs = new JTextField();
		textYearDayOffs.setBounds(488, 87, 49, 20);
		textYearDayOffs.setForeground(new Color(0, 122, 255));
		textYearDayOffs.setEditable(false);
		textYearDayOffs.setColumns(10);

		JLabel lblDayOffs = new JLabel("Day offs");
		lblDayOffs.setBounds(417, 90, 61, 14);

		JLabel lblThisYear = new JLabel("This Year");
		lblThisYear.setBounds(267, 42, 70, 23);
		lblThisYear.setHorizontalAlignment(SwingConstants.CENTER);
		lblThisYear.setForeground(new Color(0, 122, 255));
		lblThisYear.setFont(new Font("SansSerif", Font.PLAIN, 17));

		JLabel lblMonth = new JLabel("Month");
		lblMonth.setBounds(267, 125, 70, 23);
		lblMonth.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonth.setVerticalAlignment(SwingConstants.TOP);
		lblMonth.setForeground(new Color(0, 122, 255));
		lblMonth.setFont(new Font("SansSerif", Font.PLAIN, 17));

		comboBoxMonth = new JComboBox();
		comboBoxMonth.setBounds(365, 129, 93, 20);
		comboBoxMonth.setEnabled(false);
		comboBoxMonth.setModel(new MonthComboBoxModel(MainWindow.this));

		JLabel lblWorkedHours = new JLabel("Worked hours");
		lblWorkedHours.setBounds(10, 165, 90, 14);

		textMonthWorkedHours = new JTextField();
		textMonthWorkedHours.setBounds(118, 162, 49, 20);
		textMonthWorkedHours.setForeground(new Color(0, 122, 255));
		textMonthWorkedHours.setEditable(false);
		textMonthWorkedHours.setColumns(10);

		JLabel label = new JLabel("Sick days");
		label.setBounds(220, 160, 64, 14);

		textMonthSickDays = new JTextField();
		textMonthSickDays.setBounds(308, 159, 54, 20);
		textMonthSickDays.setForeground(new Color(0, 122, 255));
		textMonthSickDays.setEditable(false);
		textMonthSickDays.setColumns(10);

		JLabel label_1 = new JLabel("Day offs");
		label_1.setBounds(417, 160, 53, 14);

		textMonthDayOffs = new JTextField();
		textMonthDayOffs.setBounds(488, 157, 49, 20);
		textMonthDayOffs.setForeground(new Color(0, 122, 255));
		textMonthDayOffs.setEditable(false);
		textMonthDayOffs.setColumns(10);

		JLabel lblWeek = new JLabel("Week");
		lblWeek.setToolTipText("Every week calculated from Monday to Sunday no matter if it's out of the selected days of the month");
		lblWeek.setBounds(267, 195, 70, 23);
		lblWeek.setVerticalAlignment(SwingConstants.TOP);
		lblWeek.setHorizontalAlignment(SwingConstants.CENTER);
		lblWeek.setForeground(new Color(0, 122, 255));
		lblWeek.setFont(new Font("SansSerif", Font.PLAIN, 17));

		comboBoxWeek = new JComboBox();
		comboBoxWeek.setBounds(365, 199, 93, 20);
		comboBoxWeek.setEnabled(false);

		JLabel labelWeekWorkHours = new JLabel("Worked hours");
		labelWeekWorkHours.setBounds(10, 235, 90, 14);
		labelWeekWorkHours.setHorizontalAlignment(SwingConstants.LEFT);

		textWorkedHoursWeek = new JTextField();
		textWorkedHoursWeek.setBounds(118, 232, 49, 20);
		textWorkedHoursWeek.setForeground(new Color(0, 122, 255));
		textWorkedHoursWeek.setEditable(false);
		textWorkedHoursWeek.setColumns(10);

		JLabel label_2 = new JLabel("Sick days");
		label_2.setBounds(220, 234, 64, 14);

		textSickDaysWeek = new JTextField();
		textSickDaysWeek.setBounds(308, 231, 54, 20);
		textSickDaysWeek.setForeground(new Color(0, 122, 255));
		textSickDaysWeek.setEditable(false);
		textSickDaysWeek.setColumns(10);

		JLabel label_3 = new JLabel("Day offs");
		label_3.setBounds(417, 234, 53, 14);

		textDaysOffsWeek = new JTextField();
		textDaysOffsWeek.setBounds(488, 231, 49, 20);
		textDaysOffsWeek.setForeground(new Color(0, 122, 255));
		textDaysOffsWeek.setEditable(false);
		textDaysOffsWeek.setColumns(10);

		btnYearWorkSessions = new JButton("Sessions");
		btnYearWorkSessions.setBounds(151, 42, 89, 23);
		btnYearWorkSessions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<WorkSession> list = new ArrayList<>();
				try (PreparedStatement pstmt = ConnectionHelper
						.getConnection()
						.prepareStatement(
								"select * from worksessions where to_char(session_date, 'YYYY') = to_char(sysdate, 'YYYY') and employee_id = ?")) {
					pstmt.setInt(1, e.getId());
					try (ResultSet rset = pstmt.executeQuery()) {
						while (rset.next()) {
							list.add(new WorkSession(rset
									.getInt("worksession_id"), rset
									.getInt("employee_id"), rset
									.getDate("session_date"), rset
									.getShort("duration"), rset
									.getString("type")));
						}
					}
				} catch (SQLException | IOException e) {
					throw new PersistentLayerException(e);
				}
				new WorksessionsDialog(null, Type.YEAR, e, null)
						.setVisible(true);
			}

		});
		btnYearWorkSessions.setEnabled(false);

		btnMonthWorkSessions = new JButton("Sessions");
		btnMonthWorkSessions.setBounds(151, 128, 89, 23);
		btnMonthWorkSessions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<WorkSession> list = new ArrayList<>();
				try (PreparedStatement pstmt = ConnectionHelper
						.getConnection()
						.prepareStatement(
								"select * from worksessions where to_char(session_date, 'YYYY.MM') = to_char(?, 'YYYY.MM') and employee_id = ?")) {
					pstmt.setDate(1, month.getDate());
					pstmt.setInt(2, e.getId());
					try (ResultSet rset = pstmt.executeQuery()) {
						while (rset.next()) {
							list.add(new WorkSession(rset
									.getInt("worksession_id"), rset
									.getInt("employee_id"), rset
									.getDate("session_date"), rset
									.getShort("duration"), rset
									.getString("type")));
						}
					}
				} catch (SQLException | IOException e) {
					throw new PersistentLayerException(e);
				}
				new WorksessionsDialog(null, Type.YEARMONTH, e, month.getDate())
						.setVisible(true);
			}
		});
		btnMonthWorkSessions.setEnabled(false);

		btnWeekWorkSessions = new JButton("Sessions");
		btnWeekWorkSessions.setBounds(151, 198, 89, 23);
		btnWeekWorkSessions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<WorkSession> list = new ArrayList<>();
				try (PreparedStatement pstmt = ConnectionHelper
						.getConnection()
						.prepareStatement(
								"select * from worksessions where session_date between ? and ? and employee_id = ?")) {
					LocalDate start = new LocalDate(week.getDate().getTime());
					start = start.minusDays(start.getDayOfWeek() - 1);
					LocalDate end = start.plusWeeks(1);
					end = end.minusDays(1);
					pstmt.setDate(1, new Date(start.toDate().getTime()));
					pstmt.setDate(2, new Date(end.toDate().getTime()));
					pstmt.setInt(3, e.getId());
					try (ResultSet rset = pstmt.executeQuery()) {
						while (rset.next()) {
							list.add(new WorkSession(rset
									.getInt("worksession_id"), rset
									.getInt("employee_id"), rset
									.getDate("session_date"), rset
									.getShort("duration"), rset
									.getString("type")));
						}
					}
				} catch (SQLException | IOException e) {
					throw new PersistentLayerException(e);
				}
				new WorksessionsDialog(null, Type.WEEK, e, week.getDate())
						.setVisible(true);
			}
		});
		btnWeekWorkSessions.setEnabled(false);

		btnSalary = new JButton("Calculate salary");
		btnSalary.setBounds(10, 295, 134, 23);
		btnSalary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				try {
					textSalary.setText(String.valueOf(Calculations
							.getSalaryInAMonthByEmployeeId(e.getId(),
									month.getDate())));
					textSalary.setForeground(new Color(90, 212, 39));
					textProblems.setText("");
				} catch (SalaryException e) {
					textSalary.setForeground(new Color(255, 58, 45));
					textSalary.setText(String.valueOf(e.getSalary()));
					textProblems.setText(e.getMessage());
				}
			}
		});
		btnSalary.setEnabled(false);
		btnSalary
				.setToolTipText("For the selected month above (eg May 1 to 31)");
		btnSalary.setForeground(new Color(255, 91, 55));

		textSalary = new JTextField();
		textSalary.setFont(new Font("SansSerif", Font.BOLD, 12));
		textSalary.setBounds(154, 296, 86, 20);
		textSalary.setForeground(new Color(90, 212, 39));
		textSalary.setEditable(false);
		textSalary.setColumns(10);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.getContentPane().add(comboBox);
		mainFrame.getContentPane().add(btnYearWorkSessions);
		mainFrame.getContentPane().add(lblThisYear);
		mainFrame.getContentPane().add(btnMonthWorkSessions);
		mainFrame.getContentPane().add(lblMonth);
		mainFrame.getContentPane().add(comboBoxMonth);
		mainFrame.getContentPane().add(btnSalary);
		mainFrame.getContentPane().add(textSalary);
		mainFrame.getContentPane().add(lblWorkedDaysThis);
		mainFrame.getContentPane().add(textYearWorkedDays);
		mainFrame.getContentPane().add(lblWorkedHours);
		mainFrame.getContentPane().add(textMonthWorkedHours);
		mainFrame.getContentPane().add(labelWeekWorkHours);
		mainFrame.getContentPane().add(textWorkedHoursWeek);
		mainFrame.getContentPane().add(label_2);
		mainFrame.getContentPane().add(textSickDaysWeek);
		mainFrame.getContentPane().add(lblSickDaysThis);
		mainFrame.getContentPane().add(textYearSickDays);
		mainFrame.getContentPane().add(label);
		mainFrame.getContentPane().add(textMonthSickDays);
		mainFrame.getContentPane().add(lblDayOffs);
		mainFrame.getContentPane().add(textYearDayOffs);
		mainFrame.getContentPane().add(label_3);
		mainFrame.getContentPane().add(label_1);
		mainFrame.getContentPane().add(textMonthDayOffs);
		mainFrame.getContentPane().add(textDaysOffsWeek);
		mainFrame.getContentPane().add(btnWeekWorkSessions);
		mainFrame.getContentPane().add(lblWeek);
		mainFrame.getContentPane().add(comboBoxWeek);
		
		textProblems = new JTextArea();
		textProblems.setEditable(false);
		textProblems.setBackground(new Color(215, 215, 215));
		textProblems.setForeground(new Color(255, 58, 45));
		textProblems.setBounds(284, 262, 253, 98);
		mainFrame.getContentPane().add(textProblems);
	}
}
