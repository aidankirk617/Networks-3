/**
 * ConnectionAgent
 *
 * Responsible for sending messages to and receiving messages from remote hosts.
 * The class extends the MessageSource class, indicating that it can play the role of the “subject” in an instance
 * of the observer pattern. The class also implements the Runnable interface, indicating that it encapsulates
 * the logic associated with a Thread.
 * @author Aidan Kirk, David Jennings
 * @version 12/11/21
 */

package common;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConnectionAgent extends MessageSource implements Runnable {

    // Initialize sources
    Socket socket;
    Scanner in;
    PrintStream out;
    Thread thread;

    public ConnectionAgent(Socket sock){
        try {
            socket = sock;  // Initialize Socket

            in = new Scanner(socket.getInputStream());  // Create scanner for inputstream
            out = new PrintStream(socket.getOutputStream());  // Initialize printstream

        }catch (IOException ioe){
            System.out.println("ERROR IN CONNECTION AGENT");  // Throw IO exception if connection fails
        }
        thread = new Thread(this);  // Create thread
    }

    /** Sends message over the network using this connection agents print stream**/
    public void sendMessage(String message){
        out.println(message);
    }

    /** Checks to see if socket is open for connection **/
    public boolean isConnected(){
        return !socket.isClosed();
    }

    /** Closes the message source **/
    public void close(){
        this.closeMessageSource();
    }

    /** Receives commands from the server and disconnects client when appropriate.
     * This method runs whenever a thread is created with a connection agent */
    @Override
    public void run() {
        // While connected, receive players commands
        while(isConnected()) {
            try {
                String received = in.nextLine();
                // If player surrenders, disconnect player from game
                if(received.equals("/surrender")){
                    socket.close();
                    System.out.println("Connection closed: " + this);
                    notifyReceipt(received);
                    break;
                }else{
                    notifyReceipt(received);  // Notify
                }

            }catch(IOException ioe){
                // If connection fails, throw an IO Exception
                System.out.println("ERROR IN CONNECTION AGENT: 56");
            }catch (NoSuchElementException nsee){
                break;
            }
        }
        // Close Connections
        this.close();
        in.close();
        out.close();
    }

    /** Start the thread **/
    public void start(){
        thread.start();
    }
}
