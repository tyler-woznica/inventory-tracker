package inventory_tracker.services;

import inventory_tracker.data.MySQLConnector;
import inventory_tracker.model.Customer;
import java.sql.*;
import java.util.Scanner;

/**
 * Service class for handling all customer-related database operations.
 * Provides CRUD (Create, Read, Update, Delete) functionality for customer management.
 */
public class CustomerService {

    /**
     * Searches for a customer by ID and displays the result
     * @param scanner Scanner instance for user input
     */
    public static void search(Scanner scanner) {
        System.out.println("*** CUSTOMER SEARCH ***");

        int id = ExceptionService.getValidatedIntInput(scanner,
                "Please enter the customer ID:", 1, Integer.MAX_VALUE);

        System.out.println("*** SEARCH RESULTS ***");
        lookup(id);
    }

    /**
     * Updates an existing customer's information with complete field selection
     * @param scanner Scanner instance for user input
     */
    public static void update(Scanner scanner) {
        System.out.println("*** CUSTOMER UPDATE ***");

        int id = ExceptionService.getValidatedIntInput(scanner,
                "Please enter the ID of the customer to update:", 1, Integer.MAX_VALUE);

        // Display current customer information
        if (!lookup(id)) {
            System.out.println("Customer not found. Update cancelled.");
            return;
        }

        if (!ExceptionService.getUserConfirmation(scanner, "Is this the correct customer to update?")) {
            System.out.println("*** UPDATE CANCELLED ***");
            return;
        }

        // Field selection menu
        System.out.println("Which field would you like to update?");
        System.out.println("""
            1. Business Name
            2. Email
            3. Phone
            4. City
            5. State
            """);

        int choice = ExceptionService.getValidatedIntInput(scanner,
                "Select field to update:", 1, 5);

        scanner.nextLine(); // Clear buffer after nextInt()

        String sql = "";
        Object newValue = null;
        boolean validInput = false;

        switch (choice) {
            case 1:
                // Update business name
                System.out.print("Enter new business name: ");
                String businessName = scanner.nextLine().trim();
                if (ValidationService.isValidString(businessName)) {
                    sql = "UPDATE customers SET business_name = ? WHERE id = ?";
                    newValue = businessName;
                    validInput = true;
                } else {
                    ExceptionService.displayValidationError("Business Name", "Non-empty text");
                }
                break;

            case 2:
                // Update email with validation
                System.out.print("Enter new email address: ");
                String email = scanner.nextLine().trim();
                if (ValidationService.isValidEmail(email)) {
                    sql = "UPDATE customers SET email = ? WHERE id = ?";
                    newValue = email;
                    validInput = true;
                } else {
                    ExceptionService.displayValidationError("Email", "user@domain.com");
                }
                break;

            case 3:
                // Update phone with validation
                System.out.print("Enter new phone number (10 digits, no spaces): ");
                try {
                    Long phone = Long.parseLong(scanner.nextLine().trim());
                    if (ValidationService.isValidPhone(phone)) {
                        sql = "UPDATE customers SET phone = ? WHERE id = ?";
                        newValue = phone;
                        validInput = true;
                    } else {
                        ExceptionService.displayValidationError("Phone", "10 digits (e.g., 5551234567)");
                    }
                } catch (NumberFormatException e) {
                    ExceptionService.displayValidationError("Phone", "Numbers only, 10 digits");
                }
                break;

            case 4:
                // Update city
                System.out.print("Enter new city: ");
                String city = scanner.nextLine().trim();
                if (ValidationService.isValidString(city)) {
                    sql = "UPDATE customers SET city = ? WHERE id = ?";
                    newValue = city;
                    validInput = true;
                } else {
                    ExceptionService.displayValidationError("City", "Non-empty text");
                }
                break;

            case 5:
                // Update state with validation
                System.out.print("Enter new state (2 letter abbreviation, e.g., CA): ");
                String state = scanner.nextLine().trim().toUpperCase();
                if (ValidationService.isValidState(state)) {
                    sql = "UPDATE customers SET state = ? WHERE id = ?";
                    newValue = state;
                    validInput = true;
                } else {
                    ExceptionService.displayValidationError("State", "2 letter abbreviation (e.g., CA)");
                }
                break;
        }

        // Execute update if input is valid
        if (validInput) {
            executeUpdate(sql, newValue, id);
        } else {
            System.out.println("*** UPDATE CANCELLED DUE TO INVALID INPUT ***");
        }
    }

    /**
     * Creates a new customer with validation for all fields
     * @param scanner Scanner instance for user input
     */
    public static void create(Scanner scanner) {
        System.out.println("*** CREATE CUSTOMER ***");
        scanner.nextLine(); // Clear buffer

        String businessName, email, city, state;
        Long phone;

        // Get and validate business name
        do {
            System.out.print("Enter new customer name: ");
            businessName = scanner.nextLine().trim();
            if (!ValidationService.isValidString(businessName)) {
                ExceptionService.displayValidationError("Business Name", "Non-empty text");
            }
        } while (!ValidationService.isValidString(businessName));

        // Get and validate email
        do {
            System.out.print("Enter new customer email: ");
            email = scanner.nextLine().trim();
            if (!ValidationService.isValidEmail(email)) {
                ExceptionService.displayValidationError("Email", "user@domain.com");
            }
        } while (!ValidationService.isValidEmail(email));

        // Get and validate phone
        do {
            System.out.print("Enter new customer phone number (10 digits, no spaces): ");
            try {
                phone = Long.parseLong(scanner.nextLine().trim());
                if (!ValidationService.isValidPhone(phone)) {
                    ExceptionService.displayValidationError("Phone", "10 digits (e.g., 5551234567)");
                    phone = null;
                }
            } catch (NumberFormatException e) {
                ExceptionService.displayValidationError("Phone", "Numbers only, 10 digits");
                phone = null;
            }
        } while (phone == null);

        // Get and validate city
        do {
            System.out.print("Enter new customer city: ");
            city = scanner.nextLine().trim();
            if (!ValidationService.isValidString(city)) {
                ExceptionService.displayValidationError("City", "Non-empty text");
            }
        } while (!ValidationService.isValidString(city));

        // Get and validate state
        do {
            System.out.print("Enter new customer state (2 letter abbreviation, e.g., CA): ");
            state = scanner.nextLine().trim().toUpperCase();
            if (!ValidationService.isValidState(state)) {
                ExceptionService.displayValidationError("State", "2 letter abbreviation (e.g., CA)");
            }
        } while (!ValidationService.isValidState(state));

        // Create customer object
        Customer customer = new Customer(businessName, email, phone, city, state);

        // Display confirmation
        System.out.println("\nThe following customer is about to be created:");
        System.out.println("Business Name: " + customer.getBusinessName());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Phone: " + customer.getPhone());
        System.out.println("City: " + customer.getCity() + ", State: " + customer.getState());

        if (ExceptionService.getUserConfirmation(scanner, "Confirm customer creation?")) {
            executeCreate(customer);
        } else {
            System.out.println("*** CUSTOMER CREATION CANCELLED ***");
        }
    }

    /**
     * Deletes a customer by ID with confirmation
     * @param scanner Scanner instance for user input
     */
    public static void delete(Scanner scanner) {
        System.out.println("*** DELETE CUSTOMER ***");

        int id = ExceptionService.getValidatedIntInput(scanner,
                "Please enter the ID of the customer to delete:", 1, Integer.MAX_VALUE);

        System.out.println("The following customer is about to be deleted:");
        if (!lookup(id)) {
            System.out.println("Customer not found. Delete cancelled.");
            return;
        }

        if (ExceptionService.getUserConfirmation(scanner, "Are you sure you want to delete this customer?")) {
            executeDelete(id);
        } else {
            System.out.println("*** DELETE CANCELLED ***");
        }
    }

    /**
     * Helper method to look up and display a customer by ID
     * @param id Customer ID to look up
     * @return true if customer was found, false otherwise
     */
    static boolean lookup(int id) {
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
                    return true;
                } else {
                    System.out.println("No customer found with ID: " + id);
                    return false;
                }
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "customer lookup");
            return false;
        }
    }

    /**
     * Executes a customer update query
     * @param sql Update SQL statement
     * @param newValue New value for the field being updated
     * @param customerId ID of customer to update
     */
    private static void executeUpdate(String sql, Object newValue, int customerId) {
        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, newValue);
            stmt.setInt(2, customerId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("*** CUSTOMER UPDATED SUCCESSFULLY ***");
            } else {
                System.out.println("No customer found with ID " + customerId);
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "customer update");
        }
    }

    /**
     * Executes customer creation in database
     * @param customer Customer object to insert
     */
    private static void executeCreate(Customer customer) {
        String query = "INSERT INTO customers (business_name, email, phone, city, state) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, customer.getBusinessName());
            stmt.setString(2, customer.getEmail());
            stmt.setLong(3, customer.getPhone());
            stmt.setString(4, customer.getCity());
            stmt.setString(5, customer.getState());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Get the generated ID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);
                        System.out.println("*** CUSTOMER CREATED SUCCESSFULLY ***");
                        System.out.println("New customer ID: " + newId);
                    }
                }
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "customer creation");
        }
    }

    /**
     * Executes customer deletion from database
     * @param customerId ID of customer to delete
     */
    private static void executeDelete(int customerId) {
        String query = "DELETE FROM customers WHERE id = ?";

        try (Connection conn = MySQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, customerId);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("*** CUSTOMER DELETED SUCCESSFULLY ***");
            } else {
                System.out.println("*** NO CUSTOMER FOUND WITH ID: " + customerId + " ***");
            }

        } catch (SQLException e) {
            ExceptionService.handleDatabaseError(e, "customer deletion");
        }
    }
}