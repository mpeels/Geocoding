package com.example.Geocoder.responses;
import java.util.List;

public record ResultResponse(List<AddressMatchResponse> addressMatches){}