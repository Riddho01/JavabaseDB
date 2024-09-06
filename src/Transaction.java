import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Transaction} class manages database transactions by buffering and executing SQL commands.
 * <p>
 * This class provides functionality to handle transaction operations, allowing commands to be buffered,
 * executed, and managed in a transactional context. The class tracks whether a transaction is active
 * and stores the commands in a buffer until they are executed or cleared.
 * </p>
 */
public class Transaction {

    /**
     * Indicates whether a transaction is currently active.
     */
    private static boolean isInTransaction=false;

    /**
     * A list that buffers SQL commands during a transaction.
     */
    private static List<String> Buffer=new ArrayList<>();

    /**
     * Get the list of transactions in the buffer
     *
     * @return A list of SQL commands in the buffer.
     */
    public static List<String> getBuffer() {
        return Buffer; }

    /**
     * Checks if a transaction is currently active.
     *
     * @return {@code true} if a transaction is active, {@code false} otherwise.
     */
    public static boolean isIsInTransaction() {
        return isInTransaction;
    }
    /**
     * Sets the transaction state.
     *
     * @param isInTransaction {@code true} to mark a transaction as active, {@code false} to mark it as inactive.
     */
    public static void setIsInTransaction(boolean isInTransaction) {
        Transaction.isInTransaction = isInTransaction;
    }

    /**
     * Adds a SQL command to the transaction buffer.
     *
     * @param command The SQL command to be added to the buffer.
     */
    public static void addToBuffer(String command){
        Buffer.add(command);
    }

    /**
     * Empty the transaction buffer
     */
    public  static void clearBuffer(){
        Buffer.clear();
    }


    public static void runCommands(){

        //Running commands in buffer
        for(String command: Buffer){
            Parse.parseCommand(command);
        }
    }




}
