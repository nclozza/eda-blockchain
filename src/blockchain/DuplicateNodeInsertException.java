package blockchain;

import com.sun.org.apache.bcel.internal.generic.DUP;

/**
 * Created by lucas_sg on 10/9/17.
 */
public class DuplicateNodeInsertException extends Exception {
    public DuplicateNodeInsertException() {}

    public DuplicateNodeInsertException(String message) {
        super(message);
    }
}
