package inventory_tracker.controller;
import inventory_tracker.services.Handlers;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Controller {
    public static void main(String[] args) {

        // create scanner and set userSelection to zero
        Scanner userScanner = new Scanner(System.in);
        int mainMenuSelection = 0;

        System.out.println("\nINVENTORY TRACKER - Version 1\n");

        // begin menu selection
        while (mainMenuSelection != 5) {

            // try catch for any non-int entries
            try{
                System.out.println("MAIN MENU");
                System.out.println("1. Inventory\n2. Orders\n3. Customers\n4. Import/Export\n5. Quit");

                mainMenuSelection = userScanner.nextInt();

                // main menu switch
                switch (mainMenuSelection) {

                    // inventory options
                    case 1:

                        System.out.println("\nINVENTORY");
                        // new input unique to inventory options
                        int inventorySelection = 0;

                        while (inventorySelection != 5) {
                            System.out.println("1. Search\n2. Report\n3. Create Item\n4. Delete Item\n5. Main Menu\n");

                            // inventory menu try statement
                            try{
                                inventorySelection = userScanner.nextInt();

                                switch (inventorySelection) {

                                    // inventory search
                                    case 1:
                                        System.out.println("\nINVENTORY SEARCH");
                                        break;

                                    // inventory report
                                    case 2:
                                        System.out.println("\nINVENTORY REPORT");
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
                                inventorySelection = Handlers.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // order menu options
                    case 2:
                        System.out.println("\nORDERS");
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
                                        System.out.println("\nORDER SEARCH");
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
                                orderSelection = Handlers.handleInputMismatch(userScanner);
                            }
                        }
                        break;
                    // customer menu options
                    case 3:
                        System.out.println("\nCUSTOMERS");
                        int customerSelection = 0;

                        while (customerSelection != 4) {
                            System.out.println("1. Search\n2. Create\n3. Delete\n4. Main Menu\n");

                            // customer menu try statement
                            try{
                                customerSelection = userScanner.nextInt();

                                switch (customerSelection) {

                                    // customer search
                                    case 1:
                                        System.out.println("\nCUSTOMER SEARCH");
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
                                customerSelection = Handlers.handleInputMismatch(userScanner);
                            }
                        }
                        break;

                    // import/export options
                    case 4:
                        System.out.println("\nIMPORT/EXPORT");
                        System.out.println("1. Inventory\n2. Customers\n3. Main Menu\n");
                        break;
                    // quit the program
                    case 5:
                        System.out.println("\nCLOSING PROGRAM\n");
                        break;
                    default:
                        System.out.println("INVALID INPUT: Please select an option\n");
                }
            // main menu catch
            } catch (InputMismatchException e) {
                mainMenuSelection = Handlers.handleInputMismatch(userScanner);
            }
        }

        userScanner.close();

    }
}
