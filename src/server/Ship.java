/**
 * Ship
 * @author Aidan Kirk, David Jennings
 * @version 11/16/21
 */

package server;

public enum Ship {
    CARRIER('C', 2, 0),
    BATTLESHIP('B', 3, 1),
    CRUISER('R', 4, 2),
    SUBMARINE('S', 3, 3),
    DESTROYER('D', 5, 4),
    BLANK(' ', -1,  -1);

    private final int size;
    private final int value;
    private final char marker;

    private Ship(char marker, int size, int value) {
        this.marker = marker;
        this.size = size;
        this.value = value;
    }

    public char getMarker() {
        return marker;
    }

    public int getSize() {
        return size;
    }

    public int getValue() {
        return value;
    }
}