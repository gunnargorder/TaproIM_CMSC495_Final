/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taproim_cmsc495;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.swing.table.AbstractTableModel;

/**
 * This class implements the abstract table model for updating user displays
 * @author Dent, Gorder, Kenyon, Montoya, Ward
 */
public class DisplayTableModel extends AbstractTableModel {
    ArrayList columnNames = new ArrayList();
    ArrayList<ArrayList> data = new ArrayList<>();
    
    //  Establishing location, UID, password and sql command string
    String url = "jdbc:mysql://siteground324.com:3306/gunnargo_cmsc495";
    String userid = "gunnargo_umuc15";
    String password = "Ib7t5BRa74mTr0N9aS6";
    String sql = "SELECT * FROM gunnargo_cmsc495.";
    
    /**
     * Fires data updates in the table
     * @param table the table to be displayed/updated
     */
    public void SetData(String table) {
        String sqlAttempt = sql + table;
        try (Connection connection = DriverManager.getConnection(url,userid,password)) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlAttempt);
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();

            //  Get column names
            for (int i = 1; i <= columns; i++) columnNames.add(md.getColumnName(i));
            
            // populate the rows
            while (rs.next()){
                ArrayList<String> row = new ArrayList<>();
                for (int i = 1; i <= columns; i++) row.add(rs.getString(columnNames.get(i).toString()));
                data.add(row);
            }
        } catch (SQLException e) { 
            System.out.println(e.getMessage()); 
        }
        
        fireTableDataChanged();
    }
    
    /** @return the number of rows for the table */
    @Override public int getRowCount() {
        return data.size();
    }
    /** @return the number of columns in the table */
    @Override public int getColumnCount() {
        return columnNames.size();
    }
    /** @index the particular index for the column name
     *  @return the column names for the table */
    @Override public String getColumnName(int index) {
        return columnNames.get(index).toString();
    }
    /** @rowIndex the row to be returned
     *  @columnIndex the column to be returned
     *  @return the values to be added to the table */
    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        ArrayList<String> rows = new ArrayList<>();
        rows.addAll(data.get(rowIndex));
        return rows.get(columnIndex);
    }
}
