package Classes_Abstract;

import java.util.Objects;
import java.util.Scanner;

import Classes_HasA.Publisher;
import Interfaces.Borrowable;

public abstract class Material implements Borrowable, Comparable<Material> {
    protected int id;
    protected String title;
    protected String author;
    protected int page;
    protected int price;
    protected int stockCount;
    protected Publisher publisher;
    

    public Material(int id, String title, String author, int page, int price, int stockCount, int p_id, String pname, String editor, String location) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.page = page;
		this.price = price;
		this.stockCount = stockCount;
		this.publisher = new Publisher(p_id, pname, editor, location);
    }

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStockCount() {
		return stockCount;
	}

	public void setStockCount(int stockCount) {
		this.stockCount = stockCount;
	}

	

    @Override
	public String toString() {
		return " \nID=" + id + "\nTitle=" + title + "\nAuthor=" + author + "\nPage=" + page + "\nPrice=" + price
				+ "\nStock Count=" + stockCount + "\nPublisher=" + publisher.getPname();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Material other = (Material) obj;
		return id == other.id;
	}

	public boolean checkMaterialID(int id) {
        return this.id == id;
    }

	
    @Override
    public boolean checkBorrowable(int id, Person p) {
        return this.stockCount > 0 && p.getBorrowedItems().size() <= LIMIT && this.id == id;
    }

    public abstract void increaseStockCount();
    
    public abstract void decreaseStockCount();

}