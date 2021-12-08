/*
 * Western Carolina University
 * Fall 2021
 * CS-465-01 - Computer Networks
 * Program 3: Battleship (Multiuser Game)
 * Instructor: Dr. Scott Barlowe
 */

package common;

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
    public void messageReceived(String message, MessageSource source);

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    public void sourceClosed(MessageSource source);
}
