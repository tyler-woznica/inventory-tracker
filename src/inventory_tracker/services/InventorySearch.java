package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventorySearch {

    public static void inventorySearch() {
        String query = "SELECT id, name, quantity, cost FROM inventory";

        try(Connection conn = MySQLConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getInt("quantity") + " | " +
                        rs.getDouble("cost")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
