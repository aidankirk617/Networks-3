/**
 * BattleClient
 * @author Aidan Kirk, David Jennings
 * @version 11/12/21
 */

package server;

public class Game {
    Grid[] grids;
    String[][] ships;
    String[] players;

    public static final String hit = "X";
    public static final String miss = "O";

    public Game(Grid[] grid, String[][] ships) {
        this.grids = grid;
        this.ships = ships;
    }

    //------------------------------------{ Getters + Setters }-------------------------------------

    public Grid[] getGrids() {
        return grids;
    }

    public void setGrids(Grid[] grids) {
        this.grids = grids;
    }

    public String[][] getShips() {
        return ships;
    }

    public void setShips(String[][] ships) {
        this.ships = ships;
    }

    public String[] getPlayers() {
        return players;
    }

    public void setPlayers(String[] players) {
        this.players = players;
    }

    //-------------------------------------{ GAME METHODS }-----------------------------------------

    public void placeShips(){
        /* BASIC LOGIC

            1.Import random number generator within size of board
            2.check length of ship
            3.if length of ship can fit on board, place it
            4.else generate new number
            5. repeat steps 1-4 for all ships


         */
    }

    public void tryHit(){
        /* BASIC LOGIC

            1. check if location of desired hit is on the board
                2. if it exists check if the location contains a ship
                3. if a ship exists, mark hit, otherwise mark miss
            5. if location is not on board or already checked, return with no changes

         */
    }

    public void playGame(){
        /*

            1. Get players
            2. initialize grids
            3. create a loop that exits when only one grid remains
            4. iterate through players asking them to attack another
            5. check grids to see if all ships have been hit on a particular grid

         */
    }
}
