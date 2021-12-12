package server;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class represents the server for where the client will communicate with the client
 * about the game.
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
            // While the server socket is not closed
            while (!serverSocket.isClosed()) {
                // Accept an incoming connection
                Socket sock = serverSocket.accept();
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
        ConnectionAgent agent = getAgent(source);

        //This if handles all commands that are not /battle
        if (!message.contains("/battle")){
            sender = addAgents(source, sender);
        }else{
            sender = getSender(message, agent);
        }

        if (message.contains("/start")){
            handleStart(agent);
        }else if (message.contains("/fire")){
            handleFire(message, sender, agent);
        }
        else if (message.contains("/surrender")) {
            handleSurrender(source, sender, agent);
        }else if (message.contains("/display")) {
            handleDisplay(message, sender, agent);
        }
    }

    /**
     * Helper method to handle messages that contain /display
     * @param message commands sent from client
     * @param sender username of sender
     * @param agent agent associated with sender
     */
    private void handleDisplay(String message, String sender, ConnectionAgent agent) {
        if (game.getRunning()) {
            //grabs username from command
            String[] displayCommand = message.split("\\s");
            if (displayCommand.length == 2) {
                String usernameToDisplay = displayCommand[1];
                Grid grid = game.display(usernameToDisplay);
                if (grid != null) {
                    if (sender.equals(grid.getUsername())) {
                        if (agent != null) {
                            agent.sendMessage(grid.toString(grid.getGrid()));
                        }
                    } else {
                        if (agent != null) {
                            agent.sendMessage(grid.toString(grid.getAltGrid()));
                        }
                    }
                } else {
                    if (agent != null) {
                        agent.sendMessage("Username given is not found in this game, " +
                                "try again!");
                    }
                }
            }
        }else{
            if (agent != null){
                agent.sendMessage("Play not in progress");
            }
        }
    }

    /**
     * Helper method to handle surrender commands
     * @param source agent associated with sender
     * @param sender username of sender
     * @param agent agent associated with sender
     */
    private void handleSurrender(MessageSource source, String sender, ConnectionAgent agent) {
        if (game.getRunning()) {
            boolean surrendered = game.surrender(sender);
            if (surrendered) {
                sourceClosed(source);
                broadcast("!!! " + sender + " surrendered");
                if (game.getGrids().size() == 1) {
                    broadcast("GAME OVER: " + game.getGrids().get(0).getUsername() + " wins!");
                }
            }
        }else{
            if (agent != null){
                agent.sendMessage("Play not in progress");
            }
        }
    }

    /**
     * Helper method to help handle messages that contain /fire
     * @param message command received from client
     * @param sender sender of command
     * @param agent agent associated with sender
     */
    private void handleFire(String message, String sender, ConnectionAgent agent) {
        if (game.getRunning()) {
            String[] commands = message.split("\\s");
            if(commands.length == 4) {
                if (sender.equals(game.getCurrentPlayer().getUsername())) {
                    performFire(sender, agent, commands);
                }else {
                    if (agent != null) {
                        agent.sendMessage("Move Failed, player turn: " +
                                game.getCurrentPlayer().getUsername());
                    }
                }
            }else{
                if (agent != null){
                    agent.sendMessage("Invalid command, should be /fire [row] [column]" +
                            " [target username]");
                }
            }
        }else{
            if (agent != null){
                agent.sendMessage("Game is not in progress");
            }
        }
    }

    /**
     * Helper method to perform fire command
     * @param sender username of sender client
     * @param agent agent associated with sender
     * @param commands commands to perform
     */
    private void performFire(String sender, ConnectionAgent agent, String[] commands) {
        String target = commands[3];
        if (!target.equals(sender)) {
            boolean fired = game.fire(commands);
            if (fired) {
                broadcast("Shots fired at " + target + " by " + sender);

                Grid gridToRemove = null;
                for (int i = 0; i < game.getGrids().size(); i++) {
                    if (game.getGrids().get(i).getHitPoints() == 0) {
                        gridToRemove = game.getGrids().get(i);
                        sourceClosed(connectionAgentsList.get(i));
                        broadcast("!!! " + game.getGrids().get(i).getUsername() + " " +
                                "ships were sunk");

                        if (game.getGrids().get(i).getUsername().equals(
                                game.getCurrentPlayer().getUsername())) {
                            game.incrementCurrentPlayer();
                        }
                    }
                }

                if (gridToRemove != null) {
                    game.getGrids().remove(gridToRemove);
                }

                game.incrementCurrentPlayer();
                broadcast(game.getCurrentPlayer().getUsername() + " it is your turn");

                if (game.getGrids().size() == 1) {
                    broadcast("GAME OVER: " + game.getCurrentPlayer().getUsername() + " wins!");
                }
            } else {
                if (agent != null) {
                    agent.sendMessage("Fired off the grid, Username is not in game, or have " +
                            "fired there already. fire again!");
                }
            }
        }else{
            if (agent != null){
                agent.sendMessage("Move Failed, player turn: " + game.getCurrentPlayer()
                        .getUsername());
            }
        }
    }

    /**
     * Helper method to handle messages that contain /start
     * @param agent agent to send messages to client associated with it
     */
    private void handleStart(ConnectionAgent agent) {
        if (!game.getRunning()) {
            boolean started = game.start();
            if (!started && agent != null) {
                agent.sendMessage("Not enough players to start!");
            } else {
                broadcast("The game begins\n" + game.getCurrentPlayer().getUsername()
                        + " it is your turn");
            }
        }
    }

    /**
     * Gets sender username of ConnectionAgent passed in
     * @param message command received from client
     * @param agent agent associated with username in command
     * @return username of sending client
     */
    private String getSender(String message, ConnectionAgent agent) {
        String sender;
        //This code handles /battle command to create new player
        sender = message.split(" ")[1];
        boolean added = game.battle(sender);
        if (!added && agent != null){
            agent.sendMessage("Username taken! Use /battle command with new username!");
        }else if (game.getRunning() && agent != null){
            agent.sendMessage("Game is already in progress");
        }
        else{
            broadcast("!!! " + sender + " has entered the battle");
        }
        return sender;
    }

    /**
     * Helper method to add agents
     * @param source source to be added to the list
     * @param sender sender that is associated with the agent passed in
     * @return username of sender associated with agent
     */
    private String addAgents(MessageSource source, String sender) {
        for (int i = 0; i < connectionAgentsList.size(); i++) {
            if (connectionAgentsList.get(i) == source) {
                sender = this.game.getGrids().get(i).getUsername();
            }
        }
        return sender;
    }

    /**
     * Helper method to get ConnectionAgent that matches MessageSource
     * @param source source to retrieve
     * @return Connection Agent of source passed in, null if not found
     */
    private ConnectionAgent getAgent(MessageSource source) {
        ConnectionAgent agent = null;
        for (ConnectionAgent connectionAgent : connectionAgentsList) {
            if (connectionAgent == source) {
                agent = connectionAgent;
            }
        }
        return agent;
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
