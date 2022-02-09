package guiclasses;

import java.util.Date;

import javafx.scene.control.CheckBox;

//Table class for Request table in Make request page.
public class RequestTable {
	String reqItemID;
	String name;
	Date requestTime;
	String status;
	CheckBox select;
	
	
	public String getReqItemID() {
		return reqItemID;
	}
	public void setReqItemID(String reqItemID) {
		this.reqItemID = reqItemID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public RequestTable(String reqID, String name, Date requestTime, String status, CheckBox select) {
		super();
		this.reqItemID = reqID;
		this.name = name;
		this.requestTime = requestTime;
		this.status = status;
		this.select = select;
	}
	public CheckBox getSelect() {
		return select;
	}
	public void setSelect(CheckBox select) {
		this.select = select;
	}
}
