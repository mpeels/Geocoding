package com.example.Geocoder;

public class Response {
    private String text;
    private boolean status;
    public Response() {}
    public Response(String text, boolean status) {
        this.text = text;
        this.status = status;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
}
