package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryTools {

    // search for a specific item using the inventory id
    public static void inventorySearch(int id) {
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

    // gets a full report of current inventory
    public static void inventoryReport() {

        System.out.println("ID | NAME | QUANTITY | COST/UNIT");
        String query = "SELECT id, name, quantity, cost FROM inventory";

        try(Connection conn = MySQLConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getInt("quantity") + " | " + "$" +
                                rs.getDouble("cost")
                );
            }
        } catch (SQLException e) {
            System.out.println("\n*** Connection to Database Failed ***\n");
        }
        System.out.println();
    }

    // creates an item based on user input
    public static void inventoryCreate() {

    }
}
