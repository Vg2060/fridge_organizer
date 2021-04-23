package frontend;

import javax.swing.*;

import customClasses.User;

import java.sql.*;
import database.Database;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class AddFridge extends JFrame implements ActionListener {
	/**
	 * 
	 */
	User currentUser;
	private JLabel name, size;
	// private JTextField nf;
	private JComboBox sf;
	private JButton b1, b2;
	private String size1[] = { "small", "medium", "large" };
	private String fridgename;
	private int userid, capacity, a;

	AddFridge( User user )  {
		this.currentUser = user;
		setTitle("Add Fridge");
		setBounds(500, 250, 350, 230);
		setLayout(null);
		setVisible(false);
		/*
		 * Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); int x =
		 * (dim.width-getSize().width); int y = (dim.height-getSize().height);
		 * setSize(x,y); setLayout(null); setResizable(false);
		 */

		// label
		
		sf = new JComboBox(size1);
		sf.setSelectedItem(null);
		b1 = new JButton("Add");
		b2 = new JButton("Cancel");

		// bounds
		userid = currentUser.userId;
		JTextField field1 = new JTextField();
		sf.setSelectedItem(null);
		Object[] fields = { "Fridgename", field1, "size", sf, };

		int result = JOptionPane.showConfirmDialog(null, fields, "Add Fridge", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) { 
			fridgename = field1.getText();
			String s = (String) sf.getSelectedItem();
			capacity = getoption(s);
			System.out.println(userid);
			if (isavailable()) {
				addition();
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, fridgename + " is already added to user: " + userid + " !");
			}
		}
		//
		
		


		
//		add(b1);
//		add(b2);
		
	}

//	public int userTable() {
//		try {
//
//			Connection con = Database.getConnection();
//			String userTable = "SELECT * FROM users WHERE isLoggedIn=1";
//			Statement st = con.createStatement();
//			ResultSet resultOne = st.executeQuery(userTable);
//			resultOne.next();
//			userid = resultOne.getInt("userId");
//			con.close();
//			return userid;
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			// System.out.println("catch");
//		}
//		return -1;
//	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getSource() == b1) {
			userid = currentUser.userId;
			JTextField field1 = new JTextField();
			sf = new JComboBox(size1);
			sf.setSelectedItem(null);
			Object[] fields = { "Fridgename", field1, "size", sf, };

			int result = JOptionPane.showConfirmDialog(null, fields, "Result", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) { 
				fridgename = field1.getText();
				String s = (String) sf.getSelectedItem();
				capacity = getoption(s);
				System.out.println(userid);
				if (isavailable()) {
					addition();
				} else {
					JOptionPane.showMessageDialog(null, fridgename + " is already added to user: " + userid + " !");
				}
			}
		} else if (a.getSource() == b2) {
			dispose();
		}
	}

	public void addition() {
		try {
			Connection con = Database.getConnection();
			System.out.println("Connection Success");
			String query = "INSERT INTO fridgelist(fridgeName,maxSize,userId,maintainDate) VALUES(?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(query);
			// int count=stmt.executeUpdate();
			// System.out.println(count);
			System.out.println(fridgename + capacity + userid);
			stmt.setString(1, fridgename);
			stmt.setInt(2, capacity);
			stmt.setInt(3, userid);
			LocalDate date = LocalDate.now();
			stmt.setDate(4, java.sql.Date.valueOf(date));
			System.out.println("Executed");
			stmt.executeUpdate();
			con.close();

			JOptionPane.showMessageDialog(null, "Fridge added Successfully!");
			currentUser.initializeFridges();
			dispose();
		} catch (Exception ex) {

//	    	JOptionPane.showMessageDialog(null, "Fridgename is already added");
			System.out.println("addition:" + ex.getMessage().toString());
		}
	}

	boolean isavailable() {
		try {
			System.out.println(userid);
			Connection con = Database.getConnection();
			String query = "SELECT * FROM fridgelist WHERE userId=?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, userid);
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				System.out.println(result.getString("fridgeName"));
				if (result.getString("fridgeName").equals(fridgename)) {
					return false;
				}
			}
			System.out.println("returning true");
			return true;
		} catch (Exception ex) {
			System.out.println("isavailable: " + ex.getMessage());
		}
		return false;
	}

	public int getoption(String s) {
		if (s == "small") {
			a = 100;
		} else if (s == "medium") {
			a = 200;
		} else if (s == "large") {
			a = 300;
		}
		return a;
	}

//	public static void main(String[] args) {
//			new AddFridge(new User(9));
//		
//	}

}
