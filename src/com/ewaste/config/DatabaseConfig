package com.ewaste.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/e_waste_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Default XAMPP password is empty

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null,
                    "MySQL Driver Library not found! Please check if the jar is in the classpath.", "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Failed to connect to Database!\nCheck if MySQL is running.\nCheck if database 'e_waste_db' exists.\nCheck username/password in DatabaseConfig.java",
                    "Database Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            // System.exit(1); // Optional: Exit if DB is essential
        }
        return connection;
    }
}
