import java.util.Random;

public class Captcha {

    //Attribute captcha
    private static String captcha;

    //captcha getter
    public static String getCaptcha() {
        return captcha;
    }
    public static void generateCaptcha(){

        Random rand=new Random();
        //Hard-Coded values of minimum and maximum length of captcha
        int minLength=1;
        int maxLength=1;

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

    public static boolean verifyCaptcha(String enteredCaptcha){

        //if enteredCaptcha is equal to generated captcha, then returning true
        return captcha.equals(enteredCaptcha);

    }


}
