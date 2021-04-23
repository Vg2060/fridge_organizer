/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;
import java.text.DecimalFormat;
import javax.swing.*;
import java.util.Date;
import javax.swing.ImageIcon;

import customClasses.User;
import database.Database;

import java.util.Calendar;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.time.Instant;
import java.util.Iterator;
import java.time.*;
//import java.sql.Date;

public class MaintainceReport extends JFrame implements ActionListener,frontend.Report{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel l1;
    JComboBox t1;
    JButton b1,b2;
    JDialog d1;
    JFrame f1=new JFrame("Check Maintenance");
    private User currentUser;
    public MaintainceReport( User user ) {
    	this.currentUser = user;
        
        f1.setLayout(null);
        l1=new JLabel("Fridge Name");
        //t1=new JTextField("Fridge Name");
        b1=new JButton("Generate");
        b2=new JButton("Maintained");
        ArrayList<String> a=new ArrayList<String>();
        a=fridgeName();
        String ar[]=new String[a.size()];
        //Iterator ir=a.iterator();
        int j=0;
        for(String i:a){
            ar[j]=i;
            j++;
        }
        t1=new JComboBox(ar);
       
        //System.out.println(t1.getItemAt(t1.getSelectedIndex()));
        Dimension dimensionToCenter=Toolkit.getDefaultToolkit().getScreenSize();
        int x=(int)((dimensionToCenter.getWidth()-f1.getWidth())/2.6);
        int y=(int)((dimensionToCenter.getHeight()-f1.getHeight())/2.7);
        f1.setLocation(x, y);
        f1.setSize(400,200);
        l1.setBounds(30, 40, 100, 30);
        t1.setBounds(160, 40,200,30);
        b1.setBounds(70,100,100,30);
        b2.setBounds(200,100 ,100 ,30);
        
        b1.addActionListener(this);
        b2.addActionListener(this);
        
        f1.add(l1);
        f1.add(t1);
        f1.add(b1);
        f1.add(b2);
        
        f1.setResizable(false);
        f1.setVisible(true);
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==b1){
            generateReport();
        }
        if(e.getSource()==b2){
            updateMaintainDate();
        }
    }
    
    @Override
     public ArrayList<String> fridgeName(){
        int user=currentUser.userId;
        
        ArrayList <String> FridgeName=new ArrayList<String>();
        try{
            Connection con= Database.getConnection(); //DriverManager.getConnection("jdbc:mysql://localhost/refridgerator","root","");
            Statement st=con.createStatement();
            String fridgeList="select * from fridgelist where userId=?";
            
             PreparedStatement ps=con.prepareStatement(fridgeList);
             ps.setInt(1,user);
            
            ResultSet resultTwo=ps.executeQuery();
            
            while(resultTwo.next()){
                 FridgeName.add(resultTwo.getString("fridgeName"));
                 
            }
            con.close();
            //System.out.println(FridgeId);
            return FridgeName;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    
    }
//    @Override
//    public int userTable(){
//      try{
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection con=DriverManager.getConnection("jdbc:mysql://localhost/refridgerator","root","");
//            con.setAutoCommit(true);
//            Statement st=con.createStatement();
//            String userTable="select * from users where isLoggedIn=1";
//            ResultSet resultOne=st.executeQuery(userTable);
//            resultOne.next();
//            int userId=resultOne.getInt("userId");
//            con.close();
//            return userId;
//         }
//        catch(Exception e){
//            System.out.println(e.getMessage());
//            //System.out.println("catch");
//        }
//       return -1;
//   }
    @Override
   public void generateReport(){
        int user=currentUser.userId;
        JFrame frame = new JFrame("Maintenance Report");
        frame.setLayout(null);
        Dimension dimensionToCenter=Toolkit.getDefaultToolkit().getScreenSize();
        int x=(int)((dimensionToCenter.getWidth()-frame.getWidth())/2.5);
        int y=(int)((dimensionToCenter.getHeight()-frame.getHeight())/3);
        frame.setLocation(x, y);
        frame.setSize(420,400);
        JButton b0=new JButton("ok");
        b0.setBounds(50,260, 100, 30);
        b0.addActionListener((e) -> {
                    if(e.getSource()==b0){
                        frame.dispose();
                    }
                });
        frame.add(b0);
        frame.setResizable(false);
        frame.setVisible(true);
        try{
            Connection con= Database.getConnection(); //DriverManager.getConnection("jdbc:mysql://localhost/refridgerator","root","");
            Statement st=con.createStatement();
            String fridgeList="select * from fridgelist where userId=?";
            
             PreparedStatement ps=con.prepareStatement(fridgeList);
             ps.setInt(1,user);
             // ps.executeUpdate();
            
            
            ResultSet resultTwo=ps.executeQuery();
            
            while(resultTwo.next()){
              if(resultTwo.getString("fridgeName").equals(t1.getItemAt(t1.getSelectedIndex())) ){
                Date date = new Date();  
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");  
                String strDate = formatter.format(date);  
                //System.out.println(strDate);  
                String strData=formatter.format(resultTwo.getDate("maintainDate"));
                //System.out.println(strData);
                Date dateObj1 = formatter.parse(strDate);
		Date dateObj2 = formatter.parse(strData);
                //System.out.println(dateObj2);
                long diff = dateObj1.getTime() - dateObj2.getTime();
                //System.out.println(diff);
                int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
               // System.out.println(diffDays);
                if(diffDays<=10){
                    int maintainAt=30-diffDays;
                    
                    JLabel label1=new JLabel((String)t1.getItemAt(t1.getSelectedIndex())+": "+"Maintained Good");
                    JLabel label2=new JLabel("Maintained Date: "+strData);
                    JLabel label3=new JLabel(maintainAt+" days left for the next");
                    JLabel label4=new JLabel("maintenance.");
                    
                    label1.setBounds(50,100,200,20);
                    label2.setBounds(50,120,200,20);
                    label3.setBounds(50,180,200,20);
                    label4.setBounds(50,200,100,20);
                   
                    frame.add(label1);
                    frame.add(label2);
                    frame.add(label3);
                    frame.add(label4);
                    
                    ImageIcon icon = new ImageIcon("images//oneonr.jpg");
                    JLabel label = new JLabel(icon);
                    label.setBounds(250, 100, 150, 150);
                    frame.add(label);
                   
                    
                }
                if(diffDays>10 && diffDays<=20){
                    int maintainAt=30-diffDays;
                    
                    JLabel label1=new JLabel((String)t1.getItemAt(t1.getSelectedIndex())+": "+"Maintained");
                    JLabel label2=new JLabel("Maintained Date: "+strData);
                    JLabel label3=new JLabel(maintainAt+" days left for the next");
                    JLabel label4=new JLabel("maintenance.");
                    
                    label1.setBounds(50,100,200,20);
                    label2.setBounds(50,120,200,20);
                    label3.setBounds(50,180,200,20);
                    label4.setBounds(50,200,100,20);
                   
                    
                    frame.add(label1);
                    frame.add(label2);
                    frame.add(label3);
                    frame.add(label4);
                    
                    ImageIcon icon = new ImageIcon("images//two2.jpg");
                    JLabel label = new JLabel(icon);
                    label.setBounds(250, 100, 150, 150);
                    frame.add(label); 
                }
                if(diffDays>20 && diffDays<=30){
                    int maintainAt=30-diffDays;
                    
                    JLabel label1=new JLabel((String)t1.getItemAt(t1.getSelectedIndex())+": "+"Bad Maintenance");
                    JLabel label2=new JLabel("Maintained Date: "+strData);
                    JLabel label3=new JLabel(maintainAt+" days left for the next");
                    JLabel label4=new JLabel("maintenance.");
                    
                    label1.setBounds(50,100,200,20);
                    label2.setBounds(50,120,200,20);
                    label3.setBounds(50,180,200,20);
                    label4.setBounds(50,200,100,20);
                   
                    
                    frame.add(label1);
                    frame.add(label2);
                    frame.add(label3);
                    frame.add(label4);
                    
                    ImageIcon icon = new ImageIcon("images//three3.jpg");
                    JLabel label = new JLabel(icon);
                    label.setBounds(250, 100, 150, 150);
                    frame.add(label);
                 }
                 if(diffDays>30){
                    int maintainAt=diffDays-10;
                    
                    JLabel label1=new JLabel((String)t1.getItemAt(t1.getSelectedIndex())+": "+"Worse Maintenance");
                    JLabel label2=new JLabel("Maintained Date: "+strData);
                    JLabel label3=new JLabel(maintainAt+" days got over maintain as");
                    JLabel label4=new JLabel("soon as posible.");
                    
                    label1.setBounds(50,100,200,20);
                    label2.setBounds(50,120,200,20);
                    label3.setBounds(50,180,200,20);
                    label4.setBounds(50,200,100,20);
                   
                    
                    frame.add(label1);
                    frame.add(label2);
                    frame.add(label3);
                    frame.add(label4);
                    
                    ImageIcon icon = new ImageIcon("images//four4.jpg");
                    JLabel label = new JLabel(icon);
                    label.setBounds(250, 100, 150, 150);
                    frame.add(label);
                   
                }
              }
            }
           
            con.close();
            
        }
        
        catch(Exception e){
            System.out.println(e.getMessage());
        }
       // return -1;
    
    }
    @Override
   public ArrayList<Integer> fridgeId(){
        int user=currentUser.userId;
        
        //System.out.println("userId:"+user);
       // int fridgeId[]=new int[10];
        //int incrementor=0;
        ArrayList <Integer> FridgeId=new ArrayList<Integer>();
        try{
            Connection con= Database.getConnection();//DriverManager.getConnection("jdbc:mysql://localhost/refridgerator","root","");
            Statement st=con.createStatement();
            String fridgeList="select * from fridgelist where userId=?";
            
              PreparedStatement ps=con.prepareStatement(fridgeList);
              ps.setInt(1,user);
              
            
              ResultSet resultTwo=ps.executeQuery();
            
            while(resultTwo.next()){
                 FridgeId.add(resultTwo.getInt("fridgeId"));
                 
            }
            con.close();
            return FridgeId;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    
    }
    void updateMaintainDate(){
        ArrayList<Integer> fId=fridgeId();
        ArrayList<String> fName=new ArrayList<String>();
        fName=fridgeName();
       // System.out.println(fId);
        //System.out.println(fName);
        /*Date date = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate= formatter.format(date);*/
        
        JFrame f0=new JFrame("Message");
        f0.setLayout(null);
        f0.setVisible(true);
        Dimension dimensionToCenter=Toolkit.getDefaultToolkit().getScreenSize();
        int x=(int)((dimensionToCenter.getWidth()-f1.getWidth())/1.9);
        int y=(int)((dimensionToCenter.getHeight()-f1.getHeight())/2.2);
        f0.setLocation(x, y);
        f0.setResizable(false);
        f0.setSize(300,200);
        
        try{
            Connection con= Database.getConnection(); //DriverManager.getConnection("jdbc:mysql://localhost/refridgerator","root","");
            //String a[]=new String[2]; 
            LocalDate javaDate1 = LocalDate.now();
            java.sql.Date sqldate1 = java.sql.Date.valueOf(javaDate1);
            //System.out.println(sqldate1);
            Statement st=con.createStatement();
            for(String i:fName){
                if(i.equals((String)t1.getItemAt(t1.getSelectedIndex()))){
                    int irr=fName.indexOf(i);
                    int ir=fId.get(irr);
                    String query="update fridgelist set maintainDate=? where fridgeId=?";
                    PreparedStatement ps=con.prepareStatement(query);
                    ps.setDate(1,sqldate1);
                    ps.setInt(2, ir);
                    ps.executeUpdate();
                    JLabel ll9=new JLabel("Maintain date sucessfully updated");
                    // JLabel l9=new JLabel("until today");
                    JButton b0=new JButton("OK");
                    //ll9.setBounds(50,20,200,23);
                    ll9.setBounds(47,40,200,23);
                    b0.setBounds(100,100,70,30);
                    f0.add(ll9);
                    //f0.add(l9);
                    f0.add(b0);
                    b0.addActionListener((e) -> {
                        if(e.getSource()==b0){
                            f0.dispose();
                        }
                    });
                    //ResultSet resultTwo=ps.executeQuery();
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
//    public static void main(String[] args) {
//         new MaintainceReport(new User(9));
//    }

    
    
}
