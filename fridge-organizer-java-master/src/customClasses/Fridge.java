package customClasses;

import java.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import database.Database;


abstract public class Fridge {
	public int fridgeId;
	public String fridgeName;
	public int capacity;
	public ArrayList<Item>itemList=new ArrayList<Item>();
	Fridge( int id , String name, int cap){
		this.capacity = cap;
		this.fridgeId = id;
		this.fridgeName = name;
		System.out.println("printing frmdge name:"+name);
		initialize();
	}
	public String toString() {
		return fridgeName;
	}
	private void initialize() {
		try {
			Connection con = Database.getConnection();
			String sql = "select * from fridgecontents where fridgeId=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, fridgeId);
			ResultSet result = stmt.executeQuery();
			int count,itemId;
			String itemName;
			Date date;
			String sql1 = "select * from items where itemId=?";
			PreparedStatement stmt1 = con.prepareStatement(sql1);
			ResultSet result1;
			while( result.next() ) {
				count = result.getInt("currentQuantity");
				date = result.getDate("addedAt");
				itemId = result.getInt("itemId");
				stmt1.setInt(1, itemId);
				result1 = stmt1.executeQuery();
				result1.next();
				for(int i=0; i<count; i++) {
					itemList.add( new Item(itemId,date,result1.getString("itemName"),result1.getInt("bestBeforeDays")) );
				}
			}
			con.close();
		}
		catch(Exception ex) {
			System.out.println("item init "+ex.getMessage());
		}
	}
	
	public ArrayList<Item> deleteFridgeItems( Item item , int count , boolean needReturn) {
		int currentCount = 0;
//		System.out.println("item given:"+item);
//		System.out.println("item avail: "+itemList);
		ArrayList<Item> itemsToDelete = new ArrayList<Item>();
		for( Item i: itemList ) {
			if( i.toString().equals(item.toString()) ) {
				itemsToDelete.add(i);
			}
		}
		for ( int i=0; i<count; i++ ) {
			deleteItem(itemsToDelete.get(i));
			
		}
		if (needReturn) {
			return itemsToDelete;
		}
		return null;
		
	}
	
	private void deleteItem( Item item ) {
		
		itemList.remove(item);
		item.delete(fridgeId);
		
		
	}
	
	public void addItems(ArrayList<Item> items) {
		
		for( Item item : items ) {
			item.addItemToFridge(fridgeId);
		}
		
	}
	
}
