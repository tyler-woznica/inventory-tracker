package inventory_tracker.services;

import java.util.Scanner;

/**
 * Service class for handling various types of exceptions and input errors.
 * Centralizes error handling logic for consistent user experience.
 */
public class ExceptionService {

    /**
     * Handles InputMismatchException by clearing the scanner buffer and providing user feedback
     * @param scanner Scanner instance to clear
     * @return 0 to reset menu selection
     */
    public static int handleInputMismatch(Scanner scanner) {
        System.out.println("*** INVALID INPUT: Please enter a number only ***\n");
        scanner.nextLine(); // Clear the invalid input from scanner buffer
        return 0;
    }

    /**
     * Handles and displays database connection errors with a message
     * @param e SQLException that occurred
     * @param operation Description of the operation that failed
     */
    public static void handleDatabaseError(Exception e, String operation) {
        System.err.println("*** DATABASE ERROR during " + operation + " ***");
        System.err.println("Error: " + e.getMessage());
    }

    /**
     * Handles file I/O errors with helpful messages
     * @param e IOException that occurred
     * @param operation Description of the file operation that failed
     */
    public static void handleFileError(Exception e, String operation) {
        System.err.println("*** FILE ERROR during " + operation + " ***");
        System.err.println("Error: " + e.getMessage());
    }

    /**
     * Displays validation error message to user
     * @param fieldName Name of the field that failed validation
     * @param expectedFormat Expected format or requirements
     */
    public static void displayValidationError(String fieldName, String expectedFormat) {
        System.out.println("*** VALIDATION ERROR: " + fieldName + " ***");
        System.out.println("Expected format: " + expectedFormat);
        System.out.println("Please try again.\n");
    }

    /**
     * Gets user confirmation for an action
     * @param scanner Scanner for user input
     * @param promptMessage Message to display to user
     * @return true if user confirms by entering 1), false otherwise
     */
    public static boolean getUserConfirmation(Scanner scanner, String promptMessage) {
        System.out.println(promptMessage);
        System.out.println("Enter 1 to confirm or 2 to cancel:");

        try {
            int choice = scanner.nextInt();
            return choice == 1;
        } catch (Exception e) {
            scanner.nextLine(); // Clear buffer
            return false;
        }
    }

    /**
     * Safely gets integer input from user with validation
     * @param scanner Scanner for user input
     * @param prompt Message to display to user
     * @param min Minimum allowed value
     * @param max Maximum allowed value
     * @return Valid integer within specified range
     */
    public static int getValidatedIntInput(Scanner scanner, String prompt, int min, int max) {
        int value;
        boolean valid = false;

        do {
            System.out.println(prompt);
            try {
                value = scanner.nextInt();
                if (value >= min && value <= max) {
                    valid = true;
                } else {
                    System.out.println("*** Please enter a value between " + min + " and " + max + " ***");
                }
            } catch (Exception e) {
                System.out.println("*** Please enter a valid number ***");
                scanner.nextLine(); // Clear invalid input
                value = min - 1; // Set to invalid value to continue loop
            }
        } while (!valid);

        return value;
    }

    /**
     * Safely gets double input from user with validation
     * @param scanner Scanner for user input
     * @param prompt Message to display to user
     * @param min Minimum allowed value (inclusive)
     * @param max Maximum allowed value (inclusive)
     * @return Valid double within specified range
     */
    public static double getValidatedDoubleInput(Scanner scanner, String prompt, double min, double max) {
        double value;
        boolean valid = false;

        do {
            System.out.println(prompt);
            try {
                value = scanner.nextDouble();
                if (value >= min && value <= max) {
                    valid = true;
                } else {
                    System.out.println("*** Please enter a value between " + min + " and " + max + " ***");
                }
            } catch (Exception e) {
                System.out.println("*** Please enter a valid number ***");
                scanner.nextLine(); // Clear invalid input
                value = min - 1; // Set to invalid value to continue loop
            }
        } while (!valid);

        return value;
    }
}