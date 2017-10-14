package blockchain;

/**
 * The Operation class represents an operation made upon an AVL tree consisting of the operation's name, the data that
 * is passed to the operation method and the status of the operation which indicates if said operation was successfull
 * or not.
 */
public abstract class Operation<T> {
    private String name;
    private T data;
    /**
     * The status indicates if the operation was applied successfully (true) or unsuccessfully (false).
     */
    private boolean status;

    public Operation(String name, T data, boolean status) {
        this.name = name;
        this.data = data;
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public T getData() {
        return data;
    }

    public boolean getStatus() {
        return status;
    }

    public String toString() {
        return "The " + this.getName() + " operation was applied with the " + this.getData()
                + " node passsed as an argument " + (this.getStatus() ? "successfully." : "unsuccessfully.");
    }

    public String toStringForHash() {
        return this.getName() + this.getData() + this.getStatus();
    }

    /**
     * IMPORTANT: This method is only available so we can simulate an unwanted data manipulation
     */
    public void modifyOperation(T data) {
        this.data = data;
    }

    public String operationClass() {
        return "";
    }
}
