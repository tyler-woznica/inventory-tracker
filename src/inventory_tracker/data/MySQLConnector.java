package inventory_tracker.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLConnector {

    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public static void main(String[] args) {
        MySQLConnector mySQLConnector = new MySQLConnector();


    }


}
