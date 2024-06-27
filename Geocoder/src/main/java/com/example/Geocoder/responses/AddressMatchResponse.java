package com.example.Geocoder.responses;

public record AddressMatchResponse(String matchedAddress, CoordinatesResponse coordinates, String error){
    public AddressMatchResponse(String error){
        this(null, null, error);
    }
}
