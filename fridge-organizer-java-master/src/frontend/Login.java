package frontend;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import customClasses.User;
import database.Database;

import java.sql.*;
import hashing.Hashing;

import java.io.File;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.*;
import java.awt.image.BufferedImage;


;
public class Login extends JFrame implements ActionListener{
	JPasswordField password;
	JTextField username;
	JButton loginButton;
	Login() throws Exception{
		//screen size
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dim.width,dim.height);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setBackground(Color.white);
		setLayout(new BorderLayout());
		setResizable(false);
		setTitle("REDBUG Login");
		//panels
		JPanel imagePanel = new JPanel();
		imagePanel.setBackground(Color.white);
		imagePanel.setPreferredSize(new Dimension((dim.width/4)*3, dim.height));
		JPanel rightPane = new JPanel();
		rightPane.setBackground(Color.white);
		rightPane.setLayout(new GridLayout(3,1));
		rightPane.setPreferredSize(new Dimension( dim.width/4,dim.height ));
		//imagePane
		BufferedImage buffImage = ImageIO.read(new File("images\\splashScreen.jpeg"));
		JLabel image = new JLabel(new ImageIcon(buffImage));
		image.setPreferredSize(new Dimension((dim.width/4)*3, dim.height));
		imagePanel.setLayout(new BorderLayout());
		imagePanel.add(image,BorderLayout.WEST);
		setResizable(true);
		//RightPane-->formPanel
		JPanel formPanel = new JPanel();
		formPanel.setBackground(Color.white);
		BoxLayout box = new BoxLayout(formPanel,BoxLayout.Y_AXIS);
		formPanel.setLayout(box);
		//formPane elements
		username = new JTextField();
		username.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10),  new EtchedBorder()));
		username.setPreferredSize(new Dimension(200,30));
		password = new JPasswordField();
		password.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10),  new EtchedBorder()));
		password.setPreferredSize(new Dimension(200,30));
		//RightPane--->formPane--->infoPane
		BufferedImage infoImg = ImageIO.read(new File("D:\\info.png"));
		JPanel infoPane = new JPanel(new GridLayout(1,5));
		infoPane.setBackground(Color.white);
		JLabel infoPic = new JLabel(new ImageIcon(infoImg));
		infoPic.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		infoPic.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				String hint = getHint();
				JOptionPane.showMessageDialog(getObj(), "Hint: "+hint,"Hint",JOptionPane.WARNING_MESSAGE);
			}
		});
		infoPane.add(infoPic);
		infoPane.add(new JLabel());
		infoPane.add(new JLabel());
		infoPane.add(new JLabel());
		infoPane.add(new JLabel());
		//Rightpanel -->FormPane-->ButtonPane
		JPanel buttonPane = new JPanel(new GridLayout(1,3));
		buttonPane.setBackground(Color.white);
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		buttonPane.add(new JLabel());
		buttonPane.add(new JLabel());
		buttonPane.add(loginButton);
		
		JLabel hyperlink = new JLabel("New User?..Register Here");
		hyperlink.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Registration reg = new Registration();
//				new JDialog(reg,"Registration");
			}
		});
		hyperlink.setForeground(Color.BLUE.darker());
		hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		BufferedImage userImg = ImageIO.read(new File("images\\profileSmall.png"));
		JLabel userPic = new JLabel(new ImageIcon(userImg));
		formPanel.add(userPic);
		formPanel.add(new JLabel("Username"));
		formPanel.add(username);
		formPanel.add(Box.createRigidArea(new Dimension(0,5)));
		formPanel.add(new JLabel("Password"));
		formPanel.add(password);
		formPanel.add(infoPane);
		formPanel.add(buttonPane);
		formPanel.add(hyperlink);
		rightPane.add(new JLabel());
		rightPane.add(formPanel);
		rightPane.add(new JLabel());
		
		add(rightPane,BorderLayout.CENTER);
		add(imagePanel,BorderLayout.WEST);
		
		
		setVisible(true);
		
	}
	
	Login getObj() {
		return this;
	}
	
	
	private void loginUser() {
		User user = new User();
		if( user.loginUser(username.getText(), new String(password.getPassword())) ) {
			
			JOptionPane.showMessageDialog(this, "Login Successful!!");
			dispose();
			new Dashboard(user);
		}
		else {
			JOptionPane.showMessageDialog(this, "Invalid Login Credentials!");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==loginButton) {
			loginUser();
		}
		
	}
	
	public static int currentUser() {
		
		Connection con = Database.getConnection();
		String sql = "select * from users where isLoggedIn=1";
		try {
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			result.next();
			int userId = result.getInt("userId");
			if ( userId >0) {
				return userId;
			}
			
		}
		catch(Exception ex) {
			System.out.println("current user "+ ex.getMessage());
		}
		return -1;
	}
	
	private String getHint() {
		String hint = "No hint available";
		try {
			String name = username.getText();
			Connection con = Database.getConnection();
			String sql = "select * from users where username=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, name);
			ResultSet result = stmt.executeQuery();
			result.next();
			hint = result.getString("hint");
			con.close();
			
		}
		catch( Exception ex ) {
			System.out.println("Hint "+ex.getMessage());
		}
		
		
		
		return hint;
	}
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		try {
//			Login login = new Login();
////			String hash = Hashing.getHash("akilA1");
////			System.out.println(hash);
////			System.out.println( Hashing.validatePassword("akilA1", hash) );
//		}
//		catch(Exception ex) {
//			System.out.println(" Login "+ex.getMessage());
//		}
//	}





}
