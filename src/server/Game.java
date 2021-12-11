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

    int gridSize;

    /**
     * Creates a game with 2 players
     * @param gridSize the size of grid to play with
     * @param p1 public Player(String username) {
        this.username = username;
    } the name of the first player
     * @param p2Name the name of the second player
     */

    public Game(int gridSize) {
        players = new ArrayList<>();
        this.gridSize = gridSize;
    }

    /**
     * Helper method to check if any players have won
     * @return true if a player has won, false otherwise
     */
    private boolean checkWin() {
        boolean result = false;
        for (Player player : players) {
            char[][] pGrid = player.getGrid().getGrid();
            boolean isShip = false;
            for (char[] chars : pGrid) {
                for (char positionChar : chars) {
                    if (positionChar == 'C' || positionChar == 'B' || positionChar == 'R' ||
                            positionChar == 'S' || positionChar == 'D') {
                        //check every spot for ships
                        isShip = true;
                        break;
                    }
                }
            }
            if (!isShip) { //if we didn't find a ship, then it's a win
                result = true;
            }
        }
        //otherwise, there's still ships left
        return result;
    }

    /**
     * Plays through a game of battleship
     */
    public void play(ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        int current = -1;
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
        String str = players.get(current).getName() + " has won!\n";
        str += players.get(0).getName() + "'s board\n" + players.get(0).getGrid().printGridA() + "\n\n";
        str += players.get(1).getName() + "'s board\n" + players.get(1).getGrid().printGridA();
        oos.writeObject(str);
        oos.writeObject(null);
    }

    private String buildPrompt(Player p1, Player p2) {
        String str = p1.getName() + " it is your turn\n";
        str += p2.getName() + "'s grid: \n";
        str += p2.getGrid().printGridA() + "\n";
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
            try {
                String x = (String) ois.readObject();
                String y = (String) ois.readObject();
                xCord = Integer.parseInt(x);
                yCord = Integer.parseInt(y);
                valid = true;
            } catch (InputMismatchException ime) {
                System.out.println("Error: Invalid Input");
                System.exit(3);
            }
        }
        Boolean result = shooter.markHit(gettingShot, xCord, yCord);
        if (result) {
            oos.writeObject("Hit!");
        } else {
            oos.writeObject("Miss!");
        }
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
}