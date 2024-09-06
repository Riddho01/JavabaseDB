import java.util.Scanner;

/**
 * Entry point to the JavabaseDB application.
 * Users are provided with options to Sign-Up, Login, or Exit.
 * After logging in, users can enter SQL commands until they choose to log out.
 */
public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            // Display application name and user choices
            System.out.println("Welcome to JavabaseDB");
            System.out.println("1. Sign-Up");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Please choose an option: ");
            String choice = sc.next();
            sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter UserId: ");
                    String UserId = sc.next();
                    sc.nextLine();
                    if (User.userExists(UserId)) {
                        System.out.println("User already exists");
                        break;
                    }
                    System.out.print("Enter Password: ");
                    String Password = sc.next();

                    if (Authentication.SignUp(UserId, Password)) {
                        System.out.println("Sign-Up successful");
                    } else {
                        System.out.println("Sign-Up failed. User might already exist.");
                    }
                    break;

                case "2":
                    System.out.print("Enter UserId: ");
                    String UserID_login = sc.next();
                    sc.nextLine();

                    System.out.print("Enter Password: ");
                    String Password_Login = sc.next();

                    if (Authentication.login(UserID_login, Password_Login)) {
                        System.out.println("Login Successful");
                        User.setCurrentUserID(UserID_login);

                        sc.nextLine();
                        // User can enter SQL commands until they log out
                        while (true) {
                            System.out.print("JavabaseDB> ");

                            String command = sc.nextLine();

                            if (command.trim().equalsIgnoreCase("LogOut")) {
                                User.setCurrentUserID(null);
                                break;
                            }

                            Parse.parseCommand(command);

                        }

                    } else {
                        System.out.println("Incorrect/Invalid Login Credentials");
                    }
                    break;

                case "3":
                    System.out.println("Exiting JavabaseDB. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Choice. Please try again.");
            }
            System.out.println();
        }
    }
}
