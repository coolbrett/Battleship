package client;

import java.net.UnknownHostException;

/**
 * Driver for Client side of Battleship
 *
 * @author Brett Dale
 * @author Katherine Blanton
 */
public class BattleDriver {

    public static void main(String[] args){
        if (args.length != 3){
            System.out.println("USAGE: java client.BattleDriver <HOST> <PORT> <USERNAME>");
            System.exit(1);
        }else {
            //CMA checks
            try{
                BattleClient battleClient = new BattleClient(args[0], Integer.parseInt(args[1]), args[2]);
            } catch (UnknownHostException e) {
                System.out.println("USAGE: java client.BattleDriver <HOST> <PORT> <USERNAME>");
                e.printStackTrace();
            }
        }
    }
}
