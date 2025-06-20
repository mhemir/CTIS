package SystemClassAndMain;

import GUI.MainFrame;

public class LibraryMain {
	public static void main(String[] args) {
		LibrarySys.readPublishersFromFile();
		LibrarySys.readMaterialsFromFile();
		LibrarySys.readPersonsFromFile();
		LibrarySys.readBorrowedItemsFromFile();
		
//		System.out.println(LibrarySys.showAllBorrowings());
		MainFrame m = new MainFrame();
		m.setVisible(true);

	
	}
}
