package guiclasses;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.Label;
import javafx.stage.Stage;
import objectclasses.Booking;
import objectclasses.Controller;
import objectclasses.Account;

public class LoggedInGUI implements Initializable {

	@FXML
	private Button btn_lengthstay;
	@FXML
	private Button btn_request;
	@FXML
	private Button btn_logout;
	@FXML
	private Button btn_checkin;

	@FXML
	private Button cancelBooking;

	@FXML
	private Label label_welcome;
	private static Controller control;
	private boolean isCheckedIn = false;
	
	public Scene getScene() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoggedIn.fxml"));
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
		LoggedInGUI.control = control;
		label_welcome.setText("Welcome " + control.getAccount().getFName() + "!");
		isCheckedIn = control.getBooking().isCheckedIn();
		if (control.getBooking().isCheckedIn()) {
			btn_checkin.setText("Check Out");
			cancelBooking.setVisible(false);
			cancelBooking.setDisable(true);
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
	
	private void changeToRequests(ActionEvent event) throws IOException {
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		MakeRequestGUI mr = new MakeRequestGUI();
		Scene mrs = mr.getScene();
		mr.setInformation(control);
		window.setScene(mrs);
		window.setResizable(false);
		window.show();
	}
	
	private void refresh(ActionEvent event) throws IOException {
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		LoggedInGUI loggedin = new LoggedInGUI();
		Scene loggedInScene = loggedin.getScene();
		loggedin.setInformation(control);
		window.setScene(loggedInScene);
		window.setResizable(false);
		window.show();
	}
	
	private void changeToLengthStay(ActionEvent event) throws IOException {
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		AdjustStayGUI aj = new AdjustStayGUI();
		Scene ajs = aj.getScene();
		aj.setInformation(control);
		window.setScene(ajs);
		window.setResizable(false);
		window.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		btn_request.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
				if (!control.getBooking().isCheckedIn()) {
					Controller.Alert(primary, "Error!", "Please check in first to make requests!");
					return;
				} else {
					try {
						changeToRequests(event);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		});
		btn_logout.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					changeToSplash(event);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		btn_checkin.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (!isCheckedIn) {
					Booking b = control.getBooking();
					b.checkIn();
					Date td = new Date();
					control.setArrival(td);
					Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
					Controller.Alert(primary, "Success!", "You have checked in to your room!");
					try {
						refresh(event);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// check out
					Booking b = control.getBooking();
					Date today = new Date();
					int lengthStay = Booking.getDaysBetween(control.getArrival(), today);
					Booking.setLengthStay(lengthStay, b.getConfNum());
					b.checkOut();
					if (lengthStay == 0) {
						lengthStay += 1;
					}
					float rate = control.getRoom().getRate();
					float total = rate * lengthStay;
					float hotelfee = total;
					float servicetotal = control.getAmountOwed();
					String sql = "SELECT r.conf_ID, ri.reqitem_ID, r.req_ID, i.item_Name, r.req_DateTime, "
							+ "ri.fulfilled, i.item_price FROM Request r INNER JOIN RequestItems ri "
							+ "ON r.req_ID = ri.req_ID INNER JOIN Items i ON i.item_ID = ri.item_ID "
							+ "WHERE conf_ID = " + b.getConfNum() + " ORDER BY ri.fulfilled";
					try {
						ResultSet rs = Controller.connection().executeQuery(sql);
						while (rs.next()) {
							if (rs.getInt("fulfilled") == 1) {
								servicetotal += rs.getFloat("item_price");
							}
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					total += servicetotal;
					String price = String.format("%.2f", total);
					Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
					Controller.Alert(primary, "Success!", "Days Stayed = " + lengthStay + "\n"
							+ "Services Fee = $" + servicetotal + "\n"
							+ "Hotel Stay Fee = $" + hotelfee + "\n"
											+ "Total = $" + price + "\n"
									+ "Payment has been Approved! Thank you for your stay!");
					try {
						changeToSplash(event);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}							
					
				}
			}
		});

		btn_lengthstay.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					changeToLengthStay(event);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}

		});

		cancelBooking.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
				if (!control.cancelBooking()) {
					Controller.Error(primary, "Error!", "There was an error cancelling your booking.");
					return;
				} else {
					Account acc = control.getAccount();
					Booking book = control.getBooking();
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					String checkin = sdf.format(control.getBooking().getArrival());
					String checkout = sdf.format(control.getBooking().getDeparture());
					try {
						control.sendMail(acc.getEmail(), acc.getFName(), book.getRoom().getRoomNumber(), checkin, checkout, control.getBooking().getConfNum(), 'c');
					} catch (MessagingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Controller.Alert(primary, "Cancel Booking", "Booking has been canceled");
					try {
						changeToSplash(event);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});

	}

}
