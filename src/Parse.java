import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {

    //Parse typed command
    public static void parseCommand(String command){

        //Remove leading and trailing spaces from command before processing
        command=command.trim().toLowerCase();

        //Parse create database command
        if(command.startsWith("create database")){
            String[] create_db_tokens=command.split("\\s+");

            if(create_db_tokens.length==3 && create_db_tokens[0].equals("create") && create_db_tokens[1].equals("database")){
                Query.createDatabase(create_db_tokens[2]);
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
            Query.createTable(tablename,columndef);

        }

        //Parse insert into command
        else if(command.startsWith("insert into")){

            String query_pattern="(?i)insert\\s+into\\s+(\\w+)\\s+values\\s*\\((.*?)\\)";
            Pattern p= Pattern.compile(query_pattern);

            Matcher m=p.matcher(command);
            if(!m.find()){
                System.out.println("Incorrect command format. Try insert into <table name> values(<value1>, <value 2>.....)");
                return;
            }

            String tablename=m.group(1);
            String values_part=m.group(2);

            if(!Table.doesTableExist(tablename)){
                System.out.println("Table does not exist");
                return;
            }

            //If table exists, get the meta-data of the tablename
            List<Column> md=Table.getTableMD(tablename);

            String[] values=values_part.split("\\s*,\\s*");

            //Number of values passed should be the equal to number of columns in table metadata
            if(values.length!=md.size()){
                System.out.println("Invalid number of provided values");
            }


        }


        //Query cannot be parsed
        else{
            System.out.println("Invalid Command");
        }
    }

}
