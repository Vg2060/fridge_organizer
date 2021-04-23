package database;

import java.sql.*;
import java.util.ArrayList;

import customClasses.*;

public class Database {

	public static Connection getConnection() {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/refridgerator", "root", "");
			con.setAutoCommit(true);
			return con;
		} catch (Exception ex) {
			System.out.println("Exception in Database" + ex.getMessage());
		}
		return null;
	}

	public static ArrayList<Item> getItemList() {
		ArrayList<Item> itemList = new ArrayList<Item>();
		try {
			Connection con = Database.getConnection();
			String sql = "select * from items";
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			while (result.next()) {
				itemList.add(new Item(result.getInt("itemId")));
			}
		} catch (Exception ex) {
			System.out.println("database getitemlist " + ex.getMessage());
		}
		return itemList;
	}

	// vg
	public static int userTable() {
		try {
//	            
			Connection con = Database.getConnection();
			con.setAutoCommit(true);
			Statement st = con.createStatement();
			String userTable = "select * from users where isLoggedIn=1";
			ResultSet resultOne = st.executeQuery(userTable);
			resultOne.next();
			int userId = resultOne.getInt("userId");
			con.close();
			return userId;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// System.out.println("catch");
		}
		return -1;
	}

	public static ArrayList<Item> itemName() {

		ArrayList<Item> ItemName = new ArrayList<Item>();
		try {

			Connection con = Database.getConnection();

			String itemList = "select itemId from items";
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery(itemList);
			while (result.next()) {
				ItemName.add(new Item(result.getInt("itemId")));

			}
			con.close();
			// System.out.println(FridgeId);
			return ItemName;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;

	}

}
