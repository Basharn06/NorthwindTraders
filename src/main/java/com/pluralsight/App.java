package com.pluralsight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        // make sure we have username + password
        if (args.length < 2) {
            System.out.println("Application needs two args to run: a username and a password for the db");
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];
        String url = "jdbc:mysql://localhost:3306/northwind";

        try {
            // load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load MySQL driver.");
            e.printStackTrace();
            return;
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Scanner scanner = new Scanner(System.in)) {

            int choice = -1;

            while (choice != 0) {
                System.out.println();
                System.out.println("What do you want to do?");
                System.out.println("1) Display all products");
                System.out.println("2) Display all customers");
                System.out.println("3) Display all categories");
                System.out.println("0) Exit");
                System.out.print("Select an option: ");

                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                } else {
                    System.out.println("Please enter a number (0, 1, 2, or 3).");
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
                    case 3:
                        displayCategoriesAndProductsInCategory(connection, scanner);
                        break;
                    case 0:
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Exercise 2: show all products
    private static void displayAllProducts(Connection connection) {
        String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products";

        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(query)) {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Exercise 3: show all customers
    private static void displayAllCustomers(Connection connection) {
        String query =
                "SELECT ContactName, CompanyName, City, Country, Phone " +
                        "FROM customers " +
                        "ORDER BY Country, CompanyName";

        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(query)) {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Exercise 4: show all categories, ask for categoryId, then show products for that category
    private static void displayCategoriesAndProductsInCategory(Connection connection, Scanner scanner) {

        // 1) List all categories
        String categoriesQuery =
                "SELECT CategoryID, CategoryName FROM categories ORDER BY CategoryID";

        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(categoriesQuery)) {

            System.out.println();
            System.out.println("CategoryId   Category Name");
            System.out.println("---------------------------");

            while (results.next()) {
                int id = results.getInt("CategoryID");
                String name = results.getString("CategoryName");
                System.out.printf("%-11d %s%n", id, name);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // 2) Ask user for category id
        System.out.print("\nEnter a category id to view its products: ");

        int categoryId;
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number for category id: ");
            scanner.nextLine();
        }
        categoryId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        // 3) Show products in that category
        String productsQuery =
                "SELECT ProductID, ProductName, UnitPrice, UnitsInStock " +
                        "FROM products " +
                        "WHERE CategoryID = " + categoryId;

        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(productsQuery)) {

            System.out.println();
            System.out.println("Products in category " + categoryId + ":");
            System.out.println("Id   Name                      Price     Stock");
            System.out.println("-----------------------------------------------");

            boolean any = false;
            while (results.next()) {
                any = true;
                int id = results.getInt("ProductID");
                String name = results.getString("ProductName");
                double price = results.getDouble("UnitPrice");
                int stock = results.getInt("UnitsInStock");

                System.out.printf("%-4d %-25s %-9.2f %d%n", id, name, price, stock);
            }

            if (!any) {
                System.out.println("No products found for that category id.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
