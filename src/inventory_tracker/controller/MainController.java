package inventory_tracker.controller;
import inventory_tracker.model.Item;
import inventory_tracker.services.CustomerService;
import inventory_tracker.services.ExceptionService;
import inventory_tracker.services.InventoryService;
import inventory_tracker.services.OrderService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainController {
    public static void main(String[] args) {

        // create scanner and set userSelection to zero
        Scanner userScanner = new Scanner(System.in);
        int mainMenuSelection = 0;

        System.out.println("\n*** INVENTORY TRACKER ***\n");

        // menu selection loop
        while (mainMenuSelection != 6) {

            System.out.println("MAIN MENU");
            System.out.println("""
                    1. Inventory
                    2. Orders
                    3. Customers
                    4. Analysis
                    5. Import/Export
                    6. Quit""");

            // try catch for any non-int entries
            try{

                // take in user selection
                mainMenuSelection = userScanner.nextInt();

                // main menu switch
                switch (mainMenuSelection) {

                    // inventory menu
                    case 1:
                        System.out.println("INVENTORY");

                        // new input unique to inventory options
                        int inventoryMenuSelection = 0;

                        while (inventoryMenuSelection != 6) {
                            System.out.println("""
                                    1. Search
                                    2. Report
                                    3. Update Item
                                    4. Create Item
                                    5. Delete Item
                                    6. Main Menu
                                    """);

                            // inventory menu try statement
                            try{

                                inventoryMenuSelection = userScanner.nextInt();

                                switch (inventoryMenuSelection) {
                                    // inventory search
                                    case 1:
                                        InventoryService.search();
                                        break;
                                    // inventory report
                                    case 2:
                                        InventoryService.report();
                                        break;
                                    // update item
                                    case 3:
                                        InventoryService.update();
                                        break;
                                    // create item
                                    case 4:
                                        InventoryService.create();
                                        break;
                                    // delete item
                                    case 5:
                                        InventoryService.delete();
                                        break;
                                    // return to main menu
                                    case 6:
                                        System.out.println("*** RETURNING TO MAIN MENU ***\n");
                                        break;
                                    default:
                                        System.out.println("*** INVALID INPUT: Please select an option ***\n");
                                }

                            // inventory menu catch statement
                            } catch (InputMismatchException e) {
                                inventoryMenuSelection = ExceptionService.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // order menu
                    case 2:
                        System.out.println("ORDERS");

                        int orderMenuSelection = 0;

                        while (orderMenuSelection != 5) {
                            System.out.println("""
                                    1. Search
                                    2. Update
                                    3. Create
                                    4. Delete
                                    5. Main Menu
                                    """);

                            // order menu try statement
                            try{
                                orderMenuSelection = userScanner.nextInt();

                                switch (orderMenuSelection) {
                                    // order search
                                    case 1:
                                        OrderService.search();
                                        break;
                                    case 2:
                                        OrderService.update();
                                    // create order
                                    case 3:
                                        OrderService.create();
                                        break;
                                    // delete order
                                    case 4:
                                        OrderService.delete();
                                        break;
                                    // return to main menu
                                    case 5:
                                        System.out.println("*** RETURNING TO MAIN MENU ***\n");
                                        break;
                                    default:
                                        System.out.println("INVALID INPUT: Please select an option\n");
                                }
                            } catch (InputMismatchException e) {
                                orderMenuSelection = ExceptionService.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // customer menu
                    case 3:
                        System.out.println("CUSTOMERS");
                        int customerMenuSelection = 0;

                        while (customerMenuSelection != 5) {
                            System.out.println("""1. Search\n2. Create\n3. Delete\n4. Main Menu\n""");

                            // customer menu try statement
                            try{
                                customerMenuSelection = userScanner.nextInt();

                                switch (customerMenuSelection) {

                                    // customer search
                                    case 1:
                                        int customerID;
                                        System.out.println("\nCUSTOMER SEARCH");
                                        System.out.println("Please enter the customer ID: ");
                                        customerID = userScanner.nextInt();
                                        CustomerService.customerSearch(customerID);
                                        break;

                                    // create customer
                                    case 2:
                                        System.out.println("\nCREATE CUSTOMER - CURRENTLY UNAVAILABLE");
                                        break;

                                    // delete customer
                                    case 3:
                                        System.out.println("\nDELETE CUSTOMER - CURRENTLY UNAVAILABLE");
                                        break;

                                    // return to main menu
                                    case 4:
                                        System.out.println("*** RETURNING TO MAIN MENU ***\n");
                                        break;
                                    default:
                                        System.out.println("INVALID INPUT: Please select an option\n");
                                }
                                // customer menu catch statement
                            } catch (InputMismatchException e) {
                                customerMenuSelection = ExceptionService.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // analysis menu options
                    case 4:
                        System.out.println("ANALYSIS");
                        int analysisSelection = 0;

                        while (analysisSelection != 4) {
                            System.out.println("*** ANALYSIS CURRENTLY UNAVAILABLE ***");
                            System.out.println("1. Best Sellers\n2. Top 3 Customers\n3. Bottom 3 Customers\n" +
                                    "4. Main Menu\n");

                            // analysis try statement
                            try{
                                analysisSelection = userScanner.nextInt();

                                switch (analysisSelection) {

                                    // best sellers
                                    case 1:
                                        System.out.println("\nBEST SELLERS");
                                        break;

                                    // export inventory
                                    case 2:
                                        System.out.println("\nTOP 3 CUSTOMERS");
                                        break;

                                    // import customers
                                    case 3:
                                        System.out.println("\nBOTTOM 3 CUSTOMERS");
                                        break;
                                    // export customers
                                    case 4:
                                        System.out.println("*** RETURNING TO MAIN MENU ***\n");
                                        break;
                                    default:
                                        System.out.println("INVALID INPUT: Please select an option\n");
                                }
                                // import/export menu catch statement
                            } catch (InputMismatchException e) {
                                analysisSelection = ExceptionService.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // import/export menu options
                    case 5:
                        System.out.println("IMPORT/EXPORT");
                        int impExpSelection = 0;

                        while (impExpSelection != 5) {
                            System.out.println("1. Import Inventory\n2. Export Inventory\n3. Import Customers\n" +
                                    "4. Export Customers\n5. Main Menu\n");

                            // import/export try statement
                            try{
                                impExpSelection = userScanner.nextInt();

                                switch (impExpSelection) {

                                    // import inventory
                                    case 1:
                                        System.out.println("\nIMPORT INVENTORY");
                                        break;

                                    // export inventory
                                    case 2:
                                        System.out.println("\nEXPORT INVENTORY");
                                        break;

                                    // import customers
                                    case 3:
                                        System.out.println("\nIMPORT CUSTOMERS");
                                        break;
                                    // export customers
                                    case 4:
                                        System.out.println("\nEXPORT CUSTOMERS");

                                    // return to main menu
                                    case 5:
                                        System.out.println("*** RETURNING TO MAIN MENU ***\n");
                                        break;
                                    default:
                                        System.out.println("INVALID INPUT: Please select an option\n");
                                }
                                // import/export menu catch statement
                            } catch (InputMismatchException e) {
                                impExpSelection = ExceptionService.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // quit the program
                    case 6:
                        System.out.println("\nCLOSING PROGRAM\n");
                        break;
                    default:
                        System.out.println("INVALID INPUT: Please select an option\n");
                }
            // main menu catch
            } catch (InputMismatchException e) {
                mainMenuSelection = ExceptionService.handleInputMismatch(userScanner);
            }
        }

        userScanner.close();

    }
}
