package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This class represents the battleship driver
 *
 * @author Brett Dale
 * @author Katherine Blanton
 */

public class BattleShipDriver {

    /** Default size of grid */
    private final static int DEFAULT_SIZE = 10;

    /** Arg index for board size */
    private final static int argSize = 0;

    /**
     *
     *
     * @param args the command line args The first arg being the port and the second being the grid
     *             size.
     */
    public static void main(String[] args){

        if (args.length == 1){
            try{
                int size = Integer.parseInt(args[argSize]);
                if (size < 5 || size > 10){
                    size = DEFAULT_SIZE;
                }

                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter username for Player One: ");
                String playerOne = scanner.next();

                boolean toggle = true;
                String playerTwo;
                Game game;
                while(toggle) {
                    System.out.print("\nEnter username for Player Two: ");
                    playerTwo = scanner.next();
                    if (!playerOne.equalsIgnoreCase(playerTwo)){
                        toggle = false;
                        game = new Game(size, new ArrayList<>(Arrays.asList(playerOne, playerTwo)), scanner);
                        System.out.println("here");
                        scanner.close();
                    }
                }

                /*
                while (!game.getGameOver()){
                    //Player One turn
                    //check win condition
                    //Player Two condition
                    //check win condition
                    //if win == true
                    game.setGameOver(true);
                }*/


            }catch(NumberFormatException e){
                System.out.println("Error! " + e.getMessage());
                System.exit(2);
            }
        }else{
            System.out.println("Usage: java portNumber [BoardSize]");
            System.exit(1);
        }
    }
}
