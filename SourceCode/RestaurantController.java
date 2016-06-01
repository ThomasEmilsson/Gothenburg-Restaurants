
/**
 * @author Axel
 * 
 * Controller Class for the Restaurant Page
 * 
 */
import java.io.IOException;
import java.net.URL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.sql.*;
import java.util.ResourceBundle;

public class RestaurantController extends Home implements Initializable {

	public static String name;
	public static String address;
	public static String city;
	public static String postalcode;
	public static String phone;
	public static String email;
	public static String website;
	public static String openinghours;
	public static String description;
	public static String avgPrice;
	public static double avgRating;

	private ObservableList<String> foodtypelist = FXCollections.observableArrayList();
	private ObservableList<String> optionlist = FXCollections.observableArrayList();

	@FXML
	private ListView<String> foodList;

	@FXML
	private ListView<String> optionList;

	@FXML
	private Label lName;

	@FXML
	private Label lDescription;

	@FXML
	private Label lAdress;

	@FXML
	private Label lPostalcode;

	@FXML
	private Label lCity;

	@FXML
	private Label lPhone;

	@FXML
	private Label lEmail;

	@FXML
	private Label lWebsite;

	@FXML
	private Label lOpeningHours;

	@FXML
	private Label lAvgPrice;

	@FXML
	private Label lRating;

	@FXML
	private Button reviewsButton;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			pageView();
			GetInfo();
			getFoodTypes();
			getOptions();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		lName.setText(name);
		lName.setCenterShape(true);
		lDescription.setText(description);
		lAdress.setText(address);
		lPostalcode.setText(postalcode);
		lCity.setText(city);
		lPhone.setText(phone);
		lEmail.setText(email);
		lWebsite.setText(website);
		lOpeningHours.setText("Open " + openinghours);
		lAvgPrice.setText("Price: " + avgPrice + " SEK");
		lRating.setText("Rating: " + String.format("%.2f", avgRating));
		lDescription.wrapTextProperty();

	}

	public void GetInfo() throws SQLException {
		try {
			DBConnection db = new DBConnection();
			String sql = "select * from restaurants where restaurantid = " + Home.restaurantID + ";";
			ResultSet rs = db.select(sql);
			name = rs.getString("name");
			address = rs.getString("streetaddress");
			city = rs.getString("city");
			postalcode = rs.getString("postalcode");
			phone = rs.getString("phone");
			email = rs.getString("email");
			website = rs.getString("homepage");
			openinghours = rs.getString("openinghours");
			description = rs.getString("description");
			int Average = rs.getInt("averageprice");
			avgPrice = Integer.toString(Average);
			rs.close();

			String sql1 = "select * from avgrating where restaurantid = " + Home.restaurantID + ";";
			ResultSet rs1 = db.select(sql1);
			avgRating = rs1.getDouble("average");
			rs1.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void getFoodTypes() { // Since foodtypes are not edited from within
									// the application we can be sure the ids
									// are ordered from 1-n
		String sql = "select ft.Foodtype from foodtypes FT INNER JOIN restaurantfoodtypes RTF on FT.foodid = RTF.Foodid where RTF.restaurantid ="
				+ Home.restaurantID + ";"; // So no need to actually look at the
											// foodids, it will always be
											// index-1 in menu
		try {
			DBConnection db = new DBConnection();
			ResultSet rs = db.select(sql);
			while (rs.next()) { // Get all foodtypes from database and populate
								// the menu
				String foodtype = rs.getString("Foodtype");
				foodtypelist.add(foodtype);
			}
			foodList.setItems(foodtypelist);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void getOptions() {
		String sql = "select o.Options from options O INNER JOIN restaurantoptions RO ON O.optionID = RO.optionID where RO.restaurantid ="
				+ Home.restaurantID + ";";
		try {
			DBConnection db = new DBConnection();
			ResultSet rs = db.select(sql);
			while (rs.next()) { // Get all foodtypes from database and populate
								// the menu
				String foodtype = rs.getString("Options");
				optionlist.add(foodtype);
			}
			optionList.setItems(optionlist);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void pageView() {
		int curView;
		int newView = 0;
		String sql = "select pageviews from restaurants where restaurantid = " + Home.restaurantID;
		try {
			DBConnection db = new DBConnection();
			ResultSet rs = db.select(sql);
			curView = rs.getInt("pageviews");
			newView = curView + 1;
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String sql1 = "update restaurants set pageviews = '" + newView + "' where restaurantid = " + Home.restaurantID;

		DBConnection db = new DBConnection();
		db.insert(sql1);

	}

	@FXML
	private AnchorPane Restaurant;

	@FXML
	private Button buttonBackToRestaurantList;

	@FXML
	private Button buttonGoHome;

	@FXML
	void goHome(ActionEvent event) throws IOException {
		Parent homeParent = FXMLLoader.load(getClass().getResource(HOME));
		Scene homeScene = new Scene(homeParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.hide();
		stage.setScene(homeScene);
		stage.show();
	}

	@FXML
	void goToRestaurantList(ActionEvent event) throws IOException {
		Parent homeParent = FXMLLoader.load(getClass().getResource(RESTAURANT_LIST));
		Scene homeScene = new Scene(homeParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.hide();
		stage.setScene(homeScene);
		stage.show();
	}

	@FXML
	void goReview(ActionEvent event) throws IOException {
		Parent homeParent = FXMLLoader.load(getClass().getResource(REVIEWS));
		Scene homeScene = new Scene(homeParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.hide();
		stage.setScene(homeScene);
		stage.show();
	}

}
