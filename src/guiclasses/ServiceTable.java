package guiclasses;

import javafx.scene.control.CheckBox;

//table class for services in Make request page
public class ServiceTable {
	
	String name;
	CheckBox select;
	
	public CheckBox getSelect() {
		return select;
	}

	public void setSelect(CheckBox select) {
		this.select = select;
	}

	public ServiceTable(String name, CheckBox select) {
		super();
		this.name = name;
		this.select = select;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
