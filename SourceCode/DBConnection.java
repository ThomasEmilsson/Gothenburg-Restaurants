/**
 * @author mattias
 * 
 * Class that allows connections to the database to 
 * both select and insert information from and into the database
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection

{
	private static Connection connection = null;

	public DBConnection(){
		
	}

	public ResultSet select(String sql) {

		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:team4.db"); // CHANGE TO YOUR DIRECTORY
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery(sql);
			return rs;
		} catch (SQLException e) {

			System.err.println(e.getMessage());
		}
		return null;

	}
	public void insert(String sql){
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:team4.db"); // CHANGE TO YOUR DIRECTORY
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {

			System.err.println(e.getMessage());
		}
	}
}