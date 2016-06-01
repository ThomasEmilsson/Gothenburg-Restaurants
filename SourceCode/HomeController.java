
/**
 *  Controller Class for the Home page
 *  
 *  Draws the interface for the Home page. Pressing the Advanced Search Button opens up more options.
 *  
 *  Creates the SQL statement for what needs to be found in the database based on the users options.
 *  
 *  Gets all the food options and foodtypes from the database and moves them into the drop down menu lists
 *  
 *  @author thomasemilsson
 */

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.scene.control.Separator;

public class HomeController extends Home {

	@FXML // fx:id="button_EnterPostalCode"
	private Button button_EnterPostalCode; // Value injected by FXMLLoader

	@FXML // fx:id="textField_EnterPostalCode"
	private TextField textField_EnterPostalCode; // Value injected by FXMLLoader

	@FXML // fx:id="seperatorTop"
	private Separator seperatorTop; // Value injected by FXMLLoader

	@FXML // fx:id="seperatorBottom"
	private Separator seperatorBottom; // Value injected by FXMLLoader

	@FXML // fx:id="buttonOwner"
	private Button buttonOwner; // Value injected by FXMLLoader

	@FXML // fx:id="buttonAboutUs"
	private Button buttonAboutUs; // Value injected by FXMLLoader

	@FXML // fx:id="buttonFAQ"
	private Button buttonFAQ; // Value injected by FXMLLoader

	@FXML // fx:id="advancedSearch"
	private ToggleButton advancedSearch; // Value injected by FXMLLoader

	@FXML // fx:id="InvalidPostalCode"
	private Label InvalidPostalCode; // Value injected by FXMLLoader

	@FXML // fx:id="Pane_AdvancedSearch"
	private Pane Pane_AdvancedSearch; // Value injected by FXMLLoader

	@FXML // fx:id="includePostalCode"
	private CheckBox includePostalCode; // Value injected by FXMLLoader

	@FXML // fx:id="showMostPopular"
	private CheckBox showMostPopular; // Value injected by FXMLLoader

	@FXML // fx:id="showHighRating"
	private CheckBox showHighRating; // Value injected by FXMLLoader

	@FXML // fx:id="button_advSearch"
	private Button button_advSearch; // Value injected by FXMLLoader

	@FXML // fx:id="foodTypeList"
	private ChoiceBox<String> foodTypeList; // Value injected by FXMLLoader

	@FXML // fx:id="optionList"
	private MenuButton optionList; // Value injected by FXMLLoader

	private ObservableList<String> datalist = FXCollections.observableArrayList();

	private ObservableList<CheckMenuItem> optionDataList = FXCollections.observableArrayList();

	// When true allow user to move to the next page
	static boolean nextPage = true;

	@FXML
	void goToAboutUs(ActionEvent event) throws IOException {
		Parent homeParent = FXMLLoader.load(getClass().getResource(ABOUT_US));
		Scene homeScene = new Scene(homeParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.hide();
		stage.setScene(homeScene);
		stage.show();
	}

	@FXML
	void goToFAQ(ActionEvent event) throws IOException {
		Parent homeParent = FXMLLoader.load(getClass().getResource(FAQ));
		Scene homeScene = new Scene(homeParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.hide();
		stage.setScene(homeScene);
		stage.show();
	}

	@FXML
	void goToOwner(ActionEvent event) throws IOException {
		Parent homeParent = FXMLLoader.load(getClass().getResource(SIGN_IN));
		Scene homeScene = new Scene(homeParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.hide();
		stage.setScene(homeScene);
		stage.show();
	}

	/**
	 * When Search Button is pressed check whether the entered information is
	 * correct. foodType keeps track of the foodType selected nextPage checks
	 * whether user is allowed to move forward SQL_advancedSearch is passed on
	 * as the final SQL search statement for the next page.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void goToRestaurantList(ActionEvent event) throws IOException {

		nextPage = true;

		// used to check if anything is selected in the options menu
		boolean option = false;

		// Find the selected food type and save as string.
		String foodType = foodTypeList.getSelectionModel().getSelectedItem().toString();



		postalCode = textField_EnterPostalCode.getText();

		// Check if an option is selected
		
		for (int i = 1; i < optionDataList.size(); i++) {
			if (optionDataList.get(i - 1).isSelected()) {
				option = true;
				break;
			}
		}

		// Check for all cases where going to the next page fails

		if (advancedOn == false) {
			if (postalCode.length() != 5) {
				nextPage = false;
			} else {
				// STANDARD SEARCH where postalCode length > 0
				standardSearch = "select restaurantid, name from Restaurants where postalcode = " + postalCode;
			}
		} else {
			if (includePostalCode.isSelected() == true && postalCode.length() != 5) {
				nextPage = false;
			}

			else if (includePostalCode.isSelected() == false && showHighRating.isSelected() == false
					&& showMostPopular.isSelected() == false && foodType == "No Food Type" && option == false) {
				nextPage = false;
			}
		}

		// =========================================================================
		// =========================================================================
		// CREATE ADVANCED SQL SEARCH STRING

		if (advancedOn) {

			SQL_advancedSearch = "Select * from Restaurants ";

			if (foodType != "No Food Type") {
				SQL_advancedSearch += "JOIN restaurantfoodtypes ON restaurants.restaurantid = restaurantfoodtypes.restaurantid ";
			}

			if (option) {
				SQL_advancedSearch += "JOIN restaurantoptions ON restaurants.restaurantid = restaurantoptions.restaurantid ";
			}

			if (showHighRating.isSelected()) {
				SQL_advancedSearch += "JOIN avgrating ON restaurants.restaurantid = avgrating.restaurantid ";
			}

			// Necessary to prevent errors after Join
			SQL_advancedSearch += " WHERE 1=1 ";

			if (foodType != "No Food Type") {

				int foodID = foodTypeList.getSelectionModel().getSelectedIndex();
				SQL_advancedSearch += "AND foodid = " + foodID + " ";
			}

			if (option) {

				for (int i = 1; i < optionDataList.size(); i++) {
					if (optionDataList.get(i - 1).isSelected()) {
						SQL_advancedSearch += "AND optionid = " + i + " ";

					}
				}
			}

			if (includePostalCode.isSelected()) {
				SQL_advancedSearch += "AND postalcode = " + postalCode + " ";
			}

			SQL_advancedSearch += "GROUP BY Restaurants.RestaurantID ";

			if (showHighRating.isSelected()) {
				SQL_advancedSearch += "ORDER BY average DESC ";
			}

			if (showMostPopular.isSelected()) {
				SQL_advancedSearch += "ORDER BY pageviews DESC ";
			}


			// CREATE ADVANCED SQL SEARCH STRING
			// =========================================================================
			// =========================================================================

		}

		// If allowed, go on to the next page
		if (nextPage == true) {

			Parent homeParent = FXMLLoader.load(getClass().getResource(RESTAURANT_LIST));
			Scene homeScene = new Scene(homeParent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.hide();
			stage.setScene(homeScene);
			stage.show();
		}

		else {
			// Display Error Message
			InvalidPostalCode.setVisible(true);
		}
	}

	/**
	 * Keep track of the advancedSearch switch to remove or add the pane. Also
	 * passes the boolean forward to choose from the standard SQL statement or
	 * the Advanced SQL statement
	 * 
	 * @param event
	 */

	@FXML
	private void showAdvancedSearch(ActionEvent event) {

		if (advancedOn) {
			Pane_AdvancedSearch.setVisible(false);
			advancedOn = false;
		} else {
			Pane_AdvancedSearch.setVisible(true);
			advancedOn = true;
		}
	}

	/**
	 * Get all the existing options from the database and put them into a
	 * checkmenu. Multiple options can be selected at once.
	 */

	private void getOptions() {

		optionList.getItems().clear();
		optionList.setText("Select Options");

		String sql = "SELECT options from Options";
		try {
			DBConnection db = new DBConnection();
			ResultSet rs = db.select(sql);
			while (rs.next()) {
				CheckMenuItem menuitem = new CheckMenuItem(rs.getString("options"));
				optionDataList.add(menuitem);
			}
			optionList.getItems().addAll(optionDataList);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get all the existing Food Types from the database and put them into a
	 * choicebox. Only one food types can be selected
	 */

	private void getFoodTypes() {
		String sql = "SELECT FOODTYPE FROM FOODTYPES";
		DBConnection db = new DBConnection();
		ResultSet rs = db.select(sql);

		// Add Option to choose no food type
		datalist.add("No Food Type");
		foodTypeList.getItems().clear();
		try {
			while (rs.next()) {
				datalist.add(rs.getString("foodtype"));
			}
			foodTypeList.getItems().addAll(datalist);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Sets Default selection is No Food Type
		foodTypeList.getSelectionModel().selectNext();
	}

	/**
	 * Allows only of two checkboxes to be selected at once; Checkbox One = Show
	 * Highest Rating Checkbox Two = Show Most Viewed
	 */

	private void limitSelection() {
		showHighRating.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (showHighRating.isSelected()) {
					showMostPopular.setSelected(!newValue);
				}
			}
		});

		showMostPopular.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (showMostPopular.isSelected()) {
					showHighRating.setSelected(!newValue);
				}
			}
		});
	}

	/**
	 * Prevents user from entering more than 5 numeric values into the
	 * textField_EnterPostal Code Using regex to only allow numeric values.
	 */
	private void limitTextField() {
		textField_EnterPostalCode.lengthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					if (textField_EnterPostalCode.getText().length() >= 5) {
						textField_EnterPostalCode.setText(textField_EnterPostalCode.getText().substring(0, 5));
					}
				}
			}
		});

		textField_EnterPostalCode.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// check when a non-numeric value is added
				if (!newValue.matches("^[0-9]")) {
					// remove all non-numeric characters from textField
					textField_EnterPostalCode.setText(newValue.replaceAll("[^0-9]", ""));
				}
			}
		});
	}

	/**
	 * Runs whenever Home is opened. Sets Advanced Pane to not be visible and
	 * changes the style of the advanced search Pane as well as the error message
	 * that gets displayed if an invalid postal code is entered.
	 * 
	 * Runs methods to get all options and food types and display them in the drop
	 * down menu lists
	 */

	@FXML
	public void initialize() {

		getOptions();
		getFoodTypes();
		limitSelection();
		limitTextField();

		advancedOn = false;

		InvalidPostalCode.setStyle(
				"-fx-background-color: rgba(0, 0, 0, 0.75); -fx-background-radius: 10; -fx-border-color: WHITE;");

		Pane_AdvancedSearch.setStyle(
				"-fx-background-color: rgba(0, 0, 0, 0.75); -fx-background-radius: 10; -fx-border-color: WHITE;");

	}
}
