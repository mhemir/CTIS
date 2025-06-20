package Classes_HasA;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import Classes_Abstract.Material;

public class Publisher implements Comparable<Publisher>{
	private int id;
	private String pname;
	private String editor;
	private String location;
	private ArrayList<Material> materialList = new ArrayList<>();

	public ArrayList<Material> getMaterialList() {
		return materialList;
	}


	public Publisher(int id, String pname, String editor, String location) {
		super();
		this.id = id;
		this.pname = pname;
		this.editor = editor;
		this.location = location;
	}

	@Override
	public String toString() {
		return "ID=" + id + "\nPublisher Name=" + pname + "\nEditor=" + editor + "\nLocation=" + location+"\n\n";
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
		Publisher other = (Publisher) obj;
		return id == other.id;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


	@Override
	public int compareTo(Publisher o) {
		// TODO Auto-generated method stub
		return this.id - o.id;
	}



}
