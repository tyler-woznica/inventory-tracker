package inventory_tracker.services;


import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Service class for handling input validation across the application.
 * Provides utility methods for validating different types of user input.
 */
public class ValidationService {

    /**
     * Validates email format using a basic regex pattern
     * @param email Email address to validate
     * @return true if email format is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Basic email validation regex
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    /**
     * Validates that a price is positive and within reasonable bounds
     * @param price Price value to validate
     * @return true if price is valid, false otherwise
     */
    public static boolean isValidPrice(double price) {
        return price > 0 && price <= 999999.99; // Reasonable upper bound
    }

    /**
     * Validates that quantity is non-negative
     * @param quantity Quantity value to validate
     * @return true if quantity is valid, false otherwise
     */
    public static boolean isValidQuantity(int quantity) {
        return quantity >= 0;
    }

    /**
     * Validates phone number format (must be 10 digits)
     * @param phone Phone number to validate
     * @return true if phone format is valid, false otherwise
     */
    public static boolean isValidPhone(Long phone) {
        if (phone == null) {
            return false;
        }
        String phoneStr = phone.toString();
        return phoneStr.length() == 10 && phoneStr.matches("\\d{10}");
    }

    /**
     * Validates that a string is not null or empty after trimming
     * @param value String value to validate
     * @return true if string is valid, false otherwise
     */
    public static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates that a state abbreviation is exactly 2 characters
     * @param state State abbreviation to validate
     * @return true if state format is valid, false otherwise
     */
    public static boolean isValidState(String state) {
        return isValidString(state) && state.trim().length() == 2
                && state.matches("[A-Za-z]{2}");
    }

    /**
     * Validates file path exists and is readable
     * @param filePath Path to validate
     * @return true if file exists and is readable, false otherwise
     */
    public static boolean isValidFilePath(String filePath) {
        if (!isValidString(filePath)) {
            return false;
        }
        try {
            return Files.exists(Paths.get(filePath)) && Files.isReadable(Paths.get(filePath));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates that a directory path exists and is writable
     * @param dirPath Directory path to validate
     * @return true if directory exists and is writable, false otherwise
     */
    public static boolean isValidOutputDirectory(String dirPath) {
        if (!isValidString(dirPath)) {
            return false;
        }
        try {
            return Files.exists(Paths.get(dirPath).getParent())
                    && Files.isWritable(Paths.get(dirPath).getParent());
        } catch (Exception e) {
            return false;
        }
    }
}