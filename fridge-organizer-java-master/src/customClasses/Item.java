package customClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import database.Database;

public class Item {
	
	public String toString() {
		return this.itemName;
	}
	
	public int itemId;
	public Date purchaseDate;
	public String itemName;
	public int bestBeforeDays;
	public Item( int id, Date date,String name, int bestBefore ){
		this.itemId = id;
		this.purchaseDate = date;
		this.itemName = name;
		this.bestBeforeDays = bestBefore;
	}
	public Item( int id ){
		this.itemId = id;
		initializeItem();
	}
	private void initializeItem() {
		try {
			Connection con = Database.getConnection();
			String sql = "select * from items where itemId = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, this.itemId);
			ResultSet result = stmt.executeQuery();
			result.next();
			this.bestBeforeDays = result.getInt("bestBeforeDays");
			this.itemName = result.getString("itemName");
			con.close();
					
		}
		catch(Exception ex) {
			System.out.println("init item "+ex.getMessage());
		}
	}
	
	
	
	public boolean delete(int fridgeId) {
		try {
//			LocalDate date = LocalDate.ofInstant(purchaseDate.toInstant(), ZoneId.systemDefault());
			LocalDate localDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(purchaseDate) );
			System.out.println( localDate );
			Connection con = Database.getConnection();
			String sql = "select * from fridgecontents where fridgeId=? and itemId=? and addedAt=? and currentQuantity <> 0";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, fridgeId);
			stmt.setInt(2, itemId);
			stmt.setDate(3, java.sql.Date.valueOf(localDate) );
			
			ResultSet result = stmt.executeQuery();
			result.next();
			int rowId = result.getInt("sNo");
			System.out.println("dfsfsdfsdfsdf  "+rowId);
			int currentQuantity = result.getInt("currentQuantity")-1;
			con.close();
			con = Database.getConnection();
			String sql1 = "update fridgeContents set currentQuantity=? where sNo=?";
			stmt = con.prepareStatement(sql1);
			stmt.setInt(1, currentQuantity );
			stmt.setInt(2, rowId);
			stmt.executeUpdate();
			con.close();
			return true;
			
		}
		catch(Exception e) {
			System.out.println("item delete +"+e);
			return false;
		}
	}
	public void addItemToFridge(int fridgeId) {
		try {
			LocalDate localDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(purchaseDate) );
			Connection con = Database.getConnection();
			String sql = "insert into fridgecontents (  fridgeId, itemId, quantity, currentQuantity,addedAt,price ) values (?,?,?,?,?,?) ";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, fridgeId);
			stmt.setInt(2, itemId);
			stmt.setInt(3,1);
			stmt.setInt(4, 1);
			stmt.setDate(5, java.sql.Date.valueOf(localDate));
			stmt.setFloat(6, (float)0);
			stmt.executeUpdate();
			con.close();
		}
		catch(Exception ex) {
			System.out.println("addItems to fridge: "+ex.getMessage());
		}
		
	}
}
