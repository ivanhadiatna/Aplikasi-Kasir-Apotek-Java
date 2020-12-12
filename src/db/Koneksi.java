/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author HADIATNA
 */
public class Koneksi {
int i;
    java.sql.Connection connection;
    Statement statement;
    Socket client;
    String SQL;
    String url;
    String username;
    String password;
    String Host;
    int Port;
    PreparedStatement ps;
    
    
    public Koneksi(String url, String username, String password, String Host, int Port) {
        System.out.println(Host+Port);
        try{
            client = new Socket(Host, Port);
            this.url = url;
            this.username = username;
            this.password = password;
            this.Host = Host;
            this.Port = Port;
        }catch(IOException ex){
            JOptionPane.showMessageDialog(null, "Hidupkan server terlebih dahulu !!!");
        }
    }
    
    public java.sql.Connection ConnectionDb() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        }catch (ClassNotFoundException | SQLException e) {
         System.out.println(e.toString());
         JOptionPane.showMessageDialog(null, "Belum Terkoneksi Database");
        }
        return connection;
    }

    public java.sql.Connection ClosedDb() {
        try {
           connection.close();
        } catch (Exception e) {
        }
        return connection;
    }
    
    public ResultSet eksekusiQuery(String sql) {
        ConnectionDb();
        ResultSet resultSet = null;
       // System.out.println(sql);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);      
        } catch (SQLException ex) {       
        }
        System.out.println(resultSet);
        return resultSet;
    }

    public String eksekusiUpdate(String sql) {
        ConnectionDb();
        String result = "";
        System.out.println(sql);
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            result = ex.toString();
            javax.swing.JOptionPane.showMessageDialog(null,result);
            //javax.swing.JOptionPane.showMessageDialog(null, ex); //Pesan Error jika Duplikat data  
        }
       return result;
    }


//Overload fungsi untuk eksekusi query select semua kolom dengan where
    public ResultSet querySelectAll(String nameTable, String condition) {
        SQL = "SELECT * FROM " + nameTable + " WHERE " + condition;
        System.out.println(SQL);
        return this.eksekusiQuery(SQL);
    }
    
   
     

//Fungsi untuk eksekusi query select dengan kolom spesifik
    public ResultSet querySelect(String[] namecolumn, String nameTable) {
        SQL = "SELECT ";
        for (i = 0; i <= namecolumn.length - 1; i++) {
            SQL += namecolumn[i];
            if (i < namecolumn.length - 1) {
                SQL += ",";
            }
        }
        SQL += " FROM " + nameTable;
        return this.eksekusiQuery(SQL);
    }

//Overload fungsi untuk eksekusi query select dengan kolom spesifik dengan where
    public ResultSet fcSelectCommand(String[] column, String nameTable, String condition) {
        SQL = "SELECT ";
        for (i = 0; i <= column.length - 1; i++) {
            SQL += column[i];
            if (i < column.length - 1) {
                SQL += ",";
            }
        }
        SQL += " FROM " + nameTable + " WHERE " + condition;
        System.out.println(SQL);
        return this.eksekusiQuery(SQL);
    }


//Fungsi eksekusi query insert
    public String queryInsert(String nameTable, String[] namecolumn, String[] value) {
        SQL = "INSERT INTO " + nameTable +" (";
        for (i = 0; i <= namecolumn.length - 1; i++) {
            SQL +=namecolumn[i];
            if (i < namecolumn.length - 1) {
                SQL += ",";
            }
        }
        SQL+=") VALUES(";
        for (i = 0; i <= value.length - 1; i++) {
            SQL += "'" + value[i] + "'";
            if (i < value.length - 1) {
                SQL += ",";
            }
        }
        SQL += ")";
        return this.eksekusiUpdate(SQL);
    }
    
//Fungsi eksekusi query update
    public String queryUpdate(String nameTable, String[] namecolumn, String[] value, String condition) {
        SQL = "UPDATE " + nameTable + " SET ";

        for (i = 0; i <= namecolumn.length - 1; i++) {
            SQL += namecolumn[i] + "='" + value[i] + "'";
            if (i < namecolumn.length - 1) {
                SQL += ",";
            }
        }
        SQL += " WHERE " + condition;
        return this.eksekusiUpdate(SQL);
    }

//Fungsi eksekusi query delete
    public String queryDelete(String nameTable) {
        SQL = "DELETE FROM " + nameTable;
        return this.eksekusiUpdate(SQL);
    }

//Overload fungsi eksekusi query delete dengan where
    public String queryDelete(String nameTable, String condition) {
        SQL = "DELETE FROM " + nameTable + " WHERE " + condition;
        return this.eksekusiUpdate(SQL);
    }
}