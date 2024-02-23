import java.io.File;

public class Table {

    public static String getTableMDPath(String tablename){
        return User.getUserDBPath()+"/"+tablename+".json";
    }

    public static boolean doesTableExist(String tablename){
        File table=new File(Table.getTableMDPath(tablename));
        return table.exists();
    }


}
