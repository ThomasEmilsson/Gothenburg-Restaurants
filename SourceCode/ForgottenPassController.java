/**
 * @author Aras
 * 
 * Class for recovering a forgetten password
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

public class ForgottenPassController extends Home {

	@FXML // fx:id="buttonHome"
	private Button buttonHome; // Value injected by FXMLLoader

	@FXML // fx:id="email"
	private TextField email; // Value injected by FXMLLoader

	@FXML // fx:id="phoneNumber"
	private TextField phoneNumber; // Value injected by FXMLLoader

	@FXML // fx:id="buttonConfirm"
	private Button buttonConfirm; // Value injected by FXMLLoader

	@FXML // fx:id="Back"
	private Button Back; // Value injected by FXMLLoader

	@FXML // fx:id="pass"
	private Label pass; // Value injected by FXMLLoader

	@FXML // fx:id="incorrectText"
	private Label incorrectText; // Value injected by FXMLLoader

	/**
	 * What happens when you press Confirm: Checks if email/number exists
	 * if it does exist, returns the password for the account
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void confirm(ActionEvent event) throws SQLException {
		incorrectText.setVisible(false);
		
		String getPass = "SELECT phone, Password from Owners where phone = '" + phoneNumber.getText() + "'";
		DBConnection db=new DBConnection();
		ResultSet rs = db.select(getPass);

		try {
			if (!rs.isAfterLast()) {

				if (phoneNumber.getText().equals(rs.getString("phone"))) {
					pass.setVisible(true);
					pass.setText(rs.getString("Password"));
					rs.close();
					//System.out.println(password);
				}
			} else {
				pass.setVisible(false);
				incorrectText.setVisible(true);
				// If phone number doesn't exist:  
				incorrectText.setText("Phone number doesn't exsit ");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

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

}
