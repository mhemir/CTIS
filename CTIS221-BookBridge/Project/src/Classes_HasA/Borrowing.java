package Classes_HasA;

import Classes_Abstract.Person;

public class Borrowing {
	private int borrowingID;
	private int personid;
	private String personNameSurname;
	private String title;

	public Borrowing(int borrowingID, int personid, String personNameSurname, String title) {
		super();
		this.borrowingID = borrowingID;
		this.personid = personid;
		this.personNameSurname = personNameSurname;
		this.title = title;
	}

	public int getBorrowingID() {
		return borrowingID;
	}

	public void setBorrowingID(int borrowingID) {
		this.borrowingID = borrowingID;
	}

	public int getPersonid() {
		return personid;
	}

	public void setPersonid(int personid) {
		this.personid = personid;
	}

	public String getPersonNameSurname() {
		return personNameSurname;
	}

	public void setPersonNameSurname(String personNameSurname) {
		this.personNameSurname = personNameSurname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "BorrowingID=" +borrowingID + "\nPersonid=" + personid + "\nPersonNameSurname="
				+ personNameSurname + "\nTitle=" + title+"\n\n";
	}

}
