/**
 * @author Axel
 * 
 * Controller Class for the Review Page
 * 
 */

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.sql.*;
import java.util.ResourceBundle;

public class ReviewController extends RestaurantController implements Initializable {

	private int[] rating;
	private String[] reviews;
	private int rows;

	@FXML
	private VBox revBox;

	public void getRating() throws SQLException {
		try {
			DBConnection db = new DBConnection();
			String ratingSql = "select rating from reviews where restaurantid = " + Home.restaurantID + " and review != '';";
			ResultSet rs = db.select(ratingSql);
			
			int rowCount = 0;
			while (rs.next()) {
				rowCount++;
			}

			rows = rowCount;

			rating = new int[rowCount];
			int i = 0;
			while (rs.next()) {
				int temp = rs.getInt("rating");
				rating[i] = temp;
				i++;
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public int getAverage(int[] rating) {
		int sum = 0;
		int counter = 0;

		for (int i = 0; i < rating.length; i++) {
			sum += rating[i];
			counter++;
		}

		return sum / counter;
	}

	/**
	 * Get and save all the reviews
	 * @throws SQLException
	 */
	public void getReviews() throws SQLException {
		try {
			DBConnection db = new DBConnection();
			String reviewSql = "select review, rating, email , date from reviews where restaurantid = " + Home.restaurantID +" and review != '' ;";
			ResultSet rs = db.select(reviewSql);

			reviews = new String[rows];

			int i = 0;
			while (rs.next()) {
				String email = rs.getString("email");
				String review = rs.getString("review");
				int rating = rs.getInt("rating");
				String date = rs.getString("date");
				String result = "Score: " + rating + "\tby: " + email + "\t \t" + date + "\n" + review;

				reviews[i] = result;
				i++;
			}
			rs.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Display all existing Reviews
	 */
	public void showReviews() {

		int i = 0;
		System.out.println(rows);
		while (i < rows) {

			TextArea review = new TextArea();

			review.setMinWidth(500);
			review.setMinHeight(75);
			review.setWrapText(true);
			review.setText(reviews[i]);
			review.setVisible(true);
			review.setEditable(false);
			revBox.getChildren().add(review);

			i++;

		}

	}

	public void initialize(URL location, ResourceBundle resources) {

		try {

			getRating();
			// countRows();
			getReviews();
			showReviews();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private AnchorPane Review;

	@FXML
	private Button buttonBackToRestaurant;

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
	void goToRestaurant(ActionEvent event) throws IOException {
		Parent homeParent = FXMLLoader.load(getClass().getResource(RESTAURANT));
		Scene homeScene = new Scene(homeParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.hide();
		stage.setScene(homeScene);
		stage.show();
	}

	@FXML
	void goReview(ActionEvent event) throws IOException {
		Parent homeParent = FXMLLoader.load(getClass().getResource(WRITE_REVIEWS));
		Scene homeScene = new Scene(homeParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.hide();
		stage.setScene(homeScene);
		stage.show();
	}

}
