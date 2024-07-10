package com.example.geocoder.responses;

public record AddressMatchResponse(String matchedAddress, CoordinatesResponse coordinates, String error) {
  public AddressMatchResponse(String matchedAddress, CoordinatesResponse coordinates) {
    this(matchedAddress, coordinates, null);
  }

  public AddressMatchResponse(String error) {
    this(null, null, error);
  }
}
