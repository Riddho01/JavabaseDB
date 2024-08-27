import java.util.Scanner;

/**
 * Entry point to the DBMS application.
 * User provided with options to Sign-Up, Login, or Exit.
 * After logging in, the user can enter MySQL commands until they choose to logout.
 */
    public class Main {
        public static void main(String[] args) {

            while (true) {

                //User Choices
                System.out.println("1. Sign-Up\n2. Login\n3. Exit");
                Scanner sc = new Scanner(System.in);
                String choice = sc.next();
                sc.nextLine();

                    switch (choice) {
                        case "1":
                            System.out.println("Enter UserId");
                            String UserId = sc.next();
                            sc.nextLine();
                            if (User.userExists(UserId)) {
                                System.out.println("User already exists");
                                break;
                            }
                            System.out.println("Enter Password");
                            String Password = sc.next();

                            if (Authentication.SignUp(UserId, Password)) {
                                System.out.println("Sign-Up successful");
                            } else {
                                System.out.println("User already exists");
                            }
                            break;

                        case "2":
                            System.out.println("Enter UserId");
                            String UserID_login = sc.next();
                            sc.nextLine();

                            System.out.println("Enter Password");
                            String Password_Login = sc.next();

                            if (Authentication.login(UserID_login, Password_Login)) {
                                System.out.println("Login Successful");
                                User.setCurrentUserID(UserID_login);

                                sc.nextLine();
                                //User can enter MySQL commands, until they enter Logout
                                while(true){
                                    System.out.print("Enter MySql command (Type 'LogOut' to return to menu): ");

                                    String command = sc.nextLine();

                                    if(command.trim().equalsIgnoreCase("Logout")){
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
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid Choice");
                    }
                System.out.println();
                }

            }

    }