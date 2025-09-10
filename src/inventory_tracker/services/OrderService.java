package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;

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
        System.out.println("*** ORDER UPDATE ***");
        System.out.println("Please enter the ID of the order to update:");
        id = userScanner.nextInt();
        lookup(id);
        System.out.println("What field would you like to update?");
        // insert switch
    }

    public static void create() {
        System.out.println("*** CREATE ORDER ***");
        userScanner.nextLine();

        System.out.println("Enter the customer ID for this order:");
        customer_id = userScanner.nextInt();

        System.out.println("Enter the date or leave blank to auto-generate:");
        order_date = userScanner.nextLine();



        // need to add functionality to auto calculate total and pull inventory
    }


    public static void delete() {
        System.out.println("*** DELETE ORDER ***");
        System.out.println("Please enter the ID of the order to delete:");
        id = userScanner.nextInt();
        System.out.println("The following order is about to be deleted:");
        lookup(id);
        System.out.println("Enter 1 to delete or 2 to cancel: ");
        check = userScanner.nextInt();
        if (check == 1) {
            String query = "DELETE FROM orders WHERE id = ?";

            try (Connection conn = MySQLConnector.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                // set input to the query
                stmt.setInt(1, id);

                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("*** ORDER DELETED ***");
                } else {
                    System.out.println("*** NO ORDER FOUND WITH ID: " + id + "***");
                }
            } catch (SQLException e) {
                System.out.println("\n*** Connection to Database Failed ***\n");
            }
            System.out.println();
        } else {
            System.out.println("*** RETURNING TO INVENTORY MENU ***");
        }

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
}
