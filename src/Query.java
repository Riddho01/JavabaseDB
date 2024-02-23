import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Query {

    //Parse query typed by user
    public static void parseCommand(String command){

        //Remove leading and trailing spaces from command before processing
        command=command.trim().toLowerCase();

        //Parse create database command
        if(command.startsWith("create database")){
            String[] create_db_tokens=command.split("\\s+");

            if(create_db_tokens.length==3 && create_db_tokens[0].equals("create") && create_db_tokens[1].equals("database")){
                createDatabase(create_db_tokens[2]);
            }
            //if command starts with create database. But has incorrect number oftokens
            else{
                System.out.println("Incorrect Format. Try 'create database <database_name>");
            }
        }

        //Parse create table command
        else if(command.startsWith("create table")){

            //Get index of opening and closing brackets
            int opening_brackets_index=command.indexOf("(");
            int closing_brackets_index=command.indexOf(")");

            if(opening_brackets_index==-1 || closing_brackets_index==-1 || opening_brackets_index>closing_brackets_index){
                System.out.println("Invalid command format: Valid Parenthesis not found");
                return;
            }

            //Parse tablename and column definitions
            String commandPrefix = "create table";
            int startIndexOfTableName = commandPrefix.length();
            String tablename = command.substring(startIndexOfTableName, opening_brackets_index).trim();

            if (tablename.isEmpty()) {
                System.out.println("Invalid command format: No tablename mentioned");
                return;
            }

            String columns_part=command.substring(opening_brackets_index+1,closing_brackets_index).trim();
            //Separating the column definitions
            String[] columndefArr=columns_part.split(",");

            //Storing column definitions in a List
            List<String> columndef=new ArrayList<>();

          //Checks for column definitions being properly formatted
          for(String column: columndefArr){
              column=column.trim();

              String[] columndef_parts=column.split("\\s+");
              //Incorrect format of column definitions if it has more than 2 parts(column_name and datatype)
              if(columndef_parts.length!=2){
                  System.out.println("Invalid command format: Invalid column definition");
                  return;
              }
              if(!(columndef_parts[1].equalsIgnoreCase("int") || columndef_parts[1].equalsIgnoreCase("decimal") || columndef_parts[1].equalsIgnoreCase("varchar"))){
                  System.out.println("Invalid command format: Invalid data_type");
                  return;
              }
              columndef.add(column);

          }
            System.out.println(columndef);

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
