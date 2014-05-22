package swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;

import core.DataLoader;
import dao.PersistentLayerException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;

public class Import extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPanel buttonPane;
	private JTextField textPath;
	
	private JFileChooser fileChooser;
	private int returnVal = JFileChooser.CANCEL_OPTION;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Import dialog = new Import();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Import() {
		setBounds(100, 100, 450, 300);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton okButton = new JButton("Import");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							if (returnVal != JFileChooser.APPROVE_OPTION) {
								return;
							}
							int answer = JOptionPane.showConfirmDialog(Import.this, "Are you sure you want to import employees? "
									+ "Current database will be lost and replaced by the employees in the XML file");
							if (answer == JOptionPane.CANCEL_OPTION || answer == JOptionPane.NO_OPTION || answer == JOptionPane.CLOSED_OPTION) {
								return;
							}
							DataLoader.importEmployees(fileChooser.getSelectedFile().getAbsolutePath());
							JOptionPane.showMessageDialog(Import.this, "Import was successful");
							Import.this.dispose();
						} catch(PersistentLayerException exc) {
							JOptionPane.showMessageDialog(Import.this,
									exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
						Import.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, 434, GroupLayout.PREFERRED_SIZE)
				.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, 434, GroupLayout.PREFERRED_SIZE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE)
					.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		
		fileChooser = new JFileChooser();
		JButton btnChooseFile = new JButton("Choose file");
		btnChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				returnVal = fileChooser.showOpenDialog(Import.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					textPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		textPath = new JTextField();
		textPath.setEditable(false);
		textPath.setColumns(10);
		JLabel lblCurrentDatabaseWill = new JLabel("Current DATABASE will be LOST");
		lblCurrentDatabaseWill.setForeground(Color.RED);
		lblCurrentDatabaseWill.setBackground(Color.RED);
		lblCurrentDatabaseWill.setFont(new Font("Tahoma", Font.PLAIN, 25));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(textPath, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(161)
							.addComponent(btnChooseFile))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(34)
							.addComponent(lblCurrentDatabaseWill)))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(40)
					.addComponent(btnChooseFile)
					.addGap(60)
					.addComponent(textPath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblCurrentDatabaseWill)
					.addContainerGap(26, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		getContentPane().setLayout(groupLayout);
	}
}
