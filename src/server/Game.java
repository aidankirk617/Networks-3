/*
 * Western Carolina University
 * Fall 2021
 * CS-465-01 - Computer Networks
 * Program 3: Battleship (Multiuser Game)
 * Instructor: Dr. Scott Barlowe
 */

package server;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    ArrayList<Player> players;

    public Game() {
        twoPlayerLocalSetup();
    }

    private void twoPlayerLocalSetup(){
        //initialize game with user input
        Scanner scan = new Scanner(System.in);
        System.out.println("Player 1 name: ");
        String player1Name = scan.next();
        System.out.println("Player 2 name: ");
        String player2Name = scan.next();

        //default invalid grid size to go into loop
        int gridSize = -1;
        while (gridSize < 0 || gridSize > 10) {
            try {
                System.out.println("Size of Grids: ");
                gridSize = scan.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter a valid integer (0 - 10)");
                scan.next();
            }
        }

        Player player1 = new Player(player1Name, 0, gridSize);
        Player player2 = new Player(player2Name, 1, gridSize);

        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
    }

    public void playTwoPlayerGame(){
        Scanner scanner = new Scanner(System.in);
        int boardSize = players.get(0).getGrid().getLength();
        int turn = -1;
        while(!win()){

            for(int i = 0; i < players.size(); i++){
                int nextPlayer;
                if(i == 0){
                    turn = 0;
                    nextPlayer = 1;
                }else{
                    turn = 1;
                    nextPlayer = 0;
                }
                System.out.println(players.get(i).getName() + " it is your turn");

                //default values to invalid x and y to go into loop
                int x = -1;
                int y = -1;
                while ((x < 0 || x > boardSize) || (y < 0 || y > boardSize)) {
                    try {
                        System.out.println("Choose target x (0-" + boardSize + "): ");
                        x = scanner.nextInt();
                        System.out.println("Choose target y (0-" + boardSize + "): ");
                        y = scanner.nextInt();
                    } catch (Exception e) {
                        System.out.println("Error: enter a valid integer between 0 and " + boardSize);
                        scanner.next();
                    }
                }

                Player.markHit(players.get(nextPlayer), x, y);
                System.out.println(players.get(i).toString());
                System.out.println(players.get(nextPlayer));
            }
        }

        System.out.println(players.get(turn).getName() + " has won! \n\tRESULTS:\n\n\n");
        System.out.println(players.get(0));
        System.out.println("\n\n\n");
        System.out.println(players.get(1));
    }

    private boolean win(){
        boolean result = false;

        for(Player player : players){

            boolean foundShip = false;
            String gridString = String.valueOf(player.gridA());

            for(char character : gridString.toCharArray()){
                if (character == 'C' || character == 'B' || character == 'R' || character == 'S' ||
                        character == 'D') {

                    foundShip = true;
                    break;
                }
            }

            if (!foundShip) {
                result = true;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.playTwoPlayerGame();
    }
}
