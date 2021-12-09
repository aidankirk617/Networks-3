package client;

import java.util.Objects;
import java.util.Scanner;

public class BattleDriver {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Error: Invalid number of arguments");
            System.out.println("Usage: java BattleDriver <host> <port> <username>");
            System.exit(1);
        }

        String host = args[0];
        int port = 0;

        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            System.out.println("Error: Port is not a valid number");
            System.exit(2);
        }

        String username = args[2];

        BattleClient bc = new BattleClient(host, port, username);
        bc.connect();

        while (true){
            Scanner input = new Scanner(System.in);
            String message = input.next();

            bc.send(message);

            if(Objects.equals(message, "/surrender")){
                System.out.println("Closing Connection...");
                System.exit(1);
                break;
            }
        }
    }
}