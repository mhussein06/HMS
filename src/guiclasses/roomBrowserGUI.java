package guiclasses;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import objectclasses.*;

public class roomBrowserGUI implements Initializable {
	
	@FXML
	private TableView<RoomTable> table_rooms;
	@FXML
	private TableColumn<RoomTable, Integer> col_roomnum;
	@FXML
	private TableColumn<RoomTable, String> col_roomtype;
	@FXML
	private TableColumn<RoomTable, String> col_price;
	@FXML
	private TableColumn<RoomTable, CheckBox> col_select;
	@FXML
	private Button btn_back;
	@FXML
	private Button btn_select;
	@FXML 
	private VBox vbox;
	@FXML 
	private Button btn_refresh;
	
	//Controller object to pass on data
	Controller control = Controller.getInstance();
	//Count variable to hold # of rooms selected
	private static int count = 0;
	//Index variable to indicate room selected from room list
	private static int index = 0;
	
	private ObservableList<RoomTable> roomlist = FXCollections.observableArrayList();


    public Scene getScene() {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("roomBrowser.fxml"));
		try {
			Parent root = loader.load();
			Scene scene = new Scene(root, 1280, 800);
			return scene;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

    private void changeToSplash(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("splashgui.fxml"));
		Scene splashView = new Scene(root);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(splashView);
		window.setResizable(false);
		window.show();
	}

    private void changeToBooking(ActionEvent event) throws IOException {
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		BookingGUI bg = new BookingGUI();
		Scene bgs = bg.getScene();
		bg.setInformation(control);
		window.setScene(bgs);
		window.setResizable(false);
		window.show();
	}
    
    private void refresh(ActionEvent event) throws IOException {
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		roomBrowserGUI rb = new roomBrowserGUI();
		Scene rbs = rb.getScene();
		window.setScene(rbs);
		window.setResizable(false);
		window.show();
	}
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		//Set values for table cells
				col_roomnum.setCellValueFactory(new PropertyValueFactory<>("roomNum"));
				col_roomtype.setCellValueFactory(new PropertyValueFactory<>("roomType"));
				col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
				col_select.setCellValueFactory(new PropertyValueFactory<>("select"));
				table_rooms.setItems(roomlist);
		
		//Populate roomlist with all available rooms from database
		try {
			String query = "SELECT room_num, r.roomType_ID, room_status, rate FROM Room r INNER JOIN RoomType rt\n" + 
					"ON r.roomType_ID = rt.roomType_ID WHERE room_status = 0;";
			ResultSet rs = Controller.connection().executeQuery(query);
			while(rs.next()) {
				String price = "$" + String.format("%.2f",rs.getFloat("rate"));
				int roomNum = rs.getInt("room_num");
				String roomType = rs.getString("roomType_ID");
				roomlist.add(new RoomTable(roomNum, roomType, price, new CheckBox()));
			} 
		} catch(Exception e) {
			e.printStackTrace();
		}
		

		btn_back.setOnAction(actionEvent -> {
			try {
				changeToSplash(actionEvent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
		

		btn_refresh.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					refresh(event);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		

		btn_select.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
				
				//Ensure only one room is selected
				boolean boo = false;
				roomlist.forEach((room) -> {
		    		if (room.getSelect().isSelected()) {
		    			count += 1;
		    			index = roomlist.indexOf(room);
		    		}
		    	});
				boo = (count == 1) ? true: false;
		 
				if (!boo) {
					Controller.Error(primary, "Error!", "Please make sure one room is selected.");
					count = 0;
					return;
				}

				int roomnum = roomlist.get(index).getRoomNum();
				control.setRoom(Room.getRoomFromDB(roomnum));

				try {
					changeToBooking(event);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
	}

}
