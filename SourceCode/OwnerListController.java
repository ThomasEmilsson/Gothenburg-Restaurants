/**
 * @author Mattias
 * 
 * Class that displays all restaurants for a particular owner
 * 
 */

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
 
public class OwnerListController extends Home {
 
    @FXML
    private Label lblName;
     
    private int[] id=new int[100];  // If we ever get an owner with more than 100 restaurants I guess we need to change this
    private int count;
     
    @FXML
    private ListView<String> listbox;   
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnEdit;
     
    private ObservableList<String> datalist=FXCollections.observableArrayList();
     
    @FXML
    private void initialize() {
        count=0;
        btnDelete.setDisable(true);
        btnEdit.setDisable(true);
         
        listbox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(false);
            btnEdit.setDisable(false);
        });
         
        String sql="SELECT firstname from Owners where Ownerid=" + ownerID;
        DBConnection db=new DBConnection();
        ResultSet rs=db.select(sql);
        try {
            lblName.setText("Welcome " +rs.getString("firstname"));
            rs.close();
            sql="SELECT Restaurantid, Name from Restaurants where Ownerid=" + ownerID;
            rs=db.select(sql);
            while (rs.next()){
                id[count]=rs.getInt("restaurantid");
                count++;
                datalist.add(rs.getString("Name"));
            }
            listbox.setItems(datalist);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void goLogout(ActionEvent event) throws IOException {
        ownerID=0;
        Parent homeParent = FXMLLoader.load(getClass().getResource(HOME));
        Scene homeScene = new Scene(homeParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(homeScene);
        stage.show();
    }
    
    @FXML
    void goAdd(ActionEvent event) throws IOException {
        restaurantID=0; // Here we go to restaurant edit window which just needs to check for restaurantID=0 and then give empty boxes         
        Parent homeParent = FXMLLoader.load(getClass().getResource(OWNER_RESTAURANT));
        Scene homeScene = new Scene(homeParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(homeScene);
        stage.show();
    }
     
    @FXML
    void goDelete(ActionEvent event) throws IOException { 
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete the Restaurant?");
        Optional<ButtonType> result = alert.showAndWait();
         if (result.isPresent() && result.get() == ButtonType.OK) {
             String sql="Delete from Restaurants where restaurantid="+id[listbox.getSelectionModel().getSelectedIndex()];
             DBConnection db=new DBConnection();
             db.insert(sql);
             db=null;
             datalist.clear();
             initialize();
         }
    }
     
    @FXML
    void goEdit(ActionEvent event) throws IOException { 
        restaurantID=id[listbox.getSelectionModel().getSelectedIndex()];
        Parent homeParent = FXMLLoader.load(getClass().getResource(OWNER_RESTAURANT));
        Scene homeScene = new Scene(homeParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(homeScene);
        stage.show();
    }
}