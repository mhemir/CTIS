package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import SystemClassAndMain.LibrarySys;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField idF;
	private JTextField titleF;
	private JLabel msgField;
	private JTextArea textArea;

	MaterialFrame material = new MaterialFrame(this);
	/**
	 * Launch the application.
	 */

	PublisherFrame pf;
	/**
	 * Create the frame.
	 */
	PersonFrame personf = new PersonFrame(this);
	BorrowingFrame bf = new BorrowingFrame(this);
	ReturnFrame rf = new ReturnFrame();

	public MainFrame() {
		setTitle("Library Management System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 553, 357);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel idLbl = new JLabel("ID:");
		idLbl.setBounds(10, 11, 46, 14);
		contentPane.add(idLbl);

		JLabel titleLbl = new JLabel("Title:");
		titleLbl.setBounds(10, 39, 46, 14);
		contentPane.add(titleLbl);

		idF = new JTextField();
		idF.setBounds(41, 8, 86, 20);
		contentPane.add(idF);
		idF.setColumns(10);

		titleF = new JTextField();
		titleF.setBounds(41, 36, 86, 20);
		contentPane.add(titleF);
		titleF.setColumns(10);

		msgField = new JLabel("");
		msgField.setBounds(381, 24, 156, 20);
		contentPane.add(msgField);

		JButton btnNewButton = new JButton("Search Material By ID");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (idF.getText().isEmpty()) {
					msgField.setText("Please fill the id field!");
				} else {
					int id = Integer.parseInt(idF.getText());
					if (LibrarySys.searchMaterialByID(id) == null) {
						msgField.setText("Item not found!");
						textArea.setText("");
					} else {
						textArea.setText(LibrarySys.searchMaterialByID(id).toString());
						msgField.setText("");
					}
				}
			}
		});
		btnNewButton.setBounds(10, 69, 166, 23);
		contentPane.add(btnNewButton);

		JButton btnSearchTitle = new JButton("Search Material By Title");
		btnSearchTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (titleF.getText().isEmpty()) {
					msgField.setText("Please fill the title field!");
				} else {
					if (LibrarySys.searchMaterialByTitle(titleF.getText()) == null) {
						msgField.setText("Item not found!");
						textArea.setText("");
					} else {
						textArea.setText(LibrarySys.searchMaterialByTitle(titleF.getText()).toString());
						msgField.setText("");
					}
				}
			}
		});
		btnSearchTitle.setBounds(186, 69, 176, 23);
		contentPane.add(btnSearchTitle);

		JButton displayBtn = new JButton("Display All Materials");
		displayBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String output;
				output = LibrarySys.displayAllMaterials();
				textArea.setText(output);
			}
		});
		displayBtn.setBounds(372, 67, 156, 23);
		contentPane.add(displayBtn);

		JButton btnAddMaterial = new JButton("Add Material");
		btnAddMaterial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				material.setVisible(true);
				dispose();
			}
		});
		btnAddMaterial.setBounds(10, 103, 166, 23);
		contentPane.add(btnAddMaterial);

		JButton btnAddPublisher = new JButton("Add Publisher");
		btnAddPublisher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pf = new PublisherFrame();
				pf.setVisible(true);
			}
		});
		btnAddPublisher.setBounds(186, 103, 176, 23);
		contentPane.add(btnAddPublisher);

		JButton btnAddPerson = new JButton("Add Person");
		btnAddPerson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				personf.setVisible(true);

			}
		});
		btnAddPerson.setBounds(372, 101, 156, 23);
		contentPane.add(btnAddPerson);

		JButton btnNewButton_6 = new JButton("Close");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_6.setBounds(210, 292, 89, 23);
		contentPane.add(btnNewButton_6);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 137, 518, 144);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		JButton btnNewButton_1 = new JButton("BORROW");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bf.setVisible(true);
				dispose();
			
			}
		
		
		});
		btnNewButton_1.setBounds(137, 11, 119, 43);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("RETURN");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rf.setVisible(true);
			}
		});
		btnNewButton_2.setBounds(266, 11, 108, 42);
		contentPane.add(btnNewButton_2);

	}
}
