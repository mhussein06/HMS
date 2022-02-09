package guiclasses;

import java.io.IOException;

import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.stage.Stage;
import objectclasses.Controller;

public class LoggedInAdminGUI implements Initializable{
	
	@FXML
	private Label label_welcome;
	@FXML
	private Button btn_checkrequests;
	@FXML
	private Button btn_add;
	@FXML
	private Button btn_createbooking;
	@FXML
	private Button btn_logout;
	
	private Controller control = Controller.getInstance();
	private boolean isAdmin;
	
	
	private void changeToSplash(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("splashgui.fxml"));
		Scene splashView = new Scene(root);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(splashView);
		window.setResizable(false);
		window.show();
	}
	
	public void setInformation(Controller control) {
		this.control = control;
		label_welcome.setText("Welcome " + control.getEmployee().getFName() + "!");
		isAdmin = (control.getEmployee().isAdmin()) ? true: false;
		if (!isAdmin) {
			btn_add.setVisible(false);
			btn_add.setDisable(true);
		}
	}
	
	public Scene getScene() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoggedInAdmin.fxml"));
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
	
	private void changeToBrowser(ActionEvent event) throws IOException {
		Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
		RoomBrowserAdmin rb = new RoomBrowserAdmin();
		Scene rbs = rb.getScene();
		rb.setInformation(control);
		primary.setScene(rbs);
		primary.setResizable(false);
		primary.show();	
	}
	
	private void changeToCheckRequests(ActionEvent event) throws IOException {
		Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
		CheckRequestsGUI cr = new CheckRequestsGUI();
		Scene crs = cr.getScene();
		cr.setInformation(control);
		primary.setScene(crs);
		primary.setResizable(false);
		primary.show();
	}
	
	private void changeToAddEmployee(ActionEvent event) throws IOException {
		Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
		AddEmployeeGUI ae = new AddEmployeeGUI();
		Scene aes = ae.getScene();
		ae.setInformation(control);
		primary.setScene(aes);
		primary.setResizable(false);
		primary.show();
	}
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
				btn_checkrequests.setOnAction(new EventHandler<ActionEvent>( ) {

					@Override
					public void handle(ActionEvent event) {
						try {
							changeToCheckRequests(event);
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
					
				});
				btn_add.setOnAction(new EventHandler<ActionEvent>( ) {

					@Override
					public void handle(ActionEvent event) {
						try {
							changeToAddEmployee(event);
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
					
				});
				btn_createbooking.setOnAction(new EventHandler<ActionEvent>( ) {

					@Override
					public void handle(ActionEvent event) {
						try {
							changeToBrowser(event);
						} catch(Exception e) {
							e.printStackTrace();
						}
						
					}
					
				});
				btn_logout.setOnAction(new EventHandler<ActionEvent>( ) {

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
				
		
	}

	

}
