package inventory_tracker.data;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * Database connection utility class for MySQL database connectivity.
 */
public class MySQLConnector {

    private static String URL = "jdbc:mysql://127.0.0.1:3306/inventory_tracker?useSSL=false&allowPublicKeyRetrieval=true";
    private static String USER = "root";
    private static String PASSWORD;
    private static final String ADDITIONAL_PARAMS = "&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";

    // Ensure driver is loaded and password is configured
    static {
        try {
            // MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            PASSWORD = System.getenv("DB_PASS");

            if (PASSWORD == null || PASSWORD.isEmpty()) {
                System.err.println("WARNING: DB_PASS environment variable not set.");
                System.err.println("Please set DB_PASS environment variable with your database password.");
                throw new RuntimeException("Database password not configured");
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found. Please add mysql-connector-java to your classpath.", e);
        }
    }

    /**
     * Gets a connection to the MySQL database
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Add additional parameters to URL if not already present
            String fullUrl = URL;
            if (!URL.contains("serverTimezone")) {
                fullUrl += ADDITIONAL_PARAMS;
            }

            Connection conn = DriverManager.getConnection(fullUrl, USER, PASSWORD);

            // Set connection properties for better performance
            conn.setAutoCommit(true);

            return conn;

        } catch (SQLException e) {
            System.err.println("Failed to connect to database:");
            System.err.println("URL: " + URL);
            System.err.println("User: " + USER);
            System.err.println("Error: " + e.getMessage());
            throw new SQLException("Database connection failed: " + e.getMessage(), e);
        }
    }

    /**
     * Tests the database connection
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets current database URL
     * @return Database URL string
     */
    public static String getDatabaseUrl() {
        return URL;
    }

    /**
     * Gets current database user
     * @return Database username
     */
    public static String getDatabaseUser() {
        return USER;
    }
}

