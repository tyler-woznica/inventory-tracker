package inventory_tracker.controller;
import inventory_tracker.services.CustomerTools;
import inventory_tracker.services.Handler;
import inventory_tracker.services.InventoryTools;
import inventory_tracker.services.OrderTools;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Controller {
    public static void main(String[] args) {

        // create scanner and set userSelection to zero
        Scanner userScanner = new Scanner(System.in);
        int mainMenuSelection = 0;

        System.out.println("\nINVENTORY TRACKER - Version 1\n");

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

                mainMenuSelection = userScanner.nextInt();

                // main menu switch
                switch (mainMenuSelection) {

                    // inventory menu options
                    case 1:

                        System.out.println("INVENTORY");

                        // new input unique to inventory options
                        int inventorySelection = 0;

                        while (inventorySelection != 5) {
                            System.out.println("""
                                    1. Search
                                    2. Report
                                    3. Create Item
                                    4. Delete Item
                                    5. Main Menu
                                    """);

                            // inventory menu try statement
                            try{

                                inventorySelection = userScanner.nextInt();

                                switch (inventorySelection) {

                                    // inventory search
                                    case 1:
                                        int inventoryID;
                                        System.out.println("\n*** INVENTORY SEARCH ***");
                                        System.out.println("Please enter the product ID: ");
                                        inventoryID = userScanner.nextInt();
                                        InventoryTools.inventorySearch(inventoryID);
                                        break;
                                    // inventory report
                                    case 2:
                                        System.out.println("\n*** INVENTORY REPORT ***");
                                        InventoryTools.inventoryReport();
                                        break;
                                    // create item
                                    case 3:
                                        System.out.println("\nCREATE ITEM");
                                        break;
                                    // delete item
                                    case 4:
                                        System.out.println("\nDELETE ITEM");
                                        break;
                                    // return to main menu
                                    case 5:
                                        System.out.println("*** RETURNING TO MAIN MENU ***\n");
                                        break;
                                    default:
                                        System.out.println("INVALID INPUT: Please select an option\n");
                                }
                            // inventory menu catch statement
                            } catch (InputMismatchException e) {
                                inventorySelection = Handler.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // order menu options
                    case 2:
                        System.out.println("ORDERS");
                        // new input unique to order options
                        int orderSelection = 0;

                        while (orderSelection != 4) {
                            System.out.println("1. Search\n2. Create\n3. Delete\n4. Main Menu\n");

                            // order menu try statement
                            try{
                                orderSelection = userScanner.nextInt();

                                switch (orderSelection) {

                                    // order search
                                    case 1:
                                        int orderID;
                                        System.out.println("\nORDER SEARCH");
                                        System.out.println("Please enter the customer ID: ");
                                        orderID = userScanner.nextInt();
                                        OrderTools.orderSearch(orderID);
                                        break;

                                    // create order
                                    case 2:
                                        System.out.println("\nCREATE ORDER");
                                        break;

                                    // delete order
                                    case 3:
                                        System.out.println("\nDELETE ORDER");
                                        break;

                                    // return to main menu
                                    case 4:
                                        System.out.println("*** RETURNING TO MAIN MENU ***\n");
                                        break;
                                    default:
                                        System.out.println("INVALID INPUT: Please select an option\n");
                                }
                                // order menu catch statement
                            } catch (InputMismatchException e) {
                                orderSelection = Handler.handleInputMismatch(userScanner);
                            }
                        }
                        break;
                    // customer menu options
                    case 3:
                        System.out.println("CUSTOMERS");
                        int customerSelection = 0;

                        while (customerSelection != 4) {
                            System.out.println("1. Search\n2. Create\n3. Delete\n4. Main Menu\n");

                            // customer menu try statement
                            try{
                                customerSelection = userScanner.nextInt();

                                switch (customerSelection) {

                                    // customer search
                                    case 1:
                                        int customerID;
                                        System.out.println("\nCUSTOMER SEARCH");
                                        System.out.println("Please enter the customer ID: ");
                                        customerID = userScanner.nextInt();
                                        CustomerTools.customerSearch(customerID);
                                        break;

                                    // create customer
                                    case 2:
                                        System.out.println("\nCREATE CUSTOMER");
                                        break;

                                    // delete customer
                                    case 3:
                                        System.out.println("\nDELETE CUSTOMER");
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
                                customerSelection = Handler.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // analysis menu options
                    case 4:
                        System.out.println("ANALYSIS");
                        int analysisSelection = 0;

                        while (analysisSelection != 5) {
                            System.out.println("1. Best Sellers\n2. Top 3 Customers\n3. Bottom 3 Customers\n" +
                                    "4. Compare Customers\n5. Compare Items\n6. Main Menu\n");

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
                                        System.out.println("\nCOMPARE CUSTOMERS");
                                    // compare items
                                    case 5:
                                        System.out.println("\nCOMPARE ITEMS");
                                    // return to main menu
                                    case 6:
                                        System.out.println("*** RETURNING TO MAIN MENU ***\n");
                                        break;
                                    default:
                                        System.out.println("INVALID INPUT: Please select an option\n");
                                }
                                // import/export menu catch statement
                            } catch (InputMismatchException e) {
                                analysisSelection = Handler.handleInputMismatch(userScanner);
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
                                impExpSelection = Handler.handleInputMismatch(userScanner);
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
                mainMenuSelection = Handler.handleInputMismatch(userScanner);
            }
        }

        userScanner.close();

    }
}
