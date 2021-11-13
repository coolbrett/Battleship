package common;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class represents the connection that is being made between the client and server.
 *
 * @author Brett Dale
 * @author Katherine Blanton
 */
public class ConnectionAgent implements Runnable{

    private Socket socket;
    private Scanner in;
    private PrintStream out;
    private Thread thread;

    public ConnectionAgent(Socket socket){

    }

    public void sendMessage(String message){

    }

    public boolean isConnected(){
        boolean connected = false;
        return connected;
    }

    public void close(){

    }

    @Override
    public void run() {

    }
}
