package objectclasses;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Functionality: Check all booked rooms
 *
 * TODO: Provide details on individual room; rate, beds etc. check all available
 * rooms
 */

public class Room {
	// TODO: Validate all needed properties, setters, and getters
	int roomNumber;
	String roomType;
	boolean booked;

	public Room(int roomNumber, String roomType, boolean booked) {
		this.roomNumber = roomNumber;
		this.roomType = roomType;
		this.booked = booked;
	}

	public Room() {

	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public void setBooked(boolean booked) {
		this.booked = booked;
	}
	
	public float getRate() {
		float rate = 0;
		String sqlQuery = "SELECT room_num, r.roomType_ID, room_status, rate FROM Room r INNER JOIN RoomType rt\n" + 
				"ON r.roomType_ID = rt.roomType_ID WHERE room_num = " + roomNumber;
		try {
			ResultSet rs = Controller.connection().executeQuery(sqlQuery);
			rs.next();
			rate = rs.getFloat("rate");
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rate;
	}

	// returns true if room is booked, false if room is not booked or does not exist
	public static boolean isBooked(Room room) {
		boolean isBooked = false;

		String sqlQuery = "SELECT room_status FROM Room WHERE room_num = " + room.roomNumber;
		int roomStatus = 0;
		try {
			ResultSet result = Controller.connection().executeQuery(sqlQuery);
			if (result.next()) {
				roomStatus = result.getInt("room_status");
			}
			result.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isBooked = (roomStatus == 1);
		return isBooked;
	}
	// returns true if room is booked, false if room is not booked or does not exist
		public static boolean isBooked(int roomNum) {
			boolean isBooked = false;
			String sqlQuery = "SELECT room_status FROM Room WHERE room_num = " + roomNum;
			int roomStatus = 0;
			try {
				ResultSet result = Controller.connection().executeQuery(sqlQuery);
				if (result.next()) {
					roomStatus = result.getInt("room_status");
				}
				result.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isBooked = (roomStatus == 1);
			return isBooked;
		}
		
		public static boolean exists(int roomNum) {
			boolean b = false;
			String sqlQuery = "SELECT * FROM Room WHERE room_num = " + roomNum;
			try {
				ResultSet result = Controller.connection().executeQuery(sqlQuery);
				if(result.next()) {
					b = true;
				}
				result.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			return b;
		}

	// prints list of all available rooms. Returns a list of available Rooms.
	public static ArrayList<Room> getAllAvailable() {
		ArrayList<Room> available = new ArrayList<>();
		try {
			String sqlQuery = "SELECT * FROM Room Where room_status = 0 AND room_active = 1;";
			ResultSet result = Controller.connection().executeQuery(sqlQuery);
			while (result.next()) {
				int roomNum = result.getInt("room_num");
				String type = result.getString("roomType_ID");
				int floor = result.getInt("floor");
				System.out.println("Room Number: " + roomNum + " Room Type: " + type + " Room Floor: " + floor);
				Room temp = new Room(roomNum, type, false);
				available.add(temp);
			}
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return available;
	}

	/*
	 * Retrieves all available rooms of a certain type
	 */
	public static ArrayList<Room> getAllAvailableType(String type) {
		ArrayList<Room> list = new ArrayList<>();
		try {
			String sqlQuery = "Select * from Room where room_status = 0 And room_active = 1 and roomtype_id = '" + type
					+ "';";
			ResultSet result = Controller.connection().executeQuery(sqlQuery);

			while (result.next()) {
				int roomNum = result.getInt("room_num");
				String roomType = result.getString("roomType_ID");
				Room temp = new Room(roomNum, roomType, false);
				list.add(temp);
			}
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}

	// prints a list of booked rooms
	public static ArrayList<Room> getAllBooked() {
		ArrayList<Room> booked = new ArrayList<>();
		try {
			String sqlQuery = "SELECT * FROM Room WHERE room_status = 1;";
			ResultSet result = Controller.connection().executeQuery(sqlQuery);
			while (result.next()) {
				int roomNum = result.getInt("room_num");
				String type = result.getString("roomType_ID");
				int floor = result.getInt("floor");
				System.out.println("Room Number: " + roomNum + " Room Type: " + type + " Room Floor: " + floor);
				booked.add(new Room(roomNum, type, true));
			}
			result.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return booked;
	}

	public boolean createRoom(int roomNum, String roomType) {
		String sqlQuery = "Replace into room(room_num, roomType_ID, room_status, room_active, floor) VALUES ( "
				+ roomNum + ",\"" + roomType + "\", " + 0 + ", " + false + ", 2);";
		try {
			Controller.connection().execute(sqlQuery);
			String sqlQuery2 = "select * from Room where room_num = " + roomNum + ";";
			ResultSet result = Controller.connection().executeQuery(sqlQuery2);
			if (result.next()) {
				result.close();
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * Retrieves data from the DB to create a room object.
	 */
	public static Room getRoomFromDB(int roomNumber) {
		String type = null;
		int status = 0;
		String sqlQuery = "select * from room where room_num=" + roomNumber + ";";
		try {
			ResultSet result = Controller.connection().executeQuery(sqlQuery);
			if (result.next()) {
				type = result.getString("roomType_ID");
				status = result.getInt("room_status");
			}
			result.close();
			Controller.connection().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Room output = new Room();
		output.setRoomNumber(roomNumber);
		output.setRoomType(type);
		if (status == 0) {
			output.setBooked(true);
		} else {
			output.setBooked(false);
		}
		return output;
	}


	public static void main(String[] args) {
		Room test = new Room();
		Room.isBooked(test);
	}
}
