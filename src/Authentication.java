import java.io.*;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The {@code Authentication} class handles user authentication i.e. sign-up and login.
 * User credentials stored in CSV format. Password hashed using MD5 algorithm.
 */
public class Authentication {

    /** Path of CSV file containing auth credentials.*/
    private static final String user_authentication_data ="data/User_Authentication/authentication_credentials.csv";
    /**Path of directory containing user's tables*/
    private static final String users="data/Users/";

    /**
     * Register a new user with given userID and password.
     * Password is hashed using MD5 and stored in CSV.
     A directory for the user is created inside the {@link #users} directory.
     *
     * @param userID user's unique identifier.
     * @param password user's login password.
     * @return {@code true} if sign-up successful, {@code false} otherwise.
     */
    public static boolean SignUp(String userID,String password)
    {
        //Storing the password in hashed format
        String hashedPassword=Authentication.hashPassword(password);

        //Creating comma separated userId and password as the UserRecord
        String userRecord= userID+","+hashedPassword;

        // Creating a directory for the user under 'Users' directory
        File userDirectory = new File(users + userID);
        if (!userDirectory.exists()) {
            if (!userDirectory.mkdirs()) {
                return false; // Failed to create user folder
            }
        }

        //Inserting new user's record using a File Writer
        try (FileWriter f = new FileWriter(user_authentication_data,true)) {
                f.write(userRecord+"\n");

                //Returning True indicating user authentication captured successfully
                return true;
        } catch (IOException e) {
            e.printStackTrace();
            //If any error occurs while capturing data, indicating failure of the operation by returning false
            return false;
        }

    }

    /**
     * Hash a given password using the MD5 algorithm.
     *
     * @param password the password to be hashed.
     * @return the hashed password as a hexadecimal string, or {@code null} if error occurs during hash operation.
     */
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

    /**
     * Log in a user by verifying the userID and password against stored credentials.
     * If the credentials match, a CAPTCHA is generated and verified for successful login.
     *
     * @param userID user's unique identifier.
     * @param password user's password.
     * @return {@code true} if login successful, {@code false} otherwise.
     */
    public static boolean login(String userID, String password){

        try(BufferedReader br=new BufferedReader(new FileReader(user_authentication_data))){

            String line;
            while((line= br.readLine())!=null){
                String storedUserID=line.split(",")[0];
                String storedPassword=line.split(",")[1];

                if(storedUserID.equals(userID)){
                    String password_hash=Authentication.hashPassword(password);
                    if(storedPassword.equals(password_hash)){
                        //Generate Captcha
                        Captcha.generateCaptcha();

                        System.out.println("Enter the following captcha: " +Captcha.getCaptcha());
                        Scanner sc=new Scanner(System.in);
                        String enteredCaptcha=sc.next();

                        return Captcha.verifyCaptcha(enteredCaptcha);
                    }
                    break;
                }
            }
            return false;
        }catch (IOException e){
            e.printStackTrace();

            //if any error occurs, indicate failure by returning false
            return false;
        }
    }

   // Getters and Setters
    public static String getUser_authentication_data(){
        return user_authentication_data;
    }

    public static String getUsers() {
        return users;
    }
}
