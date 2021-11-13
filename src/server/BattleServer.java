package server;

import common.MessageListener;
import common.MessageSource;
import java.net.ServerSocket;

/**
 * This class represents the server for where the client will communicate with the client about the game.
 *
 * @author Brett Dale
 * @author Katherine Blanton
 */
public class BattleServer implements MessageListener {

    private ServerSocket serverSocket;
    private int current;
    private Game game;

    public BattleServer(int port){

    }

    public void listen(){

    }

    public void broadcast(String message){

    }

    @Override
    public void messageReceived(String message, MessageSource source) {

    }

    @Override
    public void sourceClosed(MessageSource source) {

    }
}
