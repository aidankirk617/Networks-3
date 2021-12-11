/**
 * MessageListener
 * MessageListener defines the interface to objects that can observe other objects that receive messages. When
 * the subject receives a message, the message is forwarded to all registered observers.
 * @author Aidan Kirk, David Jennings
 * @version 12/11/21
 */

package common;

import java.io.IOException;

/**
 * This interface represents observers of MessageSource.
 *
 * @author William Kreahling
 * @version November 2017
 */
public interface MessageListener {
    /**
     * Used to notify observers that the subject has received a message.
     * @param message The message received by the subject
     * @param source The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source) throws IOException;

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    public void sourceClosed(MessageSource source);
}