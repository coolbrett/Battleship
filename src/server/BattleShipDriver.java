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
 * @version 11/22/2021
 */

public class BattleShipDriver {

    /** Default size of grid */
    private final static int DEFAULT_SIZE = 10;

    /** Arg index for board size */
    private final static int argSize = 1;

    /**
     * Main method that checks the args and calls other classes to play a game of Battleship
     *
     * @param args the command line args The first arg being the port and the second being the grid
     *             size.
     * @throws NumberFormatException If command arg is not an integer
     */

    public static void main(String[] args) {

        //checking for arg length of 1 or 2
        if (args.length == 2 || args.length == 1) {
            try {
                int size = DEFAULT_SIZE;
                if (args.length == 2){
                    size = Integer.parseInt(args[argSize]);
                    if (size < 5 || size > 10) {
                        size = DEFAULT_SIZE;
                    }
                }
                BattleServer server = new BattleServer(Integer.parseInt(args[0]), size);
                server.listen();
            } catch (NumberFormatException e) {
                System.out.println("Error! " + e.getMessage());
                System.exit(2);
            }
        } else {
            System.out.println("Usage: java BoardSize");
            System.exit(1);
        }
    }
}
