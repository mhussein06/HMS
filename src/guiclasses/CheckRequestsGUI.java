package guiclasses;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import objectclasses.*;

public class CheckRequestsGUI implements Initializable {
	@FXML
	private TableView<CheckTable> table_checkreq;
	@FXML
	private TableColumn<CheckTable, Integer> col_confnum;
	@FXML
	private TableColumn<CheckTable, Integer> col_reqID;
	@FXML
	private TableColumn<CheckTable, String> col_reqname;
	@FXML
	private TableColumn<CheckTable, Integer> col_room;
	@FXML
	private TableColumn<CheckTable, String> col_price;
	@FXML
	private TableColumn<CheckTable, Date> col_time;
	@FXML
	private TableColumn<CheckTable, String> col_status;
	@FXML
	private TableColumn<CheckTable, CheckBox> col_select;
	@FXML
	private Button btn_complete;
	@FXML
	private Button btn_back;
	private boolean select;
	
	private Controller control = Controller.getInstance();
	
	
	ObservableList<CheckTable> checklist = FXCollections.observableArrayList();
	
	public Scene getScene() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CheckRequests.fxml"));
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
	
	private void changeToEmpLoggedIn(ActionEvent event) throws IOException {
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		LoggedInAdminGUI loggedin = new LoggedInAdminGUI();
		Scene loggedInScene = loggedin.getScene();
		loggedin.setInformation(control);
		window.setScene(loggedInScene);
		window.setResizable(false);
		window.show();
	}
	
	private void refresh(ActionEvent event) throws IOException {
		Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
		CheckRequestsGUI cr = new CheckRequestsGUI();
		Scene crs = cr.getScene();
		cr.setInformation(control);
		primary.setScene(crs);
		primary.setResizable(false);
		primary.show();
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		btn_complete.setOnAction(new EventHandler<ActionEvent>( ) {

			@Override
			public void handle(ActionEvent event) {
				Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
				select = false;
				checklist.forEach((check) -> {
					if (check.getSelect() != null && check.getSelect().isSelected()) {
						select = true;
						Request.setRequestItemComplete(check.getReqID());
					}
				});
				if (select) {
					try {
						refresh(event);
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else {
					Controller.Error(primary, "Error!", "Make sure to select requests to set as complete!");
					return;
				}
			}
			
		});
		
		btn_back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				try {
					changeToEmpLoggedIn(event);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
		try {
			String query = "SELECT b.room_num, r.conf_ID, ri.reqitem_ID, i.item_Name, i.item_price, r.req_DateTime, ri.fulfilled FROM Request r \n" + 
					"INNER JOIN RequestItems ri ON r.req_ID = ri.req_ID\n" + 
					"INNER JOIN Items i ON ri.item_ID = i.item_ID "
					+ "INNER JOIN Booking b on r.conf_ID = b.conf_ID "
					+ "ORDER BY r.req_DateTime ASC";
			ResultSet rs = Controller.connection().executeQuery(query);
			while(rs.next()) {
				String price = null;
				if (rs.getFloat("item_price") != 0) {
					price = "$" + String.valueOf(rs.getFloat("item_price"));
				}
				String temp = null;
				if (rs.getInt("fulfilled") == 0) {
					temp = "Pending";
					checklist.add(new CheckTable(rs.getInt("conf_ID"), rs.getInt("reqitem_ID"), 
							rs.getString("item_Name"), rs.getInt("room_num"), price, 
							rs.getDate("req_DateTime"), temp, new CheckBox()));
				} else if (rs.getInt("fulfilled") == 1) {
					temp = "Completed";
					checklist.add(new CheckTable(rs.getInt("conf_ID"), rs.getInt("reqitem_ID"), 
							rs.getString("item_Name"), rs.getInt("room_num"), price, 
							rs.getDate("req_DateTime"), temp, null));
				}
				
			}
			rs.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		col_confnum.setCellValueFactory(new PropertyValueFactory<>("confNum"));
		col_reqID.setCellValueFactory(new PropertyValueFactory<>("reqID"));
		col_reqname.setCellValueFactory(new PropertyValueFactory<>("name"));
		col_room.setCellValueFactory(new PropertyValueFactory<> ("room"));
		col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
		col_time.setCellValueFactory(new PropertyValueFactory<>("time"));
		col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
		col_select.setCellValueFactory(new PropertyValueFactory<>("select"));
		table_checkreq.setItems(checklist);
	}
	
}
