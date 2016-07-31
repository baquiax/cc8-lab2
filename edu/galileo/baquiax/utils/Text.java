package edu.galileo.baquiax.utils;

public class Text extends ContentType {
    private String contentType;
    public Text(String ct) {
        this.contentType = ct;
    }

    public String toString() {
        return this.contentType;
    } 
}