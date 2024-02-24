import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Query {

    //Create database
    public static void createDatabase(String dbname) {

        if (User.getUserDBPath() != null) {
            System.out.println("Cannot create more than one database.");
            return;
        }

        File databaseFolder = new File(Authentication.getUsers() + "/" + User.getCurrentUserID(), dbname);
        if (databaseFolder.mkdir()) {
            System.out.println("Database created successfully");
        } else {
            System.out.println("Failed to create database");
        }

    }

    //Create tables in database
    public static void createTable(String tablename, List<String> columndefs) {

        //Current user's database folder path
        String dbPath = User.getUserDBPath();

        //Cannot create tables without database folder
        if (dbPath == null) {
            System.out.println("No database found. Create a database with: create database <database name>");
        }

        //Table data and meta data paths
        String tbmetadata_path = dbPath + "/" + tablename + ".json";
        String tbdata_path = dbPath + "/" + tablename + ".csv";

        //Check if table with the same name exists in directory
        if (new File(tbdata_path).exists() || new File(tbmetadata_path).exists()) {
            System.out.println("Table " + tablename + " already exists");
            return;
        }

        //Create Table meta data and data files
        try {

            try (FileWriter fw = new FileWriter(tbmetadata_path)) {
                fw.write("{\n\"columns\": [\n");
                for (int i = 0; i < columndefs.size(); i++) {
                    String[] parts = columndefs.get(i).split("\\s+");
                    fw.write(String.format("  {\"name\": \"%s\", \"type\": \"%s\"}", parts[0], parts[1]));
                    if (i < columndefs.size() - 1) fw.write(",");
                    fw.write("\n");
                }
                fw.write("]\n}");

                //Create a csv file for Table Data
                Files.createFile(Paths.get(tbdata_path));

                System.out.println("Table " + tablename + " created");
            }
        } catch (IOException e) {
            System.out.println("Failed to create table");

        }

    }

    //Insert data in tables
    public static void insertInto(String tablename, String[] values) {

        //Table data file path
        String tbdataPath = Table.getTableMDPath(tablename).replace(".json", ".csv");

        try (FileWriter f = new FileWriter(tbdataPath, true); BufferedWriter br = new BufferedWriter(f); PrintWriter write = new PrintWriter(br)) {

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                sb.append(values[i]);
                if (i < values.length - 1) { // If not the last value, append a comma
                    sb.append(",");
                }
            }
            write.println(sb);

            System.out.println("Values inserted into " + tablename + ".");

        } catch (IOException e) {
            System.out.println("Failed to insert values into: " + tablename);
        }
    }

    public static void selectStar(String tablename) {

        if (Table.isTableEmpty(tablename)) {
            System.out.println("Empty Set");
            return;
        }


        //Getting the column details of table
        List<Column> columns = Table.getTableMD(tablename);

        try (BufferedReader br = new BufferedReader(new FileReader(Table.getTableMDPath(tablename).replace(".json", ".csv")))) {

            //Print column names
            for (Column column : columns) {
                System.out.print(column.getName() + "\t");
            }

            System.out.println();
            String row;
            //Print each row
            while ((row = br.readLine()) != null) {

                //Values separated by comma in csv file
                String[] values = row.split(",");

                for (String value : values) {
                    System.out.print(value + "\t");
                }

                //Print each new row in a new line
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Failed to read Table: " + tablename);
        }
    }

    static void selectColumns(String tablename, String[] selectedColumns) {

        try (BufferedReader br = new BufferedReader(new FileReader(Table.getTableMDPath(tablename).replace(".json", ".csv")))) {

            //Print column names
            for (String column : selectedColumns) {
                System.out.print(column + "\t");
            }
            //New line before data
            System.out.println();

            // Iterate over eachrow
            String row;

            while ((row = br.readLine()) != null) {

                //Separating data in row by ','
                String[] rowdata = row.split(",");

                List<String> MDColumnNames = Table.getTableMD(tablename).stream().map(Column::getName).collect(Collectors.toList());

                for (String selectedColumn : selectedColumns) {
                    int columnIndex = MDColumnNames.indexOf(selectedColumn);

                        System.out.print(rowdata[columnIndex] + "\t");
                }

                //New line after each row
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Failed to read Table: " +tablename);
        }
    }
}

