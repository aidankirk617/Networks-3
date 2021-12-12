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

public class BattleServer implements MessageListener {

    //initialize fields
    private ServerSocket serverSocket;
    private int current;
    private final Game game;
    private final ArrayList<Player> players;
    private final Vector<ConnectionAgent> connections;
    private final ArrayList<Integer> eliminated;
    private boolean gameInProgress;

    public BattleServer(int port, int size) {
        this.game = new Game(size);
        connections = new Vector<>(); // Store connections in a vector
        players = new ArrayList<>(); // Store list of players in an array
        gameInProgress = false;
        eliminated = new ArrayList<>();
        try {
            this.serverSocket = new ServerSocket(port); // Create server socket
        } catch (IOException ioe) { // If connection fails, run IOException
            System.out.println("Error: IO Exception " + ioe.getMessage());
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
            }catch (IOException ioe){
                System.out.println("ERROR IN BATTLE SERVER");
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
     * Used to notify observers that the subject has received a message. This will check to see
     * if a valid command has been sent, if one has, the message is sent to a helper method
     * to operate game functions.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) {

        Player currentPlayer = null;

        // If player starts the game without enough players, prompt accordingly
        if(players.size() < connections.size()){
            if(!message.startsWith("/battle")){
                ((ConnectionAgent) source).sendMessage("A player has not joined.\n" +
                        "Please join by entering: /battle <username>");
                return;
            }

        }else{
            currentPlayer = players.get(connections.indexOf((ConnectionAgent) source));

        }

        assert currentPlayer != null;

        if (message.equals("/start")){
            startGame((ConnectionAgent) source);

        }else if(message.startsWith("/battle")) {
            joinGame(message, (ConnectionAgent) source);

        }else if(message.equals("/surrender")){
            leaveGame((ConnectionAgent) source, currentPlayer);

        }else if(message.startsWith("/display")){
            display(message, (ConnectionAgent) source, currentPlayer);

        }else if(message.startsWith("/fire")){
            fire(message, (ConnectionAgent) source, currentPlayer);

        }else{
            invalidInput((ConnectionAgent) source);
        }
    }

    /**
     * Helper for leaving a game. Closes the connection and announces that a player has left.
     * @param source player to disconnect.
     * @param currentPlayer player to remove from list.
     */
    private void leaveGame(ConnectionAgent source, Player currentPlayer) {
        broadcast(currentPlayer.getName() + " " +
                "has left the game.");
        players.remove(connections.indexOf(source));
    }

    /**
     * Helper for adding players to a game. Receives the message and adds appropriate username.
     * @param message players command to join along with their username.
     * @param source connection of player joining.
     */
    private void joinGame(String message, ConnectionAgent source) {
        if(gameInProgress){
            source.sendMessage("Game already in progress.");
            return;
        }
        String name = message.substring(8);
        players.add(new Player(name));
        broadcast("!!! " + name + " has entered battle");
    }

    /**
     * Helper for starting the game, given that the right amount of players have joined.
     * @param source player asking to start game.
     */
    private void startGame(ConnectionAgent source) {
        if(gameInProgress){
            source.sendMessage("Game already in progress.");
            return;
        }

        if(players.size() < 2){
            source.sendMessage("Not enough players to play the game");

        }else {
            //Initializing the game
            broadcast("The game begins");
            for (Player player : players){
                game.addPlayer(player);
            }
            game.generate();
            gameInProgress = true;
        }
    }

    /**
     * Helper for displaying players board. If a player wishes to see their own board, their ships
     * will be shown, otherwise they will be hidden.
     * @param message player request for board to view
     * @param source player attempting to display board
     * @param currentPlayer player object of current player
     */
    private void display(String message, ConnectionAgent source, Player currentPlayer) {
        if(!gameInProgress){
            source.sendMessage("Game not in progress.");
            return;
        }
        String name = message.substring(9);

        for(Player player : players){
            if(player.getName().equals(name)){
                if(player.getName().equals(currentPlayer.getName())){
                    source.sendMessage(currentPlayer.gridA());
                }else {
                    source.sendMessage(currentPlayer.gridB(player));
                }
                break;
            }if(players.indexOf(player) == (players.size() - 1)){
                //if this is the last player and no player has been found with given name, alert source
                source.sendMessage("Player not found, check name and try again.");
            }
        }
    }

    /**
     * Helper for firing at a player during a game.
     * @param message request for firing at a player
     * @param source connection of player making request
     * @param currentPlayer player object of current player
     */
    private void fire(String message, ConnectionAgent source, Player currentPlayer) {
        if(!gameInProgress){
            source.sendMessage("Game not in progress.");
            return;
        }
        if(isEliminated(currentPlayer.getName())){
            source.sendMessage("Cannot fire when eliminated.");
            return;
        }
        int x;
        int y;

        try {
            x = Integer.parseInt(message.substring(6, 7));
            y = Integer.parseInt(message.substring(8, 9));

        }catch (NumberFormatException numberFormatException){
            String invalid = "Invalid target: please enter integers between 0 and " + (game.gridSize-1);
            source.sendMessage(invalid);
            return;
        }
        try {
            broadcast(game.fire(currentPlayer.getName(), message.substring(10), x, y));
        }catch (StringIndexOutOfBoundsException sioobe){
            source.sendMessage("Error firing please try again.");
        }
        if(game.checkElimination(message.substring(10))){
            broadcast(message.substring(10) + " is eliminated by " + currentPlayer.getName());
            for(Player player : players){
                if(player.getName().equals(message.substring(10))){
                    eliminated.add(players.indexOf(player));
                    break;
                }
            }
        }
        if(game.checkWin()){
            broadcast("GAME OVER " + currentPlayer.getName() + " wins!");
        }
    }

    /**
     * Helper method to catch unrecognized input
     * @param source source making request with bad input
     */
    private void invalidInput(ConnectionAgent source) {
        String invalidInput = "Unknown input. Available commands:\n" +
                "start\n" +
                "fire <[0-"+(game.gridSize-1)+"]> <[0-"+(game.gridSize-1)+"]> <username>\n" +
                "surrender\n" +
                "display <username>\n" +
                "remember: all commands must begin with /";

        source.sendMessage(invalidInput);
    }

    /**
     * Helper to check if a given player is eliminated
     * @param player name of the player to check
     * @return true if the player has been eliminated, false otherwise
     */
    private boolean isEliminated(String player){
        boolean isEliminated = false;
        int playerIndex = -1;

        for(Player toFind : players){
            if(toFind.getName().equals(player)){
                playerIndex = players.indexOf(toFind);

            }
        }
        for (Integer indexEliminated : eliminated){
            if (indexEliminated == playerIndex) {
                isEliminated = true;
                break;
            }
        }
        return isEliminated;
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