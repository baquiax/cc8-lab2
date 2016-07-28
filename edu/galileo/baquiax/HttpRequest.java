package edu.galileo.baquiax;
import java.net.Socket;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.*;

public class HttpRequest implements Runnable {
    private static String NOT_FOUND_FILENAME = "404.html";
    private Socket clientSocket;

    public HttpRequest(Socket c) {
        System.out.println("New connection");
        this.clientSocket = c;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(this.clientSocket.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                break;                
                //if We need read all headers
                //if (line.length() == 0) {
                //    break;
                //}
            }

            String fileName = NOT_FOUND_FILENAME;
            String[] parts = line.split(" ");
            String conteType = "text/html";
            if (parts.length == 3) {
                fileName = parts[1];                
            }

            if (fileName.equals("/")) {
                fileName = "\\index.html";
            } 
            
            if (fileName.endsWith("jpg")) {        
                conteType = "image/jpeg"; 
            }

            fileName = fileName.replace("\\", File.separator);
            fileName = fileName.replace("/", File.separator);
            System.out.println(System.getProperty("user.dir") + File.separator + "www" + fileName);                        
            
            Writer w = new OutputStreamWriter(this.clientSocket.getOutputStream(), "UTF-8");
            HttpStatus status = new HttpStatus("HTTP/1.1", 200, "OK");            

            String html = "";
            try {
                String realPath = System.getProperty("user.dir") + File.separator + "www" + fileName;
                if (conteType.equals("text/html")) {
                    BufferedReader in = new BufferedReader(new FileReader(realPath));                                        
                    String lineO;
                    while((lineO = in.readLine()) != null) {
                        html += lineO;
                    }
                    String headers = this.prepareHeaders(status, conteType, html.length());
                    w.write(headers);
                    w.write(html);
                } else {                                        
                    File file = new File(realPath);
                    byte[] fileData = new byte[(int) file.length()];
                    DataInputStream dis = new DataInputStream(new FileInputStream(file));
                    dis.readFully(fileData);
                    dis.close();
                    String headers = this.prepareHeaders(status, conteType, fileData.length);
                    byte[] headresBytes = headers.getBytes();                                        
                    this.clientSocket.getOutputStream().write(headresBytes, 0, headresBytes.length);
                    this.clientSocket.getOutputStream().write(fileData, 0, fileData.length);
                }                                                
            } catch (Exception e) {
                html = "NOT FOUND";
            }            
                                    
            w.close();            
            this.clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public String prepareHeaders(HttpStatus statusReponse, String conteType, int length) {
        ArrayList<String> headers = new ArrayList<String>();        
        Date d = new Date();
        headers.add(statusReponse.toString());        
        headers.add("Connection: close");            
        headers.add("Date:" + d);
        headers.add("Server: ChamanServer");            
        headers.add("Content-Length:" + length);
        headers.add("Content-Type: " + conteType);
        headers.add("\r\n");        
        return Utilis.joinArrayList("\r\n", headers);
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