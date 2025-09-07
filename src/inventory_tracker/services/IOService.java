package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class IOService {
    public static Scanner userScanner = new Scanner(System.in);
    public static String filePath;

    public static void inventoryImport() {
        System.out.println("*** INVENTORY IMPORT ***");
        System.out.println("REMINDER: CSV must contain header.");
        System.out.println("Enter path of CSV file to import:");
        filePath = userScanner.nextLine();

        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());

        try (Connection conn = MySQLConnector.getConnection();
             Stream<String> lines = Files.lines(Paths.get(filePath))) {
                 conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement(
                    "INSTERT INTO inventory (name, quantity, price) VALUES (?, ?, ?)");
            lines.skip(1).forEach(line -> executor.submit(() -> {
                try {
                    String[] values = line.split(",");
                    stmt.setString(1, values[0].trim());
                    stmt.setInt(2, Integer.parseInt(values[1].trim()));
                    stmt.setDouble(3, Double.parseDouble(values[2].trim()));
                    synchronized (stmt) {
                        stmt.addBatch();
                    }
                } catch (SQLException e) {
                    System.out.println("*** Error inserting record into inventory ***");
                }
            }));

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);

            System.out.println("*** Inventory CSV IMPORTED SUCCESSFULLY ***");

        } catch (SQLException | InterruptedException | IOException e) {
            System.out.println("\n*** Connection to Database Failed ***\n");
        }
    }

    public static void inventoryExport() {
        System.out.println("Enter path to export CSV file:");
        filePath = userScanner.nextLine();

        try (Connection conn = MySQLConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM inventory");
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {

            writer.write("id,name,quantity,price\n");

            while (rs.next()) {
                writer.write(rs.getInt("id") + "," +
                        rs.getString("name") + "," +
                        rs.getInt("quantity") + "," +
                        rs.getDouble("price") + "\n");
            }

            System.out.println("*** INVENTORY CSV EXPORTED SUCCESSFULLY ***");

        } catch (SQLException | IOException e) {
            System.out.println("\n*** Connection to Database Failed ***\n");
        }
    }

    public static void customerImport() {
        System.out.println("*** CUSTOMER IMPORT ***");
        System.out.println("REMINDER: CSV must contain header.");
        System.out.println("Enter path of CSV file to import:");
        filePath = userScanner.nextLine();

        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());

        try (Connection conn = MySQLConnector.getConnection();
             Stream<String> lines = Files.lines(Paths.get(filePath))) {
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement(
                    "INSTERT INTO customers (business_name,email,phone,city,state) " +
                            "VALUES (?, ?, ?, ?, ?)");
            lines.skip(1).forEach(line -> executor.submit(() -> {
                try {
                    String[] values = line.split(",");
                    stmt.setString(1, values[0].trim());
                    stmt.setString(2, values[1].trim());
                    stmt.setLong(3, Long.parseLong(values[2].trim()));
                    stmt.setString(4, values[3].trim());
                    stmt.setString(5, values[4].trim());
                    synchronized (stmt) {
                        stmt.addBatch();
                    }
                } catch (SQLException e) {
                    System.out.println("*** Error inserting record into customers table ***");
                }
            }));

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);

            System.out.println("*** CUSTOMER CSV IMPORTED SUCCESSFULLY ***");

        } catch (SQLException | InterruptedException | IOException e) {
            System.out.println("\n*** Connection to Database Failed ***\n");
        }
    }

    public static void customerExport() {
        System.out.println("Enter path to export CSV file:");
        filePath = userScanner.nextLine();

        try (Connection conn = MySQLConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers");
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {

            writer.write("id,business_name,email,phone,city,state\n");

            while (rs.next()) {
                writer.write(rs.getInt("id") + "," +
                        rs.getString("business_name") + "," +
                        rs.getString("email") + "," +
                        rs.getLong("phone") + "," +
                        rs.getString("city") + "," +
                        rs.getString("state") + "\n");
            }

            System.out.println("*** CUSTOMER CSV EXPORTED SUCCESSFULLY ***");

        } catch (SQLException | IOException e) {
            System.out.println("\n*** Connection to Database Failed ***\n");
        }
    }
}
