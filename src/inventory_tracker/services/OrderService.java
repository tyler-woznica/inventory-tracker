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

    }

    // creates an item based on user input
    public static void create() {

    }

    // deletes an item based on product id with confirmation
    public static void delete() {

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
