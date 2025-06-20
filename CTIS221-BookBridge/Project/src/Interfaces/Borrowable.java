package Interfaces;

import Classes_Abstract.Person;

public interface Borrowable {
    
	public final int LIMIT = 10;
	
	public abstract boolean checkBorrowable(int id, Person p);

						
}

