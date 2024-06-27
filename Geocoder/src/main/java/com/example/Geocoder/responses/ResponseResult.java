package com.example.Geocoder.responses;
import java.util.List;

public record ResponseResult(List<AddressMatchResponse> addressMatches){}