package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;
import inventory_tracker.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class OrderService {

    static int id;
    static int customer_id;
    static String order_date;
    static double total;
    static Scanner userScanner = new Scanner(System.in);
    static int check;

    public static void search() {

        System.out.println("*** ORDER SEARCH ***");

        System.out.println("Please enter the order ID:");
        id = userScanner.nextInt();

        System.out.println("*** SEARCH RESULTS ***");
        lookup(id);
    }

    public static void update() {
        System.out.println("*** FEATURE IN DEVELOPMENT *** ");
//        System.out.println("*** ORDER UPDATE ***");
//        System.out.println("Please enter the ID of the order to update:");
//        id = userScanner.nextInt();
//        lookup(id);
//        System.out.println("What field would you like to update?");
//        // insert switch
    }

    public static void create() {
        System.out.println("*** CREATE ORDER ***");
        userScanner.nextLine();

        System.out.println("Enter customer ID:");
        int customerId = Integer.parseInt(userScanner.nextLine());

        int orderId = -1;

        String insertOrderQuery = "INSERT INTO orders (customer_id) VALUES (?)";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {

            // Insert order
            orderStmt.setInt(1, customerId);
            orderStmt.executeUpdate();

            // Get generated order ID
            try (ResultSet rs = orderStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error creating order.");
            e.printStackTrace();
            return;
        }

        if (orderId == -1) {
            System.out.println("Failed to retrieve order ID.");
            return;
        }

        // Loop to add order items
        boolean addingItems = true;
        while (addingItems) {
            System.out.println("Enter inventory item ID to add:");
            int inventoryId = Integer.parseInt(userScanner.nextLine());

            System.out.println("Enter quantity:");
            int quantity = Integer.parseInt(userScanner.nextLine());

            String insertItemQuery = "INSERT INTO order_items (order_id, inventory_id, quantity) VALUES (?, ?, ?)";

            try (Connection conn = MySQLConnector.getConnection();
                 PreparedStatement itemStmt = conn.prepareStatement(insertItemQuery)) {

                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, inventoryId);
                itemStmt.setInt(3, quantity);

                itemStmt.executeUpdate();

                System.out.println("Item added to order.");

            } catch (SQLException e) {
                System.out.println("Error adding item to order.");
                e.printStackTrace();
            }

            System.out.println("Add another item? (y/n):");
            String choice = userScanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) {
                addingItems = false;
            }
        }

        System.out.println("*** ORDER CREATED SUCCESSFULLY ***\n");
    }


    public static void delete() {
        System.out.println("*** FEATURE IN DEVELOPMENT ***");

//        System.out.println("*** DELETE ORDER ***");
//        System.out.println("Please enter the ID of the order to delete:");
//        id = userScanner.nextInt();
//        System.out.println("The following order is about to be deleted:");
//        lookup(id);
//        System.out.println("Enter 1 to delete or 2 to cancel: ");
//        check = userScanner.nextInt();
//        if (check == 1) {
//            String query = "DELETE FROM orders WHERE id = ?";
//
//            try (Connection conn = MySQLConnector.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement(query)) {
//
//                // set input to the query
//                stmt.setInt(1, id);
//
//                int rowsDeleted = stmt.executeUpdate();
//
//                if (rowsDeleted > 0) {
//                    System.out.println("*** ORDER DELETED ***");
//                } else {
//                    System.out.println("*** NO ORDER FOUND WITH ID: " + id + "***");
//                }
//            } catch (SQLException e) {
//                System.out.println("\n*** Connection to Database Failed ***\n");
//            }
//            System.out.println();
//        } else {
//            System.out.println("*** RETURNING TO INVENTORY MENU ***");
//        }

    }

    static void lookup(int id) {
        System.out.println("ID | CUSTOMER ID | ORDER DATE | TOTAL");
        String query = "SELECT id, customer_id, order_date, total FROM orders WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // set input to the query
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    System.out.println(
                            rs.getInt("id") + " | " +
                                    rs.getInt("customer_id") + " | " +
                                    rs.getString("order_date") + " | " + "$" +
                                    rs.getDouble("total")
                    );
                } else {
                    System.out.println("No order found with ID: " + id);
                }
            }

        } catch (SQLException e) {
            System.out.println("*** Connection to Database Failed ***");
        }
    }

    public static void checkOrderAlerts() {
        String query = "SELECT o.id, o.customer_id, o.total, o.alert, c.business_name " +
                "FROM orders o " +
                "JOIN customers c ON o.customer_id = c.id " +
                "WHERE o.alert = 1";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("*** ORDER ALERTS (INSUFFICIENT STOCK) ***");
            boolean hasAlerts = false;
            while (rs.next()) {
                hasAlerts = true;
                int id = rs.getInt("id");
                int customerId = rs.getInt("customer_id");
                double total = rs.getDouble("total");
                String businessName = rs.getString("business_name");

                System.out.println("Order ID: " + id + " | Customer: " + businessName +
                        " (ID: " + customerId + ") | Total: $" + total);
            }
            if (!hasAlerts) {
                System.out.println("No order alerts.");
            }

        } catch (SQLException e) {
            System.out.println("Error checking order alerts.");
            e.printStackTrace();
        }
    }

}
