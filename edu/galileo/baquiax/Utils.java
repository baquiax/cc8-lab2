package edu.galileo.baquiax;
import java.util.ArrayList;

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

    public String splitAndReturnElement(String string, String delimiter,int index) {
        if (string == null || delimiter == null) return null;

        String[] elements = string.split(delimiter);
        if (elements.count < index + 1) {
            return null;
        }
        return elements[index];
    }

    private String getRelativePathTo(String dir, String fileName) {
        return System.getProperty("user.dir") + File.separator + dir + fileName;
    }

    public String getRelativePathToError(String fileName) {
        return getRelativePathTo("www", File.separator + fileName);        
    }

    public String getRelativePathToWWW(String fileName) {
        return getRelativePathTo("errors", fileName);        
    }

    public ContentType getContentTypeForExtension(String extension) {
        ContentType ct = Utils.knownContentTypes.get(extension);
        if (ct == null) {
            return new Binary("application/octet-stream")
        }
        return ct;
    }

    final abstract class ContentType { }

    final class Binary extends ContentType {
        private String contentType;
        public Binary(String ct) {
            this.contentType = ct;
        } 
    }

    final class Text extends ContentType {
        private String contentType;
        public Text(String ct) {
            this.contentType = ct;
        } 
    }
}