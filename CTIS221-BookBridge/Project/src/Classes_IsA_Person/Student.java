package Classes_IsA_Person;
import java.util.Scanner;

import Classes_Abstract.Person;

public class Student extends Person {
    private int gradYear;
    private String department;
   
    public Student(String nameSurname, int id, int totalPenaltyFee, int gradYear, String department) {
        super(nameSurname, id, totalPenaltyFee);
        this.gradYear = gradYear;
        this.department = department;
    }

    
    public void getInput() {
    	super.getInput();
    	Scanner sc = new Scanner(System.in);
    	System.out.println("Please Enter The Graduate Year");
    	this.gradYear = sc.nextInt();
    	System.out.println("Please Enter the Department");
    	this.id = sc.nextInt();
    }

    public String toString() {
        return "STUDENT"+super.toString() + "\nGrad Year: " + gradYear + "\nDepartment: " + department;
    }


	public int getGradYear() {
		return gradYear;
	}


	public String getDepartment() {
		return department;
	}

	@Override
	public int compareTo(Person o) {
		// TODO Auto-generated method stub
		return this.id-o.getId();
	}
	
	
}




