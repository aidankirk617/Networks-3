/*
 * Western Carolina University
 * Fall 2021
 * CS-465-01 - Computer Networks
 * Program 3: Battleship (Multiuser Game)
 * @authors Aidan Kirk, David Jennings
 */

package client;

import java.util.Objects;
import java.util.Scanner;


public class BattleDriver {

    public static void main(String[] args) {
        // If incorrect number of arguments are provided, print error message
        if (args.length != 3) {
            System.out.println("Error: Invalid number of arguments");
            System.out.println("Usage: java BattleDriver <host> <port> <username>");
            System.exit(1); // Close program with exit code 1
        }

        String host = args[0];  // Take host as first argument
        int port = 0;

        try {
            port = Integer.parseInt(args[1]);   // Take in the port number as second argument
        } catch (NumberFormatException nfe) {
            System.out.println("Error: Port is not a valid number");
            System.exit(2); // Close program with exit code 2
        }

        String username = args[2];  // Take in players username as third argument

        BattleClient bc = new BattleClient(host, port, username);   // Initialize battle client
        bc.connect();   // Connect battle client

        while (true){
            Scanner input = new Scanner(System.in);
            String message = input.nextLine();  // Read in players

            bc.send(message);   // Send players commands over the network

            // If player surrenders close connection to the game
            if(Objects.equals(message, "/surrender")){
                System.out.println("Closing Connection...");
                System.exit(1);     // Throw exit code 1
                break;
            }
        }
    }
}