package edu.galileo.baquiax.http;
import edu.galileo.baquiax.utils.Binary;
import edu.galileo.baquiax.utils.Text;
import edu.galileo.baquiax.utils.ContentType;
import edu.galileo.baquiax.utils.Utils;
import java.net.Socket;
import java.util.Date;
import java.util.ArrayList;
import java.io.File;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

public class HttpRequest implements Runnable {    
    private static String NOT_FOUND_FILENAME = "404.html";
    private static String BAD_REQUEST_FILENAME = "400.html";
    private Socket clientSocket;
    private long startTime;     

    public HttpRequest(Socket c) {
        this.startTime = System.currentTimeMillis();
        this.print("NEW HTTP REQUEST");
        this.clientSocket = c;
    }

    public void run() {
        try {
            String requestString = this.getStringFromInputStream();
            this.print(requestString);
            String fileName = null;
            if (requestString != null) {                
                String requestFirstLine = Utils.splitAndReturnElement(requestString, "\n", 0);
                if (requestFirstLine != null) {
                    fileName = Utils.splitAndReturnElement(requestFirstLine, " ", 1);      
                } 
            }       
            this.print("filename: " + fileName);
            if (fileName == null) {
                this.writeWithError(400);                                        
            } else {
                fileName = fileName.replace("\\", File.separator);
                fileName = fileName.replace("/", File.separator);
                this.writeWithSuccess(fileName);
            }

            this.clientSocket.close();
            long endTime = System.currentTimeMillis();
            long difference = endTime - this.startTime;
            Utils.addReponseTime(difference);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }        

    public ContentType getContentType(String fileName) {
        this.print("getContentType: " + fileName);
        String[] fileNameParts = fileName.split("\\.");
        if (fileNameParts.length > 0) {
            return Utils.getContentTypeForExtension(fileNameParts[fileNameParts.length - 1]);
        }
        return Utils.getContentTypeForExtension(null);
    }

    private void writeWithSuccess(String fileName) throws Exception {        
        String relativePath = Utils.getRelativePathToWWW(fileName);
        this.print("writeWithSuccess: " + relativePath);
        File file = new File(relativePath);
        if(!file.exists() || file.isDirectory()) {
            writeWithError(404);            
            return;
        }
        HttpStatus responseStatus = new HttpStatus("HTTP/1.1", 200, "OK");        
        ContentType ct = getContentType(fileName);
        
        this.write(file, responseStatus, ct);                
    }    

    private void writeWithError(int errorCode) throws Exception {
        String relativePath = Utils.getRelativePathToError(BAD_REQUEST_FILENAME);        
        ContentType ct = getContentType(BAD_REQUEST_FILENAME);
        String desc = "BAD REQUEST";
        switch (errorCode) {
            case 404:
                desc = "NOT FOUD";
                relativePath = Utils.getRelativePathToError(NOT_FOUND_FILENAME);
                ct = getContentType(NOT_FOUND_FILENAME);
                break;
            default:
        }
        this.print("writeWithError: " + relativePath + ", code:" + errorCode);
        
        File file = new File(relativePath);                  
        HttpStatus responseStatus = new HttpStatus("HTTP/1.1", errorCode, desc);
        this.write(file, responseStatus, ct);
    }


    private void write(File file, HttpStatus status, ContentType ct) throws Exception {                
        byte[] fileData = new byte[(int) file.length()];
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        dis.readFully(fileData);
        dis.close();                        
        
        String headers = this.prepareHeaders(status, ct.toString(), fileData.length);
        byte[] headersBytes = headers.getBytes();
        this.clientSocket.getOutputStream().write(headersBytes, 0, headersBytes.length);        
        this.clientSocket.getOutputStream().write(fileData, 0, fileData.length);        
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
            e.printStackTrace();
        }                
        return result;
    }

    private String prepareHeaders(HttpStatus statusReponse, String conteType, int length) {
        ArrayList<String> headers = new ArrayList<String>();        
        Date d = new Date();
        headers.add(statusReponse.toString());
        headers.add("Connection: close");            
        headers.add("Date:" + d);
        headers.add("Cache-Control: no-cache, no-store, must-revalidate");
        headers.add("Pragma: no-cache");
        headers.add("Expires: 0");
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