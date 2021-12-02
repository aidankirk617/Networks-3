package client;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;
import server.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class BattleClient extends MessageSource implements MessageListener {

        public static void main(String[] args) {
            //Get arg values
            String address = args[0];
            String portNum = args[1];
            //Confirmation
            System.out.println("Address: " + address + " Port Number: " + portNum);
            try {
                //Create the socket, reader, writer, scanner
                Socket sock = new Socket(InetAddress.getByName(address), Integer.parseInt(portNum));
                InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
                PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);
                BufferedReader buffReader = new BufferedReader(streamReader);
                Scanner scan = new Scanner(System.in);
                //Read in the age from the user
                System.out.println("Enter your string: ");
                String age = scan.nextLine();
                //Send it to the writer
                writer.println(age);
                //Receive a response from the server
                String response = buffReader.readLine();
                //Print out the result
                System.out.println("Look David i sent a " + response);
                //Close everything
                sock.close();
                writer.close();
                buffReader.close();
                scan.close();
            } catch (IOException ioe) {
                System.out.println("Error!");
                System.exit(1);
            }
        }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) {

    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {

    }

    public void send(String message) {

    }
}