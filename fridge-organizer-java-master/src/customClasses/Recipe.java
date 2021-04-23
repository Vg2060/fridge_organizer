package customClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import database.Database;

public class Recipe {
	public int recipeId;
	public String recipeName;
	public String cuisine;
	public float preparationTime; //minutes
	public Map<Item,Integer>ingredients;
	
	public String toString() {
		return recipeName;
	}
	
	Recipe( int id, String name,String cu, float time ){
		this.recipeId = id;
		this.recipeName = name;
		this.cuisine = cu;
		this.preparationTime = time;
		ingredients = new HashMap<Item,Integer>();
		initialize();
	}
	private void initialize() {
		try {
			Connection con = Database.getConnection();
			String sql = "select * from recipedetails where recipeId=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, this.recipeId);
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				ingredients.put(new Item( result.getInt("itemId") ), result.getInt("quantity"));
			}
		}
		catch(Exception ex) {
			System.out.println("init recipe "+ex.getMessage());
		}
	}
}
