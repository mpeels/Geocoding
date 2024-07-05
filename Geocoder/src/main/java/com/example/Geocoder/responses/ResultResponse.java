package com.example.Geocoder.responses;
import java.util.List;

public record ResultResponse(List<AddressMatchResponse> addressMatches){

    public ResultResponse(AddressMatchResponse matches) {
        this(List.of(matches));
    }
    
}