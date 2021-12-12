package server;

import java.util.ArrayList;

public class Game {
    /**An arraylist of players that are participating in this game**/
    ArrayList<Player> players;

    int gridSize;

    /**
     *
     */

    public Game(int gridSize) {
        players = new ArrayList<>();
        this.gridSize = gridSize;
    }

    /**
     * Helper method to check if any players have won
     * @return true if a player has won, false otherwise
     */
    public boolean checkWin() {
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

    public boolean checkElimination(String name){
        Player toCheck = null;
        for(Player player : players){
            if(player.getName().equals(name)){
                toCheck = player;
            }
        }
        boolean isEliminated = true;
        assert toCheck != null;
        for (char[] chars : toCheck.getGrid().getGrid()) {
            for (char positionChar : chars) {
                if (positionChar == 'C' || positionChar == 'B' || positionChar == 'R' ||
                        positionChar == 'S' || positionChar == 'D') {
                    //check every spot for ships
                    isEliminated = false;
                    break;
                }
            }
            if(!isEliminated){
                break;
            }
        }
        return isEliminated;

    }

    public void generate(){
        for(Player player : players){
            player.gridGen(gridSize);
        }
    }

    public String fire(String firing, String firedAt, int x, int y){
        Player attacker = null;
        Player victim = null;
        String output = "";

        for(Player player : players){
            if(player.getName().equals(firing)){
                attacker = player;
            }else if(player.getName().equals(firedAt)){
                victim = player;
            }
        }

        if(victim == null || attacker == null){
            output += "target unknown. ";
            if(attacker == null){
                output += "player unknown.";
            }
        }else{
            if(attacker.markHit(victim, x, y)){
                output = "Shots fired at " + victim.getName() + " by " + attacker.getName();
            }else{
                output = attacker.getName() + "attack out of bounds";
            }
        }

        return output;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
}