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
 * This pop-out simply displays SQL table information for Customers
 */
public class Customer extends JFrame {
    //  Establishing location, UID, password and sql command string
    String url = "jdbc:mysql://siteground324.com:3306/gunnargo_cmsc495";
    String userid = "gunnargo_umuc15";
    String password = "Ib7t5BRa74mTr0N9aS6";
    
    String custID;
    String custName;
    String custAddress;
    String custEmail;
    
    Connection con;
    
    public boolean addCustomerRecord(ArrayList<String> info){
        
        String id = info.get(0);
        String name = info.get(1);
        String address = info.get(2);
        String email = info.get(3);
        
        boolean result = false;
        
        if(custExists(custID)){
            String sqlCreate = "INSERT INTO gunnargo_cmsc495.Customer SET "
                    + " CustID = " + id
                    + ", Name = " + name
                    + ", Address = " + address
                    + ", E-mail = " + email + ";";
        
            try {
                con = DriverManager.getConnection(url, userid, password);
                Statement stmt = con.createStatement();
                boolean success = stmt.execute(sqlCreate);
                con.close();
                return success;
            } catch (SQLException ex) {
                System.out.println("Customer was not updated");
                return false; // creation failed
            }
        }
        
        return result;
    
    
//        this.custName = name;
//        this.custAddress = address;
//        this.custEmail = email;
        
    }
    
    public HashMap retrieveCust(int custID){
        HashMap results = new HashMap();
        String sqlSelect = "SELECT * FROM gunnargo_cmsc495.Customer WHERE id = " + custID + ";";
        
        try {
                con = DriverManager.getConnection(url, userid, password);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sqlSelect);
                int id = rs.getInt("CustID");
                String name = rs.getString("Name");
                String address = rs.getString("Address");
                String email = rs.getString("E-mail");
                results.put("CustID", id);
                results.put("Name", name);
                results.put("Address", address);
                results.put("E-mail", email);
                con.close();
            } catch (SQLException ex) {
                System.out.println("Customer was not retrieved");
            }
        
        
        return results;
    }
    
    public boolean updateCustomer(ArrayList<String> info){
        
        String id = info.get(0);
        String name = info.get(1);
        String address = info.get(2);
        String email = info.get(3);
        
        boolean result = false;
        
        if(custExists(custID)){
            String sqlCreate = "UPDATE gunnargo_cmsc495.Customer SET "
                    + "Name = " + name
                    + " Address = " + address
                    + " E-mail = " + email
                    + " WHERE CustID = " + id + ";";
        
            try {
                con = DriverManager.getConnection(url, userid, password);
                Statement stmt = con.createStatement();
                boolean success = stmt.execute(sqlCreate);
                con.close();
                return success;
            } catch (SQLException ex) {
                System.out.println("Customer was not updated");
                return false; // creation failed
            }
        }
        
        return result;
    }
    
    public boolean deleteCustomer(String custID){
        this.custID = custID;
        boolean result = false;
            
        if(custExists(custID)){

            String sqlSelect = "DELETE id FROM gunnargo_cmsc495.Customer WHERE id = " + custID + ";";

            try {
                con = DriverManager.getConnection(url, userid, password);
                Statement stmt = con.createStatement();
                result = stmt.execute(sqlSelect);
                con.close();
            } catch (SQLException ex) {
                System.out.println("Customer ID: " + custID +"not found");
                result = false; // find failed
            }
        }
        
        return result;
    }
    
    public boolean custExists(String id){
        boolean result;
        
        String sqlSelect = "SELECT id FROM gunnargo_cmsc495.Customer WHERE id = " + id + ";";

        try {
            con = DriverManager.getConnection(url, userid, password);
            Statement stmt = con.createStatement();
            result = stmt.execute(sqlSelect);
            con.close();
        } catch (SQLException ex) {
            System.out.println("Customer ID: " + id +"not found");
            return false; // find failed
        }
        
        return result;
    }
  
    public Customer()
    {
        this.setTitle("TAPRO-IM Customer Table");
        ArrayList columnNames = new ArrayList();
        ArrayList data = new ArrayList();

        
        String sql = "SELECT * FROM gunnargo_cmsc495.Customer;";

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
    
}
