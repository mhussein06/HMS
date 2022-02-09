package guiclasses;

import javafx.scene.control.CheckBox;

//Table class for rooms in room bruiser page.
public class RoomTable {
	
	int roomNum;
	String roomType;
	String price;
	CheckBox select;
	
	
	public int getRoomNum() {
		return roomNum;
	}


	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}


	public String getRoomType() {
		return roomType;
	}


	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}


	public CheckBox getSelect() {
		return select;
	}


	public void setSelect(CheckBox select) {
		this.select = select;
	}


	public RoomTable(int roomNum, String roomType, String price, CheckBox select) {
		super();
		this.roomNum = roomNum;
		this.roomType = roomType;
		this.select = select;
		this.price = price;
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}
	
	

}
