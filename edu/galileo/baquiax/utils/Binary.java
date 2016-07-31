package edu.galileo.baquiax.utils;

public class Binary extends ContentType {
    private String contentType;
 
    public Binary(String ct) {
        this.contentType = ct;
    }
        
    public String toString() {
        return this.contentType;
    }
}
