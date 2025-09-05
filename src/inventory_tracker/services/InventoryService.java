package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;
import inventory_tracker.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryService {

    // search for a specific item using the inventory id
    public static void inventorySearch(int id) {
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
            System.out.println("\n*** Connection to Database Failed ***\n");
        }
        System.out.println();
    }

    // gets a full report of current inventory
    public static void inventoryReport() {

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
            System.out.println("\n*** Connection to Database Failed ***\n");
        }
        System.out.println();
    }

    // updates existing item based on user input
    public static void updateInventory() {

    }

    // creates an item based on user input
    public static void createInventory(Item item) {
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
    public static void deleteInventory(int id) {
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

    public static void search() {

    }

    public static void report() {

    }

    public static void create() {

    }

    public static void delete() {

    }

}
