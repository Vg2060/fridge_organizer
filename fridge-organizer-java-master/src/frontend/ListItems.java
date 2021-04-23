package frontend;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import customClasses.Item;
import database.Database;

public class ListItems extends JFrame implements ActionListener{
	
	JTable table;
	String[] tableHead = { "Item Name", " Best before (Days)" };
	JScrollPane scroll;
	JButton okButton;
	ListItems(){
		setTitle("Item List");
		setResizable(false);
		setSize(new Dimension(550,440));
		setLayout(null);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width-getSize().width)/2; 
		int y = (dim.height-getSize().height)/2; 
		setLocation(x,y);
		setVisible(true);
		
		ArrayList<Item> itemList = Database.getItemList();
		
		if( itemList.size()==0 ) {
			JOptionPane.showMessageDialog(this, "No Items defined yet!");
			dispose();
		}
		
		String[][] itemsData = new String[itemList.size()][2];
		int index=0;
		for( Item i : itemList ) {
			itemsData[index][0] = i.toString();
			itemsData[index][1] = String.valueOf(i.bestBeforeDays);
			index++;
		}
		
		table = new JTable(itemsData,tableHead){
			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
		};
		scroll = new JScrollPane(table);
		scroll.setBounds(30,30,470,300);
		add(scroll);
		
		okButton = new JButton("OK");
		okButton.setBounds(400,350,100,30);
		okButton.addActionListener(this);
		add(okButton);
		
		
		
		
		
	}
	
//	public static void main(String[] arg) {
//		new ListItems();
//	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource()==okButton) {
			dispose();
		}
	}
	
}
