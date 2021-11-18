/**
 * Grid
 * @author Aidan Kirk, David Jennings
 * @version 11/16/21
 */

package server;

import java.util.Random;

// TODO: maybe move ship logic to different file
// TODO: Comment more and javadoc
// TODO: randPlacement funky

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

  public void placeShip(Ship ship, int axisX, int axisY, String direction) throws ArrayIndexOutOfBoundsException {

    int size = ship.getSize();
    switch (direction) {
      case "left":
        if(canPlace(size,axisX,axisY,direction)){
          for (int i = 0; i < size; i++) {
            board[axisY][axisX + i] = ship.getMarker();
          }
        }
        break;
      case "right":
        if(canPlace(size,axisX,axisY,direction)){
          for (int i = 0; i < size; i++) {
            board[axisY][axisX - i] = ship.getMarker();
          }
        }
        break;
      case "up":
        if(canPlace(size,axisX,axisY,direction)){
          for (int i = 0; i < size; i++) {
            board[axisY - i][axisX] = ship.getMarker();
          }
        }
        break;
      case "down":
        if(canPlace(size,axisX,axisY,direction)){
          for (int i = 0; i < size; i++) {
            board[axisY + i][axisX] = ship.getMarker();
          }
        }
        break;
    }
  }

  private boolean canPlace(int shipSize, int xAxis, int yAxis, String direction){
    boolean result = false;
    switch (direction) {
      case "left":
        for (int i = 0; i < shipSize; i++) {
          if (check(xAxis + i, yAxis)) {
            result = true;
          }else{
            result = false;
            break;
          }
        }
        break;
      case "right":
        for (int i = 0; i < shipSize; i++) {
          if (check(xAxis - i, yAxis)) {
            result = true;
          }else{
            result = false;
            break;
          }
        }
        break;
      case "up":
        for (int i = 0; i < shipSize; i++) {
          if (check(xAxis, yAxis - i)) {
            result = true;
          }else{
            result = false;
            break;
          }
        }
        break;
      case "down":
        for (int i = 0; i < shipSize; i++) {
          if (check(xAxis, yAxis + i)) {
            result = true;
          }else{
            result = false;
            break;
          }
        }
        break;
    }

    return result;
  }

  public boolean check(int x, int y) {
    try{
      return board[y][x] == ' ';
    }catch(ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException){
      return false;
    }
  }

  public void randPlacement() {
    int num = amount();
    while (num > 0) {
      Ship ship = randShip();
      int x = randCoordinate();
      int y = randCoordinate();
      String direction = randDirection();
      assert ship != null;
      if(canPlace(ship.getSize(), x, y, direction)){
        placeShip(ship, x, y, direction);
        num--;
      }
    }
  }

  private Ship randShip() {
    Random random = new Random();
    int rand = random.nextInt(5); //the number of types of Ship + 1
    switch (rand) {
      case 0:
        return Ship.CARRIER;
      case 1:
        return Ship.BATTLESHIP;
      case 2:
        return Ship.CRUISER;
      case 3:
        return Ship.SUBMARINE;
      case 4:
        return Ship.DESTROYER;
      default:
        return null;
    }
  }

  public int amount() {
    
    int num = 0;
    if (board.length == 10) {
      num = (int) ((Math.random() * (6 - 4)) + 4);
    } else if (board.length == 9 || board.length == 8) {
      num = (int) ((Math.random() * (5 - 3)) + 3);
    } else if (board.length == 7 || board.length == 6) {
      num = (int) ((Math.random() * (3 - 2)) + 2);
    } else if (board.length == 5) {
      num = (int) ((Math.random() * (2 - 1)) + 1);
    }
    return num;
  }

  public int randCoordinate() {
    Random random = new Random();
    return random.nextInt(board.length);
  }

  public String randDirection() {
    Random random = new Random();
    int num = random.nextInt(5);
    String direction = "";
    if (num == 0) {
      direction = "left";
    } else if (num == 1) {
      direction = "right";
    } else if (num == 2) {
      direction = "up";
    } else if (num == 3) {
      direction = "down";
    }
    return direction;
  }

  public static void main(String[] args) {
    Grid grid = new Grid(10);    // Output 9x9 game board
    grid.randPlacement();
    grid.placeShip(Ship.BATTLESHIP, 0, 1, "down");
    grid.print();                     // Prints game board
  }
}