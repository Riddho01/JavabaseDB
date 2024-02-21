import java.util.Scanner;
public class Main {
    public static void main(String[] args) {

        System.out.println("1. Sign-Up\n2. Login\n3. Exit");

        Scanner sc = new Scanner(System.in);
        String choice = sc.next();
        
            switch (choice) {
                case "1":
                    System.out.println("Enter UserId");
                    String UserId = sc.next();
                    sc.nextLine();
                    if (Authentication.userExists(UserId)) {
                        System.out.println("User already exists");
                        break;
                    }
                    System.out.println("Enter Password");
                    String Password = sc.next();

                    boolean SignedUp = Authentication.SignUp(UserId, Password);
                    if (SignedUp) {
                        System.out.println("Sign-Up successful");
                    } else {
                        System.out.println("User already exists");
                    }
                    break;
                case "3":
                    System.exit(0);


                default:
                    System.out.println("Invalid Choice");
            }


        }

}