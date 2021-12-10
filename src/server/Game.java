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
    }

    public void handleCommand(String command, String sender){
        //battle, start, fire, display, surrender

    }

    public ArrayList<Grid> getGrids() {
        return grids;
    }

    /**
     * Command to display grids. Shows grid of current player with ships and opponent grids with 
     * just hits and misses
     */
    public void display(){
        //display for current player grid with ships and opponent grids with just hit and misses
        System.out.println(currentPlayer.toString(currentPlayer.getGrid()));
        for (Grid grid : grids){
            if (!grid.getUsername().equalsIgnoreCase(currentPlayer.getUsername())){
                System.out.println(grid.toString(grid.getAltGrid()));
            }
        }
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
}
