package guiclasses;

import java.io.IOException;
import java.net.URL;
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
import javafx.stage.Stage;

public class splashGUI extends Application implements Initializable{

	@FXML
	private Button bookButton;

	@FXML
	private Button loginButton;

	@Override
	public void start(Stage primary) throws Exception {
		Parent guiView = FXMLLoader.load(getClass().getResource("/guiclasses/splashgui.fxml"));
		Scene scene = new Scene(guiView);
		primary.setTitle("uCheckIn");
		primary.setScene(scene);
		primary.setResizable(false);
		primary.show();
	}
	

	private void changeToBrowser(ActionEvent event) throws IOException {
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		roomBrowserGUI rb = new roomBrowserGUI();
		Scene rbs = rb.getScene();
		window.setScene(rbs);
		window.setResizable(false);
		window.show();
	}


	private void changeToLogin(ActionEvent event) throws IOException {
		Parent view = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene loginView = new Scene(view);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(loginView);
		window.setResizable(false);
		window.show();
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		

		bookButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					changeToBrowser(event);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});

		loginButton.setOnAction(actionEvent -> {
			try {
				changeToLogin(actionEvent);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
