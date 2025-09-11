package inventory_tracker.controller;
import inventory_tracker.data.SalesAnalysis;
import inventory_tracker.services.*;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainController {
    public static void main(String[] args) {

        // create scanner and set userSelection to zero
        Scanner userScanner = new Scanner(System.in);
        int mainMenuSelection = 0;

        System.out.println("\n*** INVENTORY TRACKER v1.0 ***\n");

        // menu selection loop
        while (mainMenuSelection != 6) {

            // try catch for any non-int entries
            try{
                System.out.println("MAIN MENU");
                System.out.println("""
                    1. Inventory
                    2. Orders
                    3. Customers
                    4. Analysis
                    5. Import/Export
                    6. Quit""");

                // take in user selection
                mainMenuSelection = userScanner.nextInt();

                // main menu switch
                switch (mainMenuSelection) {

                    // inventory menu
                    case 1:

                        int inventoryMenuSelection = 0;

                        while (inventoryMenuSelection != 6) {

                            try{
                                System.out.println("INVENTORY");
                                System.out.println("""
                                    1. Search
                                    2. Report
                                    3. Update Item
                                    4. Create Item
                                    5. Delete Item
                                    6. Main Menu
                                    """);

                                inventoryMenuSelection = userScanner.nextInt();

                                switch (inventoryMenuSelection) {

                                    case 1:
                                        InventoryService.search();
                                        break;

                                    case 2:
                                        InventoryService.report();
                                        break;

                                    case 3:
                                        InventoryService.update();
                                        break;

                                    case 4:
                                        InventoryService.create();
                                        break;

                                    case 5:
                                        InventoryService.delete();
                                        break;

                                    case 6:
                                        System.out.println("\n*** RETURNING TO MAIN MENU ***\n");
                                        break;

                                    default:
                                        System.out.println("\n*** INVALID INPUT: Please select an option. ***\n");
                                }
                            } catch (InputMismatchException e) {
                                inventoryMenuSelection = ExceptionService.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // order menu
                    case 2:

                        int orderMenuSelection = 0;

                        while (orderMenuSelection != 5) {

                            try{
                                System.out.println("ORDERS");
                                System.out.println("""
                                    1. Search
                                    2. Create Order
                                    3. Update Order
                                    4. Delete Order
                                    5. Main Menu
                                    """);

                                orderMenuSelection = userScanner.nextInt();

                                switch (orderMenuSelection) {

                                    case 1:
                                        OrderService.search();
                                        break;
                                    case 2:
                                        OrderService.create();

                                    case 3:
                                        OrderService.update();
                                        break;

                                    case 4:
                                        OrderService.delete();
                                        break;

                                    case 5:
                                        System.out.println("\n*** RETURNING TO MAIN MENU ***\n");
                                        break;

                                    default:
                                        System.out.println("\n*** INVALID INPUT: Please select an option. ***\n");
                                }
                            } catch (InputMismatchException e) {
                                orderMenuSelection = ExceptionService.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // customer menu
                    case 3:

                        int customerMenuSelection = 0;

                        while (customerMenuSelection != 5) {

                            try{
                                System.out.println("CUSTOMERS");
                                System.out.println("""
                                    1. Search
                                    2. Update Customer
                                    3. Create Customer
                                    4. Delete Customer
                                    5. Main Menu
                                    """);

                                customerMenuSelection = userScanner.nextInt();

                                switch (customerMenuSelection) {

                                    case 1:
                                        CustomerService.search();
                                        break;

                                    case 2:
                                        CustomerService.update();

                                    case 3:
                                        CustomerService.create();
                                        break;

                                    case 4:
                                        CustomerService.delete();
                                        break;

                                    case 5:
                                        System.out.println("\n*** RETURNING TO MAIN MENU ***\n");
                                        break;

                                    default:
                                        System.out.println("\n*** INVALID INPUT: Please select an option. ***\n");
                                }
                            } catch (InputMismatchException e) {
                                customerMenuSelection = ExceptionService.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // analysis menu
                    case 4:

                        int analysisMenuSelection = 0;

                        while (analysisMenuSelection != 5) {

                            try{
                                System.out.println("ANALYSIS");
                                System.out.println("""
                                    1. Top 3 Items
                                    2. Top 3 Customers
                                    3. Bottom 3 Items
                                    4. Bottom 3 Customers
                                    5. Main Menu
                                    """);

                                analysisMenuSelection = userScanner.nextInt();

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

                                    case 5:
                                        System.out.println("\n*** RETURNING TO MAIN MENU ***\n");
                                        break;

                                    default:
                                        System.out.println("\n*** INVALID INPUT: Please select an option. ***\n");
                                }
                            } catch (InputMismatchException e) {
                                analysisMenuSelection = ExceptionService.handleInputMismatch(userScanner);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;

                    // import/export menu
                    case 5:

                        int impExpSelection = 0;

                        while (impExpSelection != 5) {

                            try{
                                System.out.println("IMPORT/EXPORT");
                                System.out.println("""
                                    1. Import Inventory
                                    2. Export Inventory
                                    3. Import Customers
                                    4. Export Customers
                                    5. Main Menu
                                    """);

                                impExpSelection = userScanner.nextInt();

                                switch (impExpSelection) {

                                    case 1:
                                        IOService.inventoryImport();
                                        break;

                                    case 2:
                                        IOService.inventoryExport();
                                        break;

                                    case 3:
                                        IOService.customerImport();
                                        break;

                                    case 4:
                                        IOService.customerExport();

                                    case 5:
                                        System.out.println("\n*** RETURNING TO MAIN MENU ***\n");
                                        break;
                                    default:
                                        System.out.println("\n*** INVALID INPUT: Please select an option. ***\n");
                                }
                            } catch (InputMismatchException e) {
                                impExpSelection = ExceptionService.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // quit program
                    case 6:
                        System.out.println("\n*** CLOSING INVENTORY TRACKER ***\n");
                        break;
                    default:
                        System.out.println("\n*** INVALID INPUT: Please select an option. ***\n");
                }
            // main menu catch
            } catch (InputMismatchException e) {
                mainMenuSelection = ExceptionService.handleInputMismatch(userScanner);
            }
        }

        userScanner.close();

    }
}
