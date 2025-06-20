package Classes_IsA_Material;
import java.util.Scanner;

import Classes_Abstract.Material;
public class Magazine extends Material {
    private String category;
    private int issue;

    public Magazine(int id, String title, String author, int page, int price, int stockCount, int p_id, String p_name, String editor, String location, String category, int issue) {
    	super(id, title, author, page, price, stockCount, p_id, p_name, editor, location);
    	this.category = category;
        this.issue = issue;
    }

    public String toString() {
        return "MAGAZINE "+ super.toString() + "\nCategory: " + category + "\nIssue: " + issue+"\n";
    }

    
	public String getCategory() {
		return category;
	}

	public int getIssue() {
		return issue;
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