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

    private Grid currentPlayer;
    //to tell if a game is in session or not
    private boolean running = false;

    private boolean gameOver = false;

    private Scanner scanner;

    public Grid battle(String username, int size){
        //create a grid with the username associated with the board
        return null;
    }

    public Game(int size, ArrayList<String> usernames, Scanner scanner){
        this.scanner = scanner;
        grids = new ArrayList<>();
        this.command = "";
        for (String username : usernames){
            Grid grid = new Grid(size, username);
            grids.add(grid);
        }

        boolean toggle = true;
        while (toggle) {
            System.out.print("\nPlayer One, Type 'Start' when ready: ");
            this.command = this.scanner.next();
            if (this.command.equalsIgnoreCase("Start")){
                toggle = false;
            }
        }
    }

    public boolean start(){
        if (grids.size() >= 2 && !running){
            //then start the game
            this.running = true;

            while (running){
                for (int i = 0; i < grids.size(); i++){
                    if (!gameOver){
                        currentPlayer = grids.get(i);
                        System.out.println("\n" + currentPlayer.getUsername() + ", it is your turn!");
                        turn();
                    }
                }
            }
        }
        return running;
    }

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

    public void fire(){
        try {
            int row = -1;
            int column = -1;
            while(column > currentPlayer.getSize() - 1 || column < 0) {
                System.out.print("Enter the column coordinate: ");
                column = Integer.parseInt(scanner.next());
            }
            while (row > currentPlayer.getSize() - 1 || row < 0){
                System.out.print("Enter the row coordinate: ");
                row = Integer.parseInt(scanner.next());
            }

            for (Grid grid : grids) {
                if (!currentPlayer.getUsername().equalsIgnoreCase(grid.getUsername())) {
                    Symbol spot = grid.getGrid()[column][row];
                    if (spot == Symbol.EMPTY) {
                        grid.getGrid()[column][row] = Symbol.MISS;
                        grid.getAltGrid()[column][row] = Symbol.MISS;
                    } else {
                        if (spot == Symbol.HIT || spot == Symbol.MISS){
                            System.out.println("You already fired here, try again!");
                            fire();
                        }else {
                            grid.getGrid()[column][row] = Symbol.HIT;
                            grid.getAltGrid()[column][row] = Symbol.HIT;

                            grid.setHitPoints(grid.getHitPoints() - 1);
                            if (grid.getHitPoints() == 0){
                                String username = currentPlayer.getUsername();
                                currentPlayer = grid;
                                System.out.println("\n" + username + " sunk " + currentPlayer.getUsername() + " ships!");
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

    private void gameOverCheck(){
        grids.remove(currentPlayer);
        if (grids.size() == 1){
            gameOver = true;
            running = false;
            System.out.println("Game is Over!");
        }
    }

    public void surrender(){
        System.out.println("\n" + currentPlayer.getUsername() + " has Surrendered!");
        gameOverCheck();
    }

    public void display(){
        // TODO: 11/20/2021 Loop through Grids, print out alt version of other player grids
        System.out.println(currentPlayer.toString(currentPlayer.getGrid()));
        for (Grid grid : grids){
            if (!grid.getUsername().equalsIgnoreCase(currentPlayer.getUsername())){
                System.out.println(grid.toString(grid.getAltGrid()));
            }
        }

    }

    public boolean getGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }
}
