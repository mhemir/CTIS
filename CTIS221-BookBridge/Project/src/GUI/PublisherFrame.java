package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Classes_HasA.Publisher;
import SystemClassAndMain.LibrarySys;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PublisherFrame extends JFrame {

	private JPanel contentPane;
	private JTextField idF;
	private JTextField nameF;
	private JTextField editorF;
	private JTextField locationF;
	private JTextField searchF;

	/**
	 * Launch the application.
	 */


	/**
	 * Create the frame.
	 */
	public PublisherFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 471, 537);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		idF = new JTextField();
		idF.setBounds(153, 19, 200, 19);
		contentPane.add(idF);
		idF.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(28, 194, 399, 225);
		contentPane.add(scrollPane);

		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		JLabel lblNewLabel = new JLabel("Publisher ID :");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(28, 21, 96, 13);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Publisher Name :");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(28, 56, 96, 13);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Editor :");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(28, 90, 45, 13);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Location :");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(28, 125, 84, 13);
		contentPane.add(lblNewLabel_3);
		
		nameF = new JTextField();
		nameF.setBounds(153, 54, 200, 19);
		contentPane.add(nameF);
		nameF.setColumns(10);
		
		editorF = new JTextField();
		editorF.setBounds(153, 88, 200, 19);
		contentPane.add(editorF);
		editorF.setColumns(10);
		
		locationF = new JTextField();
		locationF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					boolean status = idF.getText().isEmpty() || nameF.getText().isEmpty() || editorF.getText().isEmpty() || locationF.getText().isEmpty();
					if(status) {
						textArea.setText("Please fill the necessary fields!");
					}
					else {
						int id=Integer.parseInt(idF.getText());
						String pname = nameF.getText();
						String editor = editorF.getText();
						String location = locationF.getText();
						if(!LibrarySys.addPublisher(id, pname, editor, location)) {
							textArea.setText("The publisher with the given ID already exists!");
						}
						else {
						textArea.setText("Publisher successfully added into the system.");
						LibrarySys.readPublishersFromFile();
						}
					}
				}
			}
		});
		locationF.setBounds(153, 123, 200, 19);
		contentPane.add(locationF);
		locationF.setColumns(10);
		
		
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean status = idF.getText().isEmpty() || nameF.getText().isEmpty() || editorF.getText().isEmpty() || locationF.getText().isEmpty();
				if(status) {
					textArea.setText("Please fill the necessary fields!");
				}
				else {
					int id=Integer.parseInt(idF.getText());
					String pname = nameF.getText();
					String editor = editorF.getText();
					String location = locationF.getText();
					if(!LibrarySys.addPublisher(id, pname, editor, location)) {
						textArea.setText("The publisher with the given ID already exists!");
					}
					else {
					textArea.setText("Publisher successfully added into the system.");
					LibrarySys.readPublishersFromFile();
					}
				}
			
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnNewButton.setBounds(28, 163, 103, 21);
		contentPane.add(btnNewButton);
		
		JButton btnDisplayID = new JButton("Display By ID");
		btnDisplayID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(LibrarySys.displayAllPublishers());
			}
		});
		btnDisplayID.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnDisplayID.setBounds(153, 163, 122, 21);
		contentPane.add(btnDisplayID);
		
		JButton btnNewButton_2 = new JButton("Display By Name");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(LibrarySys.displayAllPublishersByName());
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnNewButton_2.setBounds(296, 163, 131, 21);
		contentPane.add(btnNewButton_2);
		
		searchF = new JTextField();
		searchF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(searchF.getText().isEmpty()) {
						textArea.setText("Please fill the field!");
					}
					else {
						Publisher temp = LibrarySys.searchPublisherById(Integer.parseInt(searchF.getText()));
						if(temp == null) {
							textArea.setText("Publisher with the given id not found!");
						}
						else {
							textArea.setText(temp.toString());
						}
					}
				}
			}
		});
		searchF.setBounds(103, 431, 96, 19);
		contentPane.add(searchF);
		searchF.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("Enter ID : ");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel_6.setBounds(28, 434, 73, 13);
		contentPane.add(lblNewLabel_6);
		
		JButton btnNewButton_3 = new JButton("Search");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(searchF.getText().isEmpty()) {
					textArea.setText("Please fill the field!");
				}
				else {
					Publisher temp = LibrarySys.searchPublisherById(Integer.parseInt(searchF.getText()));
					if(temp == null) {
						textArea.setText("Publisher with the given id not found!");
					}
					else {
						textArea.setText(temp.toString());
					}
				}
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnNewButton_3.setBounds(229, 430, 85, 21);
		contentPane.add(btnNewButton_3);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}
		});
		btnClear.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnClear.setBounds(342, 430, 85, 21);
		contentPane.add(btnClear);
		
		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setBounds(171, 464, 89, 23);
		contentPane.add(btnNewButton_1);
	}
}
