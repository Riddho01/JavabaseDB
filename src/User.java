import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * The {@code User} class manages user-related operations, such as checking if a user exists,
 * retrieving the current user's database path, and handling the current user's ID.
 * <p>
 * This class provides utility functions for verifying user existence, obtaining the path to a user's
 * database directory, and managing the current user's ID.
 * </p>
 */
public class User {

    /**
     * The ID of the currently logged-in user.
     */
    private static String currentUserID=null;

    /**
     * Check if a user with the given ID exists in the system.
     * <p>
     * Read the user authentication data from the CSV file and checks if the specified
     * user ID is present.
     * </p>
     *
     * @param userID The ID of the user to check for existence.
     * @return {@code true} if the user exists, {@code false} otherwise.
     */
    public static boolean userExists(String userID){

        //Traversing through the users.csv file and checking if the entered UserId already exists
        try(BufferedReader br=new BufferedReader( new FileReader(Authentication.getUser_authentication_data()))){
            String line;
            while((line = br.readLine())!=null){
                String storedUserID=line.split(",")[0];
                if(storedUserID.equals(userID)){

                    //returning true if found a user with passed userID
                    return true;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        //If UserId does not exist in users.csv, returning false.
        return false;
    }

    /**
     * Retrieve the path to the current user's database folder.
     * <p>
     * This method constructs the path to the user's directory and checks for the existence of a database
     * directory within it. If a database exists, its path is returned.
     * </p>
     *
     * @return The path to the user's database folder, or {@code null} if no database is found.
     */
    public static String getUserDBPath() {
        //current user's directory
        String userdirPath=Authentication.getUsers()+User.getCurrentUserID();

        File userdir=new File(userdirPath);
        File[]  db=userdir.listFiles(File::isDirectory);

        if(db==null || db.length==0){
            return null;
        }

        return userdirPath+"/"+db[0].getName();

    }

    //Getters and Setters
    public static String getCurrentUserID() {
        return currentUserID;
    }

    public static void setCurrentUserID(String currentUserID) {
        User.currentUserID = currentUserID;
    }


}
