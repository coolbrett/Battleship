package common;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class represents the connection that is being made between the client and server.
 *
 * @author Brett Dale
 * @author Katherine Blanton
 */
public class ConnectionAgent extends MessageSource implements Runnable{

    /** Socket for agent */
    private Socket socket;

    /** Scanner for agent */
    private Scanner in;

    /** PrintStream to write messages to server */
    private PrintStream out;

    /** Thread to run agent on */
    private Thread thread;

    /**
     * Constructor for ConnectionAgent
     * @param socket socket to use for message sending and receiving
     * @throws IOException exception for Scanner and PrintStream
     */
    public ConnectionAgent(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new Scanner(this.socket.getInputStream());
        this.out = new PrintStream(this.socket.getOutputStream());
        this.thread = new Thread(this);
    }

    /**
     * Message sending method for ConnectionAgent
     * @param message message to send
     */
    public void sendMessage(String message){
        this.out.println(message);
    }

    /**
     * Checks if socket is connected
     * @return true if socket is connected, false if not
     */
    public boolean isConnected(){
        return socket.isConnected();
    }

    /**
     * Closes all fields in ConnectionAgent object
     * @throws IOException for Socket exceptions
     */
    public void close() throws IOException {
        socket.close();
        in.close();
        out.close();
        thread.interrupt();
    }

    /**
     * Run method for Thread field to call when it starts
     */
    @Override
    public void run() {
        while (!socket.isClosed()){
            if (in.hasNextLine()){
                notifyReceipt(in.nextLine());
            }
        }
    }

    /**
     * Getter method for Thread field
     * @return Thread field
     */
    public Thread getThread() {
        return thread;
    }
}
