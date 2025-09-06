package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;
import inventory_tracker.model.Item;

import java.sql.*;
import java.util.Scanner;

public class InventoryService {

    static int id;
    static String name;
    static int quantity;
    static double price;
    static Scanner userScanner = new Scanner(System.in);


    public static void search() {

        System.out.println("*** INVENTORY SEARCH ***");
        System.out.println("Please enter the item ID:");

        id = userScanner.nextInt();

        System.out.println("ID | NAME | QUANTITY | PRICE/UNIT");
        String query = "SELECT id, name, quantity, price FROM inventory WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // set input to the query
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("*** SEARCH RESULTS ***");

                    System.out.println(
                            rs.getInt("id") + " | " +
                                    rs.getString("name") + " | " +
                                    rs.getInt("quantity") + " | " + "$" +
                                    rs.getDouble("price")
                    );
                } else {
                    System.out.println("No product found with ID: " + id);
                }
            }

        } catch (SQLException e) {
            System.out.println("*** Connection to Database Failed ***");
        }
    }


    public static void report() {
        System.out.println("*** INVENTORY REPORT *** ");
        System.out.println("ID | NAME | QUANTITY | PRICE/UNIT");
        String query = "SELECT id, name, quantity, price FROM inventory";

        try(Connection conn = MySQLConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getInt("quantity") + " | " + "$" +
                                rs.getDouble("price")
                );
            }
        } catch (SQLException e) {
            System.out.println("*** Connection to Database Failed ***");
        }
    }

    // updates existing item based on user input
    public static void update() {

    }

    // creates an item based on user input
    public static void create() {
        // CODE FROM MENU TO ADD TO METHOD
//        System.out.println("\nCREATE ITEM");
//        userScanner.nextLine();
//        System.out.println("Enter new item name: ");
//        String name = userScanner.nextLine();
//        System.out.println("Enter new item quantity: ");
//        int quantity = Integer.parseInt(userScanner.nextLine());
//        System.out.println("Enter new item price per unit");
//        double price = Double.parseDouble(userScanner.nextLine());
//        Item item = new Item(name, quantity, price);
//        InventoryService.createInventory(item);
//        System.out.println("*** Item Created ***");
        String query = "INSERT INTO inventory (name, quantity, price) VALUES (?, ?, ?)";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getQuantity());
            stmt.setDouble(3, item.getPrice());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error inserting record into inventory.");
        }

    }

    // run after confirming with user with inventorySearch
    public static void delete() {
        // CODE FROM MENU TO ADD TO METHOD
        // System.out.println("\nDELETE ITEM");
        // System.out.println("Please enter the id of the item to be deleted: ");
        // int id = userScanner.nextInt();
        // System.out.println("The following product is about to be deleted: ");
        // InventoryService.inventorySearch(id);
        // System.out.println("Press 1 to delete or press 2 to cancel: ");
        // int check = userScanner.nextInt();
        // if (check == 1) {
        // InventoryService.deleteInventory(id);
        // } else {
        // break;
        // }
        String query = "DELETE FROM inventory WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // set input to the query
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Product with ID " + id + " deleted.");
            } else {
                System.out.println("No product found with ID: " + id);
            }

        } catch (SQLException e) {
            System.out.println("\n*** Connection to Database Failed ***\n");
        }
        System.out.println();
    }
}
