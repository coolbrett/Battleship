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
        //Game object initialization, use size here
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

                // Create readers to read data being sent by client
                InputStreamReader inStream = new InputStreamReader(sock.getInputStream());
                BufferedReader buffer = new BufferedReader(inStream);

                // Get the socket output stream and wrap it to write objects
                OutputStream sockOut = sock.getOutputStream();
                ObjectOutputStream outStream = new ObjectOutputStream(sockOut);

                // Close streams for this connection
                sock.close();
                inStream.close();
                buffer.close();
                sockOut.close();
                outStream.close();
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

    @Override
    public void messageReceived(String message, MessageSource source) {
        if (message.equals("/start")){
            if (connectionAgentsList.size() > 1){
                broadcast("The game begins\n");
            }else {
                broadcast("Not enough players to play the game\n");
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
        this.connectionAgentsList.remove(source);
    }
}
