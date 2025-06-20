package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Classes_Abstract.Material;
import Classes_IsA_Material.Book;
import SystemClassAndMain.LibrarySys;

public class MaterialFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField titleF;
	private JTextField authorF;
	private JTextField pageF;
	private JTextField priceF;
	private JTextField stockCountF;
	private JTextField issueF;
	private JTextField bookTypeF;
	private JTextField categoryF;
	private JComboBox comboBox;
	private String title;
	private String author;
	private int page;
	private int price;
	private int stockCount;
	private JLabel warningText;
	private String bookType;
	private String category;
	private int issue;

	public JComboBox getComboBox() {
		return comboBox;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public int getPrice() {
		return price;
	}

	public int getStockCount() {
		return stockCount;
	}

	public int getPage() {
		return page;
	}

	public String getBookType() {
		return bookType;
	}

	public String getCategory() {
		return category;
	}

	public int getIssue() {
		return issue;
	}

	/**
	 * Launch the application.
	 */

	public JTextField getTitleF() {
		return titleF;
	}

	public JTextField getAuthorF() {
		return authorF;
	}

	public JTextField getPageF() {
		return pageF;
	}

	public JTextField getPriceF() {
		return priceF;
	}

	public JTextField getStockCountF() {
		return stockCountF;
	}

	public JTextField getIssueF() {
		return issueF;
	}

	public JTextField getBookTypeF() {
		return bookTypeF;
	}

	public JTextField getCategoryF() {
		return categoryF;
	}

	/**
	 * Create the frame.
	 */
	MaterialToPublisher mp = new MaterialToPublisher(this);

	public MaterialFrame(MainFrame m) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 427);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Material Type:");
		lblNewLabel.setBounds(20, 20, 89, 14);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Title:");
		lblNewLabel_1.setBounds(20, 49, 46, 14);
		contentPane.add(lblNewLabel_1);

		titleF = new JTextField();
		titleF.setColumns(10);
		titleF.setBounds(140, 46, 134, 20);
		contentPane.add(titleF);

		comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox.getSelectedItem().equals("Book")) {
					bookTypeF.setEditable(true);
					categoryF.setEditable(false);
					issueF.setEditable(false);
					categoryF.setText("");
					issueF.setText("");
				} else if (comboBox.getSelectedItem().equals("Magazine")) {
					bookTypeF.setEditable(false);
					categoryF.setEditable(true);
					issueF.setEditable(true);
					bookTypeF.setText("");
				}
			}
		});

		comboBox.setBounds(140, 16, 134, 22);
		contentPane.add(comboBox);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Book", "Magazine" }));

		warningText = new JLabel("");
		warningText.setBounds(33, 363, 224, 14);
		contentPane.add(warningText);

		JLabel lblNewLabel_2 = new JLabel("Author:");
		lblNewLabel_2.setBounds(20, 77, 134, 20);
		contentPane.add(lblNewLabel_2);

		authorF = new JTextField();
		authorF.setBounds(140, 77, 134, 20);
		contentPane.add(authorF);
		authorF.setColumns(10);

		JButton btnNewButton = new JButton("Back");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setBounds(10, 316, 89, 23);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Select Publisher");
		btnNewButton_1.addActionListener(new ActionListener() {
			String item;

			public void actionPerformed(ActionEvent e) {
				boolean status = titleF.getText().isEmpty() || authorF.getText().isEmpty() || pageF.getText().isEmpty()
						|| priceF.getText().isEmpty();
				String warnmsg = "Please fill all the necessary fields!";
				if (status) {
					warningText.setText(warnmsg);
				} else {
					title = titleF.getText();
					author = authorF.getText();
					page = Integer.parseInt(pageF.getText());
					price = Integer.parseInt(priceF.getText());
					stockCount = Integer.parseInt(stockCountF.getText());
					item = comboBox.getSelectedItem().toString();
					int newID = LibrarySys.generateIDforMaterial();
					if (item.equals("Book")) {
						if (bookTypeF.getText().isEmpty()) {
							warningText.setText(warnmsg);
						} else {
							bookType = bookTypeF.getText();
							warningText.setText("");
							mp.setVisible(true);
							setVisible(false);
							dispose();

						}
					} else if (item.equals("Magazine")) {

						if (categoryF.getText().isEmpty() || issueF.getText().isEmpty()) {
							warningText.setText(warnmsg);
						} else {
							category = categoryF.getText();
							issue = Integer.parseInt(issueF.getText());
							warningText.setText("");
							mp.setVisible(true);
							dispose();
						}
					}

				}

			}

		});
		btnNewButton_1.setBounds(140, 316, 134, 23);
		contentPane.add(btnNewButton_1);

		JLabel lblNewLabel_4 = new JLabel("Page: ");
		lblNewLabel_4.setBounds(20, 112, 46, 20);
		contentPane.add(lblNewLabel_4);

		pageF = new JTextField();
		pageF.setBounds(140, 112, 134, 20);
		contentPane.add(pageF);
		pageF.setColumns(10);

		JLabel lblNewLabel_5 = new JLabel("Price:");
		lblNewLabel_5.setBounds(20, 149, 46, 14);
		contentPane.add(lblNewLabel_5);

		JLabel lblNewLabel_3 = new JLabel("Stock Count:");
		lblNewLabel_3.setBounds(20, 187, 89, 14);
		contentPane.add(lblNewLabel_3);

		priceF = new JTextField();
		priceF.setColumns(10);
		priceF.setBounds(140, 146, 134, 20);
		contentPane.add(priceF);

		stockCountF = new JTextField();
		stockCountF.setColumns(10);
		stockCountF.setBounds(140, 184, 134, 20);
		contentPane.add(stockCountF);

		JLabel lblNewLabel_6 = new JLabel("Book Type:");
		lblNewLabel_6.setBounds(20, 233, 79, 14);
		contentPane.add(lblNewLabel_6);

		JLabel lblNewLabel_7 = new JLabel("Category:");
		lblNewLabel_7.setBounds(20, 256, 66, 14);
		contentPane.add(lblNewLabel_7);

		JLabel lblNewLabel_8 = new JLabel("Issue:");
		lblNewLabel_8.setBounds(20, 281, 46, 14);
		contentPane.add(lblNewLabel_8);

		issueF = new JTextField();
		issueF.setColumns(10);
		issueF.setBounds(140, 278, 134, 20);
		contentPane.add(issueF);

		bookTypeF = new JTextField();
		bookTypeF.setColumns(10);
		bookTypeF.setBounds(140, 230, 134, 20);
		contentPane.add(bookTypeF);

		categoryF = new JTextField();
		categoryF.setColumns(10);
		categoryF.setBounds(140, 253, 134, 20);
		contentPane.add(categoryF);
		categoryF.setEditable(false); // Default
		issueF.setEditable(false); // Default

	}

	public JLabel getWarningText() {
		return warningText;
	}
}
