package server;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class represents the server for where the client will communicate with the client about the game.
 *
 * @author Brett Dale
 * @author Katherine Blanton
 */
public class BattleServer implements MessageListener {

    /**Server socket to connect to */
    private ServerSocket serverSocket;

    /**Whose turn it is */
    private int current;

    /**The Game object*/
    private Game game;

    /**List of Players */
    private ArrayList<ConnectionAgent> connectionAgentsList;

    /**Port to connect to */
    private final int port;

    /**
     * Constructor for BattleServer
     * @param port port to use for connections
     * @param size size to make board in game
     */
    public BattleServer(int port, int size) {
        this.port = port;
        this.game = new Game(size);
        this.connectionAgentsList = new ArrayList<>();
        this.current = 0;
    }

    /**
     * Creates server for clients to connect to, waits for clients to send messages
     */
    public void listen(){
        try {
            // Create a TCP socket to send data through
            serverSocket = new ServerSocket(this.port);
            System.out.println("Server started on port: " + port);

            // While the server socket is not closed
            while (!serverSocket.isClosed()) {
                // Accept an incoming connection
                Socket sock = serverSocket.accept();
                System.out.println("Socket made on port: " + sock.getLocalPort());
                ConnectionAgent agent = new ConnectionAgent(sock);
                agent.addMessageListener(this);
                this.connectionAgentsList.add(agent);
                agent.getThread().start();
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to every client
     * @param message message to send to clients
     */
    public void broadcast(String message){
        for (ConnectionAgent agent : connectionAgentsList){
            agent.sendMessage(message);
        }
    }

    /**
     * Server receives commands from clients and handles those commands appropriately
     * @param message The message received by the subject
     * @param source The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) {
        String sender = "";

        //Get agent to send message to if something they send doesn't go through
        //can be turned into helper method
        ConnectionAgent agent = null;
        for (ConnectionAgent connectionAgent : connectionAgentsList) {
            if (connectionAgent == source) {
                agent = connectionAgent;
            }
        }

        //This if handles all commands that are not /battle
        if (!message.contains("/battle")){
            //This for loop determines who the sender of the command is
            //can be turned into a helper method
            for (int i = 0; i < connectionAgentsList.size(); i++) {
                if (connectionAgentsList.get(i) == source) {
                    sender = this.game.getGrids().get(i).getUsername();
                    System.out.printf("Sender is: %s\n", sender);
                }
            }
        }else{
            //This code handles /battle command to create new player
            sender = message.split(" ")[1];
            System.out.println("Received /battle from: " + sender);
            boolean added = game.battle(sender);
            if (!added && agent != null){
                agent.sendMessage("Username taken! Use /battle command with new username!");
            }
        }

        if (message.contains("/start")){
            boolean started = game.start();
            if (!started && agent != null){
                agent.sendMessage("Not enough players to start!");
            }
        }

    }

    /**
     * Closes a connection to the client
     * @param source The MessageSource that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        for (ConnectionAgent agent : connectionAgentsList){
            if (agent == source){
                try {
                    agent.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        source.removeMessageListener(this);
        this.connectionAgentsList.remove((ConnectionAgent) source);
    }
}
