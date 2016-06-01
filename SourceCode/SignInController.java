/**
 * @author Mattias
 * 
 * Controller Class for the Sign In page
 */
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SignInController extends Home {

	@FXML // fx:id="email"
	private TextField email;

	@FXML // fx:id="password"
	private TextField password;

	@FXML // fx:id="incorrectText"
	private Label incorrectText;
	
	@FXML // fx:id="incorrectText"
	private Label signup;
	
	@FXML // fx:id="incorrectText"
	private Label pass;

	@FXML
	void goForgottenPassword(MouseEvent event) throws IOException {
		Parent homeParent = FXMLLoader.load(getClass().getResource(FORGOTTEN_PASS));
		Scene homeScene = new Scene(homeParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.hide();
		stage.setScene(homeScene);
		stage.show();
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
	void goSignUp(MouseEvent event) throws IOException {
		Parent homeParent = FXMLLoader.load(getClass().getResource(SIGN_UP));
		Scene homeScene = new Scene(homeParent);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.hide();
		stage.setScene(homeScene);
		stage.show();
	}

	/**
	 * Method that checks if owner Exists or not and 
	 * if the owner exists, directs them to their 
	 * list of restaurants
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void goToOwner(ActionEvent event) throws IOException {

		boolean correct = false;

		String sql = "SELECT OwnerID, firstname, Password from Owners where email='" + email.getText() + "'";
		DBConnection db=new DBConnection();
		ResultSet rs = db.select(sql);
		try {
			if (!rs.isAfterLast()) { // Checking if result set is empty

				if (rs.getString("Password").equals(password.getText())) {
					ownerID = rs.getInt("Ownerid");
					correct = true;
					rs.close();
				}
				else{
					incorrectText.setVisible(true);
				}
			} else {
				// If result set is empty, there is no such user
				incorrectText.setText("  No such user exists");
				
				incorrectText.setStyle(
						"-fx-background-color: rgba(0, 0, 0, .6); -fx-background-radius: 10; -fx-border-color: GREY;");


				
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if (correct) {
			Parent homeParent = FXMLLoader.load(getClass().getResource(OWNER_LIST));
			Scene homeScene = new Scene(homeParent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.hide();
			stage.setScene(homeScene);
			stage.show();
		}

		else {
			incorrectText.setVisible(true);
		}
	}
}
