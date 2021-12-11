package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Driver for Client side of Battleship
 *
 * @author Brett Dale
 * @author Katherine Blanton
 */
public class BattleDriver {

    /**
     * Main method for driver
     * @param args command line arguments
     */
    public static void main(String[] args){
        if (args.length != 3){
            System.out.println("USAGE: java client.BattleDriver <HOST> <PORT> <USERNAME>");
            System.exit(1);
        }else {
            try{

                BattleClient battleClient = new BattleClient(args[0], Integer.parseInt(args[1]), args[2]);
                battleClient.addMessageListener(new PrintStreamMessageListener(System.out));
                battleClient.connect();

                Scanner scanner = new Scanner(System.in);
                System.out.print("Type command: ");
                String command = "";
                while (!command.equalsIgnoreCase("/surrender")){
                    command = scanner.nextLine();
                    battleClient.send(command);
                }
                scanner.close();

            } catch (UnknownHostException e) {
                System.out.println("USAGE: java client.BattleDriver <HOST> <PORT> <USERNAME>");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
