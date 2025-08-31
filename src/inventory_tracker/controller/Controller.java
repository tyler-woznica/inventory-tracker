package inventory_tracker.controller;
import java.util.Scanner;

public class Controller {
    public static void main(String[] args) {

        Scanner userScanner = new Scanner(System.in);
        int userSelection = 0;

        System.out.println("\nINVENTORY TRACKER v1\n");
        // check for low alerts

        while (userSelection != 5) {
            System.out.println("*** Options ***");
            System.out.println("1. Inventory\n2. Orders\n3. Customers\n4. Import/Export\n5. Quit");

            userSelection = userScanner.nextInt();

            switch (userSelection) {
                case 1:
                    System.out.println("\n*** Inventory ***");
                    System.out.println("1. Check Finished Goods\n2. Check Raw Materials\n");
                    break;
                case 2:
                    System.out.println("\n*** Orders ***");
                    System.out.println("1. Check Orders\n2. Create Order\n3. Delete Order\n");
                    break;
                case 3:
                    System.out.println("\n*** Customers ***");
                    System.out.println("\n1. Search Customers\n2. Create Customer\n3. Delete Customer");
                    break;
                case 4:
                    System.out.println("\n*** Import/Export ***");
                    System.out.println("\n1. Raw Materials\n2. Finished Goods\n3. Customers");
                    break;
                case 5:
                    System.out.println("\n*** Closing Program ***");
                    break;
                default:
                    System.out.println("\n*** Invalid Input ***");
            }
        }
    }
}
