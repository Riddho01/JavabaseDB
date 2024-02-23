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

}
