/*
 * Western Carolina University
 * Fall 2021
 * CS-465-01 - Computer Networks
 * Program 3: Battleship (Multiuser Game)
 * Instructor: Dr. Scott Barlowe
 */

package server;

import client.Player;
import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class BattleServer implements MessageListener {
    private ServerSocket serverSocket;
    private int current;
    private Game game;
    private int size;
    private ArrayList<ConnectionAgent> connections;

    public BattleServer(int port, int size) {
        this.game = new Game(size);
        this.size = size;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ioe) {
            System.out.println("Error: IO Exception " + ioe.getMessage());
            System.exit(1);
        }
        connections = new ArrayList<>();
    }

    public void listen() {
        try {
            boolean done = false;
            while (!done) {
                Socket sock = serverSocket.accept();
                OutputStream os = sock.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                InputStream is = sock.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                Scanner scan = new Scanner(System.in);
                Player p = (Player) ois.readObject();
                p.gridGen(size);
                game.addPlayer(p);
                ConnectionAgent ca = new ConnectionAgent(sock, oos, ois, scan, p);
                connections.add(ca);
                Thread t = new Thread(ca);
                System.out.println("Player " + p.getName() + " connected");
                t.start();
                if (game.players.size() >= 2) {
                    //receive input to start game/close
                    String command = (String) connections.get(0).getOis().readObject(); //only p1 can start the game
                    if (command.equals("/start")) {
                        for (int i = 0; i < connections.size(); i++) {
                            connections.get(i).getOos().writeObject("The game begins");
                        }
                        game.play(connections);
                        done = true;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException ioe) {
            System.out.println("Error: IO Exception " + ioe.getMessage());
            System.exit(2);
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