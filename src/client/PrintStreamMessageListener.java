/**
 * PrintStreamMessageListener
 *
 * Responsible for writing messages to a PrintStream.
 * The class implements the MessageListener interface, indicating that it plays the role of “observer”
 * in an instance of the observer pattern.
 * @author Aidan Kirk, David Jennings
 * @version 12/11/21 
 */

package client;

import common.MessageListener;
import common.MessageSource;
import java.io.PrintStream;

/** **/
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
        source.addMessageListener(this);
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