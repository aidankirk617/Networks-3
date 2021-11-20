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
        System.out.println("Size of Grids: ");
        int gridSize = scan.nextInt();

        Player player1 = new Player(player1Name, 0, gridSize);
        Player player2 = new Player(player2Name, 1, gridSize);

        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
    }

    public void playTwoPlayerGame(){
        Scanner scanner = new Scanner(System.in);
        while(!win()){
            for(int i = 0; i < players.size(); i++){
                int nextPlayer;
                if(i == 0){
                    nextPlayer = 1;
                }else{
                    nextPlayer = 0;
                }
                System.out.println(players.get(i).getName() + " it is your turn");
                System.out.println("Choose target x: ");
                int x = scanner.nextInt();
                System.out.println("Choose target y: ");
                int y = scanner.nextInt();
                Player.markHit(players.get(nextPlayer), x, y);
            }
        }
    }

    private boolean win(){
        return false;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.playTwoPlayerGame();
    }
}
