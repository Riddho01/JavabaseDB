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
                return;
            }

            //Verify value passed matches datatype of corresponding column
            for(int i=0;i<md.size();i++){
                    if(!Util.verifyType(values[i], md.get(i).getDatatype())){
                        System.out.println("Type mismatch for column: "+md.get(i).getName()+". Expected type: "+md.get(i).getDatatype());
                        return;
                    }

            }

            Query.insertInto(tablename,values);


        }

        //Parse select commands
        else if(command.startsWith("select")){

            //Regex patterns to identify select query type
            Pattern select_all=Pattern.compile("(?i)^\\s*SELECT\\s*\\*\\s*FROM\\s+(\\w+)\\s*;?$");
            Pattern select_allWhere = Pattern.compile("(?i)^\\s*SELECT\\s*\\*\\s*FROM\\s+(\\w+)\\s+WHERE\\s+(\\w+)\\s*(=|<>|<=|>=|<|>)\\s*(.+?)\\s*;?$");
            Pattern selectColumns=Pattern.compile("(?i)^\\s*SELECT\\s+([\\w\\s,]+)\\s+FROM\\s+(\\w+)\\s*;?$");
            Pattern selectColumnsWhere = Pattern.compile("(?i)^\\s*SELECT\\s+([\\w\\s,]+)\\s+FROM\\s+(\\w+)\\s+WHERE\\s+(\\w+)\\s*(=|<>|<=|>=|<|>)\\s*(.+?)\\s*;?$");

            Matcher match;

            //Match select* from tablename command
            if ((match = select_all.matcher(command)).matches()) {
                String tablename=match.group(1);
                if(!Table.doesTableExist(tablename)){
                    System.out.println("Table: "+tablename+" does not exist");
                    return;
                }

                Query.selectStar(tablename);
            }

            //Match select*from tablename where col=value
            else if ((match = select_allWhere.matcher(command)).matches()) {

                String tablename = match.group(1);
                String columnanme=match.group(2);

                //Table name passed does not exist
                if(!Table.doesTableExist(tablename)){
                    System.out.println("Table does not exist");
                    return;
                }

                //If column name does not exist in table
                else if(!Table.doesColExist(tablename,columnanme)){
                    System.out.println("Column: "+columnanme+" does not exist in Table: "+tablename);
                    return;
                }

                String operator=match.group(3);
                String value=match.group(4);

                // Additional check for varchar values
                if (!(value.matches("-?\\d+(\\.\\d+)?"))) {

                    //Ensure varchar values enclosed in single quotes
                    if (!value.matches("^'[^']*'$")) {
                        System.out.println("Invalid format: Varchar values in WHERE clause must be enclosed within single quotes");
                        return;
                    }
                    // Ensure only '=' can be applied to varchar values
                    if (!operator.equals("=")) {
                        System.out.println("Value: "+value+" incompatible with operator: "+operator);
                        return;
                    }
                }

//                System.out.println(tablename+","+columnanme);

             // Query.selectStarWhere(tablename,value,operator)
            }

            //Match select col1,col2 from tablename
            else if ((match = selectColumns.matcher(command)).find()) {

                //Get column names and tablename from command
                String column_part = match.group(1).trim();
                String tablename = match.group(2);

                //Check if table with name exists
                if (!Table.doesTableExist(tablename)) {
                    System.out.println("Table does not exist");
                    return;
                }

                //Get the column names
                String[] columns = column_part.split("\\s*,\\s*");

                //Check if any of the column names do not exist in table
                for(String column: columns){
                    if(!Table.doesColExist(tablename,column)){
                        System.out.println("Column: "+column+" does not exist in Table: "+tablename);
                        return;
                    }
                }

//                System.out.println(tablename+","+column_part);

                Query.selectColumns(tablename,columns);


            }

            //Match select col1,col2 from tablename where col=val
            else if ((match = selectColumnsWhere.matcher(command)).matches()) {

                String tablename=match.group(2);
                String columns_part=match.group(1).trim();
                String whereColumn=match.group(3);

                //Check if table exists
                if(!Table.doesTableExist(tablename)){
                    System.out.println("Table: "+tablename+" does not exist");
                    return;
                }

                //Check if where clause column name in valid
                if(!Table.doesColExist(tablename,whereColumn)){
                    System.out.println("Column: "+whereColumn+" does not exist in Table: "+tablename);
                }

                //Check if any of the columns provided do not exist in the table
                String[] columns=columns_part.split("\\s*,\\s*");
                for(String column: columns){
                    if(!Table.doesColExist(tablename,column)){
                        System.out.println("Column: "+column+" does not exist in Table: "+tablename);
                        return;
                    }
                }

                String operator=match.group(4);
                String value=match.group(5);

//                System.out.println(tablename+","+columns_part+","+operator+","+value);

                // Additional check for varchar values
                if (!(value.matches("-?\\d+(\\.\\d+)?"))) {

                    //Ensure varchar values enclosed in single quotes
                    if (!value.matches("^'[^']*'$")) {
                        System.out.println("Invalid format: Varchar values in WHERE clause must be enclosed within single quotes");
                        return;
                    }
                    // Ensure only '=' can be applied to varchar values
                    if (!operator.equals("=")) {
                        System.out.println("Value: "+value+" incompatible with operator: "+operator);
                        return;
                    }
                }
                //Query.selectcolumnsWhere(tablename,columns,whereColumn);

            }


            else{
                System.out.println("Invalid command format: Try select <* OR Column Names> from <table name> where <column name> = <value>");
            }

        }

        //Query cannot be parsed
        else{
            System.out.println("Invalid Command");
        }
    }

}
