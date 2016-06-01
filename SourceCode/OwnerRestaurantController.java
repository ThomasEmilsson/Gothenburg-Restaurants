/**
 * @author Mattias
 * 
 * Controller for page that is used when Adding a 
 * new restaurant or Editing an existing restaurant
 * 
 */
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.sql.*;
  
public class OwnerRestaurantController extends Home {
  
    private String name;
    private String address="";
    private String city;
    private String postalcode;
    private String phone="";
    private String email;
    private String website;
    private String openinghours;
    private String description;
    private String avgPrice;
    private boolean newrestaurant;
    private ObservableList<CheckMenuItem> foodtypelist = FXCollections.observableArrayList();
    private ObservableList<CheckMenuItem> optionlist = FXCollections.observableArrayList();
     
    @FXML
    private MenuButton menufoodtypes;
     
    @FXML
    private MenuButton menuoptions;
     
    @FXML
    private TextField lName;
  
    @FXML
    private TextArea lDescription;
  
    @FXML
    private TextField lAdress;
  
    @FXML
    private TextField lPostalcode;
  
    @FXML
    private TextField lCity;
  
    @FXML
    private TextField lPhone;
  
    @FXML
    private TextField lEmail;
  
    @FXML
    private TextField lWebsite;
  
    @FXML
    private TextField lOpeningHours;
  
    @FXML
    private TextField lAvgPrice;
  
    @FXML 
    private Label lblResult;
    
   
    public void initialize() {
        getFoodTypes();
        getOptions();
        try {
        	// Check if owner is editing or adding a restaurant
            if (restaurantID != 0) {
                newrestaurant = false;
                GetInfo();
            } else {
                newrestaurant = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        lName.setText(name);
        lDescription.setText(description);
        lAdress.setText(address);
        lPostalcode.setText(postalcode);
        lCity.setText(city);
        lPhone.setText(phone);
        lEmail.setText(email);
        lWebsite.setText(website);
        lOpeningHours.setText(openinghours);
        lAvgPrice.setText(avgPrice);
  
    }
  
    private void getFoodTypes() {       //Since foodtypes are not edited from within the application we can be sure the ids are ordered from 1-n
        String sql="SELECT foodtype from FoodTypes";    // So no need to actually look at the foodids, it will always be index-1 in menu
        try {
            DBConnection db = new DBConnection();
            ResultSet rs = db.select(sql);
            while (rs.next()) {     // Get all foodtypes from database and populate the menu
                CheckMenuItem menuitem=new CheckMenuItem(rs.getString("foodtype"));
                foodtypelist.add(menuitem);
            }
            menufoodtypes.getItems().addAll(foodtypelist);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     
    private void getOptions() {     
        String sql="SELECT options from Options";   
        try {
            DBConnection db = new DBConnection();
            ResultSet rs = db.select(sql);
            while (rs.next()) { 
                CheckMenuItem menuitem=new CheckMenuItem(rs.getString("options"));
                optionlist.add(menuitem);
            }
            menuoptions.getItems().addAll(optionlist);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  
    public void GetInfo() throws SQLException {
        try {
            String sql = "select * from restaurants where restaurantid = " + Home.restaurantID + ";";
            DBConnection db = new DBConnection();
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
            sql="SELECT foodid from restaurantfoodtypes where restaurantid="+restaurantID; //Get what foodtypes this restaurant has from database
            rs=db.select(sql);
            while (rs.next()){      // This makes the correct foodtypes selected in the menu
                foodtypelist.get(rs.getInt("foodid")-1).setSelected(true);
            }
            rs.close();
             
            sql="SELECT optionid from restaurantOptions where restaurantid="+restaurantID;
            rs=db.select(sql);
            while (rs.next()){      // This makes the correct options selected in the menu
                optionlist.get(rs.getInt("optionid")-1).setSelected(true);
            }
            rs.close();
             
        } catch (Exception e) {
            System.out.println(e);
        }
    }
  
    @FXML
    void goHome(ActionEvent event) throws IOException {
        restaurantID = 0;
        ownerID = 0;
        Parent homeParent = FXMLLoader.load(getClass().getResource(HOME));
        Scene homeScene = new Scene(homeParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(homeScene);
        stage.show();
    }
  
    @FXML
    void goOwnerList(ActionEvent event) throws IOException {
        Parent homeParent = FXMLLoader.load(getClass().getResource(OWNER_LIST));
        Scene homeScene = new Scene(homeParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(homeScene);
        stage.show();
    }
  
    /**
     * Method that takes care of saving the new restaurant information
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    void goSave(ActionEvent event) throws IOException {
        String sql;
        grabtext();
        if (isValid()){
        if (newrestaurant) {
            sql = "INSERT INTO Restaurants VALUES(NULL," + ownerID + ",'" + name + "','" + address + "'," + postalcode
                    + ",'" + city + "','" + phone + "','" + email + "','" + website + "','" + openinghours + "','"
                    + description + "'," + avgPrice + ",0)";
        } else {
            sql = "UPDATE Restaurants SET Name='" + name + "'," + "streetaddress='" + address + "'," + "postalcode="
                    + postalcode + "," + "city='" + city + "'," + "phone='" + phone + "'," + "email='" + email + "',"
                    + "homepage='" + website + "'," + "openinghours='" + openinghours + "'," + "description='"
                    + description + "'," + "averageprice=" + avgPrice + " WHERE restaurantid=" + restaurantID;
        }
        DBConnection db = new DBConnection();
        db.insert(sql);
        db=null;
        db=new DBConnection();
        if (newrestaurant){
            try{
            sql="SELECT MAX(restaurantid) from restaurants";
            ResultSet rs=db.select(sql);
            restaurantID=rs.getInt(1);
            rs.close();
            }
            catch(Exception e){
            System.out.println(e);
            }
        }       
        sql="Delete from restaurantfoodtypes where restaurantid="+restaurantID; //Easier to just overwrite the old information than check what has changed
        db.insert(sql);                                         // So deleting all information for this restaurantid
        for (int count=1;count<=foodtypelist.size();count++){    // Then going through the menu and doing an insert for each selected foodtype
            if (foodtypelist.get(count-1).isSelected()){
                sql="Insert into restaurantfoodtypes values(" + count + "," + restaurantID+")";
                db.insert(sql);
            }
        }
         
        sql="Delete from restaurantOptions where restaurantid="+restaurantID;   
        db.insert(sql);             
        for (int count=1;count<=optionlist.size();count++){  
            if (optionlist.get(count-1).isSelected()){
                sql="Insert into restaurantOptions values(" + count + "," + restaurantID+")";
                db.insert(sql);
            }
        }
         
        newrestaurant = false;
        lblResult.setText("Restaurant data saved. Use back button to go back and edit a different restaurant.");
        }
    }
  
    private boolean isValid(){		// Here we check the entered data. Can easily add more conditions. Only the first problem will be displayed as a result since it stops checking
    	if (!postalcode.matches("[0-9]{5}")){
    		lblResult.setText("Postal code needs to be 5 numbers");
    		return false;
    	}
    	if (!phone.matches("[0-9]+")){
    		lblResult.setText("Phone number can only have digits");
    		return false;
    	}
//    	if (address.length()<1 || city.length()<1){
//    		lblResult.setText("All address information is required");
//    		return false;
//    	}
    	else{
    	return true;
    	}
    }
    
    private void grabtext() {
        name = lName.getText();
        description = lDescription.getText();
        address = lAdress.getText();
        postalcode = lPostalcode.getText();
        city = lCity.getText();
        phone = lPhone.getText();
        email = lEmail.getText();
        website = lWebsite.getText();
        openinghours = lOpeningHours.getText();
        avgPrice = lAvgPrice.getText();
    }
}