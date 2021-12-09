/*
 * Western Carolina University
 * Fall 2021
 * CS-465-01 - Computer Networks
 * Program 3: Battleship (Multiuser Game)
 * Instructor: Dr. Scott Barlowe
 */

package common;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionAgent extends MessageSource implements Runnable {

    Socket socket;
    Scanner in;
    PrintStream out;
    Thread thread;

    public ConnectionAgent(Socket sock){
        try {
            socket = sock;
            in = new Scanner(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
        }catch (IOException ioe){
            System.out.println("ERROR IN CONNECTION AGENT");
        }
        thread = new Thread(this);
    }

    public void sendMessage(String message){
        out.println(message);
    }

    public boolean isConnected(){
        return socket.isConnected();
    }

    public void close() throws IOException {
        socket.close();
    }

    @Override
    public void run() {
        while(true) {
            try {
                String received = in.next();
                if (received.equals("/surrender")) {
                    System.out.println("Client: " + socket + " is disconnecting...");
                    socket.close();
                    System.out.println("Client disconnected");

                    break;
                }

                System.out.println("Message received: " + received);

            }catch(IOException ioe){
                System.out.println("ERROR IN CONNECTION AGENT: 56");
            }
        }
    }
}
