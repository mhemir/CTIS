package Classes_IsA_Material;

import java.util.Scanner;

import Classes_Abstract.Material;

public class Book extends Material {
	private String type;

	public Book(String type, int id, String title, String author, int page, int price, int stockCount, int p_id,
			String pname, String editor, String location) {
		super(id, title, author, page, price, stockCount, p_id, pname, editor, location);
		this.type = type;
	}

	@Override
	public String toString() {
		return "BOOK " + super.toString() + "\nBookType: " + type+"\n";
	}

	public String getType() {
		return type;
	}

	@Override
	public int compareTo(Material o) {
		// TODO Auto-generated method stub
		return this.getId()- o.getId();
	}

	@Override
	public void increaseStockCount() {
		this.stockCount++;
		
	}

	@Override
	public void decreaseStockCount() {
		this.stockCount--;
		
	}


}
