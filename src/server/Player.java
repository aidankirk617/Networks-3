package server;

public class Player {

    /** Fields **/
    private String name;        // The Players Name
    private boolean winner;     // Determines if the player has won or not
    private int turn;           // Determines if it is the players turn or not
    private Grid grid;           // Determines the players grid

    // Initialize values for the player class
    public Player(){
        this.name = new String();
        this.winner = false;
        this.turn = 0;
    }

    /** Constructor for getter methods **/
    public Player(String name, int turn, int size){
        this.name = name;
        this.turn = turn;
        this.grid = new Grid(size);
    }

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

    /** Getter method for player turn **/
    public int getTurn(){
        return turn;
    }

    /** Getter for player name **/
    public String getName(){
        return name;
    }

    /** Getter for players grid **/
    public Grid getGrid(){
        return grid;
    }

    /** Print Player 1's Grid **/
    public StringBuilder gridA() {
        return this.grid.printGridA();
    }

    /** Print Player 2's Grid **/
    public void gridB(Player player) {
        player.grid.printGridB();
    }

    public static Boolean markHit(Player player, int axisX, int axisY) {

        if (axisX >= player.getGrid().getLength() || axisX < 0 ||   // Check Bounds
                axisY >=player.getGrid().getLength() || axisY < 0) {
            return false;
        }
        char hold = player.grid.getSpace(axisX, axisY);     // Check hit
        if (hold == 'C' || hold == 'B' || hold == 'R' ||
                hold == 'S' || hold == 'D') {

            player.grid.setSpace('X', axisX, axisY);              // Set hit
            return true;
        } else if (hold == ' ') {
            //set char there to O for miss
            player.grid.setSpace('O', axisX, axisY);              // Set miss
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Name: " + this.getName() +  "\n\tTurn: " + this.getTurn() + "\n\tGrid: \n" + this.getGrid().printGridA();
    }
}