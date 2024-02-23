import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Query {

    //create database method
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

}
