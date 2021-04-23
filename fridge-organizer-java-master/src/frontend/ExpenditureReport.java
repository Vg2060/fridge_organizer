
package frontend;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import customClasses.Item;
import customClasses.User;
import database.Database;
import mail.MailService;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class ExpenditureReport implements ActionListener, frontend.Report {
	JLabel l1, l2, l3;
	JComboBox t1;
	public ArrayList<Item> itemList;
	JButton b1;
	JDialog d1;
	JFrame f1 = new JFrame("Expenditure Report");
	private String reportContent;
	UtilDateModel modelFrom = new UtilDateModel();
	Properties p = new Properties();
	JDatePanelImpl datePanelFrom = new JDatePanelImpl(modelFrom);
	JDatePickerImpl datePickerFrom = new JDatePickerImpl(datePanelFrom, new DateLabelFormatter());

	UtilDateModel modelTo = new UtilDateModel();
	JDatePanelImpl datePanelTo = new JDatePanelImpl(modelTo);
	JDatePickerImpl datePickerTo = new JDatePickerImpl(datePanelTo, new DateLabelFormatter());
	private User currentUser;
	private String[] tableHead = { "Item Name","Quantity", "Price" };
	JScrollPane scroll;
	private String[][] tableData;
	JTable table;
	public ExpenditureReport(User user) {

		this.currentUser = user;

		f1.setLayout(null);
		l1 = new JLabel("Fridge Name");
		// t1=new JTextField("Fridge Name");
		b1 = new JButton("generate");
		ArrayList<String> a = new ArrayList<String>();
		// ArrayList<Date> b=new ArrayList<Date>();
		// b=gotoFridgeContentsDate();

		// UtilDateModel modelFrom = new UtilDateModel();
		// modelFrom.setDate(30,07,2020);
//        p.put("text.today","Today" );
//        p.put("text.month", "Month");
//        p.put("text.year", "Year");
		datePickerFrom.setBounds(170, 90, 200, 30);
		f1.add(datePickerFrom);

		// modelTo.setDate(30,07,2020);
		datePickerTo.setBounds(170, 140, 200, 30);
		f1.add(datePickerTo);

		a = fridgeName();
		String ar[] = new String[a.size()];
		int j = 0;
		for (String i : a) {
			ar[j] = i;
			j++;
		}
		t1 = new JComboBox(ar);
		/*
		 * String ar1[]; ar1 = new String[b.size()]; int p=0;
		 * 
		 * for(Date y:b){ //b.toString(); DateFormat af=new
		 * SimpleDateFormat("yyyy-MM-dd"); ar1[p]=af.format(y); //
		 * System.out.println(ar1[p]); p++; } t2=new JComboBox(ar1); t3=new
		 * JComboBox(ar1);
		 */
		l2 = new JLabel("From Date");
		l3 = new JLabel("To Date");
		// System.out.println(t1.getItemAt(t1.getSelectedIndex()));
		Dimension dimensionToCenter = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimensionToCenter.getWidth() - f1.getWidth()) / 2.5);
		int y = (int) ((dimensionToCenter.getHeight() - f1.getHeight()) / 3);
		f1.setLocation(x, y);
		f1.setSize(440, 350);
		l1.setBounds(40, 40, 100, 30);
		t1.setBounds(170, 40, 200, 30);
		l2.setBounds(40, 90, 100, 30);
		// t2.setBounds(170,90,200,30);
		l3.setBounds(40, 140, 100, 30);
		// t3.setBounds(170, 140,200, 30);
		b1.setBounds(140, 200, 100, 50);
		b1.addActionListener(this);

		f1.add(l1);
		f1.add(t1);
		// f1.add(t2);
		// f1.add(t3);
		f1.add(b1);
		f1.add(l2);
		f1.add(l3);

		f1.setResizable(false);
		f1.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) {
			generateReport();
		}
	}

	@Override
	public void generateReport() {
		ArrayList<Integer> fridgeId = fridgeId();
		ArrayList<String> fridgeName = fridgeName();
		JFrame f0 = new JFrame("Message");
		f0.setLayout(null);
		f0.setSize(500, 400);
		Dimension dimensionToCenter = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimensionToCenter.getWidth() - f0.getWidth()) / 1.75);
		int y = (int) ((dimensionToCenter.getHeight() - f0.getHeight()) / 1.7);
		f0.setLocation(x, y);
		// System.out.println(fridgeId);
		// System.out.println(fridgeName);
		Float sum = null;
		try {
//            Date date = new Date();  
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");  
//            String strDate = formatter.format(date);  
			// System.out.println(strDate);
			Connection con = Database.getConnection();// DriverManager.getConnection("jdbc:mysql://localhost/refridgerator","root","");
			Statement st = con.createStatement();
			// String fromDate=(String) t2.getItemAt(t2.getSelectedIndex());
			// System.out.println(fromDate);
			// String toDate=(String) t3.getItemAt(t3.getSelectedIndex());
			// System.out.println(toDate);
			String fromDate = datePickerFrom.getJFormattedTextField().getText();
			String toDate = datePickerTo.getJFormattedTextField().getText();
			// System.out.println(fromDate);
			// System.out.println(toDate);

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


			Date dateObj1 = formatter.parse(fromDate);
			System.out.println(dateObj1);
			Date dateObj2 = formatter.parse(toDate);
			System.out.println(dateObj2);
			long diff = dateObj2.getTime() - dateObj1.getTime();
			System.out.println(diff);
			int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
			System.out.println(diffDays);
			if (diffDays < 0) {
				JOptionPane.showMessageDialog(null, "Enter a valid Date!");
				return;
//				JLabel ll9 = new JLabel("Enter a valid Date");
//				JButton b0 = new JButton("OK");
//				ll9.setBounds(130, 40, 300, 23);
//				b0.setBounds(200, 100, 70, 30);
//				f0.add(ll9);
//				f0.add(b0);
//				b0.addActionListener((e) -> {
//					if (e.getSource() == b0) {
//						f0.dispose();
//					}
//				});

			} else {
				for (String i : fridgeName) {
					if (i.equals((String) t1.getItemAt(t1.getSelectedIndex()))) {
						int irr = fridgeName.indexOf(i);
						int ir = fridgeId.get(irr);
						String query = "select * from fridgecontents where addedAt between ? and ? and fridgeId=?";
						PreparedStatement ps = con.prepareStatement(query);
						ps.setString(1, fromDate);
						ps.setString(2, toDate);
						ps.setInt(3, ir);
						ResultSet rs = ps.executeQuery();
						sum = 0.0f;
						reportContent = "ItemName\tQuantity\tPrice\n";
						 itemList = Database.getItemList();
						 tableData = new String[50][3];
						 int tableIndex=0;
						while (rs.next()) {
							String itemName = getNameItem( rs.getInt("itemId") );
							String itemQuantity = String.valueOf(rs.getInt("quantity"));
							String price = String.valueOf(rs.getFloat("price"));  
							tableData[tableIndex][0] = itemName;
							tableData[tableIndex][1] = itemQuantity;
							tableData[tableIndex][2] = price;
							tableIndex++;
							reportContent+=itemName+"\t\t"+itemQuantity+"\t\t"+price+"\n";
							sum = sum + rs.getFloat("price");
						}
						
						if (sum > 0.0f) {
							String fridgeNameString = (String) t1.getItemAt(t1.getSelectedIndex());
							JLabel ll9 = new JLabel("Total Expenditure Spent on ");
							JLabel l9 = new JLabel(
									fridgeNameString + " is Rs." + sum);
							JButton b0 = new JButton("OK");
							System.out.println(reportContent);
							table = new JTable(tableData,tableHead);
							scroll = new JScrollPane(table);
							scroll.setBounds(50,105,350,130);
							ll9.setBounds(150, 20, 200, 23);
							l9.setBounds(160, 50, 200, 23);
							b0.setBounds(330, 245, 70, 30);
							f0.add(ll9);
							f0.add(scroll);
							f0.add(l9);
							f0.add(b0);
							b0.addActionListener((e) -> {
								if (e.getSource() == b0) {
									JTextField mailField = new JTextField();
									Object[] fields = { "Send Report to", mailField };
									int result = JOptionPane.showConfirmDialog(null, fields, "Email Report", JOptionPane.OK_CANCEL_OPTION);
									if(result == JOptionPane.OK_OPTION) {
										String emailId = mailField.getText();
										MailService.sendMail(emailId, "Expense Report: "+fridgeNameString, reportContent);
										JOptionPane.showMessageDialog(f0, "Mail Sent Successfully!");
									}
									f0.dispose();
									f1.dispose();
								}
							});
						}
						if (sum == 0.0f) {
							JLabel ll9 = new JLabel("No contents added in the given dates!");
							// JLabel l9=new JLabel("until today");
							JButton b0 = new JButton("OK");
							// ll9.setBounds(50,20,200,23);
							ll9.setBounds(130, 40, 300, 23);
							b0.setBounds(200, 100, 70, 30);
							f0.add(ll9);
							// f0.add(l9);
							f0.add(b0);
							b0.addActionListener((e) -> {
								if (e.getSource() == b0) {
									f0.dispose();
								}
							});

						}

					}
				}
			}

			f0.setResizable(false);
			f0.setVisible(true);
			con.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	private String getNameItem( int itemId ) {
		for( Item item : itemList ) {
			if ( item.itemId==itemId ) {
				return item.toString();
			}
		}
		return "Invalid item Name";
	}
	

	@Override
	public ArrayList<Integer> fridgeId() {
		int user = currentUser.userId;

		// System.out.println("userId:"+user);
		// int fridgeId[]=new int[10];
		// int incrementor=0;
		ArrayList<Integer> FridgeId = new ArrayList<Integer>();
		try {
			Connection con = Database.getConnection();// DriverManager.getConnection("jdbc:mysql://localhost/refridgerator","root","");
			Statement st = con.createStatement();
			String fridgeList = "select * from fridgelist where userId=?";

			PreparedStatement ps = con.prepareStatement(fridgeList);
			ps.setInt(1, user);

			ResultSet resultTwo = ps.executeQuery();

			while (resultTwo.next()) {
				FridgeId.add(resultTwo.getInt("fridgeId"));

			}
			con.close();
			return FridgeId;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;

	}

	@Override
	public ArrayList<String> fridgeName() {
		int user = currentUser.userId;
		ArrayList<String> FridgeName = new ArrayList<String>();
		try {
			Connection con = Database.getConnection();// DriverManager.getConnection("jdbc:mysql://localhost/refridgerator","root","");
			Statement st = con.createStatement();
			String fridgeList = "select * from fridgelist where userId=?";

			PreparedStatement ps = con.prepareStatement(fridgeList);
			ps.setInt(1, user);

			ResultSet resultTwo = ps.executeQuery();

			while (resultTwo.next()) {
				FridgeName.add(resultTwo.getString("fridgeName"));

			}
			con.close();
			// System.out.println(FridgeId);
			return FridgeName;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;

	}

//	ArrayList<Date> gotoFridgeContentsDate() {
//		ArrayList<Integer> al = new ArrayList<Integer>();
//		al = fridgeId();
//		// System.out.println(al);
//		ArrayList<Date> all = new ArrayList<Date>();
//		try {
//			Connection con = Database.getConnection(); // DriverManager.getConnection("jdbc:mysql://localhost/refridgerator","root","");
//			Statement st = con.createStatement();
//			for (int i : al) {
//				String fridgeContents = "select *from fridgecontents where fridgeId=?";
//				PreparedStatement ps = con.prepareStatement(fridgeContents);
//				ps.setInt(1, i);
//				ResultSet rs = ps.executeQuery();
//				// ArrayList<Date> all=new ArrayList<Date>();
//				while (rs.next()) {
//					all.add(rs.getDate(5));
//				}
//
//				return all;
//			}
//
//			con.close();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		// System.out.println("yea");
//		return null;
//	}

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
//	public static void main(String[] args) {
//		// new ExpenditureReport().goToFridgeContents();
//		ExpenditureReport expenditureReport = new ExpenditureReport(new User(9));
//	}

}

class DateLabelFormatter extends AbstractFormatter {

	private String datePattern = "yyyy-MM-dd";
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	@Override
	public Object stringToValue(String text) throws ParseException {
		return dateFormatter.parseObject(text);
	}

	@Override
	public String valueToString(Object value) throws ParseException {
		if (value != null) {
			Calendar cal = (Calendar) value;
			return dateFormatter.format(cal.getTime());
		}

		return "";
	}

}
//select * from fridgecontents where addedAt between ? and ? and fridgeName=?;
//setString(1,from)
//setString(2,to)
//setString(3,fridgeName)
//while(rs.next())
//sum=sum+getFloat(5)
//SELECT * FROM fridgecontents WHERE addedAt BETWEEN '2020-07-12' AND CURRENT_DATE() AND fridgeId=2

/*
 * boolean a = d3.after(d1); System.out.println("Date d3 comes after " +
 * 
 * "date d2: " + a);
 * 
 * 
 * UtilDateModel model = new UtilDateModel(); model.setDate(30,07,2020);
 * JDatePanelImpl datePanel = new JDatePanelImpl(model); JDatePickerImpl
 * datePicker = new JDatePickerImpl(datePanel); datePicker.setBounds(
 * 10,10,50,20 ); f1.add( datePicker );
 * 
 * UtilDateModel model = new UtilDateModel(); model.setDate(30,07,2020);
 * JDatePanelImpl datePanel = new JDatePanelImpl(model); JDatePickerImpl
 * datePicker = new JDatePickerImpl(datePanel); datePicker.setBounds(
 * 10,10,50,20 ); f1.add( datePicker );
 * 
 * String string = "January 2, 2010"; DateFormat format = new
 * SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH); Date date =
 * format.parse(string); System.out.println(date); // Sat Jan 02 00:00:00 GMT
 * 2010
 */
