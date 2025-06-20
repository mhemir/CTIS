package Classes_Abstract;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public abstract class Person implements Comparable<Person>{

	protected String nameSurname;
	protected int id;
	protected int totalPenaltyFee;
	protected ArrayList<Material> borrowedItems = new ArrayList<Material>();

	public Person(String nameSurname, int id, int totalPenaltyFee) {
		this.nameSurname = nameSurname;
		this.id = id;
		this.totalPenaltyFee = totalPenaltyFee;
	}

	public void getInput() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please Enter The Name-Surname");
		this.nameSurname = sc.next();
		System.out.println("Please Enter the id");
		this.id = sc.nextInt();
	}

	public boolean checkPerson(int id) {
		return this.id == id;
	}

	@Override
	public String toString() {
		return "\nName Surname=" + nameSurname + "\nID=" + id +
				 "\nTotal Penalty Fee= " + totalPenaltyFee;
	}

	@Override
	public int hashCode() {
		return Objects.hash(borrowedItems, id, nameSurname, totalPenaltyFee);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return Objects.equals(borrowedItems, other.borrowedItems) && id == other.id
				&& Objects.equals(nameSurname, other.nameSurname) && totalPenaltyFee == other.totalPenaltyFee;
	}

	public String getNameSurname() {
		return nameSurname;
	}

	public void setNameSurname(String nameSurname) {
		this.nameSurname = nameSurname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Material> getBorrowedItems() {
		return borrowedItems;
	}

	public int getPenaltyFee() {
		return totalPenaltyFee;
	}

	public void setPenaltyFee(int totalPenaltyFee) {
		this.totalPenaltyFee = totalPenaltyFee;
	}


}
