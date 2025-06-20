package Comparator;

import java.util.Comparator;

import Classes_Abstract.Person;

public class PersonNameComparator implements Comparator<Person>{

	@Override
	public int compare(Person p1, Person p2) {
		// TODO Auto-generated method stub
		return p1.getNameSurname().compareTo(p2.getNameSurname());
	}

}
