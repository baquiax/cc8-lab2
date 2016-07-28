package edu.galileo.baquiax.http;
import java.net.Socket;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.*;
import edu.galileo.baquiax.Utils;
import os.path;

public class HttpRequest implements Runnable {    
    private static String NOT_FOUND_FILENAME = "404.html";
    private static String BAD_REQUEST_FILENAME = "400.html";
    private Socket clientSocket;

    public HttpRequest(Socket c) {
        this.print("NEW HTTP REQUEST");
        this.clientSocket = c;
    }

    public void run() {
        try {
            String requestString = this.getStringFromInputStream();
            HttpStatus responseStatus;
            if (requestString != null) {
                String requestFirstLine = Utils.splitAndReturnElement(requestString, "\n", 0);
                if (requestFirstLine != null) {
                    String fileName = Utils.splitAndReturnElement(requestFirstLine, " ", 1);      
                } 
            }       

            if (fileName == null) {
                this.writeWithError(400);
                return;                        
            } 


            String line = new String(clientDataAsBytes);            
            this.print(line);
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

    public Utils.ContentType getContentType(fileName) {
        String[] fileNameParts = fileName.split(".");
        if (fileNameParts.length > 0) {
            return Utils.getContentTypeForExtension(fileNameParts[fileNameParts.length - 1]);
        }
        return Utils.getContentTypeForExtension(null);
    }

    private void writeWithFilename(String fileName) {
        Strign relativePath = Utils.getRelativePathToWWW(fileName);
        if (!os.path.isfile(relativePath)) {
            writeWithError(404);
            return;
        }

        HttpStatus responseStatus = new HttpStatus("HTTP/1.1", errorCode, "OK");
        Utils.ContentType ct = getContentType(fileName);

        File file = new File(relativePath);
        byte[] fileData = new byte[(int) file.length()];
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        dis.readFully(fileData);
        dis.close();        
              
        if (ct instanceof Binary) {
            String headers = this.prepareHeaders(responseStatus, ct, fileData.length);
            byte[] headersBytes = headers.getBytes();
            this.clientSocket.getOutputStream().write(headersBytes, 0, headersBytes.length);
            this.clientSocket.getOutputStream().write(fileData, 0, fileData.length);
        } else {

        }
    }    

    private void writeWithError(int errorCode) {
        Strign relativePath = Utils.getRelativePathToError(fileName);
        String desc = "BAD REQUEST";
        switch (errorCode) {
            case 404:
                desc = "NOT FOUD";
                break;
            default:

        }
        HttpStatus responseStatus = new HttpStatus("HTTP/1.1", errorCode, desc);
    }

    private String getStringFromInputStream() {
        String result = null;
        try {
            InputStream is = this.clientSocket.getInputStream();
            int inputLength = is.available();
            byte[] inputBytes = new byte[inputLength];
            is.read(inputBytes,0,inputLength);
            result = new String(inputBytes);
        } catch (Exception e) {
        }                
        return result
    }

    private String prepareHeaders(HttpStatus statusReponse, String conteType, int length) {
        ArrayList<String> headers = new ArrayList<String>();        
        Date d = new Date();
        headers.add(statusReponse.toString());        
        headers.add("Connection: close");            
        headers.add("Date:" + d);
        headers.add("Server: ChamanServer");            
        headers.add("Content-Length:" + length);
        headers.add("Content-Type: " + conteType);
        headers.add("\r\n");        
        return Utils.joinArrayList("\r\n", headers);
    }

    private void print(Object o) {
        System.out.println("CLIENT -> " + o.toString());
    }
}