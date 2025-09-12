package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;
import inventory_tracker.model.Item;
import java.sql.*;
import java.util.Scanner;

/**
 * Service class for handling all inventory-related database operations.
 * Provides CRUD functionality and inventory management features including alerts.
 */
public class InventoryService {

    /**
     * Searches for an inventory item by ID and displays the result
     * @param scanner Scanner instance for user input
     */
    public static void search(Scanner scanner) {
        System.out.println("*** INVENTORY SEARCH ***");

        int id = ExceptionService.getValidatedIntInput(scanner,
                "Please enter the item ID:", 1, Integer.MAX_VALUE);

        System.out.println("*** SEARCH RESULTS ***");
        lookup(id);
    }

    /**
     * Displays a complete report of all inventory items
     * @param scanner Scanner instance (for consistency, though not used in this method)
     */
    public static void report(Scanner scanner) {
        System.out.println("*** INVENTORY REPORT ***");
        System.out.println("ID | NAME | QUANTITY | PRICE/UNIT | ALERT\n");

        String query = "SELECT id, name, quantity, price, alert FROM inventory ORDER BY id";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            boolean hasItems = false;
            while (rs.next()) {
                hasItems = true;
                String alertStatus = rs.getInt("alert") == 1 ? "LOW STOCK" : "OK";
                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getInt("quantity") + " | $" +
                                String.format("%.2f", rs.getDouble("price")) + " | " +
                                alertStatus
                );
            }

            if (!hasItems) {
                System.out.println("No inventory items found.");
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "inventory report generation");
        }
        System.out.println();
    }

    /**
     * Updates an existing inventory item's information with complete field selection
     * @param scanner Scanner instance for user input
     */
    public static void update(Scanner scanner) {
        System.out.println("*** ITEM UPDATE ***");

        int id = ExceptionService.getValidatedIntInput(scanner,
                "Please enter the ID of the item to update:", 1, Integer.MAX_VALUE);

        // Display current item information
        if (!lookup(id)) {
            System.out.println("Item not found. Update cancelled.");
            return;
        }

        if (!ExceptionService.getUserConfirmation(scanner, "Is this the correct item to update?")) {
            System.out.println("*** UPDATE CANCELLED ***");
            return;
        }

        // Field selection menu
        System.out.println("What field would you like to update?");
        System.out.println("""
            1. Name
            2. Quantity
            3. Price
            """);

        int choice = ExceptionService.getValidatedIntInput(scanner,
                "Select field to update:", 1, 3);

        scanner.nextLine(); // Clear buffer after nextInt()

        String sql = "";
        Object newValue = null;
        boolean validInput = false;

        switch (choice) {
            case 1:
                // Update name
                System.out.print("Enter new name: ");
                String name = scanner.nextLine().trim();
                if (ValidationService.isValidString(name)) {
                    sql = "UPDATE inventory SET name = ? WHERE id = ?";
                    newValue = name;
                    validInput = true;
                } else {
                    ExceptionService.displayValidationError("Item Name", "Non-empty text");
                }
                break;

            case 2:
                // Update quantity with validation
                int quantity = ExceptionService.getValidatedIntInput(scanner,
                        "Enter new quantity:", 0, Integer.MAX_VALUE);
                sql = "UPDATE inventory SET quantity = ? WHERE id = ?";
                newValue = quantity;
                validInput = true;
                break;

            case 3:
                // Update price with validation
                double price = ExceptionService.getValidatedDoubleInput(scanner,
                        "Enter new price:", 0.01, 999999.99);
                sql = "UPDATE inventory SET price = ? WHERE id = ?";
                newValue = price;
                validInput = true;
                break;
        }

        // Execute update if input is valid
        if (validInput) {
            executeUpdate(sql, newValue, id);
        } else {
            System.out.println("*** UPDATE CANCELLED DUE TO INVALID INPUT ***");
        }
    }

    /**
     * Creates a new inventory item with validation for all fields
     * @param scanner Scanner instance for user input
     */
    public static void create(Scanner scanner) {
        System.out.println("*** CREATE ITEM ***");
        scanner.nextLine(); // Clear buffer

        String name;
        int quantity;
        double price;

        // Get and validate item name
        do {
            System.out.print("Enter new item name: ");
            name = scanner.nextLine().trim();
            if (!ValidationService.isValidString(name)) {
                ExceptionService.displayValidationError("Item Name", "Non-empty text");
            }
        } while (!ValidationService.isValidString(name));

        // Get and validate quantity
        quantity = ExceptionService.getValidatedIntInput(scanner,
                "Enter new item quantity:", 0, Integer.MAX_VALUE);

        // Get and validate price
        price = ExceptionService.getValidatedDoubleInput(scanner,
                "Enter new item price per unit:", 0.01, 999999.99);

        // Create item object
        Item item = new Item(name, quantity, price);

        // Display confirmation
        System.out.println("\nThe following item is about to be created:");
        System.out.println("Name: " + item.getName() + " | " +
                "Quantity: " + item.getQuantity() + " | " +
                "Price/Unit: $" + String.format("%.2f", item.getPrice()));

        if (ExceptionService.getUserConfirmation(scanner, "Confirm item creation?")) {
            executeCreate(item);
        } else {
            System.out.println("*** ITEM CREATION CANCELLED ***");
        }
    }

    /**
     * Deletes an inventory item by ID with confirmation
     * @param scanner Scanner instance for user input
     */
    public static void delete(Scanner scanner) {
        System.out.println("*** DELETE ITEM ***");

        int id = ExceptionService.getValidatedIntInput(scanner,
                "Please enter the ID of the item to delete:", 1, Integer.MAX_VALUE);

        System.out.println("The following item is about to be deleted:");
        if (!lookup(id)) {
            System.out.println("Item not found. Delete cancelled.");
            return;
        }

        // Check if item is used in any orders before allowing deletion
        if (isItemUsedInOrders(id)) {
            System.out.println("*** WARNING: This item is referenced in existing orders ***");
            System.out.println("Deleting this item may cause data integrity issues.");
            if (!ExceptionService.getUserConfirmation(scanner,
                    "Are you absolutely sure you want to delete this item?")) {
                System.out.println("*** DELETE CANCELLED ***");
                return;
            }
        }

        if (ExceptionService.getUserConfirmation(scanner, "Confirm item deletion?")) {
            executeDelete(id);
        } else {
            System.out.println("*** DELETE CANCELLED ***");
        }
    }

    /**
     * Displays current inventory alert status for low stock items
     */
    public static void checkInventoryAlerts() {
        String query = "SELECT id, name, quantity, alert FROM inventory WHERE alert = 1 ORDER BY quantity ASC";

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
            ExceptionService.handleDatabaseError(e, "inventory alert check");
        }
    }

    /**
     * Helper method to look up and display an inventory item by ID
     * @param id Item ID to look up
     * @return true if item was found, false otherwise
     */
    static boolean lookup(int id) {
        System.out.println("ID | NAME | QUANTITY | PRICE/UNIT | ALERT");
        String query = "SELECT id, name, quantity, price, alert FROM inventory WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String alertStatus = rs.getInt("alert") == 1 ? "LOW STOCK" : "OK";
                    System.out.println(
                            rs.getInt("id") + " | " +
                                    rs.getString("name") + " | " +
                                    rs.getInt("quantity") + " | $" +
                                    String.format("%.2f", rs.getDouble("price")) + " | " +
                                    alertStatus
                    );
                    return true;
                } else {
                    System.out.println("No product found with ID: " + id);
                    return false;
                }
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "inventory lookup");
            return false;
        }
    }

    /**
     * Checks if an inventory item is referenced in any orders
     * @param itemId ID of the inventory item to check
     * @return true if item is used in orders, false otherwise
     */
    private static boolean isItemUsedInOrders(int itemId) {
        String query = "SELECT COUNT(*) FROM order_items WHERE inventory_id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, itemId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "order reference check");
        }

        return false;
    }

    /**
     * Executes an inventory item update query
     * @param sql Update SQL statement
     * @param newValue New value for the field being updated
     * @param itemId ID of item to update
     */
    private static void executeUpdate(String sql, Object newValue, int itemId) {
        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, newValue);
            stmt.setInt(2, itemId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("*** ITEM UPDATED SUCCESSFULLY ***");

                // Show updated item
                System.out.println("Updated item:");
                lookup(itemId);
            } else {
                System.out.println("No item found with ID " + itemId);
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "inventory update");
        }
    }

    /**
     * Executes inventory item creation in database
     * @param item Item object to insert
     */
    private static void executeCreate(Item item) {
        String query = "INSERT INTO inventory (name, quantity, price, alert) VALUES (?, ?, ?, ?)";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getQuantity());
            stmt.setDouble(3, item.getPrice());
            stmt.setInt(4, item.getAlert()); // Default is 0 from constructor

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Get the generated ID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);
                        System.out.println("*** INVENTORY ITEM CREATED SUCCESSFULLY ***");
                        System.out.println("New item ID: " + newId);
                    }
                }
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "inventory creation");
        }
    }

    /**
     * Executes inventory item deletion from database
     * @param itemId ID of item to delete
     */
    private static void executeDelete(int itemId) {
        String query = "DELETE FROM inventory WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, itemId);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("*** ITEM DELETED SUCCESSFULLY ***");
            } else {
                System.out.println("*** NO ITEM FOUND WITH ID: " + itemId + " ***");
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "inventory deletion");
        }
    }
}