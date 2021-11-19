package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This class represents the logic for the game of battleship
 *
 * @author Brett Dale
 * @author Katherine Blanton
 */

public class Game {

    //string for the command typed
    private String command;
    //array of the grids in the game
    private ArrayList<Grid> grids;
    //to tell if a game is in session or not
    private boolean running = false;

    private boolean gameOver = false;

    public Grid battle(String username, int size){
        //create a grid with the username associated with the board
        return null;
    }

    public Game(int size, ArrayList<String> usernames, Scanner scanner){
        grids = new ArrayList<>();
        this.command = "";
        for (String username : usernames){
            Grid grid = new Grid(size, username);
            grids.add(grid);
        }

        boolean toggle = true;
        scanner = new Scanner(System.in);
        while (toggle) {
            System.out.print("\nPlayer One, Type 'Start' when ready: ");
            this.command = scanner.next();
            if (this.command.equalsIgnoreCase("Start")){
                toggle = false;
            }
        }
        start();


    }

    public boolean start(){
        if (grids.size() >= 2 && !running){
            //then start the game
            this.running = true;
        }
        return running;
    }

    public void fire(){

    }

    public void surrender(){

    }

    public String display(){
        return "Grid display";
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }
}
