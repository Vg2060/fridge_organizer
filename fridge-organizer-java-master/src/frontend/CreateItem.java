package frontend;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.Border;
import java.sql.*;
import database.Database;


public class CreateItem
{
    String itemName;
            int bestBeforeDays;
            JFrame frame1;
        CreateItem(){
//         frame1=new JFrame("load items");
//        Dimension ScreenSize=Toolkit.getDefaultToolkit().getScreenSize();
//        int h=ScreenSize.height;
//        int w=ScreenSize.width;
//        frame1.setSize(w,h);
//        frame1.setVisible(false); 
        
        
        //akil
        JTextField field1=new JTextField();
        JTextField field2=new JTextField();
        Object[] fields={
        "Enter item name",field1,
        "Enter No.of.days to expire",field2,
        };
             
        int result=JOptionPane.showConfirmDialog(null,fields,"Create Item",JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
        itemName=field1.getText();
        bestBeforeDays=Integer.parseInt(field2.getText());
        if(isItemNameNew())
        {
            ijdbc();
        }
        else{
        JOptionPane.showMessageDialog(null, itemName+" is already added!");
        }
        }
        //akil
        
        
//        JPanel panelNorth=new JPanel();
//        JPanel panelSouth=new JPanel();
//        JPanel panelWest=new JPanel();
//        JPanel panelCenter=new JPanel();
//        
//        panelNorth.setPreferredSize(new Dimension(640,100));
//        panelSouth.setPreferredSize(new Dimension(640,100));
//        panelWest.setPreferredSize(new Dimension(w/4,480));
//        panelCenter.setPreferredSize(new Dimension((w*3)/4,480));
//        //Border border = BorderFactory.createLineBorder(Color.BLACK);
//        //panelWest.setBorder(border);
//        //panelWest.setPreferredSize(new Dimension(150, 100));
//       
//        panelNorth.setBackground(Color.BLACK);
//        panelSouth.setBackground(Color.BLACK);
//        panelWest.setBackground(Color.LIGHT_GRAY);
//        panelCenter.setBackground(Color.white);
//        JLabel label1=new JLabel("LOAD ITEMS");
//        Font  f4  = new Font(Font.DIALOG_INPUT,  Font.BOLD|Font.ITALIC, 48);
//        label1.setFont(f4);
//        label1.setForeground(Color.white);
//        label1.setHorizontalAlignment(JLabel.CENTER);
//        label1.setVerticalAlignment(JLabel.CENTER);
//        
//        JButton button1=new JButton("Add Item"); 
//        button1.setBounds(50,100,95,30);
//        button1.addActionListener(new ActionListener(){  
//        public void actionPerformed(ActionEvent e){
//            
//            JTextField field1=new JTextField();
//            JTextField field2=new JTextField();
//            Object[] fields={
//            "Enter item name",field1,
//            "Enter No.of.days to expire",field2,
//            };
//                 
//            int result=JOptionPane.showConfirmDialog(null,fields,"Result",JOptionPane.OK_CANCEL_OPTION);
//            if (result == JOptionPane.OK_OPTION) {
//            itemName=field1.getText();
//            bestBeforeDays=Integer.parseInt(field2.getText());
//            if(isItemNameNew())
//            {
//                ijdbc();
//            }
//            else{
//            JOptionPane.showMessageDialog(null, itemName+" is already added!");
//            }
//            }
//          
//
//        }  
//    });  
//        
//        frame1.add(panelNorth,BorderLayout.NORTH);
//        frame1.add(panelSouth,BorderLayout.SOUTH);
//        frame1.add(panelWest,BorderLayout.WEST);
//        frame1.add(panelCenter,BorderLayout.CENTER);
//        panelNorth.add(label1);
//        panelWest.add(button1);
     
        }
        
        public void ijdbc()
           {
                try {

                Connection con = Database.getConnection();
		System.out.print("Connection Success");
                String query = "insert into items(itemName,bestBeforeDays) values(?,?)";
	        PreparedStatement stmt = con.prepareStatement(query);
               
		stmt.setString(1,itemName);
		stmt.setInt(2, bestBeforeDays);
	        stmt.execute();
		System.out.println("Executed");
		con.close();

		JOptionPane.showMessageDialog(null, "item added Successfully!");
		frame1.dispose();
		}
	    catch(Exception ex) {
		System.out.println("ijdbc "+ex.getMessage());
		}
           }
        
        
        boolean isItemNameNew(){
		try {

			Connection con = Database.getConnection();
			String query = "select * from items";
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery(query);
			while( result.next()) {
				if(result.getString("itemName").equals(itemName) ) {
					return false;
				}
                               
			}
			return true; 

		}
		catch(Exception ex) {
			System.out.println("isItemNameNew "+ex.getMessage());
		}
		return false;
	}
        
            
//      public static void main(String[] args) {
//    
//		
//		try {
//			CreateItem i1= new CreateItem();
//
//		}
//		catch(Exception ex) {
//			System.out.println("items "+ex.getMessage());
//		}
//	}  
        
        
    }
     
    



