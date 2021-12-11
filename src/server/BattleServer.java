/**
 * BattleServer
 * BattleServer is one of the classes that implement the server-side logic of this client-server application. It is
 * responsible for accepting incoming connections, creating ConnectionAgents, and passing the ConnectionA-
 * gent off to threads for processing. The class implements the MessageListener interface (i.e., it can “observe”
 * objects that are MessageSources).
 *
 * @author Aidan Kirk, David Jennings
 * @version 12/11/21
 */

package server;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Vector;

/** **/
public class BattleServer implements MessageListener {
    // Initialize fields
    private ServerSocket serverSocket;
    private int current;
    private Game game;
    private ArrayList<Player> players;
    private Vector<ConnectionAgent> connections;

    /** **/
    public BattleServer(int port) {
        this.game = new Game(10);  // Set default grid size to 10
        connections = new Vector<>();  // Store connections in a vector
        players = new ArrayList<>();  // Store list of players in an array
        try {
            this.serverSocket = new ServerSocket(port); // Create server socket
        } catch (IOException ioe) {
            System.out.println("Error: IO Exception " + ioe.getMessage());  // If connection fails, run IOException
            System.exit(1); // Exit program with code 1
        }
    }

    /** Creates the server and listens for commands from the client **/
    public void listen() {
        while(!serverSocket.isClosed()){
            try{
                // Accept the connection into the server socket
                ConnectionAgent connection = new ConnectionAgent(serverSocket.accept());
                // Add message listener
                connection.addMessageListener(this);
                // Send introduction prompt
                connection.sendMessage("\nWelcome to battleship. To begin please enter: " +
                        "/battle <username>");
                connections.add(connection);
                // Establish and start the connection
                System.out.println("Connection established: " + connection);
                connection.start();
                // If connection fails throw IOException
            }catch (IOException ioe){
                System.out.println("IOException: Connection failed");
            }
        }
    }

    /** Sends a message to every connection agent connected to the server. **/
    public void broadcast(String message) {
        for(ConnectionAgent connection : connections){
            connection.sendMessage(message);
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
        // If player starts the game without enough players, prompt accordingly
        if (message.equals("/start")){
           if(connections.size() < 2){
               ((ConnectionAgent) source).sendMessage("Not enough players to play the game");
           }else {
               broadcast("The game begins");  // Begin game
           }
        }else if(message.startsWith("/battle")) {
            String name = message.substring(8); // Max number of players: 8
            players.add(new Player(name));  // Add player to game
            broadcast("!!! " + name + " has entered battle");
        }else{
            broadcast(message);
        }
    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        // Remove connection agents and listeners
        connections.remove((ConnectionAgent) source);
        source.removeMessageListener(this);
    }
}