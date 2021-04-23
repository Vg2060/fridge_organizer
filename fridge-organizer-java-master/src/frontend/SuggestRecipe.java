package frontend;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import customClasses.User;
import customClasses.Item;
import customClasses.Recipe;
import java.util.ArrayList;


public class SuggestRecipe extends JFrame implements ActionListener{
	
	User currentUser;
	ArrayList<Recipe> recipes;
	ArrayList<Item> fridgeItems;
	JButton okButton;
	SuggestRecipe( User user ){
		this.currentUser = user;
		setSize(new Dimension(550,440));
		setResizable(false);
		setLayout(null);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width-getSize().width)/2; 
		int y = (dim.height-getSize().height)/2; 
		setLocation(x,y);
		setVisible(true);
		
		recipes = currentUser.suggestRecipes();
		
		if (recipes.size()==0) {
			JOptionPane.showMessageDialog(this, "No Recipe Suggestions Found!");
			dispose();
		}
		setTitle("Suggested Recipes");
		
		
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.setBounds(30,30,470,300);
		
		okButton = new JButton("OK");
		okButton.setBounds(400,350,100,30);
		okButton.addActionListener(this);
		add(okButton);
		
		String[] tableHead = {"Ingredient","Quantity required", "Quantity available"}; 
		String[][][] recipeTableData = new String[recipes.size()][30][3];
		
		fridgeItems = currentUser.getAllItems();
		int index=-1;
		for( Recipe recipe: recipes ) {
			index++;
			int itemIndex=0;
			for( Item item: recipe.ingredients.keySet() ) {
				recipeTableData[index][itemIndex][0] = item.toString();
				recipeTableData[index][itemIndex][1] = String.valueOf(recipe.ingredients.get(item));
				recipeTableData[index][itemIndex][2] = String.valueOf(currentUser.countItems(fridgeItems, item));
				itemIndex++;
				
			}
			JTable table = new JTable(recipeTableData[index],tableHead) {
				@Override
	            public boolean isCellEditable(int row, int column) {
	                return false;
	            }
			};
			
			JScrollPane scroll = new JScrollPane(table);
			
			tabPane.add(recipes.get(index).toString(),scroll);
			
			
			
		}
		add(tabPane);
		revalidate();
		repaint();
		
		
		
		
	}
	
	public static void main(String[] args) {
		new SuggestRecipe( new User(9) );
//		System.out.println(new User(9).suggestRecipes());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==okButton) {
			dispose();
		}
	}

}
