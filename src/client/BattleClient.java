package client;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

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
        Player p = new Player(this.username);
        Socket sock;
        Scanner scan = new Scanner(System.in);
        try {
            //output objs
            sock = new Socket(this.host, this.port);
            PrintStream out = new PrintStream(sock.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(out);

            //input objs
            InputStream is = sock.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            //make a connectionAgent
            ConnectionAgent cA = new ConnectionAgent(sock, oos, ois, scan, p);

            //send player info once connected
            cA.sendObject(cA.getPlayer());

            //run
            cA.run();
        } catch (IOException e) {
            System.out.println("ERROR");
            System.exit(2);
        }

    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) throws IOException {
        //Not sure if this is how the message receiving is supposed to work, but I need the
        // connection agents to be working in order to test it. Same with the method below
        source.addMessageListener(this);
        this.send(message);
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

    public void send(String message) throws IOException {
        connection.sendMessage(message);
    }
}