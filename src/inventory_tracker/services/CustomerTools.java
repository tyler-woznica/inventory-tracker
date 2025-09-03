package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerTools {

    // search for a specific item using the inventory id
    public static void customerSearch(int id) {
        String query = "SELECT id, name, quantity, cost FROM inventory WHERE id = ?";

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
                                    rs.getDouble("cost")
                    );
                } else {
                    System.out.println("No product found with ID: " + id);
                }
            }

        } catch (SQLException e) {
            System.out.println("\n*** Connection to Database Failed ***\n");
            e.printStackTrace();
        }
        System.out.println();
    }


}
