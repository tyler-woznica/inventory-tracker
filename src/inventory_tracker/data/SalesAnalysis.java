package inventory_tracker.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Sales analysis utility class providing business intelligence queries.
 * Generates reports on top/bottom performing items and customers.
 * Uses custom sorting algorithm and provides formatted output.
 */
public class SalesAnalysis {

    /**
     * Internal class to hold result data for analysis queries
     */
    static class ResultRow {
        String label;
        double value;

        ResultRow(String label, double value) {
            this.label = label;
            this.value = value;
        }

        @Override
        public String toString() {
            return label + " - " + value;
        }
    }

    /**
     * Generic method to execute top/bottom analysis queries
     *
     * @param sql         SQL query to execute
     * @param labelColumn Column name for the label (item name, customer name)
     * @param n           Number of results to return
     * @param top         true for top results (descending), false for bottom (ascending)
     * @return List of ResultRow objects containing results
     */
    private static java.util.List<ResultRow> getTopOrBottom(
            String sql, String labelColumn, int n, boolean top) {

        java.util.List<ResultRow> results = new java.util.ArrayList<>();

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Collect all results
            while (rs.next()) {
                String label = rs.getString(labelColumn);
                double value = rs.getDouble("value");
                results.add(new ResultRow(label, value));
            }

        } catch (SQLException e) {
            System.err.println("*** ERROR CONNECTING TO DATABASE ***");
            System.err.println("Error: " + e.getMessage());
            return new java.util.ArrayList<>(); // Return empty list on error
        }

        // Sort results using custom insertion sort
        insertionSort(results, !top); // if top=true → descending, else ascending

        // Return top N results
        return results.subList(0, Math.min(n, results.size()));
    }

    /**
     * Displays top 3 best-selling items by quantity sold
     *
     * @throws SQLException if database query fails
     */
    public static void topThreeItems() throws SQLException {
        String sql = """
                SELECT i.name, SUM(oi.quantity) AS value
                FROM inventory i
                JOIN order_items oi ON i.id = oi.inventory_id
                GROUP BY i.id, i.name
                HAVING SUM(oi.quantity) > 0
                """;

        java.util.List<ResultRow> results = getTopOrBottom(sql, "name", 3, true);

        System.out.println("\n*** TOP 3 BEST-SELLING ITEMS ***");
        if (results.isEmpty()) {
            System.out.println("No sales data available.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                ResultRow r = results.get(i);
                System.out.println((i + 1) + ". " + r.label + " - Units Sold: " + (int) r.value);
            }
        }
        System.out.println();
    }

    /**
     * Displays bottom 3 worst-selling items by quantity sold
     *
     * @throws SQLException if database query fails
     */
    public static void bottomThreeItems() throws SQLException {
        String sql = """
                SELECT i.name, COALESCE(SUM(oi.quantity), 0) AS value
                FROM inventory i
                LEFT JOIN order_items oi ON i.id = oi.inventory_id
                GROUP BY i.id, i.name
                """;

        java.util.List<ResultRow> results = getTopOrBottom(sql, "name", 3, false);

        System.out.println("\n*** BOTTOM 3 WORST-SELLING ITEMS ***");
        if (results.isEmpty()) {
            System.out.println("No inventory data available.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                ResultRow r = results.get(i);
                System.out.println((i + 1) + ". " + r.label + " - Units Sold: " + (int) r.value);
            }
        }
        System.out.println();
    }

    /**
     * Displays top 3 customers by total order value
     *
     * @throws SQLException if database query fails
     */
    public static void topThreeCustomers() throws SQLException {
        String sql = """
                SELECT c.business_name, COALESCE(SUM(o.total), 0) AS value
                FROM customers c
                LEFT JOIN orders o ON c.id = o.customer_id
                GROUP BY c.id, c.business_name
                HAVING COALESCE(SUM(o.total), 0) > 0
                """;

        java.util.List<ResultRow> results = getTopOrBottom(sql, "business_name", 3, true);

        System.out.println("\n*** TOP 3 CUSTOMERS BY TOTAL ORDER VALUE ***");
        if (results.isEmpty()) {
            System.out.println("No customer order data available.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                ResultRow r = results.get(i);
                System.out.println((i + 1) + ". " + r.label + " - Total Orders: $" +
                        String.format("%.2f", r.value));
            }
        }
        System.out.println();
    }

    /**
     * Displays bottom 3 customers by total order value
     *
     * @throws SQLException if database query fails
     */
    public static void bottomThreeCustomers() throws SQLException {
        String sql = """
                SELECT c.business_name, COALESCE(SUM(o.total), 0) AS value
                FROM customers c
                LEFT JOIN orders o ON c.id = o.customer_id
                GROUP BY c.id, c.business_name
                """;

        java.util.List<ResultRow> results = getTopOrBottom(sql, "business_name", 3, false);

        System.out.println("\n*** BOTTOM 3 CUSTOMERS BY TOTAL ORDER VALUE ***");
        if (results.isEmpty()) {
            System.out.println("No customer data available.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                ResultRow r = results.get(i);
                System.out.println((i + 1) + ". " + r.label + " - Total Orders: $" +
                        String.format("%.2f", r.value));
            }
        }
        System.out.println();
    }

    /**
     * Custom insertion sort implementation for sorting ResultRow objects
     *
     * @param list      List of ResultRow objects to sort
     * @param ascending true for ascending order, false for descending
     */
    private static void insertionSort(java.util.List<ResultRow> list, boolean ascending) {
        for (int i = 1; i < list.size(); i++) {
            ResultRow key = list.get(i);
            int j = i - 1;

            // Compare based on sort order
            while (j >= 0 && (ascending
                    ? list.get(j).value > key.value
                    : list.get(j).value < key.value)) {
                list.set(j + 1, list.get(j));
                j = j - 1;
            }

            list.set(j + 1, key);
        }
    }

    /**
     * Generates a comprehensive sales summary report
     *
     * @throws SQLException if database query fails
     */
    public static void generateSalesSummary() throws SQLException {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        COMPREHENSIVE SALES ANALYSIS");
        System.out.println("=".repeat(50));

        topThreeItems();
        bottomThreeItems();
        topThreeCustomers();
        bottomThreeCustomers();

        // Additional summary statistics
        displaySummaryStatistics();
    }

    /**
     * Displays overall summary statistics
     */
    private static void displaySummaryStatistics() {
        try (Connection conn = MySQLConnector.getConnection()) {

            // Total orders
            String orderCountSql = "SELECT COUNT(*) as total_orders FROM orders";
            try (PreparedStatement stmt = conn.prepareStatement(orderCountSql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("*** SUMMARY STATISTICS ***");
                    System.out.println("Total Orders: " + rs.getInt("total_orders"));
                }
            }

            // Total revenue
            String revenueSql = "SELECT SUM(total) as total_revenue FROM orders";
            try (PreparedStatement stmt = conn.prepareStatement(revenueSql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double revenue = rs.getDouble("total_revenue");
                    System.out.println("Total Revenue: $" + String.format("%.2f", revenue));
                }
            }

            // Total customers
            String customerCountSql = "SELECT COUNT(*) as total_customers FROM customers";
            try (PreparedStatement stmt = conn.prepareStatement(customerCountSql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Total Customers: " + rs.getInt("total_customers"));
                }
            }

            // Total inventory items
            String inventoryCountSql = "SELECT COUNT(*) as total_items, SUM(quantity) as total_quantity FROM inventory";
            try (PreparedStatement stmt = conn.prepareStatement(inventoryCountSql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Unique Inventory Items: " + rs.getInt("total_items"));
                    System.out.println("Total Inventory Quantity: " + rs.getInt("total_quantity"));
                }
            }

            System.out.println();

        } catch (SQLException e) {
            System.err.println("Error generating summary statistics: " + e.getMessage());
        }
    }
}
