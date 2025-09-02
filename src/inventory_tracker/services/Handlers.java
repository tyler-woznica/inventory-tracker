package inventory_tracker.services;

import java.util.Scanner;

public class Exceptions {

    private static int handleInputMismatch(Scanner scanner) {
        System.out.println("INVALID INPUT: Please enter a number only\n");
        scanner.nextLine();
        return 0;
    }
}
