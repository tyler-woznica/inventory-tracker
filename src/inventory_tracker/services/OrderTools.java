package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderTools {

    public static void orderSearch(int id) {
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
            System.out.println("\n*** Connection to Database Failed ***\n");
            e.printStackTrace();
        }
        System.out.println();
    }

    // creates an item based on user input
    public static void inventoryCreate() {

    }

    // deletes an item based on product id with confirmation
    public static void inventoryDelete() {

    }
}
