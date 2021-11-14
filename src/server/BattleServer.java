/**
 * BattleClient
 * @author Aidan Kirk, David Jennings
 * @version 11/12/21
 */
package server;
import common.MessageListener;
import common.MessageSource;
import java.net.ServerSocket;

public class BattleServer implements MessageListener {

    private ServerSocket serverSocket;
    private int current;
    private Game game;

    public BattleServer(int port) {
    }

    public void listen() {

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
}
