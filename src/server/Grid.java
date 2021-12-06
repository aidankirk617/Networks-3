/*
 * Western Carolina University
 * Fall 2021
 * CS-465-01 - Computer Networks
 * Program 3: Battleship (Multiuser Game)
 * Instructor: Dr. Scott Barlowe
 */

package server;

import client.Player;

import java.util.Arrays;
import java.util.Random;

// TODO: maybe move ship logic to different file

/**
 * <h2>This class will represent the grid in a game of battleship.</h2>
 *
 * <p>The class will hold the board which is an 2d array, and the board will contain ships,
 * along with the attempted (or successful) hits from other players. The grid can generate a board
 * with random ships on random spaces with random directions. The grid can also be displayed to the
 * console.</p>
 *
 * @see server.Game
 * @see server.Ship
 * @author Aidan Kirk
 * @author David Jennings
 * @version November 18 2021
 * @since 1.0
 */
public class Grid {

  /**
   * This will be the 2d array that represents the actual board of battleship, the contents of each
   * element in the array will contain either a ship, a hit, a miss, or an empty space.
   */
  private final char[][] board;

  /**
   * Represents the size of the board. Boards will always be square.
   */
  private final int size;

  /**
   * Constructor for the grid. Takes one size parameter and uses that to make the 2d array.
   * @param size the length and height of the board to be generated.
   */
  public Grid(int size) {
    // Initialize Game board

    this.size = size; // Initialize length of array

    board = new char[this.size][this.size]; // Take size of array

    for (char[] chars : board) {
      Arrays.fill(chars, ' ');
    }

    randPlacement(); // Place ships randomly on board
  }

  /**
   * Places a ship on the board. By checking the location and the potential placement, a ship will
   * only be placed in valid locations.
   * @param ship the ship to be placed.
   * @param axisX the x coordinate for the ship to be placed at.
   * @param axisY the y coordinate for the ship to be placed at.
   * @param direction the direction: up, down, left, right.
   * @throws ArrayIndexOutOfBoundsException if the presented x and y coordinates are not in bounds.
   */
  public void placeShip(Ship ship, int axisX, int axisY, String direction)
          throws ArrayIndexOutOfBoundsException {

    int size = ship.getSize(); // size of ship to be placed

    //Switch on direction: up, down, left, right. Checks every direction to look at spaces.
    switch (direction) {
      case "left":
        if(canPlace(size,axisX,axisY,direction)){ // if the ship can be placed, begin placing
          for (int i = 0; i < size; i++) {
            board[axisY][axisX + i] = ship.getMarker(); // place marker for the board at each spot
          }
        }
        break;
      case "right":
        if(canPlace(size,axisX,axisY,direction)){ // if the ship can be placed, begin placing
          for (int i = 0; i < size; i++) {
            board[axisY][axisX - i] = ship.getMarker(); // place marker for the board at each spot
          }
        }
        break;
      case "up":
        if(canPlace(size,axisX,axisY,direction)){ // if the ship can be placed, begin placing
          for (int i = 0; i < size; i++) {
            board[axisY - i][axisX] = ship.getMarker(); // place marker for the board at each spot
          }
        }
        break;
      case "down":
        if(canPlace(size,axisX,axisY,direction)){ // if the ship can be placed, begin placing
          for (int i = 0; i < size; i++) {
            board[axisY + i][axisX] = ship.getMarker(); // place marker for the board at each spot
          }
        }
        break;
    }
  }

  /**
   * Checks to see if the location is available for a ship to be placed at, given a size and location.
   * @param shipSize number of spaces to check.
   * @param xAxis x coordinate to start at.
   * @param yAxis y coordinate to start at.
   * @param direction direction to check.
   * @return true if the ship can be placed at the location, false otherwise.
   */
  private boolean canPlace(int shipSize, int xAxis, int yAxis, String direction){

    boolean result = false;

    //Switch on direction: up, down, left, right. Checks every direction to look at spaces.
    switch (direction) {
      case "left":
        for (int i = 0; i < shipSize; i++) {
          if (check(xAxis + i, yAxis)) { //if the space is empty, result is true
            result = true;
          }else{
            result = false; // if the space is not empty, result is false, break from loop and return.
            break;
          }
        }
        break;
      case "right":
        for (int i = 0; i < shipSize; i++) {
          if (check(xAxis - i, yAxis)) { //if the space is empty, result is true
            result = true;
          }else{
            result = false; // if the space is not empty, result is false, break from loop and return.
            break;
          }
        }
        break;
      case "up":
        for (int i = 0; i < shipSize; i++) {
          if (check(xAxis, yAxis - i)) { //if the space is empty, result is true
            result = true;
          }else{
            result = false; // if the space is not empty, result is false, break from loop and return.
            break;
          }
        }
        break;
      case "down":
        for (int i = 0; i < shipSize; i++) {
          if (check(xAxis, yAxis + i)) { //if the space is empty, result is true
            result = true;
          }else{
            result = false; // if the space is not empty, result is false, break from loop and return.
            break;
          }
        }
        break;
    }

    return result;
  }

  /**
   * Checks to see if a space is empty.
   * @param x coordinate to check in the x direction.
   * @param y coordinate to check in the y direction.
   * @return true if the location is empty, false otherwise.
   */
  public boolean check(int x, int y) {
    try{
      return board[y][x] == ' ';
    }catch(ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException){
      return false;
    }
  }

  /**
   * Places random amount of ships on the board with random coordinates and directions.
   */
  public void randPlacement() {

    int num = amount(); //number of ships to be placed

    while (num > 0) {

      Ship ship = randShip(); // generate random ship to be placed

      int x = randCoordinate(); //generate random coordinate to represent x-axis
      int y = randCoordinate(); //generate random coordinate to represent y-axis

      String direction = randDirection(); // generate random direction for ship to be placed

      assert ship != null;
      if(canPlace(ship.getSize(), x, y, direction)){
        placeShip(ship, x, y, direction);
        num--; // only decrement num when a ship is successfully placed
      }
    }
  }

  /**
   * Generates a random number from 0-4 which will map to a ship which will be returned.
   * @return Ship from switch case
   */
  private Ship randShip() {
    Random random = new Random();
    int rand = random.nextInt(5); //the number of types of Ship + 1 (to account for 0)
    if (rand == 0) {
      return Ship.CARRIER;
    } else if (rand == 1) {
      return Ship.BATTLESHIP;
    } else if (rand == 2) {
      return Ship.CRUISER;
    } else if (rand == 3) {
      return Ship.SUBMARINE;
    } else { // if number is not 0-3 it must be 4
      return Ship.DESTROYER;
    }
  }

  /**
   * Generate the amount of ships to be generated on the board.
   * Note: Math.random() * (max - min) + min -- where max is exclusive.
   * @return random number between predetermined range for each board size.
   */
  public int amount() {

    int num = 0;

    if (board.length == 10) { // if board is 10 x 10 : number between 4-6
      num = (int) ((Math.random() * (7 - 4)) + 4);
    } else if (board.length == 9 || board.length == 8) { //if board is 8x8 or 9x9 : number between 4-6
      num = (int) ((Math.random() * (6 - 3)) + 3);
    } else if (board.length == 7 || board.length == 6) { //if board is 7x7 or 6x6 : number between 4-6
      num = (int) ((Math.random() * (4 - 2)) + 2);
    } else if (board.length == 5) { // if board is 5 x 5 : number between 4-6
      num = (int) ((Math.random() * (3 - 1)) + 1);
    }
    return num;
  }

  /**
   * Generates a random coordinate within the range of the board.
   * @return a random number within the range of the size of the board.
   */
  public int randCoordinate() {
    Random random = new Random();
    return random.nextInt(size);
  }

  /**
   * Generates a string holding a random direction: up, down, left, right.
   * @return a string holding a random direction: up, down, left, right.
   */
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

  public int getLength() {
    return this.board.length;
  }

  public char getSpace(int x, int y){
    if( x > board.length || x < 0){
      System.out.println("PLEASE ENTER VALID X COORDINATE");
      return '!';
    }if( y > board.length || y < 0){
      System.out.println("PLEASE ENTER VALID Y COORDINATE");
      return '!';
    }
    return board[x][y];
  }

  public void setSpace(char value, int x, int y){
    if( x > board.length || x < 0){
      System.out.println("PLEASE ENTER VALID X COORDINATE");
    }else if( y > board.length || y < 0){
      System.out.println("PLEASE ENTER VALID Y COORDINATE");
    }else
      board[x][y] = value;
  }

  /**
   * Prints the current board from the perspective of the player who controls it.
   * @return
   */
  public StringBuilder printGridA() {

    StringBuilder graphic = new StringBuilder("  ");
    StringBuilder result = new StringBuilder("   ");

    for (int i = 0; i < board.length; i++) {
      result.append(i).append("   ");
      graphic.append("+---");
    }
    graphic.append("+");
    result.append('\n');

    for (int i = 0; i < board.length; i++) {
      result.append(graphic);
      result.append('\n').append(i);
      for (int j = 0; j < board[i].length; j++) {
        result.append(" | ").append(board[i][j]);
      }
      result.append(" |");
      result.append('\n');
    }
    result.append(graphic);
    return result;
  }

  /** Print Player 2's Grid **/
  public StringBuilder printGridB() {
    //holds
    StringBuilder graphic;
    graphic = new StringBuilder("  ");
    StringBuilder result;
    result = new StringBuilder("   ");

    for (int i = 0; i < board.length; i++) {
      result.append(i).append("   ");
      graphic.append("+---");
    }
    graphic.append("+");
    result.append('\n');

    for (int i = 0; i < board.length; i++) {
      result.append(graphic);
      result.append('\n').append(i);
      for (int j = 0; j < board[i].length; j++) {
        char hold = board[i][j];
        if (hold == 'C' || hold == 'B' || hold == 'R' ||
                hold == 'S' || hold == 'D') {
          result.append(" |  ");
        } else {
          result.append(" | ").append(board[i][j]);
        }
      }
      result.append(" |");
      result.append('\n');
    }
    result.append(graphic);
    return result;
  }

  public char[][] getGrid() {
    return board;
  }

  /** Main **/
  public static void main(String[] args) {
//    Scanner scanner = new Scanner(System.in);
//    System.out.println("What board size would you like (5-10): ");
//    int boardSize = scanner.nextInt();
//    while (boardSize < 5 || boardSize > 10){
//      System.out.println("Please enter a valid number (5-10): ");
//      boardSize = scanner.nextInt();
//    }
//    Grid grid = new Grid(boardSize);
//    grid.randPlacement();
//    System.out.println(grid.printGridA() + "\n\n\n");
//    grid.printGridB();

    Player david = new Player("David", 0, 10);
    System.out.println(david);
    david.markHit(david, 3, 3);
    System.out.println(david);
    david.markHit(david, 3, 4);
    System.out.println(david);
    david.markHit(david, 3, 5);
    System.out.println(david);
    david.markHit(david, 5, 4);
    System.out.println(david);
  }
}