package inventory_tracker.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {

        private static final String URL = "jdbc:mysql://localhost:3306/inventory_tracker";
        private static final String USER = "root";
        private static final String PASSWORD = "099122T$ugu@";

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



}
