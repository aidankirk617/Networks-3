/*
 * Western Carolina University
 * Fall 2021
 * CS-465-01 - Computer Networks
 * Program 3: Battleship (Multiuser Game)
 * Instructor: Dr. Scott Barlowe
 */

package client;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class BattleDriver {

    public static void main(String[] args) {

        if (args.length != 3){
            System.out.println("Please enter the correct number of arguments");
            System.exit(1);
        }

        InetAddress host = null;
        try {
            host = InetAddress.getByName(args[1]);
        }catch (UnknownHostException unknownHostException){
            System.out.println("Unknown Host");
            System.exit(1);
        }

        int port = 0;
        if(args[1].length() != 4){
            System.out.println("Invalid port: ports must be 4 numbers");
        }
        try{
            port = Integer.parseInt(args[1]);
        }catch (NumberFormatException numberFormatException){
            System.out.println("Port number can only contain integers");
            System.exit(1);
        }

        String username = args[2];

        System.out.println("Hello " + username + ", connecting you to: " + host.getHostName() +
                " on port: " + port);

    }
}
