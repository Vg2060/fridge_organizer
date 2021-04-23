package frontend;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import customClasses.Fridge;
import customClasses.Item;
import customClasses.User;

public class TransferItems extends JFrame implements ActionListener,ItemListener,ChangeListener{
	
	User currentUser;
	JComboBox<Fridge> fromFridgeCombo;
	JComboBox<Fridge> toFridgeCombo;
	JComboBox<Item> itemListCombo;
	JButton checkSpace;
	JSpinner itemQuantitySpinner;
	JButton transfer;
	int itemSize;
	TransferItems( User user ){
		this.currentUser = user;
		setLayout(null);
		setTitle("Transfer Items");
		setSize(new Dimension( 500,400 ));
		setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width-getSize().width)/2; 
		int y = (dim.height-getSize().height)/2; 
		setLocation(x,y);
		setVisible(true);
		JLabel fromFridgeLabel = new JLabel("Transfer From");
		fromFridgeLabel.setBounds(60,60,100,20);
		add(fromFridgeLabel);
		
		fromFridgeCombo = new JComboBox<Fridge>();
		for( Fridge fridge : currentUser.fridges ) {
			fromFridgeCombo.addItem(fridge);
		}
		fromFridgeCombo.addItemListener(this);
		fromFridgeCombo.setBounds(200,60,120,20);
		add(fromFridgeCombo);
		
		JLabel toFridgeLabel = new JLabel("Transfer To");
		toFridgeLabel.setBounds(60,100,100,20);
		add(toFridgeLabel);
		
		toFridgeCombo = new JComboBox<Fridge>();
		for( Fridge fridge : currentUser.fridges ) {
			toFridgeCombo.addItem(fridge);
		}
		toFridgeCombo.setBounds(200,100,120,20);
		toFridgeCombo.addItemListener(this);
		add(toFridgeCombo);
		
		JLabel itemLabel = new JLabel("Select Item to Transfer");
		itemLabel.setBounds(60,140,150,20);
		add(itemLabel);
		
		itemListCombo = new JComboBox<Item>();
		
		try {
			for( Item item : getUniqueItems(currentUser.fridges.get(0).itemList) ) {
				itemListCombo.addItem(item);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "No Fridge Found!!");
			dispose();
		}
		itemListCombo.setBounds(200,140,120,20);
		add(itemListCombo);
		
		JLabel quantityLabel = new JLabel("Select Quantity");
		quantityLabel.setBounds(60,180,120,20);
		add(quantityLabel);
		
		itemQuantitySpinner = new JSpinner();
		itemQuantitySpinner.addChangeListener(this);
		itemQuantitySpinner.setBounds(200,180,120,20);
		SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 50, 1);
		itemQuantitySpinner.setModel(model);
		((DefaultEditor) itemQuantitySpinner.getEditor()).getTextField().setEditable(false);
		add(itemQuantitySpinner);
		
		checkSpace = new JButton("Check Space");
		checkSpace.setBounds(60,220,130,30);
		checkSpace.addActionListener(this);
		add(checkSpace);
		
		transfer = new JButton("Transfer");
		transfer.setBounds(210,220,130,30);
		transfer.addActionListener(this);
		
		
		
		
		
		
	}
	
	private ArrayList<Item> getUniqueItems( ArrayList<Item> itemList ){
		HashSet<Integer> itemIds = new HashSet<Integer>();
		ArrayList<Item> uniqueItems = new ArrayList<Item>();
		for( Item item : itemList ) {
			itemIds.add(item.itemId);
		}
		for( Integer i : itemIds ) {
			uniqueItems.add(new Item(i));
		}
		return uniqueItems;
	}
	
	public static void main(String[] arg) {
		new TransferItems(new User(9));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if( e.getSource() == checkSpace ) {
			int flag = 0;
			Fridge fromFridge = ((Fridge)fromFridgeCombo.getSelectedItem());
			Item selectedItem = (Item)itemListCombo.getSelectedItem();
			int destinationFridgeSpace = ((Fridge)toFridgeCombo.getSelectedItem()).capacity;
			itemSize = (int)itemQuantitySpinner.getValue();
			if(((Fridge)fromFridgeCombo.getSelectedItem())==((Fridge)toFridgeCombo.getSelectedItem())) {
				JOptionPane.showMessageDialog(this, "Source and Destination cannot be the same!");
				flag=1;
			}
			
			if( itemSize > currentUser.countItems(fromFridge.itemList, selectedItem) ) {
				JOptionPane.showMessageDialog(this, "Selected Quantity not available!");
				flag=1;
			}
//			System.out.println(((Fridge)toFridgeCombo.getSelectedItem()).itemList.size()+itemSize);
//			System.out.println(destinationFridgeSpace);
			if( flag==0 && ((Fridge)toFridgeCombo.getSelectedItem()).itemList.size()+itemSize <= destinationFridgeSpace) {
				JOptionPane.showMessageDialog(this, "Space Available at Destination Fridge!");
				add(transfer);
				revalidate();
				repaint();
			}
		}
		else if( e.getSource()==transfer ) {
			Fridge fromFridge = (Fridge)fromFridgeCombo.getSelectedItem();
			Item selectedItem = (Item)itemListCombo.getSelectedItem();
			System.out.println(selectedItem);
			System.out.println(itemSize);
			ArrayList<Item>itemsRemoved = fromFridge.deleteFridgeItems(selectedItem , itemSize,true);
			Fridge destinationFridge = (Fridge)toFridgeCombo.getSelectedItem();
			destinationFridge.addItems(itemsRemoved);
			currentUser.initializeFridges();
//			System.out.println("TRansfer");
			JOptionPane.showMessageDialog(this, "Transfer Complete!");
			dispose();
		}
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		if( e.getSource()== fromFridgeCombo && e.getStateChange()==java.awt.event.ItemEvent.SELECTED ) {
			Fridge selectedFridge = (Fridge)fromFridgeCombo.getSelectedItem();
			remove(itemListCombo);
			itemListCombo.removeAllItems();
			for ( Item item: getUniqueItems(selectedFridge.itemList) ) {
				itemListCombo.addItem(item);
				
			}
			add(itemListCombo);
			remove(transfer);
			revalidate();
			repaint();
		}
		else if( e.getSource()== toFridgeCombo && e.getStateChange()==java.awt.event.ItemEvent.SELECTED ) {
			remove(transfer);
			revalidate();
			repaint();
		}
		
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==itemQuantitySpinner) {
//			System.out.println((Integer)itemQuantitySpinner.getValue());
			remove(transfer);
			revalidate();
			repaint();
		}
	}

}
