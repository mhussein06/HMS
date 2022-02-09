package guiclasses;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import objectclasses.Controller;

public class AddEmployeeGUI extends Application implements Initializable{
	
	@FXML
	private TextField txt_fname;
	@FXML
	private TextField txt_lname;
	@FXML
	private TextField txt_username;
	@FXML
	private TextField txt_password;
	@FXML
	private RadioButton rbtn_admin;
	@FXML
	private Button btn_cancel;
	@FXML
	private Button btn_submit;
	
	private Controller control = Controller.getInstance();
	
	@Override
	public void start(Stage primary) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddEmployee.fxml"));
		try {
			Parent root = loader.load();
			Scene scene = new Scene(root, 1280, 800);
			primary.setTitle("uCheckin");
			primary.setScene(scene);
			primary.show();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public Scene getScene() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddEmployee.fxml"));
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
	}
	
	private static boolean validate(String fname, String lname, String username, String password) {
		if (fname.equals("") || lname.equals("") || username.equals("") || password.equals("")) {
			return false;
		} else {
			return true;
		}
	}
	
	private void changeToEmpLoggedIn(ActionEvent event) throws IOException {
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		LoggedInAdminGUI loggedin = new LoggedInAdminGUI();
		Scene loggedInScene = loggedin.getScene();
		loggedin.setInformation(control);
		window.setScene(loggedInScene);
		window.setResizable(false);
		window.show();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		btn_cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					changeToEmpLoggedIn(event);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		btn_submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
				if (!validate(txt_fname.getText(), txt_lname.getText(), txt_username.getText(), txt_password.getText())) {
					Controller.Error(primary, "Error!", "Please fill in all the required fields!");
					return;
				}
				if (!control.checkEmpName(txt_username.getText())) {
					Controller.Error(primary, "Error!", "Username already exists! Please Pick another one.");
					return;
				}
				try {
					if (control.createEmployee(txt_fname.getText(), txt_lname.getText(), txt_username.getText(), txt_password.getText(), rbtn_admin.isSelected())) {
						Controller.Alert(primary, "Success!", "Employee Created!");
						try {
							changeToEmpLoggedIn(event);
						} catch(Exception e) {
							e.printStackTrace();
						}
					} else {
						Controller.Error(primary, "Error!", "Unable to create employee!");
						return;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		
	}



}
