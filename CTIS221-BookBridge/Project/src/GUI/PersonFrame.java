package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Classes_Abstract.Person;
import SystemClassAndMain.LibrarySys;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PersonFrame extends JFrame {

	private JPanel contentPane;
	private JTextField nameSurnameF;
	private JTextField idF;
	private JTextField gradYearF;
	private JTextField departmentF;
	private JTextField idSearchF;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public PersonFrame(MainFrame mf) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 603);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "Student", "Academician", "Guest" }));
		comboBox.setBounds(222, 10, 221, 21);
		contentPane.add(comboBox);
		comboBox.setSelectedIndex(0);

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label4 = new JLabel("Graduate Year : ");
		label4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label4.setBounds(43, 105, 104, 13);
		contentPane.add(label4);

		JLabel label5 = new JLabel("Department : ");
		label5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label5.setBounds(43, 134, 104, 13);
		contentPane.add(label5);

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox.getSelectedItem().equals("Student")) {
					label4.setText("Graduate Year : ");
					label5.setText("Department : ");
					label4.setVisible(true);
					gradYearF.setVisible(true);
				} else if (comboBox.getSelectedItem().equals("Academician")) {
					label4.setVisible(false);
					gradYearF.setVisible(false);
				} else {
					label4.setVisible(true);
					gradYearF.setVisible(true);
					label4.setText("Entry Date : ");
					label5.setText("Exit Date : ");
				}
			}
		});

		JLabel lblNewLabel = new JLabel("Person Type :");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(43, 13, 82, 13);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Name Surname :");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(43, 44, 104, 13);
		contentPane.add(lblNewLabel_1);

		nameSurnameF = new JTextField();
		nameSurnameF.setBounds(222, 41, 221, 21);
		contentPane.add(nameSurnameF);
		nameSurnameF.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Person ID : ");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(43, 75, 104, 13);
		contentPane.add(lblNewLabel_2);

		idF = new JTextField();
		idF.setBounds(222, 72, 221, 21);
		contentPane.add(idF);
		idF.setColumns(10);

		gradYearF = new JTextField();
		gradYearF.setBounds(222, 103, 221, 19);
		contentPane.add(gradYearF);
		gradYearF.setColumns(10);

		departmentF = new JTextField();
		departmentF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					String nameSurname = nameSurnameF.getText();
					String warningMsg = "Please fill the necessary fields!";

					if (idF.getText().isEmpty() || nameSurnameF.getText().isEmpty()) {
						textArea.setText(warningMsg);
					} else {
						int id = Integer.parseInt(idF.getText());
						if (comboBox.getSelectedItem().equals("Student")) {
							int gradYear = Integer.parseInt(gradYearF.getText());
							String department = departmentF.getText();
							if (gradYearF.getText().isEmpty() || departmentF.getText().isEmpty()) {
								textArea.setText(warningMsg);
							} else {
								if (LibrarySys.addPerson("Student", nameSurname, id, 0, gradYear, department, "", ""))
									textArea.setText("Student was successfully added!");
								else
									textArea.setText("You cannot add a person with the same id!");
							}
						} else if (comboBox.getSelectedItem().equals("Academician")) {
							String department = departmentF.getText();
							if (departmentF.getText().isEmpty()) {
								textArea.setText(warningMsg);
							} else {
								if (LibrarySys.addPerson("Academician", nameSurname, id, 0, 0, department, "", ""))
									textArea.setText("Academician was successfully added!");
								else
									textArea.setText("You cannot add a person with the same id!");
							}
						} else if (comboBox.getSelectedItem().equals("Guest")) {
							String entryDate = gradYearF.getText();
							String exitDate = departmentF.getText();
							if (entryDate.isEmpty() || exitDate.isEmpty()) {
								textArea.setText(warningMsg);
							} else {
								if (LibrarySys.addPerson("Guest", nameSurname, id, 0, 0, "", entryDate, exitDate))
									textArea.setText("Guest was successfully added!");
								else
									textArea.setText("You cannot add a person with the same id!");
							}
						}
						LibrarySys.readPersonsFromFile();
					}
				}
			}
		});
		departmentF.setBounds(222, 132, 221, 19);
		contentPane.add(departmentF);
		departmentF.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(43, 220, 400, 251);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		JButton btnDispByName = new JButton("Display By Name");
		btnDispByName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(LibrarySys.displayAllPersonsByName());
			}
		});
		btnDispByName.setBounds(39, 173, 131, 21);
		contentPane.add(btnDispByName);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nameSurname = nameSurnameF.getText();
				String warningMsg = "Please fill the necessary fields!";

				if (idF.getText().isEmpty() || nameSurnameF.getText().isEmpty()) {
					textArea.setText(warningMsg);
				} else {
					int id = Integer.parseInt(idF.getText());
					if (comboBox.getSelectedItem().equals("Student")) {
						int gradYear = Integer.parseInt(gradYearF.getText());
						String department = departmentF.getText();
						if (gradYearF.getText().isEmpty() || departmentF.getText().isEmpty()) {
							textArea.setText(warningMsg);
						} else {
							if (LibrarySys.addPerson("Student", nameSurname, id, 0, gradYear, department, "", ""))
								textArea.setText("Student was successfully added!");
							else
								textArea.setText("You cannot add a person with the same id!");
						}
					} else if (comboBox.getSelectedItem().equals("Academician")) {
						String department = departmentF.getText();
						if (departmentF.getText().isEmpty()) {
							textArea.setText(warningMsg);
						} else {
							if (LibrarySys.addPerson("Academician", nameSurname, id, 0, 0, department, "", ""))
								textArea.setText("Academician was successfully added!");
							else
								textArea.setText("You cannot add a person with the same id!");
						}
					} else if (comboBox.getSelectedItem().equals("Guest")) {
						String entryDate = gradYearF.getText();
						String exitDate = departmentF.getText();
						if (entryDate.isEmpty() || exitDate.isEmpty()) {
							textArea.setText(warningMsg);
						} else {
							if (LibrarySys.addPerson("Guest", nameSurname, id, 0, 0, "", entryDate, exitDate))
								textArea.setText("Guest was successfully added!");
							else
								textArea.setText("You cannot add a person with the same id!");
						}
					}
					LibrarySys.readPersonsFromFile();
				}

			}
		});
		btnAdd.setBounds(200, 173, 85, 21);
		contentPane.add(btnAdd);

		JButton btnDispByID = new JButton("Display By ID");
		btnDispByID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(LibrarySys.displayAllPersons());
			}
		});
		btnDispByID.setBounds(325, 173, 117, 21);
		contentPane.add(btnDispByID);

		JLabel lblNewLabel_3 = new JLabel("Enter ID : ");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(43, 502, 74, 13);
		contentPane.add(lblNewLabel_3);

		idSearchF = new JTextField();
		idSearchF.setBounds(127, 500, 104, 19);
		contentPane.add(idSearchF);
		idSearchF.setColumns(10);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int id;
				if (idSearchF.getText().isEmpty()) {
					textArea.setText("Please enter the ID to search!");
				} else {
					id = Integer.parseInt(idSearchF.getText());
					Person p = LibrarySys.searchPerson(id);
					if (p == null) {
						textArea.setText("Person with the given ID not found!");
					} else {
						textArea.setText(p.toString());
					}
				}
			}
		});
		btnSearch.setBounds(262, 499, 85, 21);
		contentPane.add(btnSearch);

		JButton btnNewButton_4 = new JButton("Clear");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}
		});
		btnNewButton_4.setBounds(357, 499, 85, 21);
		contentPane.add(btnNewButton_4);

		JButton btnNewButton = new JButton("Back");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				mf.setVisible(true);
			}
		});
		btnNewButton.setBounds(26, 526, 89, 23);
		contentPane.add(btnNewButton);
	}
}
