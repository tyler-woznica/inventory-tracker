package inventory_tracker.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SalesAnalysis {

    // holds data
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

    private static List<ResultRow> getTopOrBottom(
            String sql, String label, int n, boolean top) {

        List<ResultRow> results = new ArrayList<>();

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(new ResultRow(rs.getString(label), rs.getDouble("value")));
            }
        } catch (SQLException e) {
            System.out.println("*** ERROR CONNECTING TO DATABASE ***");
        }

        // Use insertion sort instead of Collections.sort
        insertionSort(results, !top); // if top=true → descending, else ascending

        // Trim to N results
        return results.subList(0, Math.min(n, results.size()));
    }

    public static void topThreeItems() throws SQLException {
        String sql = """
        SELECT i.name, SUM(oi.quantity) AS value
        FROM inventory i
        JOIN order_items oi ON i.id = oi.inventory_id
        GROUP BY i.id, i.name
        """;

        List<ResultRow> results = getTopOrBottom(sql, "name", 3, true);

        System.out.println("*** TOP 3 ITEMS SOLD ***");
        for (ResultRow r : results) {
            System.out.println(r.label + " - Total Sold: " + (int) r.value);
        }
    }

    public static void bottomThreeItems() throws SQLException {
        String sql = """
        SELECT i.name, SUM(oi.quantity) AS value
        FROM inventory i
        JOIN order_items oi ON i.id = oi.inventory_id
        GROUP BY i.id, i.name
        """;

        List<ResultRow> results = getTopOrBottom(sql, "name", 3, false);

        System.out.println("*** BOTTOM 3 ITEMS SOLD ***");
        for (ResultRow r : results) {
            System.out.println(r.label + " - Total Sold: " + (int) r.value);
        }
    }

    public static void topThreeCustomers() throws SQLException {
        String sql = """
        SELECT c.business_name, SUM(o.total) AS value
        FROM customers c
        JOIN orders o ON c.id = o.customer_id
        GROUP BY c.id, c.business_name
        """;

        List<ResultRow> results = getTopOrBottom(sql, "business_name", 3, true);

        System.out.println("*** TOP 3 CUSTOMERS BY TOTAL ORDERS ***");
        for (ResultRow r : results) {
            System.out.println(r.label + " - Total Orders: $" + r.value);
        }
    }

    public static void bottomThreeCustomers() throws SQLException {
        String sql = """
        SELECT c.business_name, SUM(o.total) AS value
        FROM customers c
        JOIN orders o ON c.id = o.customer_id
        GROUP BY c.id, c.business_name
    """;

        List<ResultRow> results = getTopOrBottom(sql, "business_name", 3, false);

        System.out.println("*** BOTTOM 3 CUSTOMERS BY TOTAL ORDERS ***");
        for (ResultRow r : results) {
            System.out.println(r.label + " - Total Orders: $" + r.value);
        }
    }

    private static void insertionSort(List<ResultRow> list, boolean ascending) {
        for (int i = 1; i < list.size(); i++) {
            ResultRow key = list.get(i);
            int j = i - 1;

            while (j >= 0 && (ascending
                    ? list.get(j).value > key.value
                    : list.get(j).value < key.value)) {
                list.set(j + 1, list.get(j));
                j = j - 1;
            }

            list.set(j + 1, key);
        }
    }
}
