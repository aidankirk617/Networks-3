package server;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BattleShipDriver {
    /**
     * Entry point to the program
     */
    /*
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int size = 0;
        //prompt for valid grid size
        System.out.print("Please enter a grid size > ");
        try {
            size = scan.nextInt();
        } catch (InputMismatchException ime) {
            System.out.println("Error: Invalid input");
            System.exit(1);
        }
        if (size < 5 || size > 10) {
            System.out.println("Error: Please enter a valid grid size (5-10)");
            System.exit(2);
        }
        //prompt for player names
        System.out.print("Please enter Player 1's name > ");
        String p1Name = scan.next();
        System.out.print("Please enter Player 2's name > ");
        String p2Name = scan.next();
        //begin a game
        Game game = new Game(size, p1Name, p2Name);
        game.play();
    }
     */
    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2) {
            System.out.println("Error: Invalid number of arguments");
            System.out.println("Usage: java BattleShipDriver <port> [grid size]");
            System.exit(1);
        }
        int port = 1000;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            System.out.println("Error: Port is not a valid number");
            System.exit(2);
        }
        int size = 0;
        if (args.length == 2) {
            try {
                size = Integer.parseInt(args[1]);
            } catch (NumberFormatException nfe) {
                System.out.println("Error: Size is not a valid number");
                System.exit(3);
            }
            if (size > 10 || size < 5) {
                System.out.println("Error: Invalid size given");
                System.out.println("Size must be between 5 and 10 (inclusive)");
                System.exit(4);
            }
        } else {
            size = 10;
        }
        BattleServer bs = new BattleServer(port, size);
        bs.listen();
    }
}