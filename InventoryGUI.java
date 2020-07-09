import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


class InventoryGUI implements ActionListener {

  private JFrame mainframe;
  private JLabel lbl_P_Id;
  private JLabel lbl_p_type;
  private JLabel lbl_quantity;
  private JLabel lbl_price_per_unit;
  private JTextField txt_P_id;
  private JComboBox ddl_quantity;
  private JComboBox ddl_p_type;
  private JTextField txt_price_per_unit;
  private JButton btn_ok;
  private JButton btn_clear_add;
  private JButton btn_add_item;
  private JButton btn_show_item;
  private JButton btn_start_billing;
  private JLabel lbl_status;
  private JTable tbl_data;
  private JButton billingButton_ok;
  private JTextField txt_namField;
  private JLabel lbl_nameLabel;
  private JLabel lbl_bill_number;
  private JLabel lbl_total_amount;
  private JButton btn_show_product_by_type;
  private JButton btn_search;
  private JButton btn_exit;
  private JComboBox ddl_pid;
  private JButton btn_show_bill;

  static  int Billing_Number;

 
 
  void prepareGUI() {
    String[] items = { "Shirt", "T-Shirt", "Pant" };

    mainframe = new JFrame("Inventory");

    mainframe.setSize(400, 400);
    mainframe.setLayout(null);
    

    lbl_P_Id = new JLabel("Product ID:");
    lbl_quantity = new JLabel("Quantity:");
    lbl_p_type = new JLabel("Product Type:");
    lbl_price_per_unit = new JLabel("Price Per Unit");
    lbl_status = new JLabel("Status");
    btn_clear_add = new JButton("Clear");
    btn_ok = new JButton("OK");
    btn_exit = new JButton("Exit");

    ddl_p_type = new JComboBox();
    ddl_quantity = new JComboBox();

    txt_P_id = new JTextField();
    txt_price_per_unit = new JTextField();
    txt_P_id.setBounds(130, 30, 100, 20);
    lbl_P_Id.setBounds(50, 30, 100, 20);
    lbl_p_type.setBounds(50, 70, 100, 20);
    lbl_quantity.setBounds(50, 100, 100, 20);
    lbl_status.setBounds(150, 250, 190, 20);
    ddl_p_type.setBounds(130, 70, 100, 20);
    ddl_quantity.setBounds(130, 100, 100, 20);
    lbl_price_per_unit.setBounds(45, 130, 100, 20);
    txt_price_per_unit.setBounds(130, 130, 100, 20);
    btn_clear_add.setBounds(200, 200, 100, 30);
    btn_exit.setBounds(250,250,100,30);
    btn_ok.setBounds(45, 200, 100, 30);
    int i;

    btn_clear_add.addActionListener(this);
    btn_ok.addActionListener(this);
    btn_exit.addActionListener(this);
    for (i = 0; i < items.length; i++) {
      ddl_p_type.addItem(items[i]);
      ddl_p_type.setEditable(false);
    }
    for (i = 1; i <= 100; i++) {
      ddl_quantity.addItem(i);
    }

    mainframe.add(lbl_P_Id);
    mainframe.add(txt_P_id);
    mainframe.add(lbl_p_type);
    mainframe.add(ddl_p_type);
    mainframe.add(lbl_quantity);
    mainframe.add(ddl_quantity);
    mainframe.add(txt_price_per_unit);
    mainframe.add(lbl_price_per_unit);
    mainframe.add(btn_clear_add);
    mainframe.add(lbl_status);
    mainframe.add(btn_ok);
    mainframe.add(btn_exit);
    mainframe.setVisible(true);

  }

  @Override
  public void actionPerformed(ActionEvent e) {

  
    if (e.getSource() == btn_ok) {
      String p_id = txt_P_id.getText().trim();

      String price_per_unit = txt_price_per_unit.getText();
      String p_type = ddl_p_type.getSelectedItem().toString();
      String quantity = ddl_quantity.getSelectedItem().toString();
      System.out.println(txt_P_id.getText());
      int i = addItems_to_DB(p_id, p_type, Integer.parseInt(quantity), Double.parseDouble(price_per_unit));
      if (i > 0) {
        String msg = "Items is Inserted";

        lbl_status.setText("Status " + msg + " " + i);
        lbl_status.setBackground(Color.green);
      } else {
        String msg = "Internal Server error";
        lbl_status.setText("Status" + " " + msg);
        lbl_status.setBackground(Color.RED);
      }
    }
    if (e.getSource() == btn_clear_add) {
      txt_P_id.setText(" ");
      txt_price_per_unit.setText(" ");
    }
    if (e.getSource() == btn_add_item) {

      prepareGUI();
      btn_add_item.setEnabled(false);

    }
    if(e.getSource()==btn_show_item)
    {
        showData();

        btn_show_item.setEnabled(false);
       
    }
    if(e.getSource()==billingButton_ok)
    {
      String name = txt_namField.getText();
      String date = java.time.LocalDate.now().toString();
   
      Billing_Number++;
  
      String [] d = date.split("-");
      String originaldate= new String();
      
       Billing_data(originaldate,name);
    }
    if(e.getSource()==btn_start_billing)
    {
      
      
     
       Billing();
       btn_start_billing.setEnabled(false);
      
    }
    if(e.getSource()==btn_show_product_by_type)
    
    {
       show_Product_by_type();
    }
    if(e.getSource()==btn_exit)
    {
      mainframe.dispose();
      btn_add_item.setEnabled(true);
      btn_show_item.setEnabled(true);
      btn_start_billing.setEnabled(true);
    }
    if(e.getSource()==btn_search)
    {
      try {
       
        int quantity=0;
       
         ArrayList<String>p_id=new ArrayList<String>();
   
         Class.forName("com.mysql.jdbc.Driver");
         Connection con = DriverManager.getConnection("jdbc:mysql://localhost/stock", "root", "");
         Statement smt = con.createStatement();
         String mydate=java.time.LocalDate.now().toString();
   
         String gg=ddl_p_type.getSelectedItem().toString();
         String s = "select quantity from  Inventory_Items where p_type ='"+gg+"' ";
         String sql ="select p_id from Inventory_Items where p_type='"+gg+"'";
         System.out.println(gg);
         
   
        // ResultSet rSet = smt.executeQuery(s);
         ResultSet rsql = smt.executeQuery(sql);
        //  while (rSet.next()) 
        //  {
             
  
        //      quantity= Integer.parseInt(rSet.getString("quantity")) ;
    
        //  }
         while(rsql.next())
         {
           p_id.add(rsql.getString("p_id"));
           
         }
        // System.out.println(quantity);
         System.out.println(p_id);
         int k = Integer.parseInt( p_id.get(0)) ;
         for( int i =0;i<k;i++)
         {
           ddl_quantity.addItem(i);
         }
   
       } catch (Exception ex) {
         ex.printStackTrace();
       }
     
    }
    if(e.getSource()==btn_show_bill)
    {
      String name = txt_namField.getText().trim();
      String pro_type = ddl_p_type.getSelectedItem().toString();
      int quantity =Integer.parseInt(ddl_quantity.getSelectedItem().toString()) ;
      show_Bill(name,pro_type,quantity);
      billingButton_ok.setEnabled(false);
    }

  }

  
  public int addItems_to_DB(String p_id, String p_type, Integer quantity, double price_per_unit) {

    int i = 0;
    try {
      System.out.println(quantity);
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://localhost/stock", "root", "");
      Statement smt = con.createStatement();
      String query = "insert into Inventory_Items(p_id,p_type,quantity,price_per_unit) values('" + p_id + "','" + p_type
          + "', '" + quantity + "' ,'" + price_per_unit + "')";
      i = smt.executeUpdate(query);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return i;

  }

  public void showData() {
    
    DefaultTableModel table = new DefaultTableModel();
   table.addColumn("procuct_id");
    table.addColumn("Product_type");
    table.addColumn("Quantity");
    table.addColumn("PricePerUnit");
  
    mainframe = new JFrame("Product Available");
    mainframe.setLayout(null);
    
    tbl_data = new JTable();
    btn_exit = new JButton("Exit");

    tbl_data.setBounds(5, 5,300, 300);
    btn_exit.setBounds(200,300,100,30);
    
   btn_exit.addActionListener(this);
    mainframe.setSize(400, 400);
    
    
    mainframe.setVisible(true);

    try {

     
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://localhost/stock", "root", "");
      Statement smt = con.createStatement();
      String s = "select * from  Inventory_Items ";

      ResultSet rSet = smt.executeQuery(s);
      while (rSet.next()) 
      {
         String pid= rSet.getString("p_id");
         String pro_type = rSet.getString("p_type");
         String quantity= rSet.getString("quantity");
         String price_per_unit= rSet.getString("price_per_unit");
         table.addRow(new Object[]{pid, pro_type, quantity,price_per_unit});
         
      }
      tbl_data.setModel(table);
      mainframe.add(tbl_data);
      mainframe.add(btn_exit);


    } catch (Exception e) {
      e.printStackTrace();
    }
    

  }

  public void masterWindow() {
    
    mainframe = new JFrame("Control Panel");
    mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    btn_add_item = new JButton("Add Items");
    btn_show_item = new JButton("Show All Items");
    btn_start_billing = new JButton("Billing");
    btn_show_product_by_type = new JButton("Show By Type");
    mainframe.setSize(400, 400);
    mainframe.setLayout(null);
    btn_add_item.setBounds(10, 30, 100, 30);
    btn_show_product_by_type.setBounds(10, 70,150, 30);
    btn_show_item.setBounds(130, 30, 120, 30);
    btn_start_billing.setBounds(270, 30, 110, 30);
    btn_add_item.addActionListener(this);
    btn_show_item.addActionListener(this);
    btn_start_billing.addActionListener(this);
    btn_show_product_by_type.addActionListener(this);
    mainframe.add(btn_add_item);
    mainframe.add(btn_show_item);
    mainframe.add(btn_start_billing);
    mainframe.add(btn_show_product_by_type);
    mainframe.setVisible(true);

  }
  public void  Billing_data(String Bill_Name, String name)
  {
    try {

      String pid= new String();
      String pro_type= new String();
     
      String price_per_unit = new String();
      int quantity=0;
 
       Class.forName("com.mysql.jdbc.Driver");
       Connection con = DriverManager.getConnection("jdbc:mysql://localhost/stock", "root", "");
       Statement smt = con.createStatement();
       String mydate=java.time.LocalDate.now().toString();
 
       String gg=ddl_p_type.getSelectedItem().toString();
       String s = "select * from  Inventory_Items where p_type ='"+gg+"' ";
         String billing ="insert into billing_details values('" + pid + "','" + name
         + "', '" + pro_type + "' ,'" + quantity + "','"+mydate+"','"+Bill_Name+" ')";
 
       ResultSet rSet = smt.executeQuery(s);
       while (rSet.next()) 
       {
           pid= rSet.getString("p_id");
           pro_type = rSet.getString("p_type");
           quantity= Integer.parseInt(rSet.getString("quantity")) ;
           price_per_unit= rSet.getString("price_per_unit");
  
       }
        int result = smt.executeUpdate(billing);
        
 
     } catch (Exception e) {
       e.printStackTrace();
     }
    
  }
  public void Billing()
  {
   
    String[] items = { "Shirt", "T-Shirt", "Pant" };
  
    ddl_quantity = new JComboBox();
    ddl_p_type = new JComboBox();
    ddl_pid = new JComboBox();
    ddl_pid = new JComboBox();
    mainframe = new JFrame("Billing");
    txt_namField = new JTextField();
    txt_namField= new JTextField();
    lbl_P_Id = new JLabel("Product ID");
    lbl_nameLabel = new JLabel("Customer Name");
    lbl_total_amount = new JLabel();
    lbl_p_type = new JLabel("Product Type:");
    lbl_quantity = new JLabel("Quantity:");
    billingButton_ok = new JButton("OK");
    lbl_status = new JLabel("Total Amount");
    lbl_status = new JLabel("Total");
    btn_search = new JButton("Search");
    lbl_bill_number = new JLabel();
    btn_exit = new JButton("Exit");
    btn_show_bill = new JButton("Show Bill");

    lbl_P_Id.setBounds(50, 120, 100, 20);
    ddl_pid.setBounds(170, 130, 100, 20);
    lbl_p_type.setBounds(50, 70, 100, 20);
    txt_namField.setBounds(170, 30, 100, 20);
    lbl_nameLabel.setBounds(50,30,100,20);
    ddl_quantity.setBounds(170, 100, 100, 20);
    ddl_p_type.setBounds(170, 70, 100, 20);
    lbl_quantity.setBounds(50, 100, 100, 20);
    lbl_status.setBounds(50,190,100,20);

    billingButton_ok.setBounds(45, 240, 100, 30);
    lbl_bill_number.setBounds(170,150,100,20);
    lbl_total_amount.setBounds(170,200,100,20);
    btn_search.setBounds(280, 70, 90, 20);
    btn_exit.setBounds(200, 300, 100, 30);
    lbl_P_Id.setBounds(170, 30, 100, 20);
    btn_show_bill.setBounds(200, 240, 100, 30);
    
    
    mainframe.add(lbl_p_type);
    mainframe.add(lbl_quantity);
    mainframe.add(lbl_status);
    mainframe.add(ddl_p_type);
    mainframe.add(billingButton_ok);
    mainframe.add(lbl_nameLabel);
    mainframe.add(txt_namField);
    mainframe.add(ddl_quantity);
    mainframe.add(lbl_status);
    mainframe.add(lbl_bill_number);
    mainframe.add(lbl_total_amount);
    mainframe.add(btn_search);
    mainframe.add(btn_exit);
    mainframe.add(btn_show_bill);
    mainframe.add(lbl_P_Id);
    mainframe.add(ddl_pid);
    billingButton_ok.addActionListener(this);
    btn_exit.addActionListener(this);
    btn_search.addActionListener(this);
    btn_show_bill.addActionListener(this);
     mainframe.setLayout(null);
     mainframe.setSize(400, 400);
    for (int i = 0; i < items.length; i++) {
      ddl_p_type.addItem(items[i]);
      ddl_p_type.setEditable(false);
    }
    for(int i =0;i<100;i++)
    {
      ddl_quantity.addItem(i);
    }
    
    
    mainframe.setVisible(true);
    
  
  }
  public void show_Bill(String name,String pro_type,int quantity)
  {
    String [] col ={"Name","pro_type","Quantity"};
    Object[][] data = {
      {name,pro_type,quantity}
  };
    mainframe = new JFrame("Bill");
    mainframe.setLayout(null);
    mainframe.setSize(400,400);
    tbl_data = new JTable( data, col);
    tbl_data.setBounds(5, 5, 390, 300);
    mainframe.add(tbl_data);
    mainframe.setVisible(true);
  }
  public void show_Product_by_type()
  {
    String[] items = { "Shirt", "T-Shirt", "Pant" };
    mainframe = new JFrame("Show Products by Type");
    btn_search = new JButton("Search");
     mainframe.setLayout(null);
     DefaultTableModel table = new DefaultTableModel();
     tbl_data = new JTable();
     tbl_data.setBounds(10, 50,400, 400);
     //
     ddl_p_type = new JComboBox();
     for (int i = 0; i < items.length; i++) {
      ddl_p_type.addItem(items[i]);
      ddl_p_type.setEditable(false);
    }
   // String selected_items = ddl_p_type.getSelectedItem().toString();
    try {

      String pid= new String();
      String pro_type= new String();
      int quantity ;
      String price_per_unit = new String();
 
       Class.forName("com.mysql.jdbc.Driver");
       Connection con = DriverManager.getConnection("jdbc:mysql://localhost/stock", "root", "");
       Statement smt = con.createStatement();
       
       String gg=ddl_p_type.getSelectedItem().toString();
       String s = "select * from  Inventory_Items where p_type ='"+gg+"' ";
       
 
       ResultSet rSet = smt.executeQuery(s);
       while (rSet.next()) 
       {
         System.out.println(rSet.getString("p_type"));
           pid= rSet.getString("p_id");
           pro_type = rSet.getString("p_type");

           quantity= Integer.parseInt(rSet.getString("quantity")) ;
           price_per_unit= rSet.getString("price_per_unit");
           table.addRow(new Object[]{pid, pro_type, quantity,price_per_unit});
  
       }
       
     ddl_p_type.setBounds(10, 30, 100, 20);
     btn_search.setBounds(150, 30, 100, 20);
     mainframe.add(ddl_p_type);
     tbl_data.setModel(table);
     mainframe.add(tbl_data);
     mainframe.add(btn_search);
     mainframe.setSize(400,400);
     mainframe.setVisible(true);
 
    }
    catch(Exception e)
       {
         e.printStackTrace();
       }
}

  public static void main(String[] args) 
  {

    new InventoryGUI().masterWindow();
   System.out.println(generatebillnumber("2020-03-18 16:55:47","harshit"));

  }

  public static String generatebillnumber(String reg_date,String billname)
  {
    //2020-03-18 16:55:47

    String d[] = reg_date.split(" ");
    String originaldate = new String();
    for(int i=0;i<d.length;i++)
      {
        originaldate=originaldate+d[i];
      }
      
    return originaldate+billname;

    
        

  }
}
// datbase table
  // CREATE TABLE Inventory_Items (
  // p_id VARCHAR(40) PRIMARY KEY,
  // p_type VARCHAR(30) NOT NULL,
  // quantity VARCHAR(30) NOT NULL,
  // price_per_unit VARCHAR(50),
  // reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  // )


//  CREATE TABLE Billing_details(
//    p_id VARCHAR(40) PRIMARY KEY,
//    Name VARCHAR(100),
//    p_type VARCHAR(30),
//    quantity VARCHAR(30),
//    date VARCHAR(80),
//    bill_number VARCHAR(50),
//    Foreign KEY (p_id) References Inventory_Items(p_id)
//  );


// CREATE TABLE Billing_details(
//    p_id VARCHAR(40) ,
//    Name VARCHAR(100),
//    p_type VARCHAR(30),
//    quantity VARCHAR(30),
//    date VARCHAR(80),
//    bill_number VARCHAR(50) PRIMARY KEY
   
//  );
// select * from  Inventory_Items where p_type = 'shirt'