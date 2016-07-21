package edu.galileo.baquiax;
import java.net.Socket;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.ArrayList;

public class HttpRequest implements Runnable {
    private static String NOT_FOUND_FILENAME = "404.html";
    private Socket clientSocket;

    public HttpRequest(Socket c) {
        System.out.println("New connection");
        this.clientSocket = c;
    }

    public void run() {
        try {
            String fileName = "index.html";
            Writer w = new OutputStreamWriter(this.clientSocket.getOutputStream(), "UTF-8");
            HttpStatus status = new HttpStatus("HTTP/1.1", 200, "OK");
            System.out.println(getHTTPReponse(status, "Hola"));
            w.write(getHTTPReponse(status, "Hola"));                            
            w.close();            
            this.clientSocket.close();
        } catch (Exception e) {
            System.out.println("Error al contestar");
        }
        
    }

    public String getHTTPReponse(HttpStatus statusReponse, String htmlResponse) {
        ArrayList<String> headers = new ArrayList<String>();        
        Date d = new Date();
        headers.add(statusReponse.toString());        
        headers.add("Connection: close");            
        headers.add("Date:" + d);
        headers.add("Server: ChamanServer");            
        headers.add("Content-Length:" + htmlResponse.getBytes().length);
        headers.add("Content-Type: text/html");
        headers.add("\r\n");
        
        return joinArrayList("\r\n", headers).concat(htmlResponse);
    }

    private String joinArrayList (String joiner, ArrayList<String> array) {
        String result = "";
        for (int i = 0; i < array.size(); i++) {
            result += array.get(i);
            if (i < array.size() - 1) {
                result += joiner;
            }
        }
        return result;
    }

    final class HttpStatus {
        private ArrayList<String> componets; 

        public HttpStatus(String version, int statusCode, String phrase) {
            this.componets = new ArrayList<String>();
            this.componets.add(version);
            this.componets.add(String.valueOf(statusCode));
            this.componets.add(phrase);            
        }

        public String toString() {            
            return joinArrayList(" ", componets);
        }
    } 
}