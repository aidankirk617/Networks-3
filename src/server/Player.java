package server;

public class Player {

    /** Fields **/
    private String name;        // The Players Name
    private boolean winner;     // Determines if the player has won or not
    private int turn;           // Determines if it is the players turn or not
    private int grid;           // Determines the players grid

    // Initialize values for the player class
    public Player(){
        this.name = new String();
        this.winner = false;
        this.turn = 0;
        this.grid = 0;
    }

    /** Constructor for getter methods **/
    public Player(String name, int turn, int grid){
        this.name = name;
        this.turn = turn;
        this.grid = grid;
    }

    /**-------- SETTERS ----------**/

    /** Setter for player name **/
    public void setName(){
        this.name = name;
    }

    /** Setter for winner boolean **/
    public void setWinner(){
        this.winner = winner;
    }

    /** Setter for player turn **/
    public void setTurn(){
        this.turn = turn;
    }

    /** Setter for grid **/
    public void setGrid(){
        this.grid = grid;
    }

    /**-------- GETTERS ----------**/

    /** Getter method for player turn **/
    public int getTurn(){
        return turn;
    }

    /** Getter for player name **/
    public String getName(){
        return name;
    }

    /** Getter for players grid **/
    public int getGrid(){
        return grid;
    }

    @Override
    public String toString() {
       return "Name: " + this.getName() +  "\n\tTurn: " + this.getTurn() + "\n\tGrid: " + this.getGrid();
    }
}
