package edu.galileo.baquiax.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;

public class Utils {
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