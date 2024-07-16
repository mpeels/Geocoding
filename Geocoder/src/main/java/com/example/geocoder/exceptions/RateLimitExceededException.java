package com.example.geocoder.exceptions;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(){
        super("You have exceed the limit please try again");
    }
}
