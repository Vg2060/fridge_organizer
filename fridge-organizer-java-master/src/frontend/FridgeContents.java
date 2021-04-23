package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import customClasses.Fridge;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import customClasses.Item;
import customClasses.User;

import java.time.format.DateTimeFormatter;
import database.Database;
import javax.swing.JSpinner.DefaultEditor;
import java.sql.*;

public class FridgeContents implements ActionListener {
	JFrame frame1, frame2;
	JButton button1, addItem, button3;
	String fridgeName, itemName;
	ArrayList<Fridge> fridgeList;
	ArrayList<Item> itemList;
	JComboBox<Item> itemComboBox;
	JSpinner quantitySpinner;
	int quantity, fridgeId;
	int w, h;
	float price;
	int itemId;
	LocalDate addedAt;
	JComboBox comboBox1, comboBox2;
	User currentUser;
	Fridge currentFridge;

	FridgeContents(User user) {
		this.currentUser = user;
		frame1 = new JFrame("DashBoard");
		Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		h = ScreenSize.height;
		w = ScreenSize.width;
		frame1.setSize(w, h);
		frame1.setVisible(false);

		JPanel panelWestt = new JPanel();
		JPanel panelCenterr = new JPanel();
		panelWestt.setPreferredSize(new Dimension(w / 4, h));
		panelCenterr.setPreferredSize(new Dimension((w * 3) / 4, h));

		button1 = new JButton("Organize Fridge");
		addItem = new JButton("Add item");
		button3 = new JButton("Remove item");

		frame1.add(panelWestt, BorderLayout.WEST);
		frame1.add(panelCenterr, BorderLayout.CENTER);
		panelWestt.add(button1);
		button1.setBounds(50, 50, 200, 60);
		addItem.setBounds(50, 50, 130, 20);
		button3.setBounds(50, 80, 130, 20);
//		button1.addActionListener(this);
		addItem.addActionListener(this);
		button3.addActionListener(this);
		
		//akil
		comboBox1 = new JComboBox();
		Object[] fields = { "Selectfridge name", comboBox1, };

		fridgeList = (ArrayList)currentUser.fridges;
		for (Fridge s : fridgeList) {
			comboBox1.addItem(s.toString());

		}

		int result = JOptionPane.showConfirmDialog(null, fields, "Manage Items", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {

			fridgeName = (String) comboBox1.getSelectedItem();
			System.out.println(fridgeName);
			for (Fridge f : fridgeList) {
				if (fridgeName.equals(f.toString())) {
					fridgeId = f.fridgeId;
					this.currentFridge = f;
				}
			}
			frame2 = new JFrame("Organize Fridge");
			frame2.setTitle("Add/Remove");
			frame2.setSize(300,200);
			frame2.setLayout(null);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (dim.width-frame2.getSize().width)/2; 
			int y = (dim.height-frame2.getSize().height)/2; 
			frame2.setLocation(x,y);

//			JPanel panelWest = new JPanel();
//			JPanel panelCenter = new JPanel();
//			panelWest.setPreferredSize(new Dimension(w / 4, h));
//			panelCenter.setPreferredSize(new Dimension((w * 3) / 4, h));
			frame2.add(addItem);
			frame2.add(button3);
//			panelWest.add(addItem);
//			panelCenter.add(button3);
//			frame2.add(panelWest, BorderLayout.WEST);
//			frame2.add(panelCenter, BorderLayout.CENTER);
			frame2.setVisible(true);
		}
		//akil
		

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// throw new UnsupportedOperationException("Not supported yet.");
		// To change body of generated methods, choose Tools | Templates.
		
		if (e.getSource() == addItem) {

			JSpinner Spinner1 = new JSpinner();
			Spinner1.setBounds(310, 120, 50, 20);
			SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 50, 1);
			Spinner1.setModel(model);
			((DefaultEditor) Spinner1.getEditor()).getTextField().setEditable(false);

			JTextField field3 = new JTextField();
			comboBox2 = new JComboBox();
			Object[] fields = { "Select itemName", comboBox2, "Enter count", Spinner1, "Enter price", field3, };

			itemList = Database.itemName();
			for (Item s : itemList) {
				comboBox2.addItem(s.toString());

			}

			int result = JOptionPane.showConfirmDialog(null, fields, "Add Items", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				itemName = (String) comboBox2.getSelectedItem();
				System.out.println("itemName here" + itemName + "\nlist of items\n");
				for (Item i : itemList) {
					System.out.println(i.toString());
					if (i.toString().equals(itemName)) {
						System.out.println("item id set to " + itemId);
						itemId = i.itemId;
						break;
					}
				}
				quantity = (Integer) (Spinner1.getValue());
				price = Float.parseFloat(field3.getText());
				addedAt = LocalDate.now();
				toTable();
			}
		}
		if (e.getSource() == button3) {
			itemComboBox = new JComboBox<Item>();
			addItemsFromFridges(itemComboBox);
			quantitySpinner = new JSpinner();
			quantitySpinner.setBounds(310, 120, 50, 20);
			SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 50, 1);
			quantitySpinner.setModel(model);
			((DefaultEditor) quantitySpinner.getEditor()).getTextField().setEditable(false);

			Object[] fields = {

					"Select itemName", itemComboBox, "Quantity", quantitySpinner, };

			int result = JOptionPane.showConfirmDialog(null, fields, "Delete Items", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				Item selectedItem = (Item)itemComboBox.getSelectedItem();
				int selectedQuantity = (Integer)quantitySpinner.getValue();
				int maxQuantity = currentUser.countItems(currentFridge.itemList, selectedItem);
				if( selectedQuantity > maxQuantity ) {
					JOptionPane.showMessageDialog(null, "Selected quantity not available in the fridge");
				}
				else {
					currentFridge.deleteFridgeItems(selectedItem, selectedQuantity,false);
					currentUser.initializeFridges();
					JOptionPane.showMessageDialog(null, "Item Deleted Successfully!!");
					System.out.println("ready to delete");
				}
				

			}
		}
	}
	
	public void addItemsFromFridges( JComboBox<Item> comboBox ) {
		HashSet<Integer> itemIdSet = new HashSet<Integer>();
		for( Item item: currentFridge.itemList ) {
			itemIdSet.add(item.itemId);
		}
		for( Integer itemId : itemIdSet ) {
			comboBox.addItem(new Item( itemId ));
		}
		
	}

	public void toTable() {
		try {

			Connection con = Database.getConnection();
			System.out.print("Connection Success");
			String query = "insert into fridgecontents(quantity,price,addedAt,fridgeId,itemId,currentQuantity) values(?,?,?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(query);

			// ResultSet r=stmt.executeQuery("select * from tableName");
			System.out.println(quantity + " " + price + " " + fridgeId + " " + itemId);
			System.out.println(addedAt);

			stmt.setInt(1, quantity);
			stmt.setInt(6, quantity);
			stmt.setFloat(2, price);
			stmt.setDate(3, java.sql.Date.valueOf(addedAt));
			stmt.setInt(4, fridgeId);
			stmt.setInt(5, itemId);

			stmt.executeUpdate();

			System.out.println("Executed");
			con.close();

			JOptionPane.showMessageDialog(null, "item added Successfully!");
			frame1.dispose();
		} catch (Exception ex) {
			System.out.println("toTable" + ex.getMessage());
		}
	}

//	public static void main(String aa[]) {
//		FridgeContents f1 = new FridgeContents(new User(9));
//	}

}
