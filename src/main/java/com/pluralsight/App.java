package com.pluralsight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class App {
    public static void main(String[] args) {

        // check for args
        if (args.length < 2) {
            System.out.println("Application needs two args to run: A username and a password for the db");
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];

        String url = "jdbc:mysql://localhost:3306/northwind";

        try {
            // load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // open connection
            Connection connection = DriverManager.getConnection(url, username, password);

            // create statement
            Statement statement = connection.createStatement();

            // run query
            String query = "SELECT ProductName FROM products";
            ResultSet results = statement.executeQuery(query);

            // print results
            while (results.next()) {
                System.out.println(results.getString("ProductName"));
            }

            // close connection
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
