package inventory_tracker.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {

        private static final String URL = "jdbc:mysql://127.0.0.1:3306/inventory_tracker?useSSL=false";
        private static final String USER = "root";

        // use a system environment variable
        private static final String PASSWORD = System.getenv("DB_PASS");

        static {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("MySQL driver not found", e);
            }

    }

    public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void methodTry() {

    }
}