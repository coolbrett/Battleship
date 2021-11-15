package client;

import common.MessageListener;
import common.MessageSource;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Client class for Battleship
 *
 * @author Brett Dale
 * @author Katherine Blanton
 */
public class BattleClient extends MessageSource implements MessageListener {

    /**Host client will connect to */
    private final InetAddress host;

    /**Port to be used */
    private final int port;

    /**Username client is assigned */
    private final String username;

    /**
     * Constructor for Battle Client
     * @param host host to connect to
     * @param port port to be used
     * @param username username to be assigned to client
     * @throws UnknownHostException if host is not valid
     */
    public BattleClient(String host, int port, String username) throws UnknownHostException {
        this.host = InetAddress.getByName(host);
        this.port = port;
        this.username = username;
    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) {

    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {

    }

    /**
     *
     * @param message message to send
     */
    public void send(String message){
        // TODO: 11/15/2021 Find out what this method does, and write it's docs
    }

    /**
     *
     */
    public void connect(){
        // TODO: 11/15/2021 Find out what this method does, and write it's docs
    }
}
