package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Classes_Abstract.Material;
import Classes_HasA.Borrowing;
import SystemClassAndMain.LibrarySys;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ReturnFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField borrowingIDF;
	private JButton btnNewButton;
	private JTextArea textArea;
	private JButton btnNewButton_1;
	private JScrollPane scrollPane;
	private JButton btnNewButton_2;

	

	/**
	 * Create the frame.
	 */
	public ReturnFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 325);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Borrowing ID:");
		lblNewLabel.setBounds(23, 23, 80, 14);
		contentPane.add(lblNewLabel);
		
		borrowingIDF = new JTextField();
		borrowingIDF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(borrowingIDF.getText().isEmpty()) {
						textArea.setText("Please fill the necessary field");
					}
					else {
						int id = Integer.parseInt(borrowingIDF.getText());
						Borrowing br = LibrarySys.searchBorrowingByID(id);
						if(br == null) {
							textArea.setText("Borrowing Not Found");
						}
						else {
							LibrarySys.getBorrowings().remove(id, br);
							System.out.println();
							textArea.setText("You have returned your material sucessfully!");
						}
					}
				}
			}
		});
		borrowingIDF.setBounds(103, 20, 86, 20);
		contentPane.add(borrowingIDF);
		borrowingIDF.setColumns(10);
		
		btnNewButton = new JButton("Return");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(borrowingIDF.getText().isEmpty()) {
					textArea.setText("Please fill the necessary field");
				}
				else {
					int id = Integer.parseInt(borrowingIDF.getText());
					Borrowing br = LibrarySys.searchBorrowingByID(id);
					if(br == null) {
						textArea.setText("Borrowing Not Found");
					}
					else {
						LibrarySys.getBorrowings().remove(id, br);
						Material returnedMaterial = LibrarySys.searchMaterialByTitle(br.getTitle());
						returnedMaterial.increaseStockCount();
						LibrarySys.searchPerson(br.getPersonid()).getBorrowedItems().remove(returnedMaterial);     
						textArea.setText("You have returned your material sucessfully!");
					}
				}
			}
		});
		btnNewButton.setBounds(23, 98, 89, 23);
		contentPane.add(btnNewButton);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(23, 132, 401, 118);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		btnNewButton_1 = new JButton("Show All Borrowings");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(LibrarySys.showAllBorrowings());
			}
		});
		btnNewButton_1.setBounds(240, 98, 184, 23);
		contentPane.add(btnNewButton_1);
		
		btnNewButton_2 = new JButton("Close");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_2.setBounds(166, 263, 89, 23);
		contentPane.add(btnNewButton_2);
	}

}
