package edu.galileo.baquiax;
public class Utils {
    
    public static String joinArrayList (String joiner, ArrayList<String> array) {
        String result = "";
        for (int i = 0; i < array.size(); i++) {
            result += array.get(i);
            if (i < array.size() - 1) {
                result += joiner;
            }
        }
        return result;
    }
}