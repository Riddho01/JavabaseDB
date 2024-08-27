import java.util.Random;

/**
 * The {@code Captcha} class deals with generation and verification of CAPTCHA strings during authentication.
 * It uses a combination of alphanumeric characters to create a random string.
 */
public class Captcha {

    /** The generated CAPTCHA string. */
    private static String captcha;

    /**
     * Generate a random CAPTCHA string within a certain minimum and maximum length range.
     * String is composed of lowercase and uppercase alphabetic characters and digits.
     */
    public static void generateCaptcha(){

        Random rand=new Random();
        // Adjust the minimum and maximum length of the CAPTCHA.
        int minLength=1;
        int maxLength=6;

        //Generate the random length of the captcha within the bounds of minLength and maxLength
        int length=rand.nextInt((maxLength-minLength)+1)+minLength;

        //Possible characters in captcha
         String characters="abcdefghijklmnopqrstuvwxyzABDCEFGHIJKLMNOPQRSTUVWXYZ0123456789";

         StringBuilder generatedCaptcha=new StringBuilder();

         //Generate each character of the captcha
        for(int i=0;i<length;i++){
            int randomIndex=rand.nextInt(characters.length());
            generatedCaptcha.append(characters.charAt(randomIndex));
        }

        //Setting the captcha attribute
        captcha=generatedCaptcha.toString();
    }

    /**
     * Verify if the entered text matches the generated CAPTCHA.
     *
     * @param enteredCaptcha the string entered by the user.
     * @return {@code true} if the entered text matches the generated CAPTCHA, {@code false} otherwise.
     */
    public static boolean verifyCaptcha(String enteredCaptcha){
        //if enteredCaptcha is equal to generated captcha, then returning true
        return captcha.equals(enteredCaptcha);

    }


    public static String getCaptcha() {
        return captcha;
    }


}
