package com.example.Geocoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class GeocoderApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeocoderApplication.class, args);

		String url ="https://geocoding.geo.census.gov/geocoder/locations/address?street=1600%20Pennsylvania%20Avenue%20NW&city=Washington&state=&zip=DC%2020500&benchmark=4&format=json";

		WebClient.Builder builder = WebClient.builder();

		String address = builder.build()
			.get()
			.uri(url)
			.retrieve()
			.bodyToMono(String.class)
			.block();

		System.out.println("-------------------------------------------------------");
		System.out.println(address);
		System.out.println("-------------------------------------------------------");

		
	}

}
