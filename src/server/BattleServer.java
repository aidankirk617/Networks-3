package server;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class BattleServer implements MessageListener {
    private ServerSocket serverSocket;
    private int current;
    private Game game;
    private ArrayList<ConnectionAgent> connections;

    public BattleServer(int port) {
        this.game = new Game(10);
        connections = new ArrayList<>();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ioe) {
            System.out.println("Error: IO Exception " + ioe.getMessage());
            System.exit(1);
        }
    }

    public void listen() {
        while(!serverSocket.isClosed()){
            try{
                ConnectionAgent connection = new ConnectionAgent(serverSocket.accept());
                connections.add(connection);
                System.out.println("Connection established: " + connection);
                connection.run();
            }catch (IOException ioe){
                System.out.println("ERROR IN BATTLE SERVER");
            }

        }
    }

    public void broadcast(String message) {

    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) {
        //Not sure if this is how the message receiving is supposed to work, but I need the
        // connection agents to be working in order to test it. Same with the method below
        source.addMessageListener(this);
        this.broadcast(message);
    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
    }
}