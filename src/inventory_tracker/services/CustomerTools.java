package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerTools {

    // search for a specific item using the inventory id
    public static void customerSearch(int id) {
        String query = "SELECT id, business_name, email, phone, city, state FROM customers WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // set input to the query
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println(
                            rs.getInt("id") + " | " +
                                    rs.getString("business_name") + " | " +
                                    rs.getString("email") + " | " +
                                    rs.getLong("phone") + " | " +
                                    rs.getString("city") + " | " +
                                    rs.getString("state")
                    );
                } else {
                    System.out.println("No customers found with ID: " + id);
                }
            }

        } catch (SQLException e) {
            System.out.println("\n*** Connection to Database Failed ***\n");
            e.printStackTrace();
        }
        System.out.println();
    }

    // creates a customer based on user input
    public static void customerCreate() {

    }
    // deletes a customer based on product id with confirmation
    public static void customerDelete() {

    }


}
