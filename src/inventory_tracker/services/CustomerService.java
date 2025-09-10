package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;
import inventory_tracker.model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CustomerService {

    static int id;
    static String business_name;
    static String email;
    static Long phone;
    static String city;
    static String state;
    static Scanner userScanner = new Scanner(System.in);
    static int check;

    public static void search() {
        System.out.println("*** CUSTOMER SEARCH ***");

        System.out.println("Please enter the customer ID:");
        id = userScanner.nextInt();

        System.out.println("*** SEARCH RESULTS ***");
        lookup(id);
    }

    public static void update() {
        System.out.println("*** CUSTOMER UPDATE ***");
        System.out.println("Please enter the ID of the customer to update:");
        id = userScanner.nextInt();
        lookup(id);
        System.out.println("Which field would you like to update?");
        // insert switch
    }

    // creates a customer based on user input
    public static void create() {
        System.out.println("*** CREATE CUSTOMER ***");
        userScanner.nextLine();

        System.out.println("Enter new customer name:");
        business_name = userScanner.nextLine();

        System.out.println("Enter new customer email:");
        email = userScanner.nextLine();

        System.out.println("Enter new customer phone number (structure example: 000111000)");
        phone = userScanner.nextLong();

        System.out.println("Enter new customer city:");
        userScanner.nextLine();
        city = userScanner.nextLine();


        System.out.println("Enter new customer state (example: CA):");
        state = userScanner.nextLine();

        Customer customer = new Customer(business_name, email, phone, city, state);

        System.out.println("The following customer is about to be created.");
        System.out.println("Business Name: " + customer.getBusiness_name());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Phone: " + customer.getPhone());
        System.out.println("City: " + customer.getCity() + " State: " + customer.getState());

        System.out.println("Enter 1 to create or 2 to cancel:");
        check = userScanner.nextInt();

        if (check == 1) {
            String query = "INSERT INTO customers (business_name, email, phone, city, state) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = MySQLConnector.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, customer.getBusiness_name());
                stmt.setString(2, customer.getEmail());
                stmt.setLong(3, customer.getPhone());
                stmt.setString(4, customer.getCity());
                stmt.setString(5, customer.getState());

                stmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Error inserting record into customers.");
            }
        } else {
            System.out.println("*** RETURNING TO CUSTOMER MENU ***");
        }

    }
    // deletes a customer based on product id with confirmation
    public static void delete() {
        System.out.println("*** DELETE CUSTOEMR ***");
        System.out.println("Please enter the id of the customer to delete:");
        id = userScanner.nextInt();
        System.out.println("The following customer is about to be deleted: ");
        lookup(id);
        System.out.println("Enter 1 to delete or 2 to cancel: ");
        check = userScanner.nextInt();
        if (check == 1) {
            String query = "DELETE FROM customers WHERE id = ?";

            try (Connection conn = MySQLConnector.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                // set input to the query
                stmt.setInt(1, id);

                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("*** CUSTOMER DELETED ***");
                } else {
                    System.out.println("*** NO CUSTOMER FOUND WITH ID: " + id + "***");
                }
            } catch (SQLException e) {
                System.out.println("\n*** Connection to Database Failed ***\n");
            }
            System.out.println();
        } else {
            System.out.println("*** RETURNING TO CUSTOMER MENU ***");
        }
    }

    static void lookup(int id) {

        System.out.println("ID | BUSINESS NAME | EMAIL | PHONE | CITY, STATE");
        String query = "SELECT id, business_name, email, phone, city, state FROM customers WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    System.out.println(
                            rs.getInt("id") + " | " +
                                    rs.getString("business_name") + " | " +
                                    rs.getString("email") + " | " +
                                    rs.getLong("phone") + " | " +
                                    rs.getString("city") + ", " +
                                    rs.getString("state")
                    );
                } else {
                    System.out.println("No customer found with ID: " + id);
                }
            }

        } catch (SQLException e) {
            System.out.println("*** Connection to Database Failed ***");
        }
    }


}
