package inventory_tracker.controller;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Controller {
    public static void main(String[] args) {

        // create scanner and set userSelection to zero
        Scanner userScanner = new Scanner(System.in);
        int userSelection = 0;

        System.out.println("\nINVENTORY TRACKER v1\n");

        // begin menu selection
        while (userSelection != 5) {

            // try catch for any non-int entries
            try{
                System.out.println("MAIN MENU");
                System.out.println("1. Inventory\n2. Orders\n3. Customers\n4. Import/Export\n5. Quit");

                userSelection = userScanner.nextInt();

                // main menu switch
                switch (userSelection) {

                    // inventory options
                    case 1:

                        System.out.println("\nINVENTORY");
                        int inventorySelection = 0;

                        while (inventorySelection != 5) {
                            System.out.println("1. Search\n2. Report\n3. Create Item\n4. Delete Item\n5. Main Menu\n");

                            try{
                                inventorySelection = userScanner.nextInt();

                                switch (inventorySelection) {
                                    case 1:
                                        System.out.println("\nINVENTORY SEARCH");
                                        break;
                                    case 2:
                                        System.out.println("\nINVENTORY REPORT");
                                        break;
                                    case 3:
                                        System.out.println("\nCREATE ITEM");
                                        break;
                                    case 4:
                                        System.out.println("\nDELETE ITEM");
                                        break;
                                    case 5:
                                        System.out.println("*** RETURNING TO MAIN MENU ***\n");
                                        break;
                                    default:
                                        System.out.println("INVALID INPUT: Please select an option\n");
                                }

                            } catch (InputMismatchException e) {
                                System.out.println("INVALID INPUT: Please enter a number only\n");
                                userScanner.nextLine();
                                inventorySelection = 0;
                            }
                        }
                        break;
                    // orders options
                    case 2:
                        System.out.println("\nORDERS");
                        System.out.println("1. Search\n2. Create\n3. Delete\n4. Main Menu\n");
                        break;
                    case 3:
                        System.out.println("\nCUSTOMERS");
                        System.out.println("1. Search\n2. Create\n3. Delete\n4. Main Menu\n");
                        break;
                    case 4:
                        System.out.println("\nIMPORT/EXPORT");
                        System.out.println("1. Inventory\n2. Customers\n3. Main Menu\n");
                        break;
                    case 5:
                        System.out.println("\nCLOSING PROGRAM\n");
                        break;
                    default:
                        System.out.println("INVALID INPUT: Please select an option\n");
                }
            // main menu catch
            } catch (InputMismatchException e) {
                System.out.println("INVALID INPUT: Please enter a number only\n");
                userScanner.nextLine();
                userSelection = 0;
            }
        }

        userScanner.close();

    }
}
