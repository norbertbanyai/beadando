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

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import core.Calculations;

import java.awt.Color;
import java.sql.Date;

import javax.swing.SwingConstants;

public class MainWindow {

	private JFrame mainFrame;
	private JScrollPane scrollPane;
	
	Employee e;
	MonthModelClass month;
	private JTextField textYearWorkedDays;
	private JTextField textYearSickDays;
	private JTextField textYearDayOffs;
	private JTextField textMonthWorkedHours;
	private JTextField textMonthSickDays;
	private JTextField textMonthDayOffs;
	
	
	public void updateYearLabels() {
		if (e == null) {
			return;
		}
		textYearWorkedDays.setText(String.valueOf(Calculations.getAllWorkedDayCurrentYearByEmployeeId(e.getId())));
		textYearSickDays.setText(String.valueOf(Calculations.getSickDaysInCurrentYearByEmployeeId(e.getId())));
		textYearDayOffs.setText(String.valueOf(Calculations.getDayOffsInCurrentYearByEmployeeId(e.getId())));
		updateMonthLabels();
		
	}
	public void updateMonthLabels() {
		if (e == null || month == null) {
			return;
		}
		Date date = month.getDate();
		textMonthWorkedHours.setText(String.valueOf(Calculations.getWorkedHoursInAMonthByEmployeeId(e.getId(), date)));
		textMonthSickDays.setText(String.valueOf(Calculations.getSickDaysInAMonthByEmployeeId(e.getId(), date)));
		textMonthDayOffs.setText(String.valueOf(Calculations.getDayOffsInAMonthByEmployeeId(e.getId(), date)));
		
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
		mainFrame.setTitle("WorkHandler");
		mainFrame.setBounds(500, 200, 450, 300);
		mainFrame.setSize(530, 400);
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
				new WorksessionsDialog().setVisible(true);
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
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		comboBox.setModel(new EmployeeComboBoxModel(MainWindow.this, edaos.findAllEmployees()));
		
		JLabel lblWorkedDaysThis = new JLabel("Worked days");
		
		textYearWorkedDays = new JTextField();
		textYearWorkedDays.setForeground(new Color(0, 122, 255));
		textYearWorkedDays.setEditable(false);
		textYearWorkedDays.setColumns(10);
		
		JLabel lblSickDaysThis = new JLabel("Sick days");
		
		textYearSickDays = new JTextField();
		textYearSickDays.setForeground(new Color(0, 122, 255));
		textYearSickDays.setEditable(false);
		textYearSickDays.setColumns(10);
		
		textYearDayOffs = new JTextField();
		textYearDayOffs.setForeground(new Color(0, 122, 255));
		textYearDayOffs.setEditable(false);
		textYearDayOffs.setColumns(10);
		
		JLabel lblDayOffs = new JLabel("Day offs");
		
		JLabel lblThisYear = new JLabel("This Year");
		lblThisYear.setHorizontalAlignment(SwingConstants.CENTER);
		lblThisYear.setForeground(new Color(0, 122, 255));
		lblThisYear.setFont(new Font("SansSerif", Font.PLAIN, 17));
		
		JLabel lblMonth = new JLabel("Month");
		lblMonth.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonth.setVerticalAlignment(SwingConstants.TOP);
		lblMonth.setForeground(new Color(0, 122, 255));
		lblMonth.setFont(new Font("SansSerif", Font.PLAIN, 17));
		
		JComboBox comboBoxMonth = new JComboBox();
		comboBoxMonth.setModel(new MonthComboBoxModel(MainWindow.this));
		
		JLabel lblWorkedHours = new JLabel("Worked hours");
		
		textMonthWorkedHours = new JTextField();
		textMonthWorkedHours.setForeground(new Color(0, 122, 255));
		textMonthWorkedHours.setEditable(false);
		textMonthWorkedHours.setColumns(10);
		
		JLabel label = new JLabel("Sick days");
		
		textMonthSickDays = new JTextField();
		textMonthSickDays.setForeground(new Color(0, 122, 255));
		textMonthSickDays.setEditable(false);
		textMonthSickDays.setColumns(10);
		
		JLabel label_1 = new JLabel("Day offs");
		
		textMonthDayOffs = new JTextField();
		textMonthDayOffs.setForeground(new Color(0, 122, 255));
		textMonthDayOffs.setEditable(false);
		textMonthDayOffs.setColumns(10);
		
		GroupLayout groupLayout = new GroupLayout(mainFrame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(48)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 405, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(212)
							.addComponent(lblThisYear))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(210)
							.addComponent(lblMonth, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(comboBoxMonth, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblWorkedDaysThis)
									.addGap(18)
									.addComponent(textYearWorkedDays, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblWorkedHours)
									.addGap(18)
									.addComponent(textMonthWorkedHours, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)))
							.addGap(50)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblSickDaysThis)
									.addGap(18)
									.addComponent(textYearSickDays, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(label, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(textMonthSickDays, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)))
							.addGap(30)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblDayOffs)
									.addGap(18)
									.addComponent(textYearDayOffs, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(textMonthDayOffs, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)))))
					.addGap(34))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblThisYear)
					.addGap(19)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWorkedDaysThis)
						.addComponent(textYearWorkedDays, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSickDaysThis)
						.addComponent(textYearSickDays, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDayOffs)
						.addComponent(textYearDayOffs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMonth, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxMonth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWorkedHours)
						.addComponent(textMonthWorkedHours, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label)
						.addComponent(label_1)
						.addComponent(textMonthDayOffs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textMonthSickDays, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(168, Short.MAX_VALUE))
		);
		mainFrame.getContentPane().setLayout(groupLayout);
	}
}
