package Comparator;

import java.util.Comparator;

import Classes_HasA.Publisher;

public class PublisherNameComparator implements Comparator<Publisher>{

	@Override
	public int compare(Publisher p1, Publisher p2) {
		// TODO Auto-generated method stub
		return p1.getPname().compareTo(p2.getPname());
	}


	
}
