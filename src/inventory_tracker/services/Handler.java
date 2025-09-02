package inventory_tracker.services;

import java.util.Scanner;

public class Handler {

    // helper for menu selection errors
    // replaces this block
    // catch (InputMismatchException e) {
    //     System.out.println("INVALID INPUT: Please enter a number only\n");
    //     userScanner.nextLine();
    //     userSelection = 0;
    // }
    public static int handleInputMismatch(Scanner scanner) {
        System.out.println("INVALID INPUT: Please enter a number only\n");
        scanner.nextLine();
        return 0;
    }
}
