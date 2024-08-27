/**
 * The {@code Column} class represents a column in a table,
 * containing name and datatype attributes.
 */
public class Column {

    /**Name of the column. */
    private String name;

    /**
     * Datatype of the column.
     * Allowed datatypes are {@code int}, {@code decimal}, and {@code varchar}.
     */
    private String datatype;

    public Column(String name, String datatype) {
        this.name = name;
        this.datatype = datatype;
    }


    public String getName() {
        return name;
    }
    public String getDatatype() {
        return datatype;
    }
}
