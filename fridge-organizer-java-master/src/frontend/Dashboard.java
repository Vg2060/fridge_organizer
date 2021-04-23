package frontend;

import javax.swing.*;
import javax.swing.Box.Filler;

import customClasses.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import java.sql.*;



public class Dashboard extends JFrame implements ActionListener{
	JButton manageFridge, manageItems, recipes,addFridge,transfer;
	JButton organizeFridge,deleteFridge,createItem,listItem,addRecipe;
	JButton suggestRecipe,reports,expenseReport,maintenanceReport,logOut;
	JPanel manageFridgePane,itemPanel,recipePanel,reportPane;
	JLabel fridgeHead,itemPanelhead,recipeHead,reportHead;
	Dimension centerPaneButtonDimension = new Dimension( 150,100 );
	User currentUser;
	Dashboard(User user){
		this.currentUser = user;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dim.width,dim.height);
		setTitle("Dashboard");
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setBackground(Color.white);
		setLayout(new BorderLayout());
		setResizable(false);
		
		JPanel navi = new JPanel();
		navi.setLayout(new BoxLayout(navi, BoxLayout.Y_AXIS));
		navi.setPreferredSize(new Dimension(dim.width/4,dim.height));
		navi.setBackground(Color.red);
		
		navi.add(Box.createRigidArea(new Dimension(1,100)));
		
		manageFridge = new JButton("Manage Fridge");
		manageFridge.setIcon(new ImageIcon("images\\manageFridge.png"));//vg
        validate();//vg
		manageFridge.setAlignmentX(CENTER_ALIGNMENT);
		manageFridge.setMaximumSize(new Dimension(200,100));
		manageFridge.addActionListener(this);
		navi.add(manageFridge);
//		navi.add(Box.createVerticalGlue());
//		navi.add(Box.createRigidArea(new Dimension(1,100)));
		
		manageItems = new JButton("Manage Items");
		manageItems.setIcon(new ImageIcon("images\\organizeFridge.png"));//vg
        validate();//vg
		manageItems.setAlignmentX(CENTER_ALIGNMENT);
		manageItems.setMaximumSize(new Dimension(200,100));
		manageItems.addActionListener(this);
		navi.add(manageItems);
		
//		navi.add(Box.createVerticalGlue());
		
		recipes = new JButton("Recipes");
		recipes.setIcon(new ImageIcon("images\\recipe.png"));//vg
        validate();//vg
		recipes.setMaximumSize(new Dimension(200,100));
		recipes.setAlignmentX(CENTER_ALIGNMENT);
		recipes.addActionListener(this);
		navi.add(recipes);
		
		reports = new JButton("Reports");
		reports.setIcon(new ImageIcon("images\\report.png"));//vg
        validate();//vg
		reports.setMaximumSize(new Dimension(200,100));
		reports.setAlignmentX(CENTER_ALIGNMENT);
		reports.addActionListener(this);
		navi.add(reports);
		
		transfer = new JButton("Transfer");
		transfer.setIcon(new ImageIcon("images\\items.png"));//vg
        validate();//vg
		transfer.setMaximumSize(new Dimension(200,100));
		transfer.setAlignmentX(CENTER_ALIGNMENT);
		transfer.addActionListener(this);
		navi.add(transfer);
		
		navi.add(Box.createRigidArea(new Dimension(3,50)));

		logOut = new JButton("Logout");
		logOut.setIcon(new ImageIcon("images\\transfer.png"));//vg
        validate();//vg
		logOut.setMaximumSize(new Dimension(200,100));
		logOut.setAlignmentX(CENTER_ALIGNMENT);
		logOut.addActionListener(this);
		navi.add(logOut);
		
		add(navi,BorderLayout.WEST);
		
		
		JPanel topPane = new JPanel( new FlowLayout() );
		topPane.setBackground(Color.black);
		JLabel title = new JLabel("RED BUG");
		Font  f4  = new Font(Font. SERIF,  Font.BOLD|Font.ITALIC, 30);//vg
        title.setFont(f4);//vg
        title.setForeground(Color.red);//vg
        title.setHorizontalAlignment(JLabel.CENTER);//vg
        title.setVerticalAlignment(JLabel.CENTER);//vg
		topPane.add(title);
		add(topPane,BorderLayout.NORTH);
		
		manageFridgePane = new JPanel();
		manageFridgePane.setBackground(Color.white);
		manageFridgePane.setLayout(new BoxLayout(manageFridgePane,BoxLayout.Y_AXIS));
		manageFridgePane.setPreferredSize(new Dimension((dim.width/4)*3,dim.height));
		manageFridgePane.setBackground(Color.white);
		manageFridgePane.setVisible(false);
		
		fridgeHead = new JLabel("Manage Fridges");
		 Font  f5  = new Font(Font. SANS_SERIF,  Font.BOLD|Font.ITALIC, 20);//vg
         fridgeHead.setFont(f5);//vg
         fridgeHead.setForeground(Color.black);//vg
		fridgeHead.setAlignmentX(CENTER_ALIGNMENT);
		manageFridgePane.add(fridgeHead);
		
		manageFridgePane.add(Box.createRigidArea(new Dimension(0,100)));
		
		addFridge = new JButton("Add Fridge");
		addFridge.setIcon(new ImageIcon("images\\addFridge.png"));//vg
        validate();//vg
		addFridge.setMaximumSize(new Dimension(200,100));
		addFridge.addActionListener(this);
		addFridge.setAlignmentX(CENTER_ALIGNMENT);
		manageFridgePane.add(addFridge);
		
		manageFridgePane.add(Box.createRigidArea(new Dimension(0,100)));
		
		organizeFridge = new JButton("Organize");
		organizeFridge.setIcon(new ImageIcon("images\\insideFridge.png"));//vg
        validate();//vg
		organizeFridge.setMaximumSize(new Dimension(200,100));
		organizeFridge.setAlignmentX(CENTER_ALIGNMENT);
		organizeFridge.addActionListener(this);
		manageFridgePane.add(organizeFridge);
		
		manageFridgePane.add(Box.createRigidArea(new Dimension(0,100)));
		
		deleteFridge = new JButton("Delete Fridge");
		deleteFridge.setIcon(new ImageIcon("images\\deleteFridge.png"));//vg
        validate();//vg
		deleteFridge.setMaximumSize(new Dimension(200,100));
		deleteFridge.addActionListener(this);
		deleteFridge.setAlignmentX(CENTER_ALIGNMENT);
		manageFridgePane.add(deleteFridge);
		
		itemPanel = new JPanel();
		itemPanel.setBackground(Color.white);
		itemPanel.setPreferredSize(new Dimension((dim.width/4)*3,dim.height));
		itemPanel.setLayout(new BoxLayout(itemPanel,BoxLayout.Y_AXIS));
		itemPanel.setVisible(false);
		
		itemPanelhead = new JLabel("Manage Items");
		Font  f6  = new Font(Font. SANS_SERIF,  Font.BOLD|Font.ITALIC, 20);//vg
        itemPanelhead.setFont(f6);//vg
        itemPanelhead.setForeground(Color.black);//vg
		itemPanelhead.setAlignmentX(CENTER_ALIGNMENT);
		itemPanel.add(itemPanelhead);
		
		itemPanel.add(Box.createRigidArea(new Dimension(0,100)));
		
		createItem = new JButton("Create Item");
		createItem.setIcon(new ImageIcon("images\\createItem.png"));//vg
        validate();//vg
		createItem.setMaximumSize(new Dimension(200,100));
		createItem.setAlignmentX(CENTER_ALIGNMENT);
		createItem.addActionListener(this);
		itemPanel.add(createItem);
		
		itemPanel.add(Box.createRigidArea(new Dimension(0,100)));

		listItem = new JButton("View Items");
		listItem.setIcon(new ImageIcon("images\\viewItems.png"));//vg
        validate();//vg;
		listItem.setMaximumSize(new Dimension(200,100));
		listItem.setAlignmentX(CENTER_ALIGNMENT);
		listItem.addActionListener(this);
		itemPanel.add(listItem);
		
		recipePanel = new JPanel();
		recipePanel.setBackground(Color.white);
		recipePanel.setPreferredSize(new Dimension((dim.width/4)*3,dim.height));
		recipePanel.setLayout(new BoxLayout(recipePanel,BoxLayout.Y_AXIS));
		recipePanel.setVisible(false);
		
		recipeHead = new JLabel("Recipe Menu");
		Font  f7  = new Font(Font. SANS_SERIF,  Font.BOLD|Font.ITALIC, 20);//vg
        recipeHead.setFont(f7);//vg
        recipeHead.setForeground(Color.black);//vg
		recipeHead.setAlignmentX(CENTER_ALIGNMENT);
		recipePanel.add(recipeHead);
		
		recipePanel.add(Box.createRigidArea(new Dimension(0,100)));
		
		addRecipe =  new JButton("Add Recipe");
		addRecipe.setIcon(new ImageIcon("images\\addRecipe.png"));//vg
        validate();//vg;
//		addRecipe.setMaximumSize(centerPaneButtonDimension);
		addRecipe.setMaximumSize(new Dimension(200,100));//vg
		addRecipe.setAlignmentX(CENTER_ALIGNMENT);
		addRecipe.addActionListener(this);
		recipePanel.add(addRecipe);
		
		recipePanel.add(Box.createRigidArea(new Dimension(0,100)));
		
		suggestRecipe = new JButton("Suggest Recipe");
		suggestRecipe.setIcon(new ImageIcon("images\\suggestRecipe.png"));//vg
        validate();//vg;
		suggestRecipe.setMaximumSize(new Dimension(200,100));
		suggestRecipe.setAlignmentX(CENTER_ALIGNMENT);
		suggestRecipe.addActionListener(this);
		recipePanel.add(suggestRecipe);
		

		
		reportPane = new JPanel();
		reportPane.setLayout(new BoxLayout(reportPane,BoxLayout.Y_AXIS));
		reportPane.setPreferredSize(new Dimension((dim.width/4)*3,dim.height));
		reportPane.setBackground(Color.white);
		reportPane.setVisible(false);
		
		reportHead = new JLabel("Reports");
		Font  f8  = new Font(Font. SANS_SERIF,  Font.BOLD|Font.ITALIC, 20);//vg
        reportHead.setFont(f8);//vg
        reportHead.setForeground(Color.black);//vg
		reportHead.setAlignmentX(CENTER_ALIGNMENT);
		reportPane.add(reportHead);
		
		reportPane.add(Box.createRigidArea(new Dimension(0,100)));
		
		expenseReport = new JButton("Expense Report");
		expenseReport.setIcon(new ImageIcon("images\\expenseReport.png"));//vg
        validate();//vg;
		expenseReport.setMaximumSize(new Dimension(300,100));
		expenseReport.setAlignmentX(CENTER_ALIGNMENT);
		expenseReport.addActionListener(this);
		reportPane.add(expenseReport);
		
		reportPane.add(Box.createRigidArea(new Dimension(0,100)));
		
		maintenanceReport = new JButton("Maintenance Report");
		maintenanceReport.setIcon(new ImageIcon("images\\maintenanceReport.png"));//vg
        validate();//vg;
		maintenanceReport.setMaximumSize(new Dimension(300,100));
		maintenanceReport.setAlignmentX(CENTER_ALIGNMENT);
		maintenanceReport.addActionListener(this);
		reportPane.add(maintenanceReport);
		

		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if( e.getSource() == manageFridge ) {
			remove(itemPanel);
			remove(reportPane);
			remove(recipePanel);
			manageFridgePane.setVisible(true);
			add(manageFridgePane,BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if( e.getSource() == manageItems ) {
			remove(manageFridgePane);
			remove(recipePanel);
			remove(reportPane);
			itemPanel.setVisible(true);
			add(itemPanel, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if( e.getSource() == recipes ) {
			remove(itemPanel);
			remove(manageFridgePane);
			remove(reportPane);
			recipePanel.setVisible(true);
			add(recipePanel, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if( e.getSource() == reports ) {
			remove(itemPanel);
			remove(manageFridgePane);
			remove(recipePanel);
			reportPane.setVisible(true);
			add(reportPane,BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		else if( e.getSource()==transfer ) {
			new TransferItems(currentUser);
		}
		else if( e.getSource() == logOut ) {
			currentUser.logout();
			JOptionPane.showMessageDialog(this, "Logged Out Successfully!");
			dispose();
			try {
				
				new Login();
			}
			catch (Exception ex) {
				System.out.println(" dashboard logout "+ex.getMessage());
			}
		}
		else if(e.getSource()==addRecipe) {
			new AddRecipe(currentUser);
		}
		else if( e.getSource()==suggestRecipe ) {
			new SuggestRecipe(currentUser);
		}
		else if( e.getSource()==listItem ) {
			new ListItems();
		}
		else if( e.getSource()==addFridge ) {
			try {
				new AddFridge(currentUser);
				System.out.println( currentUser.fridges );
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println("Exception in add fridge"+e1.getMessage());
			}
		}
		else if( e.getSource()==deleteFridge ) {
			try {
				new DeleteFridge(currentUser);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println("Exception in delete fridge"+e1.getMessage());
			}
		}
		else if( e.getSource()==expenseReport ) {
			new ExpenditureReport(currentUser);
		}
		else if( e.getSource()== maintenanceReport ) {
			new MaintainceReport(currentUser);
		}
		else if( e.getSource()==createItem ) {
			new CreateItem();
		}
		else if( e.getSource()==organizeFridge ) {
			new FridgeContents(currentUser);
		}
	}
	
	
//	public static void main(String[] args) {
//	}

}



