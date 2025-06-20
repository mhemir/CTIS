package Classes_IsA_Person;

import java.util.Scanner;

import Classes_Abstract.Person;

public class Guest extends Person {
	private String entryDate;
	private String exitDate;

	public Guest(String nameSurname, int id, int totalPenaltyFee, String entryDate, String exitDate) {
		super(nameSurname, id, totalPenaltyFee);
		this.entryDate = entryDate;
		this.exitDate = exitDate;
	}

	public void getInput() {
		super.getInput();
		Scanner sc = new Scanner(System.in);
		System.out.println("Please Enter The Entry Date");
		this.entryDate = sc.next();
		System.out.println("Please Enter the Exit Date");
		this.exitDate = sc.next();
	}

	public String toString() {
		return "GUEST" + super.toString() + "\nEntry Date= " + entryDate + "\nExit Date=" + exitDate;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public String getExitDate() {
		return exitDate;
	}

	@Override
	public int compareTo(Person o) {
		// TODO Auto-generated method stub
		return this.id-o.getId();
	}
	
}