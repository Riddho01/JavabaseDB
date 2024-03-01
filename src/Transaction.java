import java.util.ArrayList;
import java.util.List;

public class Transaction {



    private static boolean isInTransaction=false;

    public static List<String> getBuffer() {
        return Buffer;
    }

    private static List<String> Buffer=new ArrayList<>();
    public static boolean isIsInTransaction() {
        return isInTransaction;
    }

    public static void setIsInTransaction(boolean isInTransaction) {
        Transaction.isInTransaction = isInTransaction;
    }

    public static void addToBuffer(String command){
        Buffer.add(command);
    }
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
