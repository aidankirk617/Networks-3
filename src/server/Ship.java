/*
 * Western Carolina University
 * Fall 2021
 * CS-465-01 - Computer Networks
 * Program 3: Battleship (Multiuser Game)
 * Instructor: Dr. Scott Barlowe
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