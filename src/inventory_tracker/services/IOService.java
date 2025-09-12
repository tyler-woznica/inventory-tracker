package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Service class for handling import/export operations for inventory and customer data.
 * Supports CSV file format with proper validation and error handling.
 */
public class IOService {

    /**
     * Imports inventory data from a CSV file with validation and batch processing
     * @param scanner Scanner instance for user input
     */
    public static void inventoryImport(Scanner scanner) {
        System.out.println("*** INVENTORY IMPORT ***");
        System.out.println("REMINDER: CSV must contain header row with columns: name,quantity,price");
        System.out.println("Example format:");
        System.out.println("name,quantity,price");
        System.out.println("Widget A,100,15.99");
        System.out.println("Widget B,50,29.99");

        scanner.nextLine(); // Clear buffer

        String filePath;
        do {
            System.out.print("Enter path of CSV file to import: ");
            filePath = scanner.nextLine().trim();

            if (!ValidationService.isValidFilePath(filePath)) {
                System.out.println("File not found or not readable. Please check the path and try again.");

                if (!ExceptionService.getUserConfirmation(scanner, "Try a different file path?")) {
                    System.out.println("*** IMPORT CANCELLED ***");
                    return;
                }
            }
        } while (!ValidationService.isValidFilePath(filePath));

        importInventoryFromCSV(filePath);
    }

    /**
     * Exports inventory data to a CSV file
     * @param scanner Scanner instance for user input
     */
    public static void inventoryExport(Scanner scanner) {
        System.out.println("*** INVENTORY EXPORT ***");
        scanner.nextLine(); // Clear buffer

        String filePath;
        do {
            System.out.print("Enter path to export CSV file (e.g., /path/to/inventory_export.csv): ");
            filePath = scanner.nextLine().trim();

            if (!ValidationService.isValidOutputDirectory(filePath)) {
                System.out.println("Invalid output path or directory not writable.");

                if (!ExceptionService.getUserConfirmation(scanner, "Try a different file path?")) {
                    System.out.println("*** EXPORT CANCELLED ***");
                    return;
                }
            }
        } while (!ValidationService.isValidOutputDirectory(filePath));

        exportInventoryToCSV(filePath);
    }

    /**
     * Imports customer data from a CSV file with validation and batch processing
     * @param scanner Scanner instance for user input
     */
    public static void customerImport(Scanner scanner) {
        System.out.println("*** CUSTOMER IMPORT ***");
        System.out.println("REMINDER: CSV must contain header row with columns: business_name,email,phone,city,state");
        System.out.println("Example format:");
        System.out.println("business_name,email,phone,city,state");
        System.out.println("ABC Corp,contact@abc.com,5551234567,Los Angeles,CA");
        System.out.println("XYZ Inc,info@xyz.com,5559876543,New York,NY");

        scanner.nextLine(); // Clear buffer

        String filePath;
        do {
            System.out.print("Enter path of CSV file to import: ");
            filePath = scanner.nextLine().trim();

            if (!ValidationService.isValidFilePath(filePath)) {
                System.out.println("File not found or not readable. Please check the path and try again.");

                if (!ExceptionService.getUserConfirmation(scanner, "Try a different file path?")) {
                    System.out.println("*** IMPORT CANCELLED ***");
                    return;
                }
            }
        } while (!ValidationService.isValidFilePath(filePath));

        importCustomersFromCSV(filePath);
    }

    /**
     * Exports customer data to a CSV file
     * @param scanner Scanner instance for user input
     */
    public static void customerExport(Scanner scanner) {
        System.out.println("*** CUSTOMER EXPORT ***");
        scanner.nextLine(); // Clear buffer

        String filePath;
        do {
            System.out.print("Enter path to export CSV file (e.g., /path/to/customer_export.csv): ");
            filePath = scanner.nextLine().trim();

            if (!ValidationService.isValidOutputDirectory(filePath)) {
                System.out.println("Invalid output path or directory not writable.");

                if (!ExceptionService.getUserConfirmation(scanner, "Try a different file path?")) {
                    System.out.println("*** EXPORT CANCELLED ***");
                    return;
                }
            }
        } while (!ValidationService.isValidOutputDirectory(filePath));

        exportCustomersToCSV(filePath);
    }

    /**
     * Imports inventory data from CSV with proper SQL and batch processing
     * @param filePath Path to the CSV file to import
     */
    private static void importInventoryFromCSV(String filePath) {
        String sql = "INSERT INTO inventory (name, quantity, price, alert) VALUES (?, ?, ?, ?)";

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        try (Connection conn = MySQLConnector.getConnection();
             Stream<String> lines = Files.lines(Paths.get(filePath));
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Disable auto-commit for batch processing
            conn.setAutoCommit(false);

            System.out.println("Processing CSV file...");

            lines.skip(1) // Skip header row
                    .forEach(line -> {
                        try {
                            String[] values = parseCsvLine(line);

                            if (values.length >= 3) {
                                String name = values[0].trim();
                                int quantity = Integer.parseInt(values[1].trim());
                                double price = Double.parseDouble(values[2].trim());

                                // Validate data
                                if (ValidationService.isValidString(name) &&
                                        ValidationService.isValidQuantity(quantity) &&
                                        ValidationService.isValidPrice(price)) {

                                    stmt.setString(1, name);
                                    stmt.setInt(2, quantity);
                                    stmt.setDouble(3, price);
                                    stmt.setInt(4, 0); // Default alert to 0
                                    stmt.addBatch();

                                    successCount.incrementAndGet();
                                } else {
                                    System.err.println("Invalid data in line: " + line);
                                    errorCount.incrementAndGet();
                                }
                            } else {
                                System.err.println("Insufficient columns in line: " + line);
                                errorCount.incrementAndGet();
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number format in line: " + line);
                            errorCount.incrementAndGet();
                        } catch (Exception e) {
                            System.err.println("Error processing line: " + line + " - " + e.getMessage());
                            errorCount.incrementAndGet();
                        }
                    });

            if (successCount.get() > 0) {
                int[] results = stmt.executeBatch();
                conn.commit();

                System.out.println("*** INVENTORY CSV IMPORTED SUCCESSFULLY ***");
                System.out.println("Records imported: " + successCount.get());
                if (errorCount.get() > 0) {
                    System.out.println("Records with errors: " + errorCount.get());
                }
            } else {
                System.out.println("No valid records found to import.");
            }

        } catch (SQLException | IOException e) {
            ExceptionService.handleFileError(e, "inventory import");
        }
    }

    /**
     * Exports inventory data to CSV file
     * @param filePath Path where the CSV file will be created
     */
    private static void exportInventoryToCSV(String filePath) {
        String query = "SELECT id, name, quantity, price, alert FROM inventory ORDER BY id";

        try (Connection conn = MySQLConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {

            // Write CSV header
            writer.write("id,name,quantity,price,alert\n");

            int recordCount = 0;
            while (rs.next()) {
                writer.write(
                        rs.getInt("id") + "," +
                                escapeCsvValue(rs.getString("name")) + "," +
                                rs.getInt("quantity") + "," +
                                rs.getDouble("price") + "," +
                                rs.getInt("alert") + "\n"
                );
                recordCount++;
            }

            System.out.println("*** INVENTORY CSV EXPORTED SUCCESSFULLY ***");
            System.out.println("Records exported: " + recordCount);
            System.out.println("File saved to: " + filePath);

        } catch (SQLException | IOException e) {
            ExceptionService.handleFileError(e, "inventory export");
        }
    }

    /**
     * FIXED: Imports customer data from CSV with proper SQL and batch processing
     * @param filePath Path to the CSV file to import
     */
    private static void importCustomersFromCSV(String filePath) {
        String sql = "INSERT INTO customers (business_name, email, phone, city, state) VALUES (?, ?, ?, ?, ?)";

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        try (Connection conn = MySQLConnector.getConnection();
             Stream<String> lines = Files.lines(Paths.get(filePath));
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Disable auto-commit for batch processing
            conn.setAutoCommit(false);

            System.out.println("Processing CSV file...");

            lines.skip(1) // Skip header row
                    .forEach(line -> {
                        try {
                            String[] values = parseCsvLine(line);

                            if (values.length >= 5) {
                                String businessName = values[0].trim();
                                String email = values[1].trim();
                                String phoneStr = values[2].trim();
                                String city = values[3].trim();
                                String state = values[4].trim().toUpperCase();

                                // Parse and validate phone number
                                Long phone = Long.parseLong(phoneStr);

                                // Validate all data
                                if (ValidationService.isValidString(businessName) &&
                                        ValidationService.isValidEmail(email) &&
                                        ValidationService.isValidPhone(phone) &&
                                        ValidationService.isValidString(city) &&
                                        ValidationService.isValidState(state)) {

                                    stmt.setString(1, businessName);
                                    stmt.setString(2, email);
                                    stmt.setLong(3, phone);
                                    stmt.setString(4, city);
                                    stmt.setString(5, state);
                                    stmt.addBatch();

                                    successCount.incrementAndGet();
                                } else {
                                    System.err.println("Invalid data in line: " + line);
                                    errorCount.incrementAndGet();
                                }
                            } else {
                                System.err.println("Insufficient columns in line: " + line);
                                errorCount.incrementAndGet();
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid phone number format in line: " + line);
                            errorCount.incrementAndGet();
                        } catch (Exception e) {
                            System.err.println("Error processing line: " + line + " - " + e.getMessage());
                            errorCount.incrementAndGet();
                        }
                    });

            if (successCount.get() > 0) {
                int[] results = stmt.executeBatch();
                conn.commit();

                System.out.println("*** CUSTOMER CSV IMPORTED SUCCESSFULLY ***");
                System.out.println("Records imported: " + successCount.get());
                if (errorCount.get() > 0) {
                    System.out.println("Records with errors: " + errorCount.get());
                }
            } else {
                System.out.println("No valid records found to import.");
            }

        } catch (SQLException | IOException e) {
            ExceptionService.handleFileError(e, "customer import");
        }
    }

    /**
     * Exports customer data to CSV file
     * @param filePath Path where the CSV file will be created
     */
    private static void exportCustomersToCSV(String filePath) {
        String query = "SELECT id, business_name, email, phone, city, state FROM customers ORDER BY id";

        try (Connection conn = MySQLConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {

            // Write CSV header
            writer.write("id,business_name,email,phone,city,state\n");

            int recordCount = 0;
            while (rs.next()) {
                writer.write(
                        rs.getInt("id") + "," +
                                escapeCsvValue(rs.getString("business_name")) + "," +
                                escapeCsvValue(rs.getString("email")) + "," +
                                rs.getLong("phone") + "," +
                                escapeCsvValue(rs.getString("city")) + "," +
                                rs.getString("state") + "\n"
                );
                recordCount++;
            }

            System.out.println("*** CUSTOMER CSV EXPORTED SUCCESSFULLY ***");
            System.out.println("Records exported: " + recordCount);
            System.out.println("File saved to: " + filePath);

        } catch (SQLException | IOException e) {
            ExceptionService.handleFileError(e, "customer export");
        }
    }

    /**
     * Parses a CSV line handling quoted values and escaped commas
     * @param line CSV line to parse
     * @return Array of values
     */
    private static String[] parseCsvLine(String line) {
        // Simple CSV parsing - handles basic quoted values
        if (line.contains("\"")) {
            // More complex parsing for quoted values
            return parseQuotedCsvLine(line);
        } else {
            // Simple comma split for non-quoted lines
            return line.split(",");
        }
    }

    /**
     * Parses CSV lines that contain quoted values
     * @param line CSV line with potential quoted values
     * @return Array of parsed values
     */
    private static String[] parseQuotedCsvLine(String line) {
        java.util.List<String> result = new java.util.ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(currentValue.toString());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }

        result.add(currentValue.toString());
        return result.toArray(new String[0]);
    }

    /**
     * Escapes CSV values that contain commas or quotes
     * @param value Value to escape
     * @return Escaped CSV value
     */
    private static String escapeCsvValue(String value) {
        if (value == null) return "";

        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            // Escape quotes by doubling them and wrap in quotes
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }
}