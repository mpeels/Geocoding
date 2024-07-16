package com.example.geocoder.exceptions;

public class MissingStreetException extends RuntimeException {
    public MissingStreetException(){
        super("Street address cannot be empty and cannot exceed 100 characters, Specify House number and Street name along with City and State and/or ZIP Code.");
    }
}
