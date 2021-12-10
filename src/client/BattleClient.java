package client;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class BattleClient extends MessageSource implements MessageListener {

    private InetAddress host;
    private int port;
    private String username;
    private ConnectionAgent connection;

    public BattleClient(String hostname, int port, String username) {
        try {
            this.host = InetAddress.getByName(hostname);
        } catch (UnknownHostException uhe) {
            System.out.println("Error: Incorrect host");
            System.exit(1);
        }
        this.port = port;
        this.username = username;
    }

    public void connect() {
        try {
            Socket socket = new Socket(host, port);
            connection = new ConnectionAgent(socket);
            connection.addMessageListener(this);
            System.out.println("Connected to: " + socket);
            connection.start();
        } catch (IOException e) {
            System.out.println("ERROR");
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
        //Not sure if this is how the message receiving is supposed to work, but I need the
        // connection agents to be working in order to test it. Same with the method below
        System.out.println(message);
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

    public void send(String message) {
        connection.sendMessage(message);
    }
}