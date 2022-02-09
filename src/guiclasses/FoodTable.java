package guiclasses;

import javafx.scene.control.CheckBox;

//Table class for food table in Make Requests page
public class FoodTable {
	String name;
	String price;
	CheckBox select;
	
	public FoodTable(String name, String price, CheckBox select) {
		super();
		this.name = name;
		this.price = price;
		this.select = select;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public CheckBox getSelect() {
		return select;
	}

	public void setSelect(CheckBox select) {
		this.select = select;
	}
	

}
