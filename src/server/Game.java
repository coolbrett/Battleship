package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This class represents the logic for the game of battleship
 *
 * @author Brett Dale
 * @author Katherine Blanton
 * @version 11/22/2021
 */

public class Game {

    /** string for the command typed */
    private String command;
    /** arraylist of the grids in the game */
    private ArrayList<Grid> grids;
    /** Grid of the current player whose turn it is */
    private Grid currentPlayer;
    /** to tell if a game is in session or not */
    private boolean running = false;
    /** to tell if a game is over or not */
    private boolean gameOver = false;
    /** Scanner to get the information from user(System.in) */
    private Scanner scanner;

    /**
     * The function for the battle command. Used to enter the game. In this version it is not used
     * but prompted already in main.
     *
     * @param username The name of the user
     * @param size The size of the grid
     * @return Nothing for now as not used in Milestone 1
     */
    public Grid battle(String username, int size){
        //create a grid with the username associated with the board
        return null;
    }

    /**
     * The constructor for creating a Game.
     * 
     * @param size The size of the grid
     * @param usernames An arraylist of the usernames in the game
     * @param scanner The scanner used to read in user input.
     */
    public Game(int size, ArrayList<String> usernames, Scanner scanner){
        this.scanner = scanner;
        grids = new ArrayList<>();
        this.command = "";
        for (String username : usernames){
            Grid grid = new Grid(size, username);
            grids.add(grid);
        }
        
        //loop to get player to type start 
        boolean toggle = true;
        while (toggle) {
            System.out.print("\nPlayer One, Type 'Start' when ready: ");
            this.command = this.scanner.next();
            if (this.command.equalsIgnoreCase("Start")){
                toggle = false;
            }
        }
    }

    /**
     * The command start used to start the game.
     *
     * @return running To tell if a game is in progress or not.
     */
    public boolean start(){
        if (grids.size() >= 2 && !running){
            //then start the game
            this.running = true;

            while (running){
                for (int i = 0; i < grids.size(); i++){
                    if (!gameOver){
                        currentPlayer = grids.get(i);
                        System.out.println("\n" + currentPlayer.getUsername() 
                                           + ", it is your turn!");
                        turn();
                    }
                }
            }
        }
        return running;
    }

    /**
     * Method to create a turn for a player and to give them 3 commands they can do for a turn.
     *
     * @throws NumberFormatException If user types something that was not a number.
     */
    private void turn(){
        try {
            int num = -1;
            while (num < 0 || num > 2) {
                System.out.print("Enter 0 to Fire, 1 to Surrender, 2 to Display your Grid: ");
                num = Integer.parseInt(scanner.next());
                if (num == 0){
                    fire();
                }else if (num == 1){
                    surrender();
                }else if (num == 2){
                    display();
                    num = -1;
                }
            }
        }catch (NumberFormatException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Command to fire. Used for letting the current player fire at opponents ships.
     *
     * @throws NumberFormatException If user types something that is not a number.
     */
    public void fire(){
        try {
            int row = -1;
            int column = -1;
            //Getting valid column number
            while(column > currentPlayer.getSize() - 1 || column < 0) {
                System.out.print("Enter the column coordinate: ");
                column = Integer.parseInt(scanner.next());
            }
            //Getting valid row number
            while (row > currentPlayer.getSize() - 1 || row < 0){
                System.out.print("Enter the row coordinate: ");
                row = Integer.parseInt(scanner.next());
            }
            //Only works for 2 player version 
            for (Grid grid : grids) {
                //get opponent grid
                if (!currentPlayer.getUsername().equalsIgnoreCase(grid.getUsername())) {
                    Symbol spot = grid.getGrid()[column][row];
                    //check if coordinate on grid is empty
                    if (spot == Symbol.EMPTY) {
                        grid.getGrid()[column][row] = Symbol.MISS;
                        grid.getAltGrid()[column][row] = Symbol.MISS;
                    } else {
                        //Check if it is a spot they have already fired at
                        if (spot == Symbol.HIT || spot == Symbol.MISS){
                            System.out.println("You already fired here, try again!");
                            fire();
                            //if not hit or miss or empty then it is now a hit
                        }else {
                            grid.getGrid()[column][row] = Symbol.HIT;
                            grid.getAltGrid()[column][row] = Symbol.HIT;
                            grid.setHitPoints(grid.getHitPoints() - 1);
                            //checking hitpoints of opponent to see if game is over
                            if (grid.getHitPoints() == 0){
                                String username = currentPlayer.getUsername();
                                currentPlayer = grid;
                                System.out.println("\n" + username + " sunk " 
                                                   + currentPlayer.getUsername() + " ships!");
                                gameOverCheck();
                            }
                        }
                    }
                }
            }

        }catch (NumberFormatException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to check if the game is over or not
     */
    private void gameOverCheck(){
        //remove player who has 0 hitpoints and check if there are any other opponents
        grids.remove(currentPlayer);
        if (grids.size() == 1){
            gameOver = true;
            running = false;
            System.out.println("Game is Over!");
        }
    }

    /**
     * Command to surrender.
     */
    public void surrender(){
        System.out.println("\n" + currentPlayer.getUsername() + " has Surrendered!");
        gameOverCheck();
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
