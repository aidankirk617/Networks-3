/**
 * Ship
 * Enumeration that represents the values of ships in Battleship.
 * @author Aidan Kirk, David Jennings
 * @version 12/11/21
 */

package server;

public enum Ship {
    CARRIER('C', 2, 0),     // Carrier
    BATTLESHIP('B', 3, 1),  // Battleship
    CRUISER('R', 4, 2),     // Cruiser
    SUBMARINE('S', 3, 3),   // Submarine
    DESTROYER('D', 5, 4),   // Destroyer
    BLANK(' ', -1,  -1);

    /** Variables **/
    private final int size;     // Variable for size of ship
    private final int value;    // Variable for value of ship
    private final char marker;  // Variable for marker space

    /** Constructor of ship **/
    private Ship(char marker, int size, int value) {
        this.marker = marker;   // Initialize marker
        this.size = size;       // Initialize size
        this.value = value;     // Initialize value
    }

    /** Getter method for marker **/
    public char getMarker() {   // Get specified marker
        return marker;
    }

    /** Getter method for size **/
    public int getSize() {      // Get specified marker of ship
        return size;
    }

    /** Getter method for value **/
    public int getValue() {     // Get value of ship
        return value;
    }
}