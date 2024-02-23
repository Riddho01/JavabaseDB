import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Query {

    //Create database
    public static void createDatabase(String dbname){

        if(User.getUserDBPath()!=null){
            System.out.println("Cannot create more than one database.");
            return;
        }

        File databaseFolder=new File(Authentication.getUsers()+"/"+User.getCurrentUserID(),dbname);
        if(databaseFolder.mkdir()){
            System.out.println("Database created successfully");
        }
        else{
            System.out.println("Failed to create database");
        }

    }

    //Create tables in database
    public static void createTable(String tablename, List<String> columndefs){

        //Current user's database folder path
        String dbPath=User.getUserDBPath();

        //Cannot create tables without database folder
        if(dbPath==null){
            System.out.println("No database found. Create a database with: create database <database name>");
        }

        //Table data and meta data paths
        String tbmetadata_path=dbPath+"/"+tablename+".json";
        String tbdata_path=dbPath+"/"+tablename+".csv";

        //Check if table with the same name exists in directory
        if(new File(tbdata_path).exists() ||new File(tbmetadata_path).exists()){
            System.out.println("Table "+tablename+" already exists");
            return;
        }

        //Create Table meta data and data files
        try{

            try(FileWriter fw=new FileWriter(tbmetadata_path)){
                fw.write("{\n\"columns\": [\n");
                for(int i=0;i<columndefs.size();i++){
                    String[] parts = columndefs.get(i).split("\\s+");
                    fw.write(String.format("  {\"name\": \"%s\", \"type\": \"%s\"}", parts[0], parts[1]));
                    if (i < columndefs.size() - 1) fw.write(",");
                    fw.write("\n");
                }
                fw.write("]\n}");

                //Create a csv file for Table Data
                Files.createFile(Paths.get(tbdata_path));

                System.out.println("Table "+tablename+" created");
            }
        }catch(IOException e){
            System.out.println("Failed to create table");

        }

    }

    //Insert data in tables
    public static void insertInto(String tablename,String[] values){

        //Table data file path
        String tbdataPath=Table.getTableMDPath(tablename).replace(".json",".csv");

        try (FileWriter f = new FileWriter(tbdataPath, true); BufferedWriter br = new BufferedWriter(f); PrintWriter write = new PrintWriter(br)) {

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                sb.append(values[i]);
                if (i < values.length - 1) { // If not the last value, append a comma
                    sb.append(",");
                }
            }

            if (new File(tbdataPath).length()==0) {
                write.print(sb.toString());
            } else {
                write.println(sb.toString());
            }

            System.out.println("Values inserted into " + tablename+ ".");

    }catch (IOException e){
            System.out.println("Failed to insert values into: "+tablename);
        }
    }



}
