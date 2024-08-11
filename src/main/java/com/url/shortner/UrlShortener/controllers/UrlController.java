package com.url.shortner.UrlShortener.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.url.shortner.UrlShortener.dtos.UrlResponseDto;
import com.url.shortner.UrlShortener.services.UrlService;

@RestController
public class UrlController {

	@Autowired
	UrlService urlService;
	
	
	@PostMapping("/shorten")
	public ResponseEntity<?> shortenUrl(@RequestParam String destinationUrl){
		
		try {
			UrlResponseDto status = urlService.shortenUrl(destinationUrl);
			
			return new ResponseEntity<>(status, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/update/url")
    public ResponseEntity<?> updateUrl(@RequestParam String shortUrl, @RequestParam String destinationUrl) {
        try {
            boolean isUpdated = urlService.updateUrl(shortUrl, destinationUrl);
            
                return new ResponseEntity<>(isUpdated, HttpStatus.OK);
           
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
	
	@GetMapping("/{shortenString}")
	public ResponseEntity<?> redirectToFullUrl(@PathVariable String shortenString) {
		
		try {
			
			RedirectView redirectView = urlService.getDestinationUrl(shortenString);
			
			URI redirectUri = new URI(redirectView.getUrl());
			
			return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/update/expiry")
	public ResponseEntity<?> updateExpiry(@RequestParam String shortUrl, @RequestParam int daysToAdd) {
		
		try {
			boolean isUpdated = urlService.updateExpiry(shortUrl, daysToAdd);
			
			return new ResponseEntity<>(isUpdated, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
		}
		
	}
}
