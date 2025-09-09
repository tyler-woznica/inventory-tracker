package inventory_tracker.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    private static void bubbleSort(List<ResultRow> list, boolean ascending) {
        int n = list.size();
    }

    private static List<ResultRow> getTopOrBottom(String sql, boolean top, int limit) {
        List<ResultRow> results = new ArrayList<>();

        try (Connection conn = MySQLConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String label = rs.getString(1);
                double value = rs.getDouble(2);
                results.add(new ResultRow(label, value));
            }
        } catch (SQLException e) {
            System.out.println("*** DATABASE ERROR ***");
        }

        results.sort((a, b) -> Double.compare(a.value, b.value));
        if (top) {
            Collections.reverse(results);
        }

        return results.stream().limit(limit).collect(Collectors.toList());
    }

    public static void topThreeItems() {

    }

    public static void bottomThreeItems() {

    }

    public static void topThreeCustomers() {

    }

    public static void bottomThreeCustomers() {

    }


}
