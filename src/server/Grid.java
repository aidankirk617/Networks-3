/**
 * Grid
 * @author Aidan Kirk, David Jennings
 * @version 11/16/21
 */

package server;

import java.util.Random;
import java.util.Scanner;

/** TODO
 * Note to David: Either put ship data in this file, or make separate standalone file
 */

public class Grid {

  private char[][] board;   // Game board used to play battleship
  private int size;         // Size of the game board

  // Constructor
  public Grid(int size) {
    // Initialize Gameboard
    board = new char[size][size]; // Take size of array
    this.size = size;

    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = ' ';   // Initialize length of array
      }
    }
  }

  public Grid(int size, char init) {

    board = new char[size][size];   // Possibly go back and just make this size 10?
    this.size = size;

    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = init;   // Populate board with char init
      }
    }
  }

  public void print() {
    //holds
    System.out.print("    ");
    String graphic = "  ";

    for (int i = 0; i < board.length; i++) {
      System.out.print(i + "   ");
      graphic += "+---";    // Graphic for the game board
    }

    graphic += "+";         // Graphic for the game board
    System.out.println();

    for (int i = 0; i < board.length; i++) {
      System.out.println(graphic);
      System.out.print(i + " ");
      for (int j = 0; j < board[i].length; j++) {
        System.out.print("| " + board[i][j] + " ");
      }
      System.out.print("|");
      System.out.println();
    }
    System.out.println(graphic);
  }

  public static void main(String[] args) {
    Grid grid = new Grid(10);    // Output 9x9 game board
    grid.print();                     // Prints game board
  }
}