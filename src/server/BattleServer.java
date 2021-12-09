package server;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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

    private ConnectionAgent[] ConArray;
    private int port;

    public BattleServer(int port) throws IOException {
        this.port = port;
    }

    public void listen(){
        try {
            // Create a TCP socket to send data through
            ServerSocket server = new ServerSocket(this.port);

            // While the server socket is not closed
            while (!server.isClosed()) {
                // Accept an incoming connection
                Socket sock = server.accept();

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message){

    }

    @Override
    public void messageReceived(String message, MessageSource source) {

    }

    @Override
    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);

    }
}
