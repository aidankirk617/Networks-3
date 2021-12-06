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

            //send player info once connected
            oos.writeObject(p);

            //send mock player
            oos.writeObject(new Player("P2"));

            //Take input
            String input = scan.next();
            //if its start, write 1 to signal start
            if (input.equals("/start")) {
                oos.writeObject(input);
            } else {
                oos.writeObject("stinky");
            }
            boolean end = true;
            while (end) {
                String str = (String) ois.readObject();
                if (str != null && !str.contains("shoot")) {
                    System.out.println(str);
                } else if (str != null && str.contains("shoot")) {
                    System.out.println(str);
                    oos.writeObject(scan.next());
                    oos.writeObject(scan.next());
                    //String coords = scan.next();
                    //oos.writeObject(coords);
                } else {
                    end = false;
                }
            }


        } catch (IOException | ClassNotFoundException ioe) {
            System.out.println("Error: IO Exception " + ioe.getMessage());
            System.exit(2);
        }
        //ConnectionAgent client = new ConnectionAgent(sock, p);

    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) {

    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {

    }

    public void send(String message) {

    }
}