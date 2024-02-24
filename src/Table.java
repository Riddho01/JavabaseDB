import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
public class Table {

    public static String getTableMDPath(String tablename){
        return User.getUserDBPath()+"/"+tablename+".json";
    }

    public static boolean doesTableExist(String tablename){
        String tablemdpath=Table.getTableMDPath(tablename);
        File table=new File(tablemdpath);
        return table.exists();
    }

    //Get table meta data
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

    //Check if column exists in table
    public static boolean doesColExist(String tablename, String columname){
        List<Column> columns=Table.getTableMD(tablename);

        for(Column col: columns){
            if(col.getName().trim().equalsIgnoreCase(columname.trim())){
                return true;
            }
        }
        return false;
    }



}
