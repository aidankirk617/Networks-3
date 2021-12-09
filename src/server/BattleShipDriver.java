package server;

public class BattleShipDriver {

    /**
     * Entry point to the program
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
        System.out.println("Connecting on port: " + port + ", with game of grid size " + size);
        BattleServer bs = new BattleServer(port, size);
        bs.listen();
    }
}