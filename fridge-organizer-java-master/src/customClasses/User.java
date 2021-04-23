package customClasses;

import java.sql.Connection;
import java.sql.*;
import java.util.*;


import database.Database;

public class User {
	public int userId;
	public String username;
	public String hint;
	public List<Fridge> fridges;
	public Map<String,Recipe>recipes ;
	
	public User( int id ){
		fridges = new ArrayList<Fridge>();
		recipes = new HashMap<String,Recipe>();
		this.userId = id;
		System.out.println("user constructor");
		initializeFridges();
		initializeRecipes();
	}
	public User() {
		fridges = new ArrayList<Fridge>();
		recipes = new HashMap<String,Recipe>();
		System.out.println("empty const");
		this.userId = -1;
	}
	
	private void initializeRecipes() {
		try {
			Connection con = Database.getConnection();
			String sql = "select * from recipelist where userId=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, this.userId);
			ResultSet result = stmt.executeQuery();
			String recipeName,cuisine;
			float time;
			int id;
			while( result.next() ) {
				id = result.getInt("recipeId");
				recipeName=result.getString("recipeName");
				time = result.getFloat("time");
				cuisine = result.getString("cuisine");
				recipes.put(recipeName, new Recipe(id,recipeName,cuisine,time));
			}
		}
		catch(Exception ex) {
			System.out.println("init recipe "+ex.getMessage());
		}
	}
	
	public void initializeFridges() {
		try {
			fridges.clear();
			Map<Integer,Integer> fridgeList = new HashMap<Integer,Integer>();
			Connection con = Database.getConnection();
			String sql = "select * from fridgelist where userId=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, this.userId);
			ResultSet result = stmt.executeQuery();
			List<String>fridgeNames = new ArrayList<String>();
			while(result.next()) {
				fridgeList.put( result.getInt("fridgeId"),result.getInt("maxSize") );
				fridgeNames.add(result.getString("fridgeName"));
			}
			int fridgeNameIndex=0;
			for( int fridgeId: fridgeList.keySet() ) {
				if( fridgeList.get(fridgeId)==100 ) {
					fridges.add(new SmallFridge(fridgeId,fridgeNames.get(fridgeNameIndex++),100));
				}
				else if( fridgeList.get(fridgeId)==200 ) {
					fridges.add(new MediumFridge(fridgeId,fridgeNames.get(fridgeNameIndex++),200));
				}
				else if( fridgeList.get(fridgeId)==300 ) {
					fridges.add(new LargeFridge(fridgeId,fridgeNames.get(fridgeNameIndex++),300));
				}
			}
			
			con.close();
		}
		catch(Exception ex) {
			System.out.println("initialize user "+ex.getMessage());
		}
		
	}
	
	private int verifyUser( String user, String pass ) {
	
		Connection con = Database.getConnection();
		try {
			String query = "select * from users where username='"+user+"'";
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery(query);
			result.next();
			if( result.getString("password").equals(pass) ) {
				return result.getInt("userId");
			}
			return -1;
		}
		catch(Exception ex) {
			System.out.println("login validate "+ex.getMessage());
		}
		return -1;
	}
	public boolean loginUser(String username, String pass) {
		int user = verifyUser(username,pass);
		
		if( user>0 ) {
			this.userId = user;
			System.out.println("user id in login user method: "+this.userId);
			try {
				Connection con = Database.getConnection();
				String sql = "update users set isLoggedIn=1 where userId=?";
				PreparedStatement stmt = con.prepareStatement(sql); 
				stmt.setInt(1, this.userId);
				stmt.executeUpdate();
//				System.out.println("User Logged in!!");
				initializeFridges();
				initializeRecipes();
				return true;
			}
			catch(Exception ex) {
				System.out.println("login user method "+ex.getMessage());
			}
		}
		else {
			return false;
			
		}
		return false;
	}
	public void logout() {
		try {
			Connection con = Database.getConnection();
			String sql = "update users set isLoggedIn=0 where userId=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, userId);
			stmt.executeUpdate();
		}
		catch (Exception ex) {
			System.out.println("logout user "+ex.getMessage());
		}
		
				
		
	}
	public void createRecipe(int[][] intTableData,String recipeName,String cuisine, int time) {
		try {
			Connection con = Database.getConnection();
			String sql = " insert into recipelist (cuisine,time,recipeName,userId) values(?,?,?,?) ";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cuisine);
			stmt.setFloat(2,(float)time);
			stmt.setString(3,recipeName);
			stmt.setInt(4,this.userId);
			stmt.executeUpdate();
			
			String sql2 = "select * from recipelist where userId=? and recipeName=?";
			stmt = con.prepareStatement(sql2);
			stmt.setInt(1, this.userId);
			stmt.setString(2, recipeName);
			ResultSet res = stmt.executeQuery();
			res.next();
			int recipeId = res.getInt("recipeId");
			
			String sql1 = "insert into recipedetails(recipeId,itemId,quantity) values(?,?,?)";
			stmt = con.prepareStatement(sql1);
			
			for( int i=0; i<intTableData.length; i++ ) {
				stmt.setInt(1, recipeId);
				stmt.setInt(2, intTableData[i][0]);
				stmt.setInt(3,intTableData[i][1]);
				stmt.executeUpdate();
			}
			this.recipes.put(recipeName,  new Recipe(recipeId,recipeName,cuisine,(float)time) );
			System.out.println("recipeAdded successfully!!");
		}
		catch(Exception ex) {
			System.out.println("user class create recipe "+ex.getMessage());
		}
		
	}
	
	public ArrayList<Recipe> suggestRecipes(){
		ArrayList<Item> items = getAllItems();
		ArrayList<Recipe> recipeList = new ArrayList<Recipe>();  
		Recipe r;
		int reqCount;
		int presentCount;
		int flag=1;
		for( String rName: recipes.keySet() ) {
			r = recipes.get(rName);
			flag=1;
			for( Item i : r.ingredients.keySet() ) {
				reqCount = r.ingredients.get(i);
				presentCount = countItems( items, i );
				if( presentCount >= (reqCount-2) && presentCount!=0 ) {
					continue;
				}
				else {
					flag=0;
					break;
				}
			}
			if(flag==1) {
				recipeList.add(r);
			}
		}
		
		return recipeList;
	}
	
	public int countItems( ArrayList<Item> items, Item i ) {
		int count = 0;
		
		for( Item item : items ) {
			if( item.toString().equals(i.toString()) ) {
				count++;
			}
		}
		
		return count;
	}
	
	public ArrayList<Item> getAllItems(){
		ArrayList<Item> items = new ArrayList<Item>();
		for( Fridge f: fridges ) {
			for( Item i : f.itemList ) {
				items.add(i);
			}
		}
		return items;
	}
	
	
	
}
