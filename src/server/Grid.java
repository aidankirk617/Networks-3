/**
 * BattleClient
 * @author Aidan Kirk, David Jennings
 * @version 11/12/21
 */

package server;

public class Grid {

  private int level;
  private int boardSize;
  private int[][] gameBoard;
  private int shipSize;
  private int shipMarker;
  private String shipString;

  public static final int NUM_ROWS = 10;
  public static final int NUM_COLS = 10;

  public void fillBoard() {
    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        gameBoard[i][j] = 0;
      }
    }
  }

  public void printToScreen() {

    System.out.print("   ");
    for (int i = 0; i < gameBoard[0].length; i++) {
      System.out.println("+---+---+---+---+---+---+---+---+---+---+");
      System.out.println("|");
      System.out.printf("%3d", i + 1);
    }
  }
}