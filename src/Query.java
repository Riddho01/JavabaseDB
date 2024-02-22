import java.io.File;
public class Query {

    //Parse query typed by user
    public static void parseCommand(String command){

        //Remove leading and trailing spaces from command before processing
        command=command.trim().toLowerCase();

        if(command.startsWith("create database")){
            String[] tokens=command.split("\\s");
            if(tokens.length==3 && tokens[0].equals("create") && tokens[1].equals("database")){
                createDatabase(tokens[2]);
            }
            //if command starts with create database. But has incorrect number of tokens
            else{
                System.out.println("Incorrect Format. Try 'create database <database_name>");
            }
        }

        //Query cannot be parsed
        else{
            System.out.println("Invalid Command");
        }
    }

    public static void createDatabase(String dbname){

        File usersFolder=new File(Authentication.getUsers()+"/"+Authentication.getCurrentUserID());

        File[] subfolders = usersFolder.listFiles(File::isDirectory);
        if (subfolders != null && subfolders.length > 0) {
            System.out.println("Cannot create more than one database.");
            return;
        }

        File databaseFolder=new File(usersFolder,dbname);
        if(databaseFolder.mkdir()){
            System.out.println("Database created successfully");
        }
        else{
            System.out.println("Failed to create database");
        }

    }


}
