package objectclasses;

import java.sql.Connection;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller {

	private Booking booking;
	private Account account;
	private Room room;
	private Employee employee;
	private Request request;
	private VirtualCCProcessor vcc;

	private float amountOwed;
	private static Controller controller = new Controller();

	private Date arrival;
	private Date depart;

	private Controller() {
	}

	public static Controller getInstance() {
		if (controller == null) {
			controller = new Controller();
		}
		return controller;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Date getArrival() {
		return arrival;
	}

	public void setArrival(Date arrival) {
		this.arrival = arrival;
	}

	public Date getDepart() {
		return depart;
	}

	public void setDepart(Date depart) {
		this.depart = depart;
	}

	public VirtualCCProcessor getVcc() {
		return vcc;
	}

	public void setVcc(VirtualCCProcessor vcc) {
		this.vcc = vcc;
	}

	public float getAmountOwed() {
		return amountOwed;
	}

	public void setAmountOwed(float amountOwed) {
		this.amountOwed = amountOwed;
	}

	public Controller getController() {
		return controller;
	}
	
	public static void Error(Stage primary, String title, String content) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(content);
		alert.setTitle(title);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(primary);
		alert.showAndWait();
	}
	
	public static void Alert(Stage primary, String title, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText(content);
		alert.setTitle(title);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(primary);
		alert.showAndWait();
	}

	/*
	 * Returns true if customer entered valid data. False if not. Use if statement
	 * to determine actions taken.
	 */
	public boolean custLogIn(String email, String confID) {
		String sqlQuery1 = "Select * from Booking where cust_email ='" + email + "' and conf_ID = '" + confID + "';";
		ResultSet sqlResults1;
		try {
			sqlResults1 = connection().executeQuery(sqlQuery1);
			if (sqlResults1.next()) {
				int roomNum = sqlResults1.getInt("room_num");
				Date checkIn = sqlResults1.getDate("check_in");
				Date checkOut = sqlResults1.getDate("check_out");
				Room room = Room.getRoomFromDB(roomNum);
				booking = new Booking(account, room, checkIn, checkOut);
				setBooking(booking);

				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean isValid(String email) 
    { 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + 
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"; 
 
        Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return pat.matcher(email).matches(); 
    } 
	
	public void sendMail(String recipient, String name, int room, String checkin, String checkout, int confID, char type) throws MessagingException {
		Properties prop = new Properties();
		prop.setProperty("mail.smtp.auth", "true");
		prop.setProperty("mail.smtp.starttls.enable", "true");
		prop.setProperty("mail.smtp.host", "smtp.gmail.com");
		prop.setProperty("mail.smtp.port", "587");
		
		String emailAccount = "ucheckinservices@gmail.com";
		String emailPassword = "UChs@9982";
		
		Session session = Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication(emailAccount, emailPassword);
			}
		});
		
		Message message = prepareMessage(session, emailAccount, recipient, name, room, checkin, checkout, confID, type);
		
		Transport.send(message);
	}
	

	private Message prepareMessage(Session session, String emailAccount, String recipient, 
			String name, int room2, String checkin2, String checkout2, int confID, char type) {
		// TODO Auto-generated method stub
		try {	
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailAccount));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			switch(type) {
            	case 'b': message.setSubject("Booking Confirmation Number");
    			message.setText("Hello " + name + ", \n\n Your confirmation number is " + confID + 
    					"\n Your reservation is scheduled from " + checkin2 + " to " + checkout2 + "  for "
    							+ "Room " + room2 + ". \n Thank you for using uCheckIn. "
    							+ "If you have any concerns or if you did not create or request for a reservation, please contact "
    							+ "uCheckIn Support at 1-123-456-7890.");
    							break;
            	case 'c' : message.setSubject("Booking Canceled");
    			message.setText("Hello " + name + ", \n\n You are receiving this email because you have canceled your booking. "
    					+ "\n Thank you for using uCheckIn. "
    							+ "If you have any concerns or if you did not cancel your reservation, please contact "
    							+ "uCheckIn Support at 1-123-456-7890.");
    							break;
			}
			
			return message;
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String hashPassword(String plainTextPassword){
	    return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	} 
	
	public static boolean checkPass(String plainPassword, String hashedPassword) {
		try {
			if (BCrypt.checkpw(plainPassword, hashedPassword)) {
				return true;
			} else {
				return false;
			}
		} catch (IllegalArgumentException e) {
			return false;
		}
		
	}
	
	public boolean checkEmpName(String username) {
		String sqlQuery = "Select * FROM Employee WHERE username = '" + username + "';";
		ResultSet r;
		try {
			r = connection().executeQuery(sqlQuery);
			if (r.next()) {
				return false;
			}
			r.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean createEmployee(String fname, String lname, String username, String password, boolean admin)
			throws SQLException {
		int i = (admin) ? 1: 0;
		String hashedpass = hashPassword(password);
		String sqlQuery = "INSERT INTO Employee (emp_Fname, emp_lname, admin, username, password) VALUES ('" + fname
				+ "', '" + lname + "', " + i + ", '" + username + "', '" + hashedpass + "');";
		int result = connection().executeUpdate(sqlQuery);

		if (result != 0) {
			System.out.println("Employee Created");
			connection().close();
			return true;
		} else {
			System.out.println("Oops, something went wrong!");
			connection().close();
			return false;
		}
	}

	public boolean empLogin(String username, String password) {		
		String query = "select * from employee where username = '" + username + "';";
		try {
			ResultSet result = connection().executeQuery(query);
			if (result.next()) {
				String fName = result.getString("emp_Fname");
				String lName = result.getString("emp_Lname");
				boolean admin = (result.getInt("admin") == 1) ? true: false;
				//add rest of fields
				Employee temp = new Employee(fName, lName, "", "", username, admin);
				setEmployee(temp);
				return true;
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// Connection to database method
	public static Statement connection() {
		Statement statement = null;
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uCheckIn", "root", "");
			statement = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return statement;
	}

	public ArrayList<Room> getAllAvailableType(String type) {
		return Room.getAllAvailableType(type);
	}

	public ArrayList<Room> getAllAvailable() {
		return Room.getAllAvailable();
	}

	/*
	 * Returns a list of open requests.
	 */
	public ArrayList<Request> checkRequests() {
		String query = "select * from requests where fullfilled = 0";
		try {
			ResultSet result = connection().executeQuery(query);
			ArrayList<Request> request = new ArrayList<>();
			while (result.next()) {
				Request temp = new Request();
				temp.setConf_id(result.getInt("req_ID"));
				temp.setFulfilled(true);
				temp.setItem_id(result.getInt("item_ID"));
				temp.setRequestDate(result.getDate("req_FulfillDate"));
				temp.setReq_id(result.getInt("req_ID"));
				request.add(temp);
			}
			return request;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public void createRequest(String type, Date reqDate, Date fulfillDate, int conf_id, int item_id, int quant) {
		String query = "insert into request(req_Type, req_DateTime, req_FulfillDate, conf_ID, item_ID values('" + type
				+ "', " + reqDate + "', " + fulfillDate + "', " + conf_id + "'," + item_id + "';";
		try {
			connection().execute(query);
			String itmeQuery = "select * from items where item_ID = " + item_id + ";";
			ResultSet result = connection().executeQuery(itmeQuery);
			float price = result.getFloat("item_price");
			price = price * quant;
			amountOwed += price;
			connection().close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public boolean createCustomer(String fName, String lName, String email, String phone) {
		Account newAccount = new Account(fName, lName, phone, email);
		try {
			boolean isCreated = newAccount.createCustomer();
			return isCreated;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean cancelBooking() {
		String cancel = "delete from booking where conf_ID = " + booking.getConfNum() + ";";
		String sqlQuery = "Update room SET room_status = 0 where room_num = " + room.getRoomNumber() + ";";
		try {
			connection().executeUpdate(sqlQuery);
			connection().execute(cancel);
			connection().close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
