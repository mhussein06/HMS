
package objectclasses;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


//Booking object class
public class Booking {
	// TODO: Validate all needed properties, setters, and getters
	Account customer;
	int confNum;
	Room room;
	Date arrival;
	Date departure;
	int lengthStay; // in days for now

	int ccToken;

	float bill;

	public Booking(Account customer, Room room, Date checkIn, Date checkOut) {
		this.customer = customer;
		this.room = room;
		this.arrival = checkIn;
		this.departure = checkOut;
		lengthStay = getDaysBetween(checkIn, checkOut);
		this.bill = 0;

	}

	public Account getCustomer() {
		return customer;
	}

	public void setAccount(Account customer) {
		this.customer = customer;
	}

	public void setConfNum(int confNum) {
		this.confNum = confNum;
	}

	public Room getRoom() {
		return room;
	}

	public Date getArrival() {
		return arrival;
	}

	public Date getDeparture() {
		return departure;
	}

	public void setArrival(Date arrival) {
		this.arrival = arrival;
	}

	public void setDeparture(Date departure) {
		this.departure = departure;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public int getConfNum() {
		return confNum;
	}

	public int getCcToken() {
		return ccToken;
	}

	public void setCcToken(int ccToken) {
		this.ccToken = ccToken;
	}

	// method returns true if confID already exists in database
	public boolean checkConfNum(int confID) {
		String sqlQuery = "SELECT * FROM Booking WHERE conf_ID = " + confID;
		ResultSet sqlResults;
		try {
			sqlResults = Controller.connection().executeQuery(sqlQuery);
			if (sqlResults.next()) {
				// duplicate exists - return true
				Controller.connection().close();
				return true;
				// duplicate does not exist - return false
			} else {
				Controller.connection().close();
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public int createConfID() {
		int confirmation = 0;
		Random rnd = new Random();
		confirmation = rnd.nextInt(999999);
		while (checkConfNum(confirmation)) {
			confirmation = rnd.nextInt();
		}
		return confirmation;
	}

	public static boolean adjustStay(Date arrival, Date newDeparture, int conf_ID) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String sdeparture = sdf.format(newDeparture);
		int i = getDaysBetween(arrival, newDeparture);
		try {
			String sqlUpdate = "UPDATE Booking SET check_out = DATE '" + sdeparture + "', stay_length = " + i
					+ " WHERE conf_ID = " + conf_ID;
			Controller.connection().executeUpdate(sqlUpdate);
			Controller.connection().close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Booking getBookingFromDB(Account acc, int confNum) {
		Booking book = null;
		String q = "SELECT * FROM Booking WHERE conf_ID = " + confNum;
		try {
			ResultSet rs = Controller.connection().executeQuery(q);
			rs.next();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Date date1 = rs.getDate("check_in");
			sdf.format(date1);
			Date date2 = rs.getDate("check_out");
			sdf.format(date2);
			Room room = Room.getRoomFromDB(rs.getInt("room_num"));
			book = new Booking(acc, room, date1, date2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return book;
	}

	/*
	 * Checks if customer data exits, creates if not check conf_ID for duplicates in
	 * table checks if room is booked or not
	 */
	public boolean createBooking() throws SQLException {
		// Check if account exists. If not the account is created.
		// ensure unique conf_ID is created
		confNum = createConfID();
		// Checks if room is booked or does not exist
		if (Room.isBooked(room)) {
			System.out.println("Sorry, room is already booked. Please choose another room");
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String date1 = sdf.format(arrival);
		String date2 = sdf.format(departure);
		// Records booking into database
		String sqlQuery = "INSERT INTO Booking VALUES (" + confNum + ", '" + customer.getEmail() + "', "
				+ room.roomNumber + ", " + lengthStay + ", DATE ('" + date1 + "'), DATE ('" + date2 + "'), NULL, 0)";
		int result = Controller.connection().executeUpdate(sqlQuery);
		// Updates room status in room database
		String sqlUpdate = "UPDATE room SET room_status = 1 WHERE room_num = " + room.roomNumber;
		Controller.connection().execute(sqlUpdate);
		if (result != 0) {
			room.setBooked(true);
			System.out.println("Booking Created");
			System.out.println("Your Confirmation Number is: " + confNum);
			System.out.println("To login to our kiosks use your e-mail address and your confirmation number");
			Controller.connection().close();
			return true;
		} else {
			System.out.println("Could not create booking");
			Controller.connection().close();
			return false;
		}
	}
	

	public void checkOut() {
		String sqlQuery = "Update room SET room_status = 0 where room_num = " + room.getRoomNumber() + ";";
		String query = "UPDATE Booking SET check_out = now() WHERE conf_ID = " + confNum;
		try {
			Controller.connection().executeUpdate(query);
			Controller.connection().executeUpdate(sqlQuery);
			Controller.connection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertCCToken(int hash) {
		String query = "UPDATE Booking SET check_out = now(), cctoken = " + hash + " WHERE conf_ID = " + confNum;
		try {
			Controller.connection().executeUpdate(query);
			Controller.connection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void checkIn() {
		String query = "UPDATE Booking SET checkin_status = 1, check_in = now() WHERE conf_ID = " + confNum;
		try {
			Controller.connection().executeUpdate(query);
			Controller.connection().close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isCheckedIn() {
		boolean b = true;
		String q = "Select * FROM Booking WHERE conf_ID = " + confNum;
		try {
			ResultSet rs = Controller.connection().executeQuery(q);
			rs.next();
			if(rs.getInt("checkin_status") != 1) {
				b = false;
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return b;
	}

	public static int getDaysBetween(Date start, Date end) {
		long difference = end.getTime() - start.getTime();
		float daysBetween = (difference / (1000 * 60 * 60 * 24));
		return (int) daysBetween;
	}

	public static Account getAccountFromDB(String lName, String email) {
		String query = "Select * from customer where cust_Lname = " + lName + ";";
		String query2 = "Select * from booking where cust_email = " + email + ";";
		try {
			ResultSet results = Controller.connection().executeQuery(query);
			ResultSet results2 = Controller.connection().executeQuery(query2);
			if (results.next() && results2.next()) {
				int accountID = results.getInt("cust_ID");
				String fName = results.getString("cust_Fname");
				String lastName = results.getString("cust_Lname");
				String phone = results.getString("phone");
				String e_mail = results.getString("email");
				Account output = new Account(accountID, fName, lastName, phone, e_mail);
				return output;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setLengthStay(int lengthStay, int confNum) {
		String sqlQuery = "UPDATE Booking SET stay_length = " + lengthStay + " WHERE conf_ID = " + confNum;
		try {
			Controller.connection().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}