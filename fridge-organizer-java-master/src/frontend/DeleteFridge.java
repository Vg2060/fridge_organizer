package frontend;

import java.awt.*;
import javax.swing.*;

import customClasses.User;

import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import database.Database;

public class DeleteFridge extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton b1, b2;
	private JLabel dname;
	private int userid;
	// JTextField ft;
	private String fridgename;
	private JComboBox field1;
	Map<String, Integer> fridgeMap = new HashMap<String, Integer>();
	private User currentUser;

	public DeleteFridge(User user) throws Exception {
		this.currentUser = user;
		setTitle("Delete Fridge");
		setBounds(500, 250, 350, 230);

		// label
		// dname=new JLabel("userid");
		b1 = new JButton("Delete");
		b2 = new JButton("Cancel");
		// ft=new JTextField();
		field1 = new JComboBox();

		// bounds
		// dname.setBounds(50,50,100,30);
		b1.setBounds(50, 100, 100, 30);
		b2.setBounds(200, 100, 100, 30);
		// ft.setBounds(140,50,100,30);

		// add
		add(b1);
		add(b2);
		// add(dname);
		// add(ft);

		setLayout(null);
		setVisible(false);

		userid = currentUser.userId;
		System.out.println(userid);
		isavailable();
		Object[] fields = { "Fridgename", field1, };

		int result = JOptionPane.showConfirmDialog(null, fields, "Delete Fridge", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			deletion();

		} else if (result == JOptionPane.CANCEL_OPTION) {
			field1.removeAllItems();
//          setVisible(true);
		}

//		b1.addActionListener(this);
//		b2.addActionListener(this);

	}

//	 public int userTable(){
//	      try{
//	       
//	    	    Connection con = Database.getConnection();
//	            String userTable="SELECT * FROM users WHERE isLoggedIn=1";
//	            Statement st=con.createStatement();
//	            ResultSet resultOne=st.executeQuery(userTable);
//	            resultOne.next();
//	            userid=resultOne.getInt("userId");
//	            con.close();
//	            return userid;
//	         }
//	        catch(Exception e){
//	            System.out.println(e.getMessage());
//	            //System.out.println("catch");
//	        }
//	       return -1;
//	   }
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) {
			userid = currentUser.userId;
			System.out.println(userid);
			isavailable();
			Object[] fields = { "Fridgename", field1, };

			int result = JOptionPane.showConfirmDialog(null, fields, "Result", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				deletion();

			} else if (result == JOptionPane.CANCEL_OPTION) {
				field1.removeAllItems();
				setVisible(true);
			}

		} else if (e.getSource() == b2) {
			dispose();
		}
	}

	public void isavailable() {
		try {

			Connection con = Database.getConnection();
			String query = "SELECT * FROM fridgelist WHERE userId=?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, userid);
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				System.out.println(result.getString("fridgeName"));
				fridgeMap.put(result.getString("fridgeName"), result.getInt("fridgeId"));
				field1.addItem(result.getString("fridgeName"));
			}
		} catch (Exception ex) {
			System.out.println("isavailable: " + ex.getMessage());
		}
	}

	public void deletion() {
		try {

			Connection con = Database.getConnection();
			System.out.println("Connection Success");
			String query = "DELETE FROM fridgelist WHERE fridgeId=?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, fridgeMap.get((String) field1.getItemAt(field1.getSelectedIndex())));
			stmt.executeUpdate();
			fridgename = (String) field1.getSelectedItem();
			System.out.println(fridgename);
			System.out.println("Executed");
			con.close();

			JOptionPane.showMessageDialog(null, "Fridge deleted Successfully!");
			dispose();
		} catch (Exception ex) {
			System.out.println("addition:" + ex.getMessage());
		}
	}

//	public static void main(String[] args) throws Exception {
//		new DeleteFridge(new User(9));
//
//	}

}
