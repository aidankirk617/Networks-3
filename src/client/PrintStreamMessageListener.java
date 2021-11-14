/**
 * BattleClient
 * @author Aidan Kirk, David Jennings
 * @version 11/12/21
 */

package client;

import common.MessageListener;
import common.MessageSource;

import java.io.PrintStream;

public class PrintStreamMessageListener implements MessageListener {

    private PrintStream out;

    public PrintStreamMessageListener(PrintStream out) {

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
