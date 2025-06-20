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

import Classes_Abstract.Material;
import Classes_HasA.Publisher;
import Classes_IsA_Material.Book;
import Classes_IsA_Material.Magazine;
import SystemClassAndMain.LibrarySys;
import java.awt.ScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MaterialToPublisher extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField chooseF;
	private JTextField searchF;
	private JLabel msgLabel;

	/**
	 * Launch the application.
	 */
	Publisher pb;

	public Publisher getPb() {
		return pb;
	}

	/**
	 * Create the frame.
	 */
	public MaterialToPublisher(MaterialFrame m) {
		setTitle("Add Material To Publisher");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 413, 407);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 140, 377, 181);
		contentPane.add(scrollPane);

		JTextArea publisherInfoF = new JTextArea();
		publisherInfoF.setEditable(false);
		scrollPane.setViewportView(publisherInfoF);
		publisherInfoF.setLineWrap(true);

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Please Choose Publisher");
		lblNewLabel.setBounds(133, 11, 158, 14);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Existing Publisher List:");
		lblNewLabel_1.setBounds(10, 108, 142, 21);
		contentPane.add(lblNewLabel_1);

		JButton displayBtn = new JButton("Display All");
		displayBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				publisherInfoF.setText(LibrarySys.displayAllPublishers());
				msgLabel.setText("");
			
			}
		});
		displayBtn.setBounds(10, 52, 111, 23);
		contentPane.add(displayBtn);

		msgLabel = new JLabel("");
		msgLabel.setBounds(138, 332, 249, 23);
		contentPane.add(msgLabel);

		JButton chooseByIdBtn = new JButton("Choose By ID");
		chooseByIdBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chooseF.getText().isEmpty()) {
					msgLabel.setText("Please Choose a publisher");
				} else {
					int pubID = Integer.parseInt(chooseF.getText());
					pb = LibrarySys.searchPublisherById(pubID);
					if (pb == null) {
						msgLabel.setText("Please enter the existing publisher ID");
					} else {
						String title = m.getTitle();
						String author = m.getAuthor();
						int page = m.getPage();
						int price = m.getPrice();
						int stockCount = m.getStockCount();
						String bookType = m.getBookType();
						String category = m.getCategory();
						int issue = m.getIssue();
						int id = LibrarySys.generateIDforMaterial();
						String materialType = m.getComboBox().getSelectedItem().toString();
						Material material = null;
						if (materialType.equals("Book")) {
							material = new Book(bookType, id, title, author, page, price, stockCount, pb.getId(),
									pb.getPname(), pb.getEditor(), pb.getLocation());

						} else if (materialType.equals("Magazine")) {
							material = new Magazine(id, title, author, page, price, stockCount, pb.getId(), pb.getPname(),
									pb.getEditor(), pb.getLocation(), category, issue);
						}

						LibrarySys.addMaterial(material, pb);
						publisherInfoF.setText("");
						dispose();
						m.setVisible(true);
						m.getWarningText().setText("Material has been added successfully!");
						LibrarySys.readMaterialsFromFile();
					}

				}
			}
		});
		chooseByIdBtn.setBounds(162, 52, 129, 23);
		contentPane.add(chooseByIdBtn);

		chooseF = new JTextField();
		chooseF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (chooseF.getText().isEmpty()) {
						msgLabel.setText("Please Choose a publisher");
					} else {
						int pubID = Integer.parseInt(chooseF.getText());
						pb = LibrarySys.searchPublisherById(pubID);
						if (pb == null) {
							msgLabel.setText("Please enter the existing publisher ID");
						} else {
							String title = m.getTitle();
							String author = m.getAuthor();
							int page = m.getPage();
							int price = m.getPrice();
							int stockCount = m.getStockCount();
							String bookType = m.getBookType();
							String category = m.getCategory();
							int issue = m.getIssue();
							int id = LibrarySys.generateIDforMaterial();
							String materialType = m.getComboBox().getSelectedItem().toString();
							Material material = null;
							if (materialType.equals("Book")) {
								material = new Book(bookType, id, title, author, page, price, stockCount, pb.getId(),
										pb.getPname(), pb.getEditor(), pb.getLocation());

							} else if (materialType.equals("Magazine")) {
								material = new Magazine(id, title, author, page, price, stockCount, pb.getId(), pb.getPname(),
										pb.getEditor(), pb.getLocation(), category, issue);
							}

							LibrarySys.addMaterial(material, pb);
							publisherInfoF.setText("");
							dispose();
							m.setVisible(true);
							m.getWarningText().setText("Material has been added successfully!");
							LibrarySys.readMaterialsFromFile();
						}

					}
				}
			
			}
		});
		chooseF.setBounds(301, 53, 86, 20);
		contentPane.add(chooseF);
		chooseF.setColumns(10);

		JButton searchBtn = new JButton("Search By Name");
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = searchF.getText();
				String output = "";
				Publisher found = LibrarySys.searchPublisherByName(name);
				if (name.isEmpty()) {
					msgLabel.setText("Please fill the field!");
				} else {

					if (found == null) {
						msgLabel.setText("The publisher with the given name not found!");
					} else {
						output = found.toString();
						publisherInfoF.setText(output);
						msgLabel.setText("");
					}
				}

			}
		});
		searchBtn.setBounds(162, 86, 129, 23);
		contentPane.add(searchBtn);

		searchF = new JTextField();
		searchF.setBounds(301, 87, 86, 20);
		contentPane.add(searchF);
		searchF.setColumns(10);

		JButton backBtn = new JButton("Back");
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.setVisible(true);
				dispose();
			}
		});
		backBtn.setBounds(20, 332, 89, 23);
		contentPane.add(backBtn);

	}
}
