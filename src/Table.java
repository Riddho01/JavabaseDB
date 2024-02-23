public class Table {

    public static String getTableMDPath(String tablename){
        return User.getUserDBPath()+tablename+".json";
    }


}
