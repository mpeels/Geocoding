package com.example.geocoder.responses;

public class CensusApiResponse {

    private ResultResponse result;
    private boolean rateLimitPass;

    public CensusApiResponse(){}
    public CensusApiResponse(ResultResponse result, boolean rateLimitPass){
        this.result = result;
        this.rateLimitPass = rateLimitPass;
    }

    public ResultResponse getResult(){
        return this.result;
    }
    public void setResult(ResultResponse result){
        this.result = result;
    }

    public boolean getRateLimitPass(){
        return this.rateLimitPass;
    }
    public void setRateLimitPass(boolean rateLimitPass){
        this.rateLimitPass = rateLimitPass;
    }

}
