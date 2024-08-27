import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Table} class provides methods for interacting with table metadata and data.
 * <p>
 * It handles operations such as checking the existence of tables and columns, retrieving table metadata,
 * and determining whether a table contains any data. The metadata of each table is stored in a JSON file,
 * while the table data is stored in a CSV file.
 * </p>
 */

public class Table {

    /**
     * Return the path for the file containing the specified table's metadata.
     * <p>
     * The metadata file is a JSON file located in the user's database directory.
     * </p>
     *
     * @param tablename Name of the table whose metadata path is to be retrieved.
     * @return The file path of the table's metadata file.
     */

    public static String getTableMDPath(String tablename){
        return User.getUserDBPath()+"/"+tablename+".json";
    }

    /**
     * Check if a table already exists in the user's database.
     *
     * @param tablename The name of the table to check for existence.
     * @return {@code true} if the table exists; {@code false} otherwise.
     */
    public static boolean doesTableExist(String tablename){
        String tablemdpath=Table.getTableMDPath(tablename);
        File table=new File(tablemdpath);
        return table.exists();
    }

    /**
     * Retrieve the metadata of the specified table.
     * <p>
     * Metadata includes column information, such as column names and data types.
     * </p>
     *
     * @param tablename The name of the table whose metadata is to be retrieved.
     * @return A list of {@code Column} objects representing the metadata of the table.
     */

    public static List<Column> getTableMD(String tablename){

        List<Column> md=new ArrayList<>();

        //Parse table meta-data json
        JSONParser parser=new JSONParser();

        try{
            JSONObject jsonobj=(JSONObject) parser.parse(new FileReader(Table.getTableMDPath(tablename)));
            JSONArray columns=(JSONArray) jsonobj.get("columns");

            for(Object columnobj: columns){
                JSONObject column=(JSONObject) columnobj;
                String name=(String) column.get("name");
                String type=(String) column.get("type");
                md.add(new Column(name,type));
            }
        }catch (Exception e){
            System.out.println("Failed to insert data");
        }
        return md;
    }

    /**
     * Check if a column with the specified name exists in the given table.
     *
     * @param tablename The name of the table in which to check for the column.
     * @param columname The name of the column to check for existence.
     * @return {@code true} if the column exists; {@code false} otherwise.
     */

    public static boolean doesColExist(String tablename, String columname){
        List<Column> columns=Table.getTableMD(tablename);

        for(Column col: columns){
            if(col.getName().trim().equalsIgnoreCase(columname.trim())){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the specified table contains any data.
     *
     * @param tablename The name of the table to check for data.
     * @return {@code true} if the table is empty; {@code false} otherwise.
     */

    public static boolean isTableEmpty(String tablename){
        try(BufferedReader br=new BufferedReader(new FileReader(Table.getTableMDPath(tablename).replace(".json",".csv")))){
                if(br.readLine()==null){
                    return true;
                }
        }catch (IOException e){
            System.out.println("Failed to read Table: "+tablename);
        }
        return false;
    }


}
