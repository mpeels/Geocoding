package com.example.Geocoder;

import java.util.List;

public record Response(Result result) {}

record Result(List<AddressMatch> addressMatches){}

record AddressMatch(String matchedAddress, Coordinates coordinates, String error){
    public AddressMatch(String error){
        this(null, null, error);
    }
}

record Coordinates(double x, double y){}


