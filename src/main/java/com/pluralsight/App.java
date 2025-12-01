package com.pluralsight;

import java.sql.*;

public class App {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/northwind";
        String username = "root";
        String password = "Bashar1212"; // if no password

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open connection
            Connection connection = DriverManager.getConnection(url, username, password);

            // Create SQL statement
            Statement statement = connection.createStatement();

            // Query all products
            String query = "SELECT ProductName FROM products";

            ResultSet results = statement.executeQuery(query);

            System.out.println("PRODUCTS SOLD BY NORTHWIND:\n");

            // Process results
            while (results.next()) {
                System.out.println(results.getString("ProductName"));
            }

            // Close connection
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
