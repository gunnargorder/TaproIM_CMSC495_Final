package taproim_cmsc495;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author Spencer
 * This pop-out simply displays SQL table information for Inventory here
 */
public class Inventory extends JFrame {
   
    //private variable associated with inventory objects
    private String itemName;
    private String description;
    private int quantity;
    private double price;
    

    
    public Inventory()
    {
        this.setTitle("TAPRO-IM Inventory Table");
        ArrayList columnNames = new ArrayList();
        ArrayList data = new ArrayList();

        //  Establishing location, UID, password and sql command string
        String url = "jdbc:mysql://siteground324.com:3306/gunnargo_cmsc495";
        String userid = "gunnargo_umuc15";
        String password = "Ib7t5BRa74mTr0N9aS6";
        String sql = "SELECT * FROM gunnargo_cmsc495.Inventory";
        Connection con;

        // Try command to establish JDBC connection with above provided credentials
        try (Connection connection = DriverManager.getConnection( url, userid, password );
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery( sql ))
        {
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();

            //  Get column names
            for (int i = 1; i <= columns; i++)
            {
                columnNames.add( md.getColumnName(i) );
            }

            //  Get row data
            while (rs.next()){
                ArrayList row = new ArrayList(columns);

                for (int i = 1; i <= columns; i++)
                {
                    row.add( rs.getObject(i) );
                }

                data.add( row );
            }
        }
        catch (SQLException e){
            System.out.println( e.getMessage() );}

        // Create Vectors and copy over elements from ArrayLists to them
        Vector columnNamesVector = new Vector();
        Vector dataVector = new Vector();

        for (int i = 0; i < data.size(); i++)
        {
            ArrayList subArray = (ArrayList)data.get(i);
            Vector subVector = new Vector();
            for (int j = 0; j < subArray.size(); j++)
            {
                subVector.add(subArray.get(j));
            }
            dataVector.add(subVector);
        }

        for (int i = 0; i < columnNames.size(); i++ )
            columnNamesVector.add(columnNames.get(i));

        //  Create table with database data    
        JTable table = new JTable(dataVector, columnNamesVector)
        {
            public Class getColumnClass(int column)
            {
                for (int row = 0; row < getRowCount(); row++)
                {
                    Object o = getValueAt(row, column);

                    if (o != null)
                    {
                        return o.getClass();
                    }
                }

                return Object.class;
            }
        };

        JScrollPane scrollPane = new JScrollPane( table );
        getContentPane().add( scrollPane );

        JPanel buttonPanel = new JPanel();
        getContentPane().add( buttonPanel, BorderLayout.SOUTH );
    }
    
    //default constructor  
    public void setItem() {
        itemName = "No name given";
        description = "No description given";
        quantity = 0;
        price = 0.00;
    }
       
    //non default constructor
    public void setItem(String name, String desc, int amount, float value) {
        itemName = name;
        description = desc;
        quantity = amount;
        price = value;
    }
           
    
    //setter for itemName
    public void setItemName(String newName) {
        itemName = newName;
    }
        
    //setter for category
    public void setDescription(String newDescription) {
        description = newDescription;
    }
    
    //setter for vendorInventory
    public void setQuantity(int newQuantity) {
        if(newQuantity>0){
            quantity = newQuantity;
        }
        else{
            quantity = 0;
        }
    }

    //setter for vendorPrice
    public void setPrice(float newPrice) {
        if(newPrice >0.00){
            price = newPrice;
        }
        else{
            price = 0.00;
        }
    }
    
    //getter for itemName
    public String getItemName() {
        return itemName;
    }
    
    //getter for category
    public String getDescription() {
        return description;
    }
    
    //getter for vendorInventory
    public int getQuantity() {
        return quantity;
    }
   
    //getter for vendorPrice
    public double getPrice() {
        return price;
    }
    
    //getter for value of item
    public double getItemValue(){
        return(price *(double)quantity);
    }
    
    
}
