/**
 * @author Amanda
 * 
 * Controller class for the Restaurant List Page
 * 
 */

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RestaurantListController extends Home implements Initializable {

	@FXML
	private Button buttonBackToHome;

	@FXML
	private Button buttonGoHome;

	@FXML
	private ScrollPane sPane; // Make background invisible

	@FXML
	private VBox restBox;

	public void getRestaurants() {

		String sql;

		if (advancedOn) {
			sql = SQL_advancedSearch;

		} else {
			sql = standardSearch;
		}

		try {
			// Connect to Database
			DBConnection db = new DBConnection();
			ResultSet rs = db.select(sql);
			
			// Add restaurants while there are matches
			while (rs.next()) {

				// Create new buttons
				Button button = new Button();
				button.setText(rs.getString("NAME"));
				button.setMinSize(200, 58);

				// Create temporary tooltip to store the id of the restaurant
				Tooltip tooltip = new Tooltip();
				tooltip.setText(rs.getString("RESTAURANTID"));
				button.setTooltip(tooltip);
				
				// Add restaurants to a VBox
				restBox.getChildren().add(button);
				
				// Add individual event handlers for each button that is created.
				button.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						Parent homeParent;

						// Save ID
						String ID = tooltip.getText();
						restaurantID = Integer.parseInt(ID);

						try {
							homeParent = FXMLLoader.load(getClass().getResource(RESTAURANT));
							Scene homeScene = new Scene(homeParent);
							Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
							stage.hide();
							stage.setScene(homeScene);
							stage.show();

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
				// Remove tooltip
				button.setTooltip(null);
			}

		}

		catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	@FXML
	void goHome(ActionEvent event) throws IOException {
		Parent homeParent = FXMLLoader.load(getClass().getResource(HOME));
		Scene homeScene = new Scene(homeParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.hide();
		stage.setScene(homeScene);
		stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		getRestaurants();

	}
}
