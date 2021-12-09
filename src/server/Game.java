/*
 * Western Carolina University
 * Fall 2021
 * CS-465-01 - Computer Networks
 * Program 3: Battleship (Multiuser Game)
 * Instructor: Dr. Scott Barlowe
 */

package server;

import client.Player;
import common.ConnectionAgent;

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
    public void play(ArrayList<ConnectionAgent> connectionAgents) throws IOException, ClassNotFoundException {
        int current = -1;
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
                this.shootInput(players.get(i), players.get(next), connectionAgents);
            }
        }
        String str = players.get(current).getName() + " has won!\n";
        str += players.get(0).getName() + "'s board\n" + players.get(0).getGrid().printGridA() + "\n\n";
        str += players.get(1).getName() + "'s board\n" + players.get(1).getGrid().printGridA();
        write(connectionAgents, str.toString());
        write(connectionAgents, null);
    }

    public void write(ArrayList<ConnectionAgent> ca, Object o) throws IOException {
        for (int i = 0; i < ca.size(); i++) {
            ca.get(i).getOos().writeObject(o);
        }
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
     */
    private void shootInput(Player shooter, Player gettingShot, ArrayList<ConnectionAgent> connectionAgents) throws IOException, ClassNotFoundException {
        boolean valid = false;
        int axisX = -1;
        int axisY = -1;
        while (!valid) {
            try {
                write(connectionAgents, shooter.getName() + " it is your turn"); // This is probably the source of all our pain and suffering  <---
                String cords = "";
                for (int i = 0; i < connectionAgents.size(); i++) {
                    if (connectionAgents.get(i).getPlayer().getName() == shooter.getName()) {
                        cords = (String) connectionAgents.get(i).getOis().readObject();
                    }
                }
                //String cords = (String) ois.readObject();
                String[] split = cords.split(" ");
                if (split[0].equals("/display")) {
                    //oos.writeObject(display(split[1]));
                    write(connectionAgents, display(split[1]));
                } else if (split[0].equals("/surrender")) {
                    System.exit(0);
                } else if (split[0].equals("/fire")) {
                    axisX = Integer.parseInt(split[1]);
                    axisY = Integer.parseInt(split[2]);
                    valid = true;
                }

            } catch (InputMismatchException ime) {
                System.out.println("Error: Invalid Input");
                System.exit(3);
            }
        }
        Boolean result = shooter.markHit(gettingShot, axisX, axisY);
        if (result) {
            write(connectionAgents, "Hit!");
        } else {
            write(connectionAgents, "Miss!");
        }
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public String display(String username) {
        String result = "";
        for (int i = 0; i < players.size(); i++) {
            if (username.equals(players.get(i).getName())) {
                result = players.get(i).getGrid().printGridA().toString();
                return result;
            } else {
                result = "Player does not exist!";
            }
        }
        return result;
    }
}