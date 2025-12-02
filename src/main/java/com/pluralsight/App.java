package com.pluralsight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        // Make sure we have username + password
        if (args.length < 2) {
            System.out.println("Application needs two args to run: a username and a password for the db");
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];
        String url = "jdbc:mysql://localhost:3306/northwind";

        Connection connection = null;

        try {
            // connect to DB
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);

            Scanner scanner = new Scanner(System.in);
            int choice = -1;

            while (choice != 0) {
                // home screen
                System.out.println();
                System.out.println("What do you want to do?");
                System.out.println("1) Display all products");
                System.out.println("2) Display all customers");
                System.out.println("0) Exit");
                System.out.print("Select an option: ");

                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // eat the newline
                } else {
                    System.out.println("Please enter a number (0, 1, or 2).");
                    scanner.nextLine(); // clear bad input
                    continue;
                }

                switch (choice) {
                    case 1:
                        displayAllProducts(connection);
                        break;
                    case 2:
                        displayAllCustomers(connection);
                        break;
                    case 0:
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }

        } catch (Exception e) {
            // any error: print it
            e.printStackTrace();
        } finally {
            // always close the connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void displayAllProducts(Connection connection) {
        String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products";

        try {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);

            System.out.println();
            System.out.println("Id   Name                      Price     Stock");
            System.out.println("-----------------------------------------------");

            while (results.next()) {
                int id = results.getInt("ProductID");
                String name = results.getString("ProductName");
                double price = results.getDouble("UnitPrice");
                int stock = results.getInt("UnitsInStock");

                System.out.printf("%-4d %-25s %-9.2f %d%n", id, name, price, stock);
            }

            results.close();
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayAllCustomers(Connection connection) {
        String query =
                "SELECT ContactName, CompanyName, City, Country, Phone " +
                        "FROM customers " +
                        "ORDER BY Country, CompanyName";

        try {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);

            System.out.println();
            System.out.println("Contact Name           Company                   City             Country        Phone");
            System.out.println("-----------------------------------------------------------------------------------------");

            while (results.next()) {
                String contact = results.getString("ContactName");
                String company = results.getString("CompanyName");
                String city = results.getString("City");
                String country = results.getString("Country");
                String phone = results.getString("Phone");

                System.out.printf("%-22s %-25s %-16s %-14s %s%n",
                        contact, company, city, country, phone);
            }

            results.close();
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
