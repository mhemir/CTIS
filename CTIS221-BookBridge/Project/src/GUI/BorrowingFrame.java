package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import SystemClassAndMain.LibrarySys;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class BorrowingFrame extends JFrame {

	private JPanel contentPane;
	private JTextField idF;
	private JTextField materialF;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JLabel msgLabel;
	private JButton btnNewButton_2;
	/**
	 * Launch the application.
	 */


	/**
	 * Create the frame.
	 */
	public BorrowingFrame(MainFrame mf) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 560, 489);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Enter Person ID : ");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(30, 34, 120, 13);
		contentPane.add(lblNewLabel);
		
		idF = new JTextField();
		idF.setBounds(187, 32, 174, 19);
		contentPane.add(idF);
		idF.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Enter Material ID : ");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(30, 69, 140, 13);
		contentPane.add(lblNewLabel_1);
		
		materialF = new JTextField();
		materialF.setBounds(187, 67, 174, 19);
		contentPane.add(materialF);
		materialF.setColumns(10);
		
		JButton btnNewButton = new JButton("Display Available Materials");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(LibrarySys.displayAvailableMaterials());
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnNewButton.setBounds(30, 114, 181, 21);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Borrow");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(idF.getText().isEmpty() || materialF.getText().isEmpty()) {
					msgLabel.setText("Please fill the fields!");
				}
				else {
					int personid = Integer.parseInt(idF.getText());
					int materialid = Integer.parseInt(materialF.getText());
					
					msgLabel.setText(LibrarySys.borrowMaterial(personid, materialid));
				}
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnNewButton_1.setBounds(241, 114, 120, 21);
		contentPane.add(btnNewButton_1);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 157, 330, 223);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		msgLabel = new JLabel("");
		msgLabel.setBounds(40, 391, 472, 14);
		contentPane.add(msgLabel);
		
		btnNewButton_2 = new JButton("Back");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				mf.setVisible(true);
			}
		});
		btnNewButton_2.setBounds(30, 416, 89, 23);
		contentPane.add(btnNewButton_2);
	}
}
