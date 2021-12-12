package server;

import java.util.ArrayList;

/**
 * This class represents the logic for the game of battleship
 *
 * @author Brett Dale
 * @author Katherine Blanton
 * @version 11/22/2021
 */
public class Game {

    /** arrayList of the grids in the game */
    private ArrayList<Grid> grids;
    /** Grid of the current player whose turn it is */
    private Grid currentPlayer;
    /** to tell if a game is in session or not */
    private boolean running = false;
    /** to tell if a game is over or not */
    private boolean gameOver = false;
    /**Size of the Board */
    private int size;

    /**
     * Constructor for Game object
     * @param size size that grid will be
     */
    public Game(int size){
        this.size = size;
        this.grids = new ArrayList<>();
    }

    /**
     * Creates a new player with username passed in if username is not taken
     * @param username username to attach to grid.
     * @return true if player was added, false otherwise
     */
    public boolean battle(String username){
        if (!running) {
            if (grids.isEmpty()) {
                grids.add(new Grid(this.size, username));
                currentPlayer = grids.get(0);
                return true;
            } else {
                //look to see if username already exists in game
                boolean found = false;
                for (int i = 0; i < grids.size(); i++) {
                    Grid grid = grids.get(i);
                    if (grid.getUsername().equalsIgnoreCase(username)) {
                        found = true;
                        i = grids.size();
                    }
                }

                if (!found) {
                    grids.add(new Grid(this.size, username));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method to handle start command
     * @return true if game started, false otherwise
     */
    public boolean start() {
        if (grids.size() > 1) {
            running = true;
            currentPlayer = grids.get(0);
            return true;
        }
        return false;
    }

    /**
     * Getter method to retrieve grids field
     * @return grids field
     */
    public ArrayList<Grid> getGrids() {
        return grids;
    }

    /**
     * Command to display grids. Shows grid of current player with ships and opponent
     * grids with just hits and misses
     * @param usernameToDisplay username of Grid to display
     */
    public Grid display(String usernameToDisplay){
        for (Grid grid : grids) {
            if (grid.getUsername().equals(usernameToDisplay)) {
                return grid;
            }
        }
        return null;
    }

    /**
     * Getter method for gameOver field
     *
     * @return gameOver Field to tell if game is over or not
     */
    public boolean getGameOver() {
        return gameOver;
    }

    /**
     * Sets gameOver to true or false
     *
     * @param gameOver A true or false depending on if a game is over or not
     */
    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }

    /**
     * Getter method for running field
     * @return running field
     */
    public boolean getRunning() {
        return this.running;
    }

    /**
     * Getter method for Current player
     * @return current player field
     */
    public Grid getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Method to handle fire command
     * @param commands commands to based fire command on
     * @return true if board was fired on, false otherwise
     */
    public boolean fire(String[] commands) {
        boolean fired = false;
        int row = Integer.parseInt(commands[1]);
        int column = Integer.parseInt(commands[2]);
        String target = commands[3];

        if (row > -1 && row < this.size && column > -1 && column < this.size) {
            for (int i = 0; i < grids.size(); i++) {
                if (grids.get(i).getUsername().equals(target)) {
                    Symbol spot = grids.get(i).getGrid()[column][row];

                    if (spot == Symbol.EMPTY) {
                        grids.get(i).getGrid()[column][row] = Symbol.MISS;
                        grids.get(i).getAltGrid()[column][row] = Symbol.MISS;
                        fired = true;
                    }else if (spot == Symbol.HIT || spot == Symbol.MISS){
                        return fired;
                    }else{
                        //should be a ship at this point
                        grids.get(i).getGrid()[column][row] = Symbol.HIT;
                        grids.get(i).getAltGrid()[column][row] = Symbol.HIT;
                        grids.get(i).setHitPoints(grids.get(i).getHitPoints() - 1);
                        fired = true;
                        i = grids.size();
                    }
                }
            }
        }else{
            return false;
        }
        return fired;
    }

    /**
     * Method to move to the next player's turn
     */
    public void incrementCurrentPlayer() {
        for (int i = 0; i < grids.size(); i++) {
            if (currentPlayer.getUsername().equals(grids.get(i).getUsername())){
                if (i == (grids.size() - 1) || grids.size() == 1){
                    currentPlayer = grids.get(0);
                } else{
                    currentPlayer = grids.get(i + 1);
                }
                i = grids.size();
            }
        }
    }

    /**
     * Method to handle surrender command
     * @param username username to remove from grids list
     * @return true if player was removed, false if current player isn't equal to
     * username surrendering
     */
    public boolean surrender(String username) {
        if (currentPlayer.getUsername().equals(username)) {
            Grid gridToRemove = null;
            for (int i = 0; i < grids.size(); i++) {
                if (grids.get(i).getUsername().equals(username)) {
                    gridToRemove = grids.get(i);
                    i = grids.size();
                }
            }
            grids.remove(gridToRemove);

            incrementCurrentPlayer();

            if (grids.size() == 1) {
                gameOver = true;
                running = false;
            }
            return true;
        }
        return false;
    }
}
