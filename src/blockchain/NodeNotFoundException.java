package blockchain;

/**
 * Created by lucas_sg on 10/9/17.
 */
public class NodeNotFoundException extends Exception {
    public NodeNotFoundException() {}

    public NodeNotFoundException(String message) {
        super(message);
    }
}
