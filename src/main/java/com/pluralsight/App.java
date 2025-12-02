package com.pluralsight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class App {
    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Application needs two args to run: A username and a password for the db");
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];

        String url = "jdbc:mysql://localhost:3306/northwind";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            Statement statement = connection.createStatement();

            String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products";
            ResultSet results = statement.executeQuery(query);

            System.out.println("Id   Name                      Price     Stock");
            System.out.println("-----------------------------------------------");

            while (results.next()) {
                int id = results.getInt("ProductID");
                String name = results.getString("ProductName");
                double price = results.getDouble("UnitPrice");
                int stock = results.getInt("UnitsInStock");

                System.out.printf("%-4d %-25s %-9.2f %d\n", id, name, price, stock);
            }

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
