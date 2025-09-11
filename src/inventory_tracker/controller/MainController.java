package inventory_tracker.controller;

import inventory_tracker.data.SalesAnalysis;
import inventory_tracker.services.*;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main controller class that handles the primary menu system for the Inventory Tracker application.
 * Provides a console-based interface for accessing inventory, order, customer, analysis, and I/O operations.
 */
public class MainController {

    // Single scanner instance to prevent resource conflicts
    private static final Scanner USER_SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        int mainMenuSelection = 0;

        System.out.println("\n*** INVENTORY TRACKER v1.0 ***\n");

        // Display startup inventory alerts
        displayStartupAlerts();

        // Main menu loop - continues until user selects quit (option 6)
        while (mainMenuSelection != 6) {
            try {
                displayMainMenu();
                mainMenuSelection = USER_SCANNER.nextInt();

                switch (mainMenuSelection) {
                    case 1:
                        handleInventoryMenu();
                        break;
                    case 2:
                        handleOrderMenu();
                        break;
                    case 3:
                        handleCustomerMenu();
                        break;
                    case 4:
                        handleAnalysisMenu();
                        break;
                    case 5:
                        handleImportExportMenu();
                        break;
                    case 6:
                        System.out.println("\n*** CLOSING INVENTORY TRACKER ***\n");
                        break;
                    default:
                        System.out.println("\n*** INVALID INPUT: Please select an option. ***\n");
                }
            } catch (InputMismatchException e) {
                mainMenuSelection = ExceptionService.handleInputMismatch(USER_SCANNER);
            }
        }

        USER_SCANNER.close();
    }

    /**
     * Displays startup alerts for low inventory and order issues
     */
    private static void displayStartupAlerts() {
        InventoryService.checkInventoryAlerts(USER_SCANNER);
        OrderService.checkOrderAlerts(USER_SCANNER);
        System.out.println();
    }

    /**
     * Displays the main menu options
     */
    private static void displayMainMenu() {
        System.out.println("MAIN MENU");
        System.out.println("""
            1. Inventory
            2. Orders
            3. Customers
            4. Analysis
            5. Import/Export
            6. Quit""");
    }

    /**
     * Handles the inventory submenu operations
     */
    private static void handleInventoryMenu() {
        int inventoryMenuSelection = 0;

        while (inventoryMenuSelection != 6) {
            try {
                System.out.println("INVENTORY");
                System.out.println("""
                    1. Search
                    2. Report
                    3. Update Item
                    4. Create Item
                    5. Delete Item
                    6. Main Menu
                    """);

                inventoryMenuSelection = USER_SCANNER.nextInt();

                switch (inventoryMenuSelection) {
                    case 1:
                        InventoryService.search(USER_SCANNER);
                        break;
                    case 2:
                        InventoryService.report(USER_SCANNER);
                        break;
                    case 3:
                        InventoryService.update(USER_SCANNER);
                        break;
                    case 4:
                        InventoryService.create(USER_SCANNER);
                        break;
                    case 5:
                        InventoryService.delete(USER_SCANNER);
                        break;
                    case 6:
                        System.out.println("\n*** RETURNING TO MAIN MENU ***\n");
                        break;
                    default:
                        System.out.println("\n*** INVALID INPUT: Please select an option. ***\n");
                }
            } catch (InputMismatchException e) {
                inventoryMenuSelection = ExceptionService.handleInputMismatch(USER_SCANNER);
            }
        }
    }

    /**
     * Handles the order submenu operations
     */
    private static void handleOrderMenu() {
        int orderMenuSelection = 0;

        while (orderMenuSelection != 5) {
            try {
                System.out.println("ORDERS");
                System.out.println("""
                    1. Search
                    2. Create Order
                    3. Update Order
                    4. Delete Order
                    5. Main Menu
                    """);

                orderMenuSelection = USER_SCANNER.nextInt();

                switch (orderMenuSelection) {
                    case 1:
                        OrderService.search(USER_SCANNER);
                        break;
                    case 2:
                        OrderService.create(USER_SCANNER);
                        break; // FIXED: Added missing break
                    case 3:
                        OrderService.update(USER_SCANNER);
                        break;
                    case 4:
                        OrderService.delete(USER_SCANNER);
                        break;
                    case 5:
                        System.out.println("\n*** RETURNING TO MAIN MENU ***\n");
                        break;
                    default:
                        System.out.println("\n*** INVALID INPUT: Please select an option. ***\n");
                }
            } catch (InputMismatchException e) {
                orderMenuSelection = ExceptionService.handleInputMismatch(USER_SCANNER);
            }
        }
    }

    /**
     * Handles the customer submenu operations
     */
    private static void handleCustomerMenu() {
        int customerMenuSelection = 0;

        while (customerMenuSelection != 5) {
            try {
                System.out.println("CUSTOMERS");
                System.out.println("""
                    1. Search
                    2. Update Customer
                    3. Create Customer
                    4. Delete Customer
                    5. Main Menu
                    """);

                customerMenuSelection = USER_SCANNER.nextInt();

                switch (customerMenuSelection) {
                    case 1:
                        CustomerService.search(USER_SCANNER);
                        break;
                    case 2:
                        CustomerService.update(USER_SCANNER);
                        break; // FIXED: Added missing break
                    case 3:
                        CustomerService.create(USER_SCANNER);
                        break;
                    case 4:
                        CustomerService.delete(USER_SCANNER);
                        break;
                    case 5:
                        System.out.println("\n*** RETURNING TO MAIN MENU ***\n");
                        break;
                    default:
                        System.out.println("\n*** INVALID INPUT: Please select an option. ***\n");
                }
            } catch (InputMismatchException e) {
                customerMenuSelection = ExceptionService.handleInputMismatch(USER_SCANNER);
            }
        }
    }

    /**
     * Handles the analysis submenu operations
     */
    private static void handleAnalysisMenu() {
        int analysisMenuSelection = 0;

        while (analysisMenuSelection != 5) {
            try {
                System.out.println("ANALYSIS");
                System.out.println("""
                    1. Top 3 Items
                    2. Top 3 Customers
                    3. Bottom 3 Items
                    4. Bottom 3 Customers
                    5. Main Menu
                    """);

                analysisMenuSelection = USER_SCANNER.nextInt();

                switch (analysisMenuSelection) {
                    case 1:
                        SalesAnalysis.topThreeItems();
                        break;
                    case 2:
                        SalesAnalysis.topThreeCustomers();
                        break;
                    case 3:
                        SalesAnalysis.bottomThreeItems();
                        break;
                    case 4:
                        SalesAnalysis.bottomThreeCustomers();
                        break; // FIXED: Added missing break
                    case 5:
                        System.out.println("\n*** RETURNING TO MAIN MENU ***\n");
                        break;
                    default:
                        System.out.println("\n*** INVALID INPUT: Please select an option. ***\n");
                }
            } catch (InputMismatchException e) {
                analysisMenuSelection = ExceptionService.handleInputMismatch(USER_SCANNER);
            } catch (SQLException e) {
                System.err.println("Database error during analysis: " + e.getMessage());
            }
        }
    }

    /**
     * Handles the import/export submenu operations
     */
    private static void handleImportExportMenu() {
        int impExpSelection = 0;

        while (impExpSelection != 5) {
            try {
                System.out.println("IMPORT/EXPORT");
                System.out.println("""
                    1. Import Inventory
                    2. Export Inventory
                    3. Import Customers
                    4. Export Customers
                    5. Main Menu
                    """);

                impExpSelection = USER_SCANNER.nextInt();

                switch (impExpSelection) {
                    case 1:
                        IOService.inventoryImport(USER_SCANNER);
                        break;
                    case 2:
                        IOService.inventoryExport(USER_SCANNER);
                        break;
                    case 3:
                        IOService.customerImport(USER_SCANNER);
                        break;
                    case 4:
                        IOService.customerExport(USER_SCANNER);
                        break; // FIXED: Added missing break
                    case 5:
                        System.out.println("\n*** RETURNING TO MAIN MENU ***\n");
                        break;
                    default:
                        System.out.println("\n*** INVALID INPUT: Please select an option. ***\n");
                }
            } catch (InputMismatchException e) {
                impExpSelection = ExceptionService.handleInputMismatch(USER_SCANNER);
            }
        }
    }
}