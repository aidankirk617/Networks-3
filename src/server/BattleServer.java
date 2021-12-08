package server;

import client.Player;
import common.MessageListener;
import common.MessageSource;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BattleServer implements MessageListener {
    private ServerSocket serverSocket;
    private int current;
    private Game game;
    private int size;

    public BattleServer(int port, int size) {
        this.game = new Game(size);
        this.size = size;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ioe) {
            System.out.println("Error: IO Exception " + ioe.getMessage());
            System.exit(1);
        }

    }

    public void listen() {
        try {
            //input objects
            Socket sock = serverSocket.accept();
            InputStream is = sock.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            //output objects
            OutputStream os = sock.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);

            //receive the player
            Player p1 = (Player) ois.readObject();
            p1.gridGen(size);
            game.addPlayer(p1);
            System.out.println("Player " + p1.getName() + " connected");

            //receive mock player
            Player p2 = (Player) ois.readObject();
            p2.gridGen(size);
            game.addPlayer(p2);
            System.out.println("Player " + p2.getName() + " connected");

            //receive input to start game/close
            String command = (String) ois.readObject();
            if (command.equals("/start")) {
                game.play(oos, ois);
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