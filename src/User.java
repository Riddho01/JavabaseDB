import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class User {

    //Logged-in user's Id
    private static String currentUserID=null;

    //Check if user with given id exists
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

    //Get user's database folder path
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
