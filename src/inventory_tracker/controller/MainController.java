package inventory_tracker.controller;
import inventory_tracker.data.Analysis;
import inventory_tracker.model.Item;
import inventory_tracker.services.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainController {
    public static void main(String[] args) {

        // create scanner and set userSelection to zero
        Scanner userScanner = new Scanner(System.in);
        int mainMenuSelection = 0;

        System.out.println("*** INVENTORY TRACKER ***");

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
                        // new input unique to inventory options
                        int inventoryMenuSelection = 0;

                        while (inventoryMenuSelection != 6) {


                            // inventory menu try statement
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
                                        System.out.println("*** RETURNING TO MAIN MENU ***");
                                        break;
                                    default:
                                        System.out.println("*** INVALID INPUT: Please select an option ***");
                                }

                            // inventory menu catch statement
                            } catch (InputMismatchException e) {
                                inventoryMenuSelection = ExceptionService.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // order menu
                    case 2:

                        int orderMenuSelection = 0;

                        while (orderMenuSelection != 5) {

                            // order menu try statement
                            try{
                                System.out.println("ORDERS");
                                System.out.println("""
                                    1. Search
                                    2. Update
                                    3. Create
                                    4. Delete
                                    5. Main Menu
                                    """);

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
                                        System.out.println("*** RETURNING TO MAIN MENU ***");
                                        break;
                                    default:
                                        System.out.println("*** INVALID INPUT: Please select an option ***");
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
                            System.out.println("""
                                    1. Search
                                    2. Create
                                    3. Delete
                                    4. Main Menu
                                    5. Main Menu
                                    """);

                            // customer menu try statement
                            try{
                                customerMenuSelection = userScanner.nextInt();

                                switch (customerMenuSelection) {
                                    // customer search
                                    case 1:
                                        CustomerService.search();
                                        break;
                                    // update customer
                                    case 2:
                                        CustomerService.update();
                                    // create customer
                                    case 3:
                                        CustomerService.create();
                                        break;
                                    // delete customer
                                    case 4:
                                        CustomerService.delete();
                                        break;
                                    // return to main menu
                                    case 5:
                                        System.out.println("*** RETURNING TO MAIN MENU ***");
                                        break;
                                    default:
                                        System.out.println("*** INVALID INPUT: Please select an option ***");
                                }
                            } catch (InputMismatchException e) {
                                customerMenuSelection = ExceptionService.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // analysis menu options
                    case 4:
                        System.out.println("ANALYSIS");
                        int analysisMenuSelection = 0;

                        while (analysisMenuSelection != 4) {
                            System.out.println("""
                                    1. Best Sellers
                                    2. Top 3 Customers
                                    3. Bottom 3 Customers
                                    4. Main Menu
                                    """);

                            // analysis try statement
                            try{
                                analysisMenuSelection = userScanner.nextInt();

                                switch (analysisMenuSelection) {
                                    // best sellers
                                    case 1:
                                        Analysis.bestSellers();
                                        break;
                                    // top 3 customers
                                    case 2:
                                        Analysis.topThree();
                                        break;
                                    // bottom 3 customers
                                    case 3:
                                        Analysis.bottomThree();
                                        break;
                                    // return to main menu
                                    case 4:
                                        System.out.println("*** RETURNING TO MAIN MENU ***");
                                        break;
                                    default:
                                        System.out.println("*** INVALID INPUT: Please select an option ***");
                                }
                            } catch (InputMismatchException e) {
                                analysisMenuSelection = ExceptionService.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // import/export menu options
                    case 5:
                        System.out.println("IMPORT/EXPORT");
                        int impExpSelection = 0;

                        while (impExpSelection != 5) {
                            System.out.println("""
                                    1. Import Inventory
                                    2. Export Inventory
                                    3. Import Customers
                                    4. Export Customers
                                    5. Main Menu
                                    """);

                            // import/export try statement
                            try{
                                impExpSelection = userScanner.nextInt();

                                switch (impExpSelection) {

                                    // import inventory
                                    case 1:
                                        IOService.inventoryImport();
                                        break;
                                    // export inventory
                                    case 2:
                                        IOService.inventoryExport();
                                        break;
                                    // import customers
                                    case 3:
                                        IOService.customerImport();
                                        break;
                                    // export customers
                                    case 4:
                                        IOService.customerExport();
                                    // return to main menu
                                    case 5:
                                        System.out.println("*** RETURNING TO MAIN MENU ***");
                                        break;
                                    default:
                                        System.out.println("*** INVALID INPUT: Please select an option ***");
                                }
                            } catch (InputMismatchException e) {
                                impExpSelection = ExceptionService.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // quit the program
                    case 6:
                        System.out.println("CLOSING INVENTORY TRACKER");
                        break;
                    default:
                        System.out.println("*** INVALID INPUT: Please select an option ***");
                }
            // main menu catch
            } catch (InputMismatchException e) {
                mainMenuSelection = ExceptionService.handleInputMismatch(userScanner);
            }
        }

        userScanner.close();

    }
}
