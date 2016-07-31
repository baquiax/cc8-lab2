package edu.galileo.baquiax.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Utils {
    public static int MAX_THREADS = 1;        
    private static ArrayList<Long> responseTime = new ArrayList<Long>();
    private static String REPORT_FILENAME = "report.csv"; 
    
    public static void writeReport( ) {        
        Utils.print("Writing report!");
        if (Utils.responseTime.size() == 0) return;
        //Avg.
        long sum = 0;
        for(int i = 0; i < responseTime.size(); i++) {
            sum += Utils.responseTime.get(i);
        }
        long avg = sum / responseTime.size();
        try {                
            Writer output = new BufferedWriter(new FileWriter(REPORT_FILENAME, true));                                 
            output.append(MAX_THREADS +"," + avg + "\n");                
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }

    public static void addReponseTime(long time) {
        Utils.responseTime.add(time);
        Utils.print("Responses: " + responseTime.size());        
    }

    private static HashMap<String, ContentType> knownContentTypes = new HashMap<String, ContentType>() {{
        put("jpg", new Binary("image/jpeg"));
        put("html", new Text("text/html"));
    }};

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

    public static String splitAndReturnElement(String string, String delimiter,int index) {
        if (string == null || delimiter == null) return null;

        String[] elements = string.split(delimiter);
        if (elements.length < index + 1) {
            return null;
        }
        return elements[index];
    }

    private static String getRelativePathTo(String dir, String fileName) {
        return System.getProperty("user.dir") + File.separator + dir + fileName;
    }

    public static String getRelativePathToError(String fileName) {
        return getRelativePathTo("errors", File.separator + fileName);        
    }

    public static String getRelativePathToWWW(String fileName) {
        return getRelativePathTo("www", fileName);        
    }

    public static ContentType getContentTypeForExtension(String extension) {
        Utils.print(extension);
        ContentType ct = Utils.knownContentTypes.get(extension);
        if (ct == null) {
            return new Binary("application/octet-stream");
        }
        return ct;
    }   

    private static void print(String s) {
        System.out.println("Utils-> " + s);
    } 
}