/**
 * The {@code Util} class provides utility functions for data type verification
 * and condition checking within SQL operations. It includes methods to verify
 * the type for a given value and to evaluate conditions in a "where" clause.
 */
public class Util {

    /**
     * Verifies whether the given value matches the expected data type.
     * <p>
     * This method checks if a value conforms to the specified type. Supported types are:
     * "int" for integers, "decimal" for floating-point numbers, and "varchar" for strings
     * enclosed in quotes.
     * </p>
     *
     * @param value The value to be verified.
     * @param type  The expected data type of the value ("int", "decimal", "varchar").
     * @return {@code true} if the value matches the expected type, {@code false} otherwise.
     */
    public static boolean verifyType(String value, String type){
        try{
            if(type.toLowerCase().equalsIgnoreCase("int")){
                Integer.parseInt(value);
            }
            else if(type.toLowerCase().equalsIgnoreCase("decimal")){
                Double.parseDouble(value);
            }
            else{
                if(!(value.trim().startsWith("'") && value.trim().endsWith("'"))){
                    return false;
                }
            }

        }catch (NumberFormatException e){
            return false;
        }
        //If type is varchar return true
       return true;
    }


    /**
     * Evaluates a condition in an SQL command's "where" clause.
     * <p>
     * Check whether the specified condition is true by comparing the column value
     * against the provided value using the specified operator. It supports comparison of both
     * numeric and string values.
     * </p>
     *
     * @param column_val The value from the column to be compared.
     * @param value      The value to compare against.
     * @param operator   The comparison operator to use ("=", "!=", "<", "<=", ">", ">=").
     * @return {@code true} if the condition is met, {@code false} otherwise.
     */
    public static boolean checkCondition(String column_val, String value, String operator) {
        try {

            //Typecast numeric data to double for comparison
            double valueNumeric = Double.parseDouble(value);
            double column_valNumeric = Double.parseDouble(column_val);

           //Check operator
            if(operator.equalsIgnoreCase("=")){
                return column_valNumeric==valueNumeric;
            }
            else if(operator.equalsIgnoreCase("!=")){
                return column_valNumeric!=valueNumeric;
            }
            else if(operator.equalsIgnoreCase("<")){
                return column_valNumeric<valueNumeric;
            }
            else if(operator.equalsIgnoreCase("<=")){
                return column_valNumeric<=valueNumeric;
            }
            else if(operator.equalsIgnoreCase(">")){
                return column_valNumeric>valueNumeric;
            }
            else if(operator.equalsIgnoreCase(">=")){
                return column_valNumeric>=valueNumeric;
            }
            else{
                return false;
            }

        } catch (NumberFormatException e) {

            //For Strings, check equality
            if (operator.equals("=")) {
                return column_val.equals(value);
            }
        }
        return false;
    }

}
