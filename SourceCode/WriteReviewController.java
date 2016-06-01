/**
 * @author Axel
 * 
 * Controller class for the Write Review Page
 */
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WriteReviewController extends Home {
	
	@FXML
    private Label saved;

	@FXML
	private RadioButton one;

	@FXML
	private RadioButton two;

	@FXML
	private RadioButton three;

	@FXML
	private RadioButton four;

	@FXML
	private RadioButton five;

	@FXML
	private AnchorPane WriteReview;

	@FXML
	private Button buttonBackToHome;

	@FXML
	private Button buttonGoHome;
	
    @FXML
    private Button Save;
    
    @FXML
    private Button ToRestaurant;

	@FXML
    private TextField email;

    @FXML
    private TextArea review;
	
    @FXML
    private ToggleGroup reviews;

	private int rating;
	private String inEmail;
	private String inReview;
	private boolean go = false;
	Calendar cal; 
	private String calendar;
	
	

	/**
	 * Method that gets the info from textfields
	 * and additionally saves the date automatically
	 */
	private void getInfo() {
		inEmail = email.getText();
		inReview = review.getText();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		cal = Calendar.getInstance();
		calendar = dateFormat.format(cal.getTime());
		

		if (one.isSelected()) {
			rating = 1;
			go = true;
		} else if (two.isSelected()) {
			rating = 2;
			go = true;
		} else if (three.isSelected()) {
			rating = 3;
			go = true;
		} else if (four.isSelected()) {
			rating = 4;
			go = true;
		} else if (five.isSelected()) {
			rating = 5;
			go = true;
		}
	}

	/**
	 * Method that saves the review and calls the getInfo() method to do so
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void SaveReview(ActionEvent event)throws IOException {
		getInfo();
		if (!go) {
			saved.setText("Pick a rating!");
		} else {
			try {
				String sql = "INSERT INTO Reviews VALUES (NULL,'" + inReview + "','" + inEmail + "','" + Home.restaurantID + "','" + rating + "','" + calendar + "');";				
				DBConnection db = new DBConnection();
				db.insert(sql);
				saved.setText("Review saved!");
			} catch (Exception e) {
				System.out.println(e);
			}
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

	@FXML
	 void goReview(ActionEvent event) throws IOException {
		Parent homeParent = FXMLLoader.load(getClass().getResource(REVIEWS));
		Scene homeScene = new Scene(homeParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.hide();
		stage.setScene(homeScene);
		stage.show();
    }
	
	@FXML
	void goToRestaurant(ActionEvent event) throws IOException {
		Parent homeParent = FXMLLoader.load(getClass().getResource(RESTAURANT));
		Scene homeScene = new Scene(homeParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.hide();
		stage.setScene(homeScene);
		stage.show();
	}

}
