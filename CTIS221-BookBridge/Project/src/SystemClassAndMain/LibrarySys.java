package SystemClassAndMain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import Classes_Abstract.Material;
import Classes_Abstract.Person;
import Classes_HasA.Borrowing;
import Classes_HasA.Publisher;
import Classes_IsA_Material.Book;
import Classes_IsA_Material.Magazine;
import Classes_IsA_Person.Academician;
import Classes_IsA_Person.Guest;
import Classes_IsA_Person.Student;
import Comparator.PersonNameComparator;
import Comparator.PublisherNameComparator;

public class LibrarySys {
	public static HashSet<Person> personList = new HashSet<>();
	public static HashSet<Publisher> publishers = new HashSet<>();
	public static HashSet<Material> materials = new HashSet<>();
	public static HashMap<Integer, Borrowing> borrowings = new HashMap<>();

	public static boolean writeToFile(String fileName, Material material, Publisher pb, Person p) {
		try (FileWriter fw = new FileWriter(fileName, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			String output = "";
			if (material != null && pb != null && p == null) {
//				materials.txt
				String type;
				int id = material.getId();
				String title = material.getTitle() + " ";
				String author = material.getAuthor() + " ";
				int page = material.getPage();
				int price = material.getPrice();
				int stockCount = material.getStockCount();
				String pname = pb.getPname() + " ";

				if (material instanceof Book) {
					String bookType = ((Book) material).getType() + " ";
					type = "BOOK ";
					output = "\n" + type + bookType + id + " " + title + author + page + " " + price + " " + stockCount
							+ " " + pname;
				} else if (material instanceof Magazine) {
					String category = ((Magazine) material).getCategory();
					int issue = ((Magazine) material).getIssue();
					type = "MAGAZINE";
					output = "\n" + type + " " + id + " " + title + author + page + " " + price + " " + stockCount + " "
							+ pname + category + " " + issue;
				}

			} else if (material == null && pb != null && p == null) {
//				publishers.txt
				int id = pb.getId();
				String pname = pb.getPname() + " ";
				String editor = pb.getEditor() + " ";
				String location = pb.getLocation();
				output = "\n" + id + " " + pname + editor + location;
			} else if (material == null && p != null && pb == null) {
				// Persons
				String type;
				String nameSurname = p.getNameSurname() + " ";
				int id = p.getId();
				int totalPenaltyFee = p.getPenaltyFee();

				if (p instanceof Student) {
					type = "STUDENT";
					int gradYear = ((Student) p).getGradYear();
					String department = ((Student) p).getDepartment();
					output = "\n" + type + " " + nameSurname + id + " " + totalPenaltyFee + " " + gradYear + " "
							+ department;
				} else if (p instanceof Academician) {
					type = "ACADEMICIAN";
					String department = ((Academician) p).getDepartment();
					output = "\n" + type + " " + nameSurname + id + " " + totalPenaltyFee + " " + department;
				} else if (p instanceof Guest) {
					type = "GUEST";
					String entryDate = ((Guest) p).getEntryDate() + " ";
					String exitDate = ((Guest) p).getExitDate();
					output = "\n" + type + " " + nameSurname + id + " " + totalPenaltyFee + " " + entryDate + exitDate;
				}

			} else if (material != null && p != null && pb == null) {
				// Borrowing
				int borrowID = borrowings.size();
				int personID = p.getId();
				String title = material.getTitle();
				output = "\n" + borrowID + " " + personID + " " + p.getNameSurname() + " " + title;
			}
			out.print(output);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static void readPublishersFromFile() {
		try {
			Scanner sc = new Scanner(new File("Publishers.txt"));
			while (sc.hasNext()) {
				int id = sc.nextInt();
				String pname = sc.next();
				String editor = sc.next();
				String location = sc.next();
				Publisher pb = new Publisher(id, pname, editor, location);
				publishers.add(pb);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void readMaterialsFromFile() {
		try {
			Scanner sc = new Scanner(new File("Materials.txt"));
			while (sc.hasNext()) {
				String type = sc.next();
				int id;
				String title;
				String author;
				int page;
				int price;
				int stockCount;
				String pname;
				Material m = null;
				Publisher pb = null;
				if (type.equals("BOOK")) {
					String bookType = sc.next();
					id = sc.nextInt();
					title = sc.next();
					author = sc.next();
					page = sc.nextInt();
					price = sc.nextInt();
					stockCount = sc.nextInt();
					pname = sc.next();
					pb = searchPublisherByName(pname);
					m = new Book(bookType, id, title, author, page, price, stockCount, pb.getId(), pname,
							pb.getEditor(), pb.getLocation());

				} else if (type.equals("MAGAZINE")) {
					id = sc.nextInt();
					title = sc.next();
					author = sc.next();
					page = sc.nextInt();
					price = sc.nextInt();
					stockCount = sc.nextInt();
					pname = sc.next();
					String category = sc.next();
					int issue = sc.nextInt();
					pb = searchPublisherByName(pname);
					m = new Magazine(id, title, author, page, price, stockCount, pb.getId(), pname, pb.getEditor(),
							pb.getLocation(), category, issue);

				}

				materials.add(m);
				pb.getMaterialList().add(m);

			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void readPersonsFromFile() {
		try {
			Scanner sc = new Scanner(new File("Persons.txt"));
			while (sc.hasNext()) {
				String type = sc.next();
				String name = sc.next();
				int id = sc.nextInt();
				int totalPenaltyFee = sc.nextInt();
				Person p = null;
				if (type.equals("STUDENT")) {
					int gradYear = sc.nextInt();
					String department = sc.next();
					p = new Student(name, id, totalPenaltyFee, gradYear, department);
				} else if (type.equals("ACADEMICIAN")) {
					String department = sc.next();
					p = new Academician(name, id, totalPenaltyFee, department);
				} else if (type.equals("GUEST")) {
					String entryDate = sc.next();
					String exitDate = sc.next();
					p = new Guest(name, id, totalPenaltyFee, entryDate, exitDate);
				}
				personList.add(p);

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void readBorrowedItemsFromFile() {
		try {
			Scanner sc = new Scanner(new File("Borrowings.txt"));
			while (sc.hasNext()) {
				int borrowID = sc.nextInt();
				int personID = sc.nextInt();
				sc.next();
				String materialTitle = sc.next();
				Material found = searchMaterialByTitle(materialTitle);
				Borrowing br;
				Person p = searchPerson(personID);
				if (found.checkBorrowable(found.getId(), p)) {
					found.decreaseStockCount();
					p.getBorrowedItems().add(found);
					br = new Borrowing(borrowID, personID, p.getNameSurname(), materialTitle);
					borrowings.put(borrowID, br);
				}

			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void addMaterial(Material material, Publisher pb) {
		if (searchMaterialByID(material.getId()) != null) {
			System.out.println("This ID for this material already exists!");
		} else {
			materials.add(material);
			pb.getMaterialList().add(material);
			writeToFile("Materials.txt", material, pb, null);
		}
	}

	public static int generateIDforMaterial() {
		int newID = materials.size() + 1;
		return newID;
	}

	public static int generateIDforPerson() {
		int newID = personList.size() + 1;
		return newID;
	}

	public static int generateIDforPublisher() {
		int newID = publishers.size() + 1;
		return newID;
	}

	public static Material searchMaterialByID(int id) {
		for (Material material : materials) {
			if (material.checkMaterialID(id)) {
				return material;
			}
		}
		return null;
	}

	public static String borrowMaterial(int personId, int materialId) {
		Person p = searchPerson(personId);
		Material found = searchMaterialByID(materialId);
		Borrowing br;
		if (p != null && found != null) {
			if (found.checkBorrowable(materialId, p)) {
				found.decreaseStockCount();
				p.getBorrowedItems().add(found);
				br = new Borrowing(borrowings.size() + 1, personId, p.getNameSurname(), found.getTitle());
				borrowings.put(borrowings.size() + 1, br);
				writeToFile("Borrowings.txt", found, null, p);
				return "Material borrowed successfully: " + found.getTitle();
			} else {
				return "Material cannot be borrowed because you \nexceed the limit of 10 or stockCount is 0";
			}
		} else {
			return "Person or material not found!";
		}
	}

	public static boolean addPublisher(int id, String pname, String editor, String location) {
		if (searchPublisherById(id) == null) {
			Publisher pb = new Publisher(id, pname, editor, location);
			publishers.add(pb);
			writeToFile("Publishers.txt", null, pb, null);
			return true;
		} else {
			return false;
		}
	}

	public static Publisher searchPublisherById(int id) {
		for (Publisher pbs : publishers) {
			if (pbs.getId() == id) {
				return pbs;
			}
		}

		return null;
	}

	public static Publisher searchPublisherByName(String pname) {
		for (Publisher pbs : publishers) {
			if (pbs.getPname().equals(pname)) {
				return pbs;
			}
		}

		return null;
	}

	public static Material searchMaterialByTitle(String title) {
		for (Material material : materials) {
			if (material.getTitle().equals(title)) {
				return material;
			}
		}
		return null;
	}

	public static String displayAllMaterials() {
		String result = "";
		for (Material material : materials) {
			result += material.toString() + "\n";
		}
		return result;
	}

	public static String displayAllPublishers() {
		String result = "";
		Set<Publisher> ts = new TreeSet<Publisher>();
		ts.addAll(publishers);
		for (Publisher publisher : ts) {
			result += publisher.toString();
		}
		return result;
	}

	public static String displayAllPublishersByName() {
		String result = "";
		Set<Publisher> ts = new TreeSet<>(new PublisherNameComparator());
		ts.addAll(publishers);
		for (Publisher publisher : ts) {
			result += publisher.toString();
		}
		return result;

	}

	public static boolean addPerson(String type, String nameSurname, int id, int totalPenaltyfee, int gradYear,
			String department, String entryDate, String exitDate) {
		if (searchPerson(id) == null) {
			Person ps;
			if (type.equalsIgnoreCase("Student")) {
				ps = new Student(nameSurname, id, totalPenaltyfee, gradYear, department);
			} else if (type.equalsIgnoreCase("academician")) {
				ps = new Academician(nameSurname, id, totalPenaltyfee, department);
			} else {
				ps = new Guest(nameSurname, id, totalPenaltyfee, entryDate, exitDate);
			}
			personList.add(ps);
			writeToFile("Persons.txt", null, null, ps);
			return true;
		} else {
			System.out.println("Person can't have the same id as the other's id");
			return false;
		}

	}

	public static Person searchPerson(int id) {
		for (Person person : personList) {
			if (person.checkPerson(id)) {
				return person;
			}
		}
		return null;
	}

	public static String displayAllPersons() {
		String result = "";
		Set<Person> ts = new TreeSet<>();
		ts.addAll(personList);
		for (Person person : ts) {
			result += person.toString() + "\n\n";
		}
		return result;
	}

	public static String displayAllPersonsByName() {
		String result = "";
		Set<Person> ts = new TreeSet<>(new PersonNameComparator());
		ts.addAll(personList);
		for (Person person : ts) {
			result += person.toString() + "\n\n";
		}
		return result;
	}

	public static String displayAvailableMaterials() {
		String result = "";
		for (Material material : materials) {
			if (material.getStockCount() > 0) {
				result += material.toString() + "\n";
			}
		}
		return result;
	}

	public static String showAllBorrowings() {
		String result = "";
		for (Map.Entry<Integer, Borrowing> entry : borrowings.entrySet()) {
			result += entry.getValue();
		}
		return result;
	}

	public static Borrowing searchBorrowingByID(int id) {
		for (Map.Entry<Integer, Borrowing> entry : borrowings.entrySet()) {
			if (entry.getValue().getBorrowingID() == id) {
				return entry.getValue();
			}
		}
		return null;
	}

	public static HashMap<Integer, Borrowing> getBorrowings() {
		return borrowings;
	}

}