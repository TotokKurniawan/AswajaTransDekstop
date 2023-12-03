package Koneksi;
import java.sql.*;
import javax.swing.*;

public class Connect {
    public static Connection KoneksiDB(){
        try{
            Class.forName("com.mysql.jdbc.Driver"); //Memanggil driver JDBC
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/aswajatrans", "root", "");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }

}