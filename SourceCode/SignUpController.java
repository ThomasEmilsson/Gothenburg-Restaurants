/**
 * @author Mattias
 * 
 * Controller Class for the Sign Up Page
 * 
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController extends Home {

	
	@FXML // fx:id="password"
	private TextField password;

	@FXML // fx:id="firstName"
	private TextField firstName;

	@FXML // fx:id="lastName"
	private TextField lastName;

	@FXML // fx:id="passwordConfirm"
	private TextField passwordConfirm;

	@FXML // fx:id="email"
	private TextField email;

	@FXML // fx:id="phoneNumber"
	private TextField phoneNumber;

	@FXML // fx:id="goToSignIn"
	private Label goToSignIn;

	@FXML // fx:id="inputErrorText"
	private Label inputErrorText;
	
	@FXML // fx:id="Back"
	private Button Back; // Value injected by FXMLLoader
	
	@FXML
	void goBack(ActionEvent event) throws IOException {
		Parent homeParent = FXMLLoader.load(getClass().getResource(SIGN_IN));
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
	void goToOwner(ActionEvent event) throws IOException {

		if (inputvalid()) {
			submit(firstName.getText(), lastName.getText(), email.getText(), password.getText(), phoneNumber.getText(),
					event);
			Parent homeParent = FXMLLoader.load(getClass().getResource(OWNER_LIST));
			Scene homeScene = new Scene(homeParent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.hide();
			stage.setScene(homeScene);
			stage.show();
		}
	}

	/**
	 * Method that checks if the the entered data was 
	 * valid and returns a boolean
	 * 
	 * @return boolean
	 */
	private boolean inputvalid() {
		if (firstName.getText().length() > 0 && lastName.getText().length() > 0 && email.getText().length() > 0
				&& phoneNumber.getText().length() > 0 && password.getText().length() > 0) {
			if (password.getText().equals(passwordConfirm.getText())) {
				DBConnection db=new DBConnection();
				ResultSet rs=db.select("SELECT COUNT(*) FROM OWNERS WHERE email='"+email.getText()+"'");
				try {
					if (rs.getInt(1)==0){
						rs.close();
					return true;}
					else {
						rs.close();
						inputErrorText.setText("Email already registered");
						return false;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
			} else {
				inputErrorText.setText("Passwords don't match");
				return false;
			}
		} else {
			inputErrorText.setText(" Please fill out all fields");
			inputErrorText.setStyle(
					"-fx-background-color: rgba(0, 0, 0, .6); -fx-background-radius: 10; -fx-border-color: GREY;");
			return false;
		}
	}

	/**
	 * Method that adds the new owner to the database 
	 * assuming inputvalid() returns true
	 * 
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 * @param phoneNumber
	 * @param event
	 * @throws IOException
	 */
	public void submit(String firstName, String lastName, String email, String password, String phoneNumber,
			ActionEvent event) throws IOException {
		String sql = "INSERT INTO Owners VALUES(NULL, '" + firstName + "','" + lastName + "','" + email + "','"
				+ password + "','" + phoneNumber + "')";
		DBConnection db = new DBConnection();
		db.insert(sql);
		sql = "SELECT MAX(ownerid) from owners";
		ResultSet rs = db.select(sql);
		try {
			ownerID = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
}
