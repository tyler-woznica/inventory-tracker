package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;
import inventory_tracker.model.OrderItem;
import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling all order-related database operations.
 * Provides complete CRUD functionality for order management including order items.
 * Works with database triggers that automatically handle inventory updates and total calculations.
 */
public class OrderService {

    /**
     * Searches for an order by ID and displays the result with order items
     * @param scanner Scanner instance for user input
     */
    public static void search(Scanner scanner) {
        System.out.println("*** ORDER SEARCH ***");

        int id = ExceptionService.getValidatedIntInput(scanner,
                "Please enter the order ID:", 1, Integer.MAX_VALUE);

        System.out.println("*** SEARCH RESULTS ***");
        if (lookup(id)) {
            displayOrderItems(id);
        }
    }

    /**
     * Updates an existing order (currently supports adding/removing items)
     * @param scanner Scanner instance for user input
     */
    public static void update(Scanner scanner) {
        System.out.println("*** ORDER UPDATE ***");

        int orderId = ExceptionService.getValidatedIntInput(scanner,
                "Please enter the ID of the order to update:", 1, Integer.MAX_VALUE);

        // Display current order information
        if (!lookup(orderId)) {
            System.out.println("Order not found. Update cancelled.");
            return;
        }

        displayOrderItems(orderId);

        if (!ExceptionService.getUserConfirmation(scanner, "Is this the correct order to update?")) {
            System.out.println("*** UPDATE CANCELLED ***");
            return;
        }

        // Update options menu
        System.out.println("What would you like to update?");
        System.out.println("""
            1. Add items to order
            2. Remove items from order
            3. Update item quantities
            """);

        int choice = ExceptionService.getValidatedIntInput(scanner,
                "Select update option:", 1, 3);

        switch (choice) {
            case 1:
                addItemsToOrder(scanner, orderId);
                break;
            case 2:
                removeItemsFromOrder(scanner, orderId);
                break;
            case 3:
                updateOrderItemQuantities(scanner, orderId);
                break;
        }

        System.out.println("*** ORDER UPDATED SUCCESSFULLY ***");
    }

    /**
     * Creates a new order with items, leveraging database triggers for calculations
     * @param scanner Scanner instance for user input
     */
    public static void create(Scanner scanner) {
        System.out.println("*** CREATE ORDER ***");
        scanner.nextLine(); // Clear buffer

        // Get and validate customer ID
        int customerId = ExceptionService.getValidatedIntInput(scanner,
                "Enter customer ID:", 1, Integer.MAX_VALUE);

        // Verify customer exists
        if (!verifyCustomerExists(customerId)) {
            System.out.println("Customer with ID " + customerId + " not found.");
            System.out.println("*** ORDER CREATION CANCELLED ***");
            return;
        }

        // Create the order first (database will set order_date automatically)
        int orderId = createOrderInDatabase(customerId);

        if (orderId == -1) {
            System.out.println("Failed to create order. Please try again.");
            return;
        }

        System.out.println("Order created with ID: " + orderId);
        System.out.println("Now adding items to the order...\n");

        // Add items to the order
        addItemsToOrder(scanner, orderId);

        System.out.println("*** ORDER CREATED SUCCESSFULLY ***");

        // Display final order summary
        System.out.println("\n*** FINAL ORDER SUMMARY ***");
        lookup(orderId);
        displayOrderItems(orderId);
    }

    /**
     * Deletes an order and all its items with confirmation
     * @param scanner Scanner instance for user input
     */
    public static void delete(Scanner scanner) {
        System.out.println("*** DELETE ORDER ***");

        int orderId = ExceptionService.getValidatedIntInput(scanner,
                "Please enter the ID of the order to delete:", 1, Integer.MAX_VALUE);

        System.out.println("The following order is about to be deleted:");
        if (!lookup(orderId)) {
            System.out.println("Order not found. Delete cancelled.");
            return;
        }

        displayOrderItems(orderId);

        System.out.println("\n*** WARNING: This will delete the order and all its items ***");
        System.out.println("Inventory quantities will be automatically restored by database triggers.");

        if (ExceptionService.getUserConfirmation(scanner, "Are you sure you want to delete this order?")) {
            executeOrderDelete(orderId);
        } else {
            System.out.println("*** DELETE CANCELLED ***");
        }
    }

    /**
     * Displays current order alert status for orders with issues
     */
    public static void checkOrderAlerts() {
        String query = """
            SELECT o.id, o.customer_id, o.total, o.alert, c.business_name 
            FROM orders o 
            JOIN customers c ON o.customer_id = c.id 
            WHERE o.alert = 1
            ORDER BY o.id
            """;

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("*** ORDER ALERTS (INSUFFICIENT STOCK OR OTHER ISSUES) ***");
            boolean hasAlerts = false;

            while (rs.next()) {
                hasAlerts = true;
                int id = rs.getInt("id");
                int customerId = rs.getInt("customer_id");
                double total = rs.getDouble("total");
                String businessName = rs.getString("business_name");

                System.out.println("Order ID: " + id + " | Customer: " + businessName +
                        " (ID: " + customerId + ") | Total: $" + String.format("%.2f", total));
            }

            if (!hasAlerts) {
                System.out.println("No order alerts.");
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "order alert check");
        }
    }

    /**
     * Helper method to look up and display an order by ID
     * @param id Order ID to look up
     * @return true if order was found, false otherwise
     */
    static boolean lookup(int id) {
        System.out.println("ID | CUSTOMER ID | ORDER DATE | TOTAL | ALERT");
        String query = "SELECT id, customer_id, order_date, total, alert FROM orders WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String alertStatus = rs.getInt("alert") == 1 ? "ALERT" : "OK";
                    System.out.println(
                            rs.getInt("id") + " | " +
                                    rs.getInt("customer_id") + " | " +
                                    rs.getTimestamp("order_date") + " | $" +
                                    String.format("%.2f", rs.getDouble("total")) + " | " +
                                    alertStatus
                    );
                    return true;
                } else {
                    System.out.println("No order found with ID: " + id);
                    return false;
                }
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "order lookup");
            return false;
        }
    }

    /**
     * Displays all items in a specific order
     * @param orderId ID of the order to display items for
     */
    private static void displayOrderItems(int orderId) {
        String query = """
            SELECT oi.id, oi.inventory_id, oi.quantity, i.name, i.price,
                   (oi.quantity * i.price) as line_total
            FROM order_items oi
            JOIN inventory i ON oi.inventory_id = i.id
            WHERE oi.order_id = ?
            ORDER BY oi.id
            """;

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("\n*** ORDER ITEMS ***");
                System.out.println("ITEM ID | INVENTORY ID | NAME | QUANTITY | UNIT PRICE | LINE TOTAL");

                boolean hasItems = false;
                while (rs.next()) {
                    hasItems = true;
                    System.out.println(
                            rs.getInt("id") + " | " +
                                    rs.getInt("inventory_id") + " | " +
                                    rs.getString("name") + " | " +
                                    rs.getInt("quantity") + " | $" +
                                    String.format("%.2f", rs.getDouble("price")) + " | $" +
                                    String.format("%.2f", rs.getDouble("line_total"))
                    );
                }

                if (!hasItems) {
                    System.out.println("No items in this order.");
                }
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "order items display");
        }
    }

    /**
     * Adds items to an existing order
     * @param scanner Scanner for user input
     * @param orderId ID of the order to add items to
     */
    private static void addItemsToOrder(Scanner scanner, int orderId) {
        boolean addingItems = true;

        while (addingItems) {
            // Display available inventory for reference
            System.out.println("\n*** AVAILABLE INVENTORY ***");
            displayAvailableInventory();

            int inventoryId = ExceptionService.getValidatedIntInput(scanner,
                    "Enter inventory item ID to add:", 1, Integer.MAX_VALUE);

            // Verify inventory item exists and check available quantity
            int availableQuantity = getAvailableQuantity(inventoryId);
            if (availableQuantity == -1) {
                System.out.println("Inventory item not found. Please try again.");
                continue;
            }

            System.out.println("Available quantity: " + availableQuantity);

            int quantity = ExceptionService.getValidatedIntInput(scanner,
                    "Enter quantity to add:", 1, availableQuantity);

            if (addItemToOrder(orderId, inventoryId, quantity)) {
                System.out.println("Item added to order successfully.");
            } else {
                System.out.println("Failed to add item to order.");
            }

            System.out.println("Add another item? (y/n):");
            scanner.nextLine(); // Clear buffer
            String choice = scanner.nextLine().trim();
            if (!choice.equalsIgnoreCase("y")) {
                addingItems = false;
            }
        }
    }

    /**
     * Removes items from an existing order
     * @param scanner Scanner for user input
     * @param orderId ID of the order to remove items from
     */
    private static void removeItemsFromOrder(Scanner scanner, int orderId) {
        // Display current order items
        displayOrderItems(orderId);

        int orderItemId = ExceptionService.getValidatedIntInput(scanner,
                "Enter the ORDER ITEM ID to remove (first column above):", 1, Integer.MAX_VALUE);

        if (ExceptionService.getUserConfirmation(scanner, "Remove this item from the order?")) {
            removeOrderItem(orderItemId);
        }
    }

    /**
     * Updates quantities of items in an existing order
     * @param scanner Scanner for user input
     * @param orderId ID of the order to update item quantities for
     */
    private static void updateOrderItemQuantities(Scanner scanner, int orderId) {
        // Display current order items
        displayOrderItems(orderId);

        int orderItemId = ExceptionService.getValidatedIntInput(scanner,
                "Enter the ORDER ITEM ID to update quantity (first column above):", 1, Integer.MAX_VALUE);

        // Get current quantity and available inventory
        OrderItemInfo itemInfo = getOrderItemInfo(orderItemId);
        if (itemInfo == null) {
            System.out.println("Order item not found.");
            return;
        }

        int availableQuantity = getAvailableQuantity(itemInfo.inventoryId) + itemInfo.currentQuantity;
        System.out.println("Current quantity: " + itemInfo.currentQuantity);
        System.out.println("Maximum available quantity: " + availableQuantity);

        int newQuantity = ExceptionService.getValidatedIntInput(scanner,
                "Enter new quantity:", 1, availableQuantity);

        if (updateOrderItemQuantity(orderItemId, newQuantity)) {
            System.out.println("Order item quantity updated successfully.");
        } else {
            System.out.println("Failed to update order item quantity.");
        }
    }

    /**
     * Helper class to hold order item information
     */
    private static class OrderItemInfo {
        int inventoryId;
        int currentQuantity;

        OrderItemInfo(int inventoryId, int currentQuantity) {
            this.inventoryId = inventoryId;
            this.currentQuantity = currentQuantity;
        }
    }

    /**
     * Gets information about a specific order item
     * @param orderItemId ID of the order item
     * @return OrderItemInfo object or null if not found
     */
    private static OrderItemInfo getOrderItemInfo(int orderItemId) {
        String query = "SELECT inventory_id, quantity FROM order_items WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, orderItemId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new OrderItemInfo(rs.getInt("inventory_id"), rs.getInt("quantity"));
                }
            }
        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "order item info retrieval");
        }

        return null;
    }

    /**
     * Updates the quantity of a specific order item
     * @param orderItemId ID of the order item to update
     * @param newQuantity New quantity for the item
     * @return true if successful, false otherwise
     */
    private static boolean updateOrderItemQuantity(int orderItemId, int newQuantity) {
        String query = "UPDATE order_items SET quantity = ? WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, newQuantity);
            stmt.setInt(2, orderItemId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "order item quantity update");
            return false;
        }
    }

    /**
     * Displays available inventory items for selection
     */
    private static void displayAvailableInventory() {
        String query = "SELECT id, name, quantity, price FROM inventory WHERE quantity > 0 ORDER BY name";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("ID | NAME | AVAILABLE | PRICE");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getInt("quantity") + " | $" +
                                String.format("%.2f", rs.getDouble("price"))
                );
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "inventory display");
        }
    }

    /**
     * Gets the available quantity for a specific inventory item
     * @param inventoryId ID of the inventory item
     * @return Available quantity, or -1 if item not found
     */
    private static int getAvailableQuantity(int inventoryId) {
        String query = "SELECT quantity FROM inventory WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, inventoryId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantity");
                }
            }
        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "inventory quantity check");
        }

        return -1;
    }

    /**
     * Verifies that a customer exists in the database
     * @param customerId ID of the customer to verify
     * @return true if customer exists, false otherwise
     */
    private static boolean verifyCustomerExists(int customerId) {
        String query = "SELECT id FROM customers WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "customer verification");
            return false;
        }
    }

    /**
     * Creates a new order in the database
     * @param customerId ID of the customer placing the order
     * @return Generated order ID, or -1 if failed
     */
    private static int createOrderInDatabase(int customerId) {
        String query = "INSERT INTO orders (customer_id) VALUES (?)";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, customerId);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "order creation");
        }

        return -1;
    }

    /**
     * Adds an item to an order
     * @param orderId ID of the order
     * @param inventoryId ID of the inventory item
     * @param quantity Quantity to add
     * @return true if successful, false otherwise
     */
    private static boolean addItemToOrder(int orderId, int inventoryId, int quantity) {
        String query = "INSERT INTO order_items (order_id, inventory_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, orderId);
            stmt.setInt(2, inventoryId);
            stmt.setInt(3, quantity);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "order item addition");
            return false;
        }
    }

    /**
     * Removes an item from an order
     * @param orderItemId ID of the order item to remove
     * @return true if successful, false otherwise
     */
    private static boolean removeOrderItem(int orderItemId) {
        String query = "DELETE FROM order_items WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, orderItemId);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Order item removed successfully.");
                return true;
            } else {
                System.out.println("No order item found with ID: " + orderItemId);
                return false;
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "order item removal");
            return false;
        }
    }

    /**
     * Deletes an entire order and all of its items
     * @param orderId ID of the order to delete
     */
    private static void executeOrderDelete(int orderId) {
        try (Connection conn = MySQLConnector.getConnection()) {

            conn.setAutoCommit(false);

            try {
                // Delete order items first due to foreign key constraint
                String deleteItemsQuery = "DELETE FROM order_items WHERE order_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteItemsQuery)) {
                    stmt.setInt(1, orderId);
                    int itemsDeleted = stmt.executeUpdate();
                    System.out.println("Deleted " + itemsDeleted + " order items.");
                }

                // Delete the order
                String deleteOrderQuery = "DELETE FROM orders WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteOrderQuery)) {
                    stmt.setInt(1, orderId);
                    int ordersDeleted = stmt.executeUpdate();

                    if (ordersDeleted > 0) {
                        System.out.println("*** ORDER DELETED SUCCESSFULLY ***");
                        System.out.println("Inventory quantities have been automatically restored.");
                    } else {
                        System.out.println("*** NO ORDER FOUND WITH ID: " + orderId + " ***");
                    }
                }

                conn.commit();

            } catch (SQLException e) {
                // Rollback on error
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "order deletion");
        }
    }
}