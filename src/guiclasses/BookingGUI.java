package guiclasses;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import javax.mail.MessagingException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import objectclasses.Account;
import objectclasses.Booking;
import objectclasses.Controller;
import objectclasses.Room;
import objectclasses.VirtualCCProcessor;

public class BookingGUI implements Initializable {

	@FXML
	private Button btn_submit;
	@FXML
	private DatePicker dtp_checkin;
	@FXML
	private DatePicker dtp_checkout;
	@FXML
	private TextField txt_fname;
	@FXML
	private TextField txt_lname;
	@FXML
	private TextField txt_email;
	@FXML
	private Label label_room;
	@FXML
	private Button btn_cancel;
	@FXML
	private DatePicker dtp_expiration;
	@FXML
	private TextField txt_cardnum;
	@FXML
	private TextField txt_csc;

	//Controller object to retrieve and pass data from pages
	Controller control = Controller.getInstance();
	private String roomnum;


	//Validate no empty fields
	private static boolean validate(String fname, String lname, String email, String room, LocalDate localDate,
			LocalDate localDate2, String cardnum, String csc, LocalDate expiration) {
		if (fname.equals("") || lname.equals("") || email.equals("") || room.equals("") || localDate == null
				|| localDate2 == null || cardnum.equals(null)  || csc.equals(null) || expiration == null) {
			return false;
		} else {
			return true;
		}
	}
	
	//Program for credit card validation
	//Source: https://www.geeksforgeeks.org/program-credit-card-number-validation/
    private static boolean isValid(long number)
    {
       return (getSize(number) >= 13 &&
               getSize(number) <= 16) &&
               (prefixMatched(number, 4) ||
                prefixMatched(number, 5) ||
                prefixMatched(number, 37) ||
                prefixMatched(number, 6)) &&
              ((sumOfDoubleEvenPlace(number) +
                sumOfOddPlace(number)) % 10 == 0);
    }
    private static int sumOfDoubleEvenPlace(long number)
    {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 2; i >= 0; i -= 2)
            sum += getDigit(Integer.parseInt(num.charAt(i) + "") * 2);
         
        return sum;
    }
    private static int getDigit(int number)
    {
        if (number < 9)
            return number;
        return number / 10 + number % 10;
    }
    private static int sumOfOddPlace(long number)
    {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 1; i >= 0; i -= 2)
            sum += Integer.parseInt(num.charAt(i) + "");       
        return sum;
    }
    private static boolean prefixMatched(long number, int d)
    {
        return getPrefix(number, getSize(d)) == d;
    }
    private static int getSize(long d)
    {
        String num = d + "";
        return num.length();
    }
    private static long getPrefix(long number, int k)
    {
        if (getSize(number) > k) {
            String num = number + "";
            return Long.parseLong(num.substring(0, k));
        }
        return number;
    }
    
    private static boolean checkCSC(String csc) {
    	return csc.matches("^\\d{3}$");
    }

	public Scene getScene() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BookingGUI.fxml"));
		loader.setController(this);
		try {
			Parent root = loader.load();
			Scene scene = new Scene(root, 1280, 800);
			return scene;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setInformation(Controller control) {
		this.control = control;
		roomnum = String.valueOf(control.getRoom().getRoomNumber());
		label_room.setText(roomnum);
	}
	
	//Method to validate arrival and departure dates make sense
	//Checks if selected dates are before today
	//Checks if departure date is before arrival date
	private static boolean checkDate(LocalDate arrival, LocalDate departure) {
		Date now = new Date();
		String sArrival = arrival.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		String sDeparture = departure.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		Date dateDeparture = null;
		Date dateArrival = null;
		try {
			dateDeparture = new SimpleDateFormat("MM/dd/yyyy").parse(sDeparture);
			dateArrival = new SimpleDateFormat("MM/dd/yyyy").parse(sArrival);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		if(dateDeparture.compareTo(now) < 0) {
			return false;
		} else if (dateDeparture.compareTo(dateArrival) <= 0) {
			return false;
		} else if (dateArrival.compareTo(now) < 0) {
			return false;
		} else {
			return true;
		}
	}

	private void changeToSplash(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("splashgui.fxml"));
		Scene splashView = new Scene(root);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(splashView);
		window.setResizable(false);
		window.show();
	}

	private void changeToLoggedIn(ActionEvent event) throws IOException {
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		LoggedInGUI loggedin = new LoggedInGUI();
		Scene loggedInScene = loggedin.getScene();
		loggedin.setInformation(control);
		window.setScene(loggedInScene);
		window.setResizable(false);
		window.show();
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		btn_cancel.setOnAction(actionEvent -> {
			try {
				changeToSplash(actionEvent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
		btn_submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
				if (!(validate(txt_fname.getText(), txt_lname.getText(), txt_email.getText(), label_room.getText(),
						dtp_checkin.getValue(), dtp_checkout.getValue(), txt_cardnum.getText(), 
						txt_csc.getText(), dtp_expiration.getValue()))) {
					Controller.Error(primary, "Error!", "Please fill in all the required fields.");
					return;
				}
				if (!checkDate(dtp_checkin.getValue(), dtp_checkout.getValue())) {
					Controller.Error(primary, "Error!", "Please make sure the dates provided make sense.");
					return;
				}
				
				String checkin = dtp_checkin.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
				String checkout = dtp_checkout.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
				String fname = txt_fname.getText();
				String lname = txt_lname.getText();
				String email = txt_email.getText();
				String cardnum = txt_cardnum.getText();
				
				//
				if (!control.isValid(email)) {
					Controller.Error(primary, "Error!", "Please make sure the email provided is valid");
					return;
				}
				
				String stringexp = dtp_expiration.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
				int room_num = Integer.parseInt(label_room.getText());
				long longcard = 0;
				try {
					longcard = Long.parseLong(cardnum);
					if (!isValid(longcard)) {
						Controller.Error(primary, "Error!", "Please enter a valid credit card number!");
						return;
					}
				} catch(Exception e) {
					Controller.Error(primary, "Error!", "Please enter a valid credit card number!");
					return;
				}
				
				int csc = 0;
				
				try {
					csc = Integer.parseInt(txt_csc.getText());
				} catch (Exception e) {
					Controller.Error(primary, "Error!", "Please ensure CSC is a 3-digit number!");
					return;
				}
				
				if (!checkCSC(txt_csc.getText())) {
					Controller.Error(primary, "Error!", "Please ensure CSC is a 3-digit number!");
					return;
				}

				if (Account.checkAccount(fname, lname, null, email)) {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					Account user = new Account(fname, lname, null, email);
					Room room = Room.getRoomFromDB(room_num);
					Date date1 = null;
					Date date2 = null;
					Date expiration = null;
					
					try {
						date1 = sdf.parse(checkin);
						date2 = sdf.parse(checkout);
						expiration = sdf.parse(stringexp);

					} catch (ParseException e) {

						e.printStackTrace();
					}
					String temp = sdf.format(expiration);
					Booking booking = new Booking(user, room, date1, date2);

					try {
						if (booking.createBooking() == true) {
							control.setAccount(user);
							control.setArrival(date1);
							control.setDepart(date2);
							control.setBooking(booking);
							control.setRoom(Room.getRoomFromDB(room_num));
							VirtualCCProcessor vc = new VirtualCCProcessor(cardnum, temp, csc);
							control.setVcc(vc);
							int hash = control.getVcc().hashCode();
							control.getBooking().insertCCToken(hash);
							control.sendMail(email, fname, room_num, checkin, checkout, control.getBooking().getConfNum(), 'b');
							Controller.Alert(primary, "Success!", "Booking Created! An email has been sent to you with your booking details."
									+ "\n\n"
									+ "To login to our kiosks use your e-mail address and your confirmation number.");
							try {
								changeToLoggedIn(event);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							Controller.Error(primary, "Error!", "Failed to create booking");
							return;
						}
						
					} catch (SQLException | MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		});
	}

}
