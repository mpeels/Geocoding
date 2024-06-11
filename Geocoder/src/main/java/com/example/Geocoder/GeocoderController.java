package com.example.Geocoder;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;
import java.util.HashMap;


@RestController
class GeocoderController{
    @GetMapping("/geocoder")
    public Result getGeocode(){
        Result result = new Result("Hello World", true, "Delievered");
        return result;
    }

    @GetMapping("/geocoderMap")
    public Map<String, String> getGeocodeMap(){
        Map<String, String> result = new HashMap<>();
        result.put("response", "Hello World");
        result.put("status", "true");
        return result;
    }
}