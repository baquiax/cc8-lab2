package edu.galileo.baquiax.http;
import edu.galileo.baquiax.utils.*;
import java.util.ArrayList;


public class HttpStatus {
    private ArrayList<String> componets;
     
    public HttpStatus(String version, int statusCode, String phrase) {
        this.componets = new ArrayList<String>();
        this.componets.add(version);
        this.componets.add(String.valueOf(statusCode));
        this.componets.add(phrase);            
    }

    public String toString() {            
        return Utils.joinArrayList(" ", componets);
    }
}