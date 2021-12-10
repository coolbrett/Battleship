package client;

import common.MessageListener;
import common.MessageSource;

import java.io.PrintStream;

/**
 * Class responsible for writing messages to a PrintStream
 *
 * @author Brett Dale
 * @author Katherine Blanton
 */
public class PrintStreamMessageListener implements MessageListener {

    /**PrintStream to write messages to */
    private final PrintStream out;

    /**
     * Constructor for PrintStreamMessageListener
     * @param out PrintStream object to write to
     */
    public PrintStreamMessageListener(PrintStream out){
        this.out = out;
    }

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) {
        this.out.println(message);
    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
    }
}
