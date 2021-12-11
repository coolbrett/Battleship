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
        System.out.println("Received: " + message);
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
            }else if (game.getRunning() && agent != null){
                agent.sendMessage("Game is already in progress");
            }
            else{
                broadcast("!!! " + sender + " has entered the battle");
            }
        }

        if (message.contains("/start")){
            boolean started = game.start();
            if (!started && agent != null){
                agent.sendMessage("Not enough players to start!");
            }else{
                broadcast("The game has begins\n" + game.getCurrentPlayer().getUsername() + " it is your turn");
            }
        }else if (message.contains("/fire")){
            if (game.getRunning()) {
                String[] commands = message.split("\\s");
                if(commands.length == 4) {
                    if (sender.equals(game.getCurrentPlayer().getUsername())) {
                        boolean fired = game.fire(commands);
                        if (fired){
                            String target = commands[3];
                            broadcast("Shots fired at " + target + " by " + sender);
                            // TODO: 12/11/2021 Check hitpoints of target to see if they need to surrender
                            broadcast(game.getCurrentPlayer().getUsername() + " it is your turn");
                        }
                    } else {
                        if (agent != null) {
                            agent.sendMessage("Move Failed, player turn: " + game.getCurrentPlayer().getUsername());
                        }
                    }
                }else{
                    if (agent != null){
                        agent.sendMessage("Invalid command, should be /fire [row] [column] [target username]");
                    }
                }
            }else{
                if (agent != null){
                    agent.sendMessage("Game is not in progress");
                }
            }
        }else if (message.contains("/surrender")){
            game.surrender(sender);
            broadcast("!!! " + sender + " surrendered");
            sourceClosed(source);
        }else if (message.contains("/display")){
            //grabs username from command
            String[] displayCommand = message.split("\\s");
            if (displayCommand.length == 2){
                String usernameToDisplay = displayCommand[1];
                Grid grid = game.display(usernameToDisplay);
                if (game.getCurrentPlayer().getUsername().equals(grid.getUsername())){
                    if (agent != null) {
                        agent.sendMessage(grid.toString(grid.getGrid()));
                    }
                }else{
                    if (agent != null) {
                        agent.sendMessage(grid.toString(grid.getAltGrid()));
                    }
                }
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
