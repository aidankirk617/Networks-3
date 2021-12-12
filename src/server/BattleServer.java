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
    private ArrayList<Integer> eliminated;
    private boolean gameInProgress;

    public BattleServer(int port) {
        this.game = new Game(5);
        connections = new Vector<>();
        players = new ArrayList<>();
        gameInProgress = false;
        eliminated = new ArrayList<>();
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

    private void leaveGame(ConnectionAgent source, Player currentPlayer) {
        broadcast(currentPlayer.getName() + " " +
                "has left the game.");
        players.remove(connections.indexOf(source));
    }

    private void joinGame(String message, ConnectionAgent source) {
        if(gameInProgress){
            source.sendMessage("Game already in progress.");
            return;
        }
        String name = message.substring(8);
        players.add(new Player(name));
        broadcast("!!! " + name + " has entered battle");
    }

    private void startGame(ConnectionAgent source) {
        if(gameInProgress){
            source.sendMessage("Game already in progress.");
            return;
        }

        if(players.size() < 2){
            source.sendMessage("Not enough players to play the game");

        }else {
            broadcast("The game begins");
            for (Player player : players){
                game.addPlayer(player);
            }
            game.generate();
            gameInProgress = true;
        }
    }

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
                source.sendMessage("Player not found, check name and try again.");
            }
        }
    }

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

    private void invalidInput(ConnectionAgent source) {
        String invalidInput = "Unknown input. Available commands:\n" +
                "start\n" +
                "fire <[0-"+(game.gridSize-1)+"]> <[0-"+(game.gridSize-1)+"]> <username>\n" +
                "surrender\n" +
                "display <username>\n" +
                "remember: all commands must begin with /";

        source.sendMessage(invalidInput);
    }

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
        connections.remove((ConnectionAgent) source);
        source.removeMessageListener(this);

    }
}