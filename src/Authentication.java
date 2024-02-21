import java.io.*;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class Authentication {

    //File-Path of file containing user authentication data
    private static final String user_data="Data/User/users.csv";

    //New User Sign Up
    public static boolean SignUp(String userID,String password){

        //Storing the password in hashed format
        String hashedPassword=Authentication.hashPassword(password);

        //Creating comma separated userId and password as the UserRecord
        String userRecord= userID+","+hashedPassword;

        //Inserting new user's record using a File Writer
        try (FileWriter f = new FileWriter(user_data,true)) {
                f.write(userRecord+"\n");

                //Returning True indicating user authentication captured successfully
                return true;
        } catch (IOException e) {
            e.printStackTrace();
            //If any error occurs while capturing data, indicating failure of the operation by returning false
            return false;
        }

    }

    //Hash new user password using MD-5 algorithm
    private static String hashPassword(String password){

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                String hex = Integer.toHexString(0xff & hashedByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean userExists(String userID){

        //Traversing through the users.csv file and checking if the entered UserId already exists
        try(BufferedReader br=new BufferedReader( new FileReader(user_data))){

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

    public static boolean login(String userID, String password){

        try(BufferedReader br=new BufferedReader(new FileReader(user_data))){

            String line;
            while((line= br.readLine())!=null){
                String storedUserID=line.split(",")[0];
                String storedPassword=line.split(",")[1];
                if(storedUserID.equals(userID)){
                    String password_hash=Authentication.hashPassword(password);
                    if(storedPassword.equals(password_hash)){
                        return true;
                    }
                }

            }
            return false;
        }catch (IOException e){
            e.printStackTrace();

            //if any error occurs, indicate failure by returning false
            return false;
        }
    }
    


}
