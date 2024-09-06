import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code Query} class provides methods to perform the corresponding operation for SQL commands. It supports creating databases and tables, inserting data,
 * and querying data with or without conditions.
 */
public class Query {

    /**
     * Create a database as a directory inside the user's directory.
     * <p>
     * The database will be created inside the `data/users/user_Id` directory.
     * Only one database can be created per user. If a database already exists,
     * a message will be displayed indicating that no more databases can be created.
     * </p>
     *
     * @param dbname Name of the database to be created.
     */
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

    /**
     * Create a table in the current user's database.
     * <p>
     * Creates two files: one for table metadata (in JSON format)
     * and another for table data (in CSV format). If a table with the same name
     * already exists, it will not create a new table.
     * </p>
     *
     * @param tablename Name of the table to be created.
     * @param columndefs List of column definitions where each definition is in
     *                   the format "column_name datatype".
     */
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

    /**
     * Insert a row of data into the specified table.
     * <p>
     * Data is appended to the end of the table's data (CSV) file.
     * </p>
     *
     * @param tablename The name of the table where the data will be inserted.
     * @param values Array of values to be inserted into the table.
     */
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
            System.out.println("Value inserted into table: "+tablename);


        } catch (IOException e) {
            System.out.println("Failed to insert values into: " + tablename);
        }
    }

    /**
     * Displays all records in the specified table.
     * <p>
     * Reads the table's CSV file and prints all rows to the console.
     * </p>
     *
     * @param tablename The name of the table from which to retrieve and display records.
     */
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

    /**
     * Display specific columns from the specified table.
     * <p>
     * Reads the table data and prints only the selected columns for each row.
     * </p>
     *
     * @param tablename Name of the table from which to display data.
     * @param selectedColumns An array of column names to be displayed.
     */
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

    /**
     * Display all records from the specified table that satisfy a given condition.
     * <p>
     * Read the table data and print the rows where the value in the specified column meets the condition
     * defined by the {@code operator} and {@code value}. The column name, operator, and value for filtering are provided as parameters.
     * </p>
     *
     * @param tablename The name of the table from which to select records.
     * @param column The name of the column on which to apply the condition.
     * @param operator The operator used for the condition (e.g., '=', '>', '<').
     * @param value The value to compare against the column data.
     */

    public static void selectStarWhere(String tablename, String column, String operator, String value) {

        try (BufferedReader br = new BufferedReader(new FileReader(Table.getTableMDPath(tablename).replace(".json", ".csv")))) {

            List<Column> TableMD = Table.getTableMD(tablename);

            //Print all column names
            for (Column col: TableMD) {
                System.out.print(col.getName() + "\t");
            }
            System.out.println();

            // Get index of comparison column from meta-data
            int index = -1;
            for (int i = 0; i < TableMD.size(); i++) {
                if (TableMD.get(i).getName().equalsIgnoreCase(column)) {
                    index = i;
                    break;
                }
            }

            String row;
            while ((row = br.readLine()) != null) {
                String[] rowdata = row.split(",");

                if (index < rowdata.length) {
                    // If condition met, print entire row
                    if (Util.checkCondition(rowdata[index], value, operator)) {
                        for (String data : rowdata) {
                            System.out.print(data + "\t");
                        }
                        System.out.println();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading Table: "+tablename);
        }
    }

    /**
     * Displays selected columns from records in the specified table that satisfy the
     * given condition.
     * <p>
     * It reads from the table's CSV file, checks each row against the condition,
     * and prints specified columns for rows that meet the condition.
     * </p>
     *
     * @param tablename The name of the table from which to retrieve records.
     * @param columns An array of column names to be displayed.
     * @param column The column name on which the condition is based.
     * @param operator The operator for the condition (e.g., '=', '<', '>', etc.).
     * @param value The value to compare against the column values.
     */
    public static void selectColumnsWhere(String tablename, String[] columns, String column, String operator, String value) {


        try (BufferedReader br = new BufferedReader(new FileReader( Table.getTableMDPath(tablename).replace(".json", ".csv")))) {
            List<Column> tableMD = Table.getTableMD(tablename);

            // Get selected column and condition column index
            int[] sIndices = new int[columns.length];
            int conditionalColIndex = -1;
            for (int i = 0; i < tableMD.size(); i++) {
                String metaColumnName = tableMD.get(i).getName();
                if (metaColumnName.equalsIgnoreCase(column)) {
                    conditionalColIndex = i;
                }
                for (int j = 0; j < columns.length; j++) {
                    if (metaColumnName.equalsIgnoreCase(columns[j])) {
                        sIndices[j] = i;
                    }
                }
            }

            // Print headers for selected columns
            Arrays.stream(columns).forEach(col -> System.out.print(col + "\t"));
            System.out.println();

            String row;
            while ((row = br.readLine()) != null) {
                String[] rowdata = row.split(",");
                if (conditionalColIndex < rowdata.length && Util.checkCondition(rowdata[conditionalColIndex],value, operator)) {

                    // Print selected rows satisfying condition
                    for (int index : sIndices) {
                        if (index < rowdata.length) {
                            System.out.print(rowdata[index] + "\t");
                        } else {
                            System.out.print("N/A\t");
                        }
                    }
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading Table:  " +tablename);
        }
    }

}



