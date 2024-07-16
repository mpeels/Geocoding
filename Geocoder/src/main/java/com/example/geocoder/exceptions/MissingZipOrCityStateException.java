package com.example.geocoder.exceptions;

public class MissingZipOrCityStateException extends RuntimeException{
    public MissingZipOrCityStateException(){
        super("Please specify City and State and/or Zip Code.");
    }
}
