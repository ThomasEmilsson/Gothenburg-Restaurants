/**
 *  Class that runs from start and loads the home page.
 *  Holds the static addresses for every page of the application as well as some data that
 *  gets passed along.
 *  
 *  @author thomasemilsson
 */

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Home extends Application {
	
	public static final String HOME = "home.fxml";

	public static final String RESTAURANT_LIST = "restaurantList.fxml";

	public static final String RESTAURANT = "restaurant.fxml";

	public static final String SUBMIT_REVIEW = "submitReview.fxml";
	
	public static final String SIGN_IN = "SignIn.fxml";

	public static final String SIGN_UP = "signUp.fxml";
	
	public static final String OWNER = "owner.fxml";
	
	public static final String OWNER_LIST = "ownerList.fxml";

	public static final String FORGOTTEN_PASS = "ForgottenPass.fxml";

	public static final String OWNER_RESTAURANT = "ownerRestaurant.fxml";

	public static final String ABOUT_US = "aboutUs.fxml";

	public static final String FAQ = "faq.fxml";
	
	public static final String REVIEWS = "review.fxml";
	
	public static final String WRITE_REVIEWS = "writeReview.fxml";
	
	public static String postalCode;

	public static int restaurantID;

	public static int ownerID;
	
	public static String SQL_advancedSearch;
	
	public static String standardSearch;
	
	public static boolean advancedOn;

	
	/**
	 * Loads the home page and sets it as the scene for the stage 
	 */
	
	@Override
	public void start(Stage stage) throws IOException {
		
		
		Parent root = FXMLLoader.load(getClass().getResource(HOME));
		Scene scene = new Scene(root);
		
		stage.setScene(scene);
		stage.show();	
	}

	public static void main(String[] args) {
		launch(args);
	}
}
