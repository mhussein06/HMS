package guiclasses;

import java.io.IOException;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import objectclasses.Controller;
import objectclasses.Request;


public class MakeRequestGUI implements Initializable{
	
	@FXML
	private TableView<ServiceTable> table_room;
	@FXML
	private TableView<FoodTable> table_food;
	@FXML
	private Label label_welcome;
	@FXML
	private Button btn_back;
	@FXML
	private Button btn_submit;
	@FXML
	private TableColumn<ServiceTable, String> col_namefree;
	@FXML
	private TableColumn<FoodTable, String> col_namefood;
	@FXML
	private TableColumn<FoodTable, String> col_price;
	@FXML
	private TableColumn<ServiceTable, CheckBox> col_select;
	@FXML
	private TableColumn<FoodTable, CheckBox> col_selectfood;
	@FXML
    private Button btn_cancel;
    @FXML
    private TableView<RequestTable> table_request;
    @FXML
    private TableColumn<RequestTable, String> col_reqID;
    @FXML
    private TableColumn<RequestTable, String> col_request;
    @FXML
    private TableColumn<RequestTable, Date> col_time;
    @FXML
    private TableColumn<RequestTable, String> col_status;
    @FXML
    private TableColumn<RequestTable, CheckBox> col_cancel;
    @FXML
    private Label label_total;
    @FXML 
    private Button btn_refresh;
	private Controller control = Controller.getInstance();
	private static float totalprice;
	private static int confNum;
	
	
	ObservableList<ServiceTable> servicelist = FXCollections.observableArrayList();
	ObservableList<FoodTable> foodlist = FXCollections.observableArrayList();
	ObservableList<RequestTable> requestlist = FXCollections.observableArrayList();
	
	
	public Scene getScene() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MakeRequest.fxml"));
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
		label_welcome.setText("Make a Request for Room " + control.getRoom().getRoomNumber());
		label_total.setText("Total: $" + totalprice);
		MakeRequestGUI.confNum = control.getBooking().getConfNum();
		MakeRequestGUI.totalprice = control.getAmountOwed();
	}

	
	private void refresh(ActionEvent event) throws IOException {
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		MakeRequestGUI mr = new MakeRequestGUI();
		Scene mrs = mr.getScene();
		mr.setInformation(control);
		window.setScene(mrs);
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
		btn_submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
				// TODO Auto-generated method stub
				ArrayList<Integer> items = new ArrayList<Integer>();
				servicelist.forEach((service) -> {
					if(service.getSelect().isSelected()) {
						String query = "Select * FROM Items WHERE item_Name = '" + service.getName() + "';";
						try {
							ResultSet result = Controller.connection().executeQuery(query);
							result.next();
							items.add(result.getInt("item_ID"));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				foodlist.forEach((food) -> {
					if(food.getSelect().isSelected()) {
						String query = "Select * FROM Items WHERE item_Name = '" + food.getName() + "';";
						try {
							ResultSet result = Controller.connection().executeQuery(query);
							result.next();
							items.add(result.getInt("item_ID"));
							totalprice += result.getFloat("item_price");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
				int reqID = Request.createRequest(confNum);
				items.forEach((item) -> {
					Request.createRequestItem(reqID, item);
				});
				control.setAmountOwed(totalprice);
				Controller.Alert(primary, "Success!", "The selected requests have been submitted.");
				try {
					refresh(event);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		btn_cancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Stage primary = (Stage) ((Node) event.getSource()).getScene().getWindow();
				requestlist.forEach((request) -> {
					if((request.getSelect() != null) & (request.getSelect().isSelected())) {
						String q = "SELECT * FROM RequestItems ri INNER JOIN Items i ON ri.item_ID = i.item_ID "
								+ "WHERE reqitem_ID = " + request.getReqItemID();
						String query = "DELETE FROM RequestItems WHERE reqitem_ID = " + request.getReqItemID();
						try {
							ResultSet rs = Controller.connection().executeQuery(q);
							rs.next();
							int i = rs.getInt("req_ID");
							float price = rs.getFloat("item_price");
							rs.close();
							Controller.connection().executeUpdate(query);
							totalprice -= price;
							String q2 = "SELECT * FROM RequestItems WHERE req_ID = " + i;
							ResultSet rs2 = Controller.connection().executeQuery(q2);
							if (!rs2.next()) {
								Request.deleteRequest(i);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				control.setAmountOwed(totalprice);
				Controller.Alert(primary, "Success!", "The selected requests have been canceled.");
				try {
					refresh(event);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		btn_back.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					changeToLoggedIn(event);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		
		try {
			
			String query1 = "SELECT * FROM Items WHERE item_price = 0";
			ResultSet rs = Controller.connection().executeQuery(query1);
			while(rs.next()) {
				servicelist.add(new ServiceTable(rs.getString("item_Name"), new CheckBox())); 
			}
			rs.close();
			table_food.setPlaceholder(null);
			String query2 = "SELECT * FROM Items WHERE item_price > 0";
			ResultSet rs1 = Controller.connection().executeQuery(query2);
			while(rs1.next()) {
				String price = String.format("%.2f",rs1.getFloat("item_price"));
				foodlist.add(new FoodTable(rs1.getString("item_Name"), "$" + price, new CheckBox()));
			}
			rs1.close();
			String query3 = "SELECT r.conf_ID, ri.reqitem_ID, r.req_ID, i.item_Name, r.req_DateTime, ri.fulfilled FROM Request r " + 
					"INNER JOIN RequestItems ri ON r.req_ID = ri.req_ID " + 
					"INNER JOIN Items i ON i.item_ID = ri.item_ID " + 
					"WHERE conf_ID = " + confNum + " ORDER BY ri.fulfilled";
			System.out.println(confNum);
			ResultSet rs2 = Controller.connection().executeQuery(query3);
			while(rs2.next()) {
				String temp = null;
				if (rs2.getInt("fulfilled") == 0) {
					temp = "Pending";
					requestlist.add(new RequestTable(String.valueOf(rs2.getInt("reqitem_ID")), rs2.getString("item_Name"), rs2.getDate("req_DateTime"), temp, new CheckBox())); 
				} else if (rs2.getInt("fulfilled") == 1) {
					temp = "Completed";
					requestlist.add(new RequestTable(String.valueOf(rs2.getInt("reqitem_ID")), rs2.getString("item_Name"), rs2.getDate("req_DateTime"), temp, null)); 
				}
				
			}
			rs2.close();
		
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		col_reqID.setCellValueFactory(new PropertyValueFactory<>("reqItemID"));
		col_request.setCellValueFactory(new PropertyValueFactory<>("name"));
		col_time.setCellValueFactory(new PropertyValueFactory<>("requestTime"));
		col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
		col_cancel.setCellValueFactory(new PropertyValueFactory<>("select"));
		col_namefree.setCellValueFactory(new PropertyValueFactory<>("name"));
		col_namefood.setCellValueFactory(new PropertyValueFactory<>("name"));
		col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
		col_select.setCellValueFactory(new PropertyValueFactory<>("select"));
		col_selectfood.setCellValueFactory(new PropertyValueFactory<>("select"));
		table_room.setItems(servicelist);
		table_food.setItems(foodlist);
		table_request.setItems(requestlist);
	}

}
