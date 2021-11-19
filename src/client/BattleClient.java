/*
 * Western Carolina University
 * Fall 2021
 * CS-465-01 - Computer Networks
 * Program 3: Battleship (Multiuser Game)
 * Instructor: Dr. Scott Barlowe
 */

package client;

import common.MessageListener;
import common.MessageSource;
import java.net.InetAddress;

public class BattleClient extends MessageSource implements MessageListener {

    private InetAddress host;
    private int port;
    private String username;

    public BattleClient(String hostname, int port, String username) {

    }

    public void connect() {

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
