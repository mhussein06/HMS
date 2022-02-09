package guiclasses;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import objectclasses.Controller;
import objectclasses.Employee;

public class EmpLogin implements Initializable{

	@FXML
	private Button loginButton;
	@FXML
	private Button cancelButton;

	@FXML
	private TextField userField;

	@FXML
	private PasswordField passField;

	Controller control = Controller.getInstance();
	
	private void changeToLogin(ActionEvent event) throws IOException {
		Parent view = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene loginView = new Scene(view);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(loginView);
		window.setResizable(false);
		window.show();
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


	private boolean validate(String userName, String password) {
		if (userName.equals("") || password.equals("")) {
			return false;
		}
		return true;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
				String user = userField.getText();
				String pass = passField.getText();
				if (!validate(user, pass)) {
					Controller.Error(primary, "Employee Login", "Please provide a username and password.");
					return;
				} else {
					try {
						String query = "select * from employee where username = '" + user + "';";
						ResultSet result = Controller.connection().executeQuery(query);
						if (result.next()) {
							String fName = result.getString("emp_Fname");
							String lName = result.getString("emp_Lname");
							boolean admin = (result.getInt("admin") == 1) ? true: false;
							String hashedPass = result.getString("password");
							if (!Controller.checkPass(pass, hashedPass)) {
								Controller.Error(primary, "Employee Login", "Password is incorrect!");
								return;
							}
							Employee temp = new Employee(fName, lName, "", "", user, admin);
							control.setEmployee(temp);
							try {
								changeToEmpLoggedIn(event);
							} catch(Exception e) {
								e.printStackTrace();
							}
						} else {
							Controller.Error(primary, "Employee Login", "Sorry, username does not exist!");
							return;
						}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
				}
			}

		});

		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					changeToLogin(event);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

}
