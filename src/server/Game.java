/**
 * Game
 * Game contains the logic for the game of BattleShip. It has a Grid for each client.
 * @author Aidan Kirk, David Jennings
 * @version 12/11/21
 */

package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
    /**An arraylist of players that are participating in this game**/
    ArrayList<Player> players;

    int gridSize;   // Size of the battleship game grid

    /**
     * Creates a game with 2 players
     * @param gridSize the size of grid to play with
     * @param p1 public Player(String username) {
        this.username = username;
    } the name of the first player
     * @param p2Name the name of the second player
     */

    public Game(int gridSize) {
        players = new ArrayList<>();    // Register and keep track of players in an array list
        this.gridSize = gridSize;       // Initialize grid size
    }

    /**
     * Helper method to check if any players have won
     * @return true if a player has won, false otherwise
     */
    private boolean checkWin() {
        // Keep result false unless win conditions are met
        boolean result = false;
        for (Player player : players) {
            char[][] pGrid = player.getGrid().getGrid();
            boolean isShip = false;
            for (char[] chars : pGrid) {
                for (char positionChar : chars) {
                    // Check all spots for ship types
                    if (positionChar == 'C' || positionChar == 'B' || positionChar == 'R' ||
                            positionChar == 'S' || positionChar == 'D') {
                        isShip = true;
                        break;
                    }
                }
            }
            if (!isShip) { // If no ship is found, return a win
                result = true;
            }
        }
        return result;  // Otherwise, we still have ships
    }

    /**
     * Plays through a game of battleship
     */
    public void play(ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        int current = -1;   // ???
        Scanner scan  = new Scanner(System.in);
        while (!checkWin()) {
            for (int i = 0; i < players.size(); i++) {
                int next;
                if (i == 0) {
                    current = 0;
                    next = 1;
                } else {
                    current = 1;
                    next = 0;
                }
                oos.writeObject(buildPrompt(players.get(i), players.get(next)));
                this.shootInput(players.get(i), players.get(next), scan, oos, ois);
            }
        }
        String str = players.get(current).getName() + " has won!\n";    // Announce this games winner
        str += players.get(0).getName() + "'s board\n" + players.get(0).getGrid().printGridA() + "\n\n";
        str += players.get(1).getName() + "'s board\n" + players.get(1).getGrid().printGridA();
        oos.writeObject(str);
        oos.writeObject(null);
    }

    /** Builds the prompt for players **/
    private String buildPrompt(Player p1, Player p2) {
        String str = p1.getName() + " it is your turn\n";   // Prompt player for turn
        str += p2.getName() + "'s grid: \n";    // Message for displaying enemies grid
        str += p2.getGrid().printGridA() + "\n";    // Display both grids
        return str;
    }

    /**
     * Helper method to detect valid input for shooting and to shoot
     * @param shooter The player who is shooting
     * @param gettingShot The player who is getting shot
     * @param scan A scanner to get input with
     */
    private void shootInput(Player shooter, Player gettingShot, Scanner scan, ObjectOutputStream oos,  ObjectInputStream ois) throws IOException, ClassNotFoundException {
        boolean valid = false;
        int xCord = -1;
        int yCord = -1;
        oos.writeObject("Please type your numeric x and y coordinates to shoot > ");
        while (!valid) {
            // Loop as long as it's still the players turn
            try {
                String x = (String) ois.readObject();
                String y = (String) ois.readObject();
                xCord = Integer.parseInt(x);
                yCord = Integer.parseInt(y);
                // It is no longer the players turn
                valid = true;
            } catch (InputMismatchException ime) {  // If invalid input is provided, throw an InputMisMatchException
                System.out.println("Error: Invalid Input");
                System.exit(3);     // Exit with error code 3
            }
        }
        Boolean result = shooter.markHit(gettingShot, xCord, yCord);
        if (result) {
            oos.writeObject("Hit!");    // Mark coordinates with a hit
        } else {
            oos.writeObject("Miss!");   // Mark coordinates with a miss
        }
    }
}