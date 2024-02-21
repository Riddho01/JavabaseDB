import java.io.*;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class Authentication {

    //File-Path of file containing user authentication data
    private static final String user_data="Data/User/users.csv";

    public static boolean SignUp(String userID,String password){

        //if user exists , check


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


}
