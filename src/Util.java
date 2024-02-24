public class Util {

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

    //Check where clause condition
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
