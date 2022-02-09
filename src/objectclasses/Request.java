package objectclasses;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Request {
	int req_id;
	Date requestDate;
	Date requestFulfilledDate;
	boolean fulfilled;
	int conf_id;
	int item_id;

	public Request() {

	}

	public Request(int req_id, Date requestDate, Date requestFulfilledDate, int conf_id, int item_id) {
		super();
		this.req_id = req_id;
		this.requestDate = requestDate;
		this.requestFulfilledDate = requestFulfilledDate;
		this.conf_id = conf_id;
		this.item_id = item_id;
		fulfilled = false;
	}

	public int getReq_id() {
		return req_id;
	}

	public void setReq_id(int req_id) {
		this.req_id = req_id;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getRequestFulfilledDate() {
		return requestFulfilledDate;
	}

	public void setRequestFulfilledDate(Date requestFulfilledDate) {
		this.requestFulfilledDate = requestFulfilledDate;
	}

	public boolean isFulfilled() {
		return fulfilled;
	}

	public void setFulfilled(boolean fulfilled) {
		this.fulfilled = fulfilled;
	}

	public int getConf_id() {
		return conf_id;
	}

	public void setConf_id(int conf_id) {
		this.conf_id = conf_id;
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public static void createRequestItem(int reqID, int itemID) {
		String sqlQuery = "INSERT INTO RequestItems (req_ID, item_ID, fulfilled) VALUES (" + reqID + ", " + itemID + ", 0)";
		try {
			Controller.connection().executeUpdate(sqlQuery);
			Controller.connection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static int createRequest(int confID) {
		int i = 0;
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		sdf.format(today);
		String sqlQuery = "INSERT INTO Request (req_DateTime, req_FullfillDateTime, fulfilled, conf_ID) "
				+ "VALUES (now(), NULL, false, " + confID + ");";
		String sqlQuery1 = "SELECT max(req_ID) req_ID FROM Request";
		try {
			Controller.connection().executeUpdate(sqlQuery);
			ResultSet result = Controller.connection().executeQuery(sqlQuery1);
			result.next();
			i = result.getInt("req_ID");
			result.close();
			result.close();
			Controller.connection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
	public static void deleteRequest(int reqID) {
		String delete = "DELETE FROM Request WHERE req_ID = " + reqID;
		try {
			Controller.connection().executeUpdate(delete);
			Controller.connection().close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void setRequestItemComplete(int reqItemID) {
		String query = "UPDATE RequestItems SET fulfilled = 1 WHERE reqitem_ID = " + reqItemID;
		try {
			Controller.connection().executeUpdate(query);
			String query1 = "SELECT * FROM RequestItems WHERE reqitem_ID = " + reqItemID;
			ResultSet rs = Controller.connection().executeQuery(query1);
			rs.next();
			int reqID = rs.getInt("req_ID");
			Boolean b = true;
			String query2 = "SELECT * FROM RequestItems WHERE req_ID = " + reqID;
			ResultSet rs2 = Controller.connection().executeQuery(query2);
			while (rs2.next()) {
				if (rs2.getInt("fulfilled") == 0) {
					b = false;
				}
			}
			rs.close();
			rs2.close();
			Controller.connection().close();
			if (b) {
				setRequestComplete(reqID);
			}
			
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void setRequestComplete(int reqID) {
		String query = "UPDATE Request SET fulfilled = 1, req_FullfillDateTime = now() WHERE req_ID = " + reqID;
		try {
			Controller.connection().executeUpdate(query);
			Controller.connection().close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

}
