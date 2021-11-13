package server;

/**
 * This class represents the battleship driver
 *
 * @author Brett Dale
 * @author Katherine Blanton
 */

public class BattleShipDriver {

    /** Default port number */
    private final static int PORTNUM = 9999;
    /** Default size of grid */
    private final static int DEFAULT_SIZE = 10;

    /**
     *
     *
     * @param args the command line args The first arg being the port and the second being the grid
     *             size.
     */
    public static void main(String[] args){

        BattleServer server = new BattleServer(PORTNUM);


    }

}
