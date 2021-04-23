package frontend;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.event.*;

import customClasses.Item;
import customClasses.User;
import database.Database;

public class AddRecipe extends JFrame implements ActionListener,ItemListener{
	User currentUser;
	JComboBox<String> itemCombo;
	JLabel recipeNameLabel,cuisineLabel,timeLabel;
	JTextField recipeName,cuisine;
	JSpinner timeSpinner;
	ArrayList<Item> itemList;
	String selectedIngredientValue;
	JButton addIngredient,saveRecipe;
	JSpinner itemQuantitySpinner;
	int itemQuantity,time;
	JTable ingredientsTable;
	JScrollPane scrollPane;
	String[][] ingredientsData;
	String[] tableHead= {"Ingredients","Quantity"};
	int rowCount=0;
	AddRecipe(User user){
		ingredientsData = new String[30][2];
		this.currentUser = user;
		setTitle("Add Recipe");
		setResizable(false);
		setSize(new Dimension(550,400));
		setLayout(null);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width-getSize().width)/2; 
		int y = (dim.height-getSize().height)/2; 
		
		recipeNameLabel = new JLabel("Recipe Name");
		recipeNameLabel.setBounds(100, 20, 100, 20);
		add(recipeNameLabel);
		
		recipeName = new JTextField();
		recipeName.setBounds(200,20,150,20);
		add(recipeName);
		
		cuisineLabel = new JLabel("Cuisine");
		cuisineLabel.setBounds(100, 50, 100, 20);
		add(cuisineLabel);
		
		cuisine = new JTextField();
		cuisine.setBounds(200,50,150,20);
		add(cuisine);
		
		timeLabel = new JLabel("Cooking Time (mins.)");
		timeLabel.setBounds(100,80,120,20);
		add(timeLabel);
		
		timeSpinner = new JSpinner();
		SpinnerNumberModel model1 = new SpinnerNumberModel(0, 0, 200, 10);
		timeSpinner.setModel(model1);
		((DefaultEditor) timeSpinner.getEditor()).getTextField().setEditable(false);
		timeSpinner.setBounds(230,80,70,20);
		add(timeSpinner);
		
		JLabel itemName = new JLabel("Select Ingredient");
		itemName.setBounds(100,120,100,20);
		add(itemName);
		
		itemCombo = new JComboBox<String>();
		itemCombo.addItemListener(this);
		itemCombo.setBounds(200,120,100,20);
		itemList = Database.getItemList();
		itemCombo.addItem("--Select--");
		for(Item i: itemList) {
			itemCombo.addItem(i.toString());
		}
		add(itemCombo);
		
		itemQuantitySpinner = new JSpinner();
		itemQuantitySpinner.setBounds(310,120,50,20);
		SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 50, 1);
		itemQuantitySpinner.setModel(model);
		((DefaultEditor) itemQuantitySpinner.getEditor()).getTextField().setEditable(false);
		add(itemQuantitySpinner);
		
		addIngredient = new JButton("Add");
		addIngredient.setBounds(370,120,110,20);
		addIngredient.addActionListener(this);
		add(addIngredient);
		
		JLabel createItem = new JLabel("Item not found? Create new Item here..");
		createItem.setBounds(100,150,300,20);
		createItem.setForeground(Color.BLUE.darker());
		createItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		add(createItem);
		
		ingredientsTable = new JTable(ingredientsData,tableHead) {
			public boolean editCellAt(int row, int column, java.util.EventObject e) {
	            return false;
		}
			};
		scrollPane = new JScrollPane(ingredientsTable);
		scrollPane.setBounds(100,190,250,150);
		add(scrollPane);
		
		saveRecipe = new JButton("Save Recipe");
		saveRecipe.setBounds(370,320,110,20);
		saveRecipe.addActionListener(this);
		add(saveRecipe);
		
		setLocation(x,y);
		setVisible(true);
	}
	
//	public static void main(String[] arg) {
//		new AddRecipe(new User(9));
//	}

	
	
	private void generateTable() {
		remove(scrollPane);
		ingredientsTable = new JTable(ingredientsData,tableHead) {
			public boolean editCellAt(int row, int column, java.util.EventObject e) {
	            return false;
		}
			};
		scrollPane = new JScrollPane(ingredientsTable);
		scrollPane.setBounds(100,190,250,150);
		add(scrollPane);
		revalidate();
		repaint();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==addIngredient) {
			itemQuantity = (Integer)itemQuantitySpinner.getValue();
			if(!selectedIngredientValue.equals("--Select--") && itemQuantity!=0 ) {
				ingredientsData[rowCount][0]=selectedIngredientValue;
				ingredientsData[rowCount][1]=String.valueOf(itemQuantity);
				rowCount++;
				itemQuantitySpinner.setValue(0);
				itemCombo.setSelectedItem("--Select--");
				generateTable();
				JOptionPane.showMessageDialog(this, "Ingredient added successfully!!");
			}
			else if( selectedIngredientValue.equals("--Select--") ) {
				JOptionPane.showMessageDialog(this, "Please select an Ingredient to add!!");
			}
			else if( itemQuantity==0) {
				JOptionPane.showMessageDialog(this, "Ingredient Quantity cannot be empty!!");
			}
				
		}
		else if( e.getSource()==saveRecipe ) {
			int[][] intTableData = getIntTableData();
			String recipeName = this.recipeName.getText();
			String cuisine = this.cuisine.getText();
			time =(Integer) this.timeSpinner.getValue();
			currentUser.createRecipe(intTableData,recipeName,cuisine,time);
			JOptionPane.showMessageDialog(this, "Recipe Added Successfully!!");
			dispose();
		}
	}
	
	private int[][] getIntTableData(){
		System.out.println("getinttabledata");
		int[][] data = new int[rowCount][2];
		int rowIndex=0;
		for( int i=0; i<rowCount;i++ ) {
			data[i][0] = getItemId(ingredientsData[i][0]);
			data[i][1] = Integer.parseInt(ingredientsData[i][1]);
			System.out.println(data[i][0]+" "+data[i][1]);
		}
		return data;
	}
	
	private int getItemId(String itemName) {
		int id=0;
		for(Item i: itemList) {
			if(i.toString().equals(itemName)) {
				return i.itemId;
			}
		}
		return 0;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()==itemCombo && e.getStateChange()==ItemEvent.SELECTED) {
			selectedIngredientValue = (String)itemCombo.getSelectedItem();
			System.out.println(((String)itemCombo.getSelectedItem()));
		}
	}

}
