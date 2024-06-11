package com.example.Geocoder;

class Result {
    private String response;
    private boolean status;
    private String delievered;

    public Result(){}
    public Result(String response, boolean status, String delievered){
        this.response = response;
        this.status = status;
        this.delievered = delievered;
    }

    public String getResponse(){
        return response;
    }
    public boolean getStatus(){
        return status;
    }
    public String getDelievered(){
        return delievered;
    }  

    public void setResponse(String response){
        this.response = response;
    }
    public void setStatus(boolean status){
        this.status = status;
    }
    public void setDelievered(String delievered){
        this.delievered = delievered;
    }
}
