public class Column {

    //Column name attribute
    private String name;

    //Column datatype attribute
    private String datatype;

    public Column(String name, String datatype) {
        this.name = name;
        this.datatype = datatype;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public String getDatatype() {
        return datatype;
    }
}
