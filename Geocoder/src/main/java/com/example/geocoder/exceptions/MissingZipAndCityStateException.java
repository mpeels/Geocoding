package com.example.geocoder.exceptions;

public class MissingZipAndCityStateException extends RuntimeException{
    public MissingZipAndCityStateException(){
        super("Please specify City and State and/or Zip Code.");
    }
}
