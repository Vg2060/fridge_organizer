package frontend;

import javax.swing.*;


import customClasses.User;

import java.awt.*;
import java.net.URL;



public class SplashScreen extends JWindow {
	/**
	 * 
	 */
	Image splashScreen;     
	ImageIcon imageIcon;    
	JFrame frame;
	SplashScreen(){
		splashScreen = Toolkit.getDefaultToolkit().getImage("images\\splash.jpeg");
		imageIcon = new ImageIcon(splashScreen);
		setSize(imageIcon.getIconWidth(),imageIcon.getIconHeight()); 
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width-getSize().width)/2; 
		int y = (dim.height-getSize().height)/2; 
		setLocation(x,y);
		setVisible(true); 

	}
	public void paint(Graphics g) {
	      super.paint(g);
	      g.drawImage(splashScreen, 0, 0, this);
	   }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SplashScreen screen = new SplashScreen();
		try {
			Thread.sleep(4000);
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		screen.dispose();
		int currentUser = Login.currentUser();
		if(  currentUser != -1 ) {
			new Dashboard(new User(currentUser));
		}
		else {
			
			try {
				new Login();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("exception in creating object to login class"+e.getMessage());
			}
		}
	}

}
