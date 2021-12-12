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

        Player currentPlayer = null;

        if(players.size() < connections.size()){
            if(!message.startsWith("/battle")){
                ((ConnectionAgent) source).sendMessage("Please join by entering: /battle <username>");
                return;
            }
        }else{
            currentPlayer = players.get(connections.indexOf((ConnectionAgent) source));
        }

        assert currentPlayer != null;

        if (message.equals("/start")){
           if(players.size() < 2){
               ((ConnectionAgent) source).sendMessage("Not enough players to play the game");

           }else {
               broadcast("The game begins");
               for (Player player : players){
                   game.addPlayer(player);
               }
               game.generate();
           }

        }else if(message.startsWith("/battle")) {
            String name = message.substring(8);
            players.add(new Player(name));
            broadcast("!!! " + name + " has entered battle");

        }else if(message.equals("/surrender")){
            broadcast(currentPlayer.getName() + " " +
                    "has left the game.");
            players.remove(connections.indexOf((ConnectionAgent) source));
            broadcast("Players remaining: " + players.size());

        }else if(message.startsWith("/display")){
            String name = message.substring(9);

            for(Player player : players){
                if(player.getName().equals(name)){
                    if(player.getName().equals(currentPlayer.getName())){
                        ((ConnectionAgent) source).sendMessage(currentPlayer.gridA());
                    }else {
                        ((ConnectionAgent) source).sendMessage(currentPlayer.gridB(player));
                    }
                    break;
                }else {
                    ((ConnectionAgent) source).sendMessage("Player not found, check name and try again.");
                }
            }

        }else if(message.startsWith("/fire")){

            int x;
            int y;
            try {
                x = Integer.parseInt(message.substring(6, 7));
                y = Integer.parseInt(message.substring(8, 9));
            }catch (NumberFormatException numberFormatException){
                String invalid = "Invalid target: please enter integers between 0 and " + (game.gridSize-1);
                ((ConnectionAgent) source).sendMessage(invalid);
                return;
            }
            broadcast(game.fire(currentPlayer.getName(), message.substring(10), x, y));
            if(game.checkElimination(message.substring(10))){
                broadcast(message.substring(10) + " is eliminated by " + currentPlayer.getName());

            }
            if(game.checkWin()){
                broadcast("GAME OVER " + currentPlayer.getName() + " wins!");
            }
        }else{
            String invalidInput = "Unknown input. Available commands:\n" +
                    "start\n" +
                    "fire <[0-"+(game.gridSize-1)+"]> <[0-"+(game.gridSize-1)+"]> <username>\n" +
                    "surrender\n" +
                    "display <username>" +
                    "remember: all commands must begin with /";

            ((ConnectionAgent) source).sendMessage(invalidInput);
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
        broadcast("Connections: " + connections.size());
    }
}