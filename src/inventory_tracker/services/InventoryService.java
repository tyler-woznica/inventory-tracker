package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;
import inventory_tracker.model.Item;
import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

public class InventoryService {

    static int id;
    static String name;
    static int quantity;
    static double price;
    static Scanner userScanner = new Scanner(System.in);
    static int check;


    public static void search() {

        System.out.println("INVENTORY SEARCH");

        System.out.println("Please enter the item ID:");
        id = userScanner.nextInt();

        System.out.println("*** SEARCH RESULTS ***");
        lookup(id);
    }


    public static void report() {

        System.out.println("*** INVENTORY REPORT ***");
        System.out.println("ID | NAME | QUANTITY | PRICE/UNIT\n");
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
        System.out.println();
    }

    public static void update() {

        int choice;
        String sql = "";
        Object newVal = null;

        System.out.println("*** ITEM UPDATE ***");
        System.out.println("Please enter the ID of the item to update:");
        id = userScanner.nextInt();
        System.out.println("If this item is correct, enter 1.");
        lookup(id);
        check = userScanner.nextInt();
        if (check == 1) {
            System.out.println("What field would you like to update?");
            System.out.println("""
                    1. Name
                    2. Quantity
                    3. Price
                    """);
            choice = userScanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    newVal = userScanner.nextLine();
                    userScanner.nextLine();
                    sql = "UPDATE inventory SET name = ? WHERE id = ?";
                    break;
                case 2:
                    System.out.print("Enter new quantity: ");
                    newVal = userScanner.nextInt();
                    sql = "UPDATE inventory SET quantity = ? WHERE id = ?";
                    break;
                case 3:
                    System.out.print("Enter new price: ");
                    newVal = userScanner.nextDouble();
                    sql = "UPDATE inventory SET price = ? WHERE id = ?";
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }

            try (Connection conn = MySQLConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setObject(1, newVal);
                stmt.setInt(2, id);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("*** ITEM UPDATED SUCCESSFULLY ***");
                } else {
                    System.out.println("No item found with ID " + id);
                }
            } catch (SQLException e) {
                System.out.println("*** Connection to Database Failed ***");

            }

        } else {
            System.out.println("*** UPDATE CANCELLED ***");

        }
    }

    // creates an item based on user input
    public static void create() {

        System.out.println("*** CREATE ITEM ***");
        userScanner.nextLine();

        System.out.println("Enter new item name:");
        name = userScanner.nextLine();

        System.out.println("Enter new item quantity:");
        quantity = Integer.parseInt(userScanner.nextLine());

        System.out.println("Enter new item price per unit");
        price = Double.parseDouble(userScanner.nextLine());

        Item item = new Item(name, quantity, price);

        System.out.println("The following item is about to be created.");
        System.out.println("Name: " + item.getName() + " | " +
                "Quantity: " + item.getQuantity() + " | " +
                "Price/Unit: " + item.getPrice());

        System.out.println("Enter 1 to create or 2 to cancel:");
        check = userScanner.nextInt();

        if (check == 1) {
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

            System.out.println("*** INVENTORY ITEM CREATED SUCCESSFULLY ***\n");
        } else {
            System.out.println("*** RETURNING TO INVENTORY MENU ***\n");
        }
    }

    // run after confirming with user with inventorySearch
    public static void delete() {

        System.out.println("*** DELETE ITEM ***");
        System.out.println("Please enter the id of the item to delete:");
        id = userScanner.nextInt();
        System.out.println("The following item is about to be deleted: ");
        lookup(id);
        System.out.println("Enter 1 to delete or 2 to cancel: ");
        check = userScanner.nextInt();
        if (check == 1) {
            String query = "DELETE FROM inventory WHERE id = ?";

            try (Connection conn = MySQLConnector.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                // set input to the query
                stmt.setInt(1, id);

                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("*** ITEM DELETED ***");
                } else {
                    System.out.println("*** NO ITEM FOUND WITH ID: " + id + "***");
                }
            } catch (SQLException e) {
                System.out.println("\n*** Connection to Database Failed ***\n");
            }
            System.out.println();
        } else {
            System.out.println("*** RETURNING TO INVENTORY MENU ***");
        }

    }

    // helper InventoryService methods
    static void lookup(int id) {

        System.out.println("ID | NAME | QUANTITY | PRICE/UNIT");
        String query = "SELECT id, name, quantity, price FROM inventory WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // set input to the query
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

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
        System.out.println();
    }

    public static void checkInventoryAlerts() {
        String query = "SELECT id, name, quantity, alert FROM inventory WHERE alert = 1";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("*** LOW INVENTORY ALERTS ***");
            boolean hasAlerts = false;
            while (rs.next()) {
                hasAlerts = true;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int quantity = rs.getInt("quantity");

                System.out.println("ID: " + id + " | Name: " + name + " | Quantity: " + quantity);
            }
            if (!hasAlerts) {
                System.out.println("No inventory alerts.");
            }

        } catch (SQLException e) {
            System.out.println("Error checking inventory alerts.");
            e.printStackTrace();
        }
    }
}