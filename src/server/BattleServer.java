package server;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Vector;

public class BattleServer implements MessageListener {
    private ServerSocket serverSocket;
    private int current;
    private Game game;
    private ArrayList<Player> players;
    private Vector<ConnectionAgent> connections;

    public BattleServer(int port) {
        this.game = new Game(10);
        connections = new Vector<>();
        players = new ArrayList<>();
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
                connection.addMessageListener(this);
                connection.sendMessage("\nWelcome to battleship. To begin please enter: " +
                        "/battle <username>");
                connections.add(connection);
                System.out.println("Connection established: " + connection);
                connection.start();
            }catch (IOException ioe){
                System.out.println("ERROR IN BATTLE SERVER");
            }

        }
    }

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
        if (message.equals("/start")){
           if(connections.size() < 2){
               ((ConnectionAgent) source).sendMessage("Not enough players to play the game");
           }else {
               broadcast("The game begins");
           }
        }else if(message.startsWith("/battle")) {
            String name = message.substring(8);
            players.add(new Player(name));
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
        connections.remove((ConnectionAgent) source);
        source.removeMessageListener(this);
    }
}