package Classes_IsA_Person;
import java.util.Scanner;

import Classes_Abstract.Person;

public class Academician extends Person {
    private String department;

    public Academician(String nameSurname, int id, int totalPenaltyFee, String department) {
        super(nameSurname, id, totalPenaltyFee);
        this.department = department;
    }
    
    public void getInput() {
    	super.getInput();
    	Scanner sc = new Scanner(System.in);
    	System.out.println("Please Enter The Department");
    	this.department = sc.next();
    }

    public String getDepartment() {
		return department;
	}

	public String toString() {
        return "ACADEMICIAN"+super.toString() + "\nDepartment: " + department;
    }

	@Override
	public int compareTo(Person o) {
		// TODO Auto-generated method stub
		return this.id-o.getId();
	}


}