package frontend;

import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;
import java.sql.*;
import javax.swing.*;

import database.Database;
public class Registration extends JFrame  implements ActionListener,TextListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton register,cancelButton;
	JTextField username,hint;
	JPasswordField password,confirmPassword;
	JLabel usernameLabel,passwordLabel,cpasswordLabel,hintLabel;
	Registration(){
		setTitle("REDBUG-Registration");
		setSize(350,400);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width-getSize().width)/2;
		int y = (dim.height-getSize().height)/2;
		setLayout(null);
		setLocation(x,y);
		setBackground(Color.red);
		setResizable(false);
		//textfields
		username = new JTextField(); 
		password = new JPasswordField();
		confirmPassword = new JPasswordField();
		hint = new JTextField();
		//Labels
		usernameLabel = new JLabel("Username");
		passwordLabel = new JLabel("Password");
		cpasswordLabel = new JLabel("Confirm Password");
		hintLabel = new JLabel("Password Hint");
		//bounds
		usernameLabel.setBounds(60,60,100,20);
		username.setBounds(60,80,200,30);
		passwordLabel.setBounds(60,110,100,20);
		password.setBounds(60,130,200,30);
		cpasswordLabel.setBounds(60,160,150,20);
		confirmPassword.setBounds(60,180,200,30);
		hintLabel.setBounds(60,210,180,20);
		hint.setBounds(60,230,200,30);
		//button panel
		JPanel buttonPane = new JPanel(new GridLayout(1,2));
		register = new JButton("Register");
		register.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);
		buttonPane.add(register);
		buttonPane.setBounds(60,300,200,40);
		//adding to frame
		add(usernameLabel);
		add(username);
		add(passwordLabel);
		add(password);
		add(cpasswordLabel);
		add(confirmPassword);
		add(hintLabel);
		add(hint);
		add(buttonPane);
		setVisible(true);
		
		
	}
	boolean isUsernameUnique(String name){
		try {
			
			Connection con = Database.getConnection();
			String query = "select * from users";
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery(query);
			while( result.next() ) {
				if(result.getString("username").equals(name) ) {
					return false;
				}
			}
			return true;
			
		}
		catch(Exception ex) {
			System.out.println("isUsernameUnique "+ex.getMessage());
		}
		return false;
	}

	void validateForm() {
		String passwordMessage = 	"Password must include "
				+ "\n• 4-15 characters\n"
				+ "• atleast one upper case letter"+
				"\n• atleast one lower case letter"
				+ "\n• atleast one numeric digit";
		String name,pass,cPass,hin;
		name = username.getText();
		pass = new String(password.getPassword());
		cPass = new String(confirmPassword.getPassword());
		hin = hint.getText();
		boolean valid = true;
		Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,15}$");
		Matcher match = pattern.matcher(pass);
		if( name.equals("") || pass.equals("") || cPass.equals("") || hin.equals("") ) {
			JOptionPane.showMessageDialog(this, "Some Fields are empty!","Warning",JOptionPane.WARNING_MESSAGE);
			valid = false;
		}
		if(!isUsernameUnique(name)) {
			JOptionPane.showMessageDialog(this, "Username already exists!","Warning",JOptionPane.WARNING_MESSAGE);
			valid = false;
		}
		if(!pass.equals(cPass)) {
			JOptionPane.showMessageDialog(this, "Password and Confirm Password do not match!","Warning",JOptionPane.WARNING_MESSAGE);
			valid = false;
		}
		if(!match.find()) {
			JOptionPane.showMessageDialog(this, passwordMessage,"Warning",JOptionPane.WARNING_MESSAGE);
			valid = false;
		}
		
		{
		if( valid )	 
				try {
					
					
					Connection con = Database.getConnection();
					System.out.print("Connection Success");
//					String tab = "create table users(id int AUTO_INCREMENT, username varchar(20),password varchar(15),hint varchar(15), PRIMARY KEY(id) )";
					String query = "insert into users(username,password,hint) values(?,?,?)";
					PreparedStatement stmt = con.prepareStatement(query);
					stmt.setString(1, name);
					stmt.setString(2, pass);
					stmt.setString(3,hin);
					stmt.execute();
					System.out.println("Executed");
					con.close();
					
					JOptionPane.showMessageDialog(this, "Registration Successful!");
					dispose();
				}
				catch(Exception e) {
					System.out.println("Validate Form "+e.getMessage());
				}
				
			
		}
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == register ) {
			
			validateForm();
		}
		else if( e.getSource() == cancelButton ) {
			
			dispose();
		}
		
	}
	
	@Override
	public void textValueChanged(TextEvent e) {
		// TODO Auto-generated method stub
		
	}
//	public static void main() {
//		// TODO Auto-generated method stub
//		new Registration();
//	}

}
