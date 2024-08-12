package com.url.shortner.UrlShortener.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import com.url.shortner.UrlShortener.dtos.UrlResponseDto;
import com.url.shortner.UrlShortener.entities.UrlEntity;
import com.url.shortner.UrlShortener.repos.UrlRepo;

@Service
public class UrlService {

	
	@Autowired
	UrlRepo urlRepo;
	
	private static final long BATCH_SIZE = 1000;
	
	
	public UrlResponseDto shortenUrl(String destinationUrl) {
		
		String shortUrl = generateShortUrl();
		
		long count = urlRepo.countByShortUrl(shortUrl);
		
		UrlEntity entity = new UrlEntity();
		
		entity.setDestinationUrl(destinationUrl);
		if(count > 0) {
			String shUrl = generateShortUrl();
			
			entity.setShortUrl(shUrl);
		}
		else {
			entity.setShortUrl(shortUrl);
		}
		entity.setExpiryDate(LocalDateTime.now().plusMonths(10));
		
		UrlEntity savedEntity = urlRepo.save(entity);
		
		UrlResponseDto dto = new UrlResponseDto();
		
		dto.setDestinationUrl(savedEntity.getDestinationUrl());
		dto.setShortUrl(savedEntity.getShortUrl());
		dto.setExpiryDate(savedEntity.getExpiryDate());
		
		return dto;
		
	}
	
	private String generateShortUrl() {
		return UUID.randomUUID().toString().substring(0,8);
	}
	
	
	public boolean updateUrl(String shortUrl, String destinationUrl) throws Exception {
		
		Optional<UrlEntity> urlEntity = urlRepo.getByShortUrl(shortUrl);
		
		if(urlEntity.isPresent()) {
			
				urlEntity.get().setDestinationUrl(destinationUrl);
				
				urlRepo.save(urlEntity.get());
				
				return true;
		}
		else {
			throw new Exception("Please Provide a valid URL");
		}
		
	}
	
	
	public RedirectView getDestinationUrl(String shortUrl) throws Exception {
		
		Optional<UrlEntity> urlEntity = urlRepo.getByShortUrl(shortUrl);
		
		if(urlEntity.get().getExpiryDate().isBefore(LocalDateTime.now())) {
			throw new Exception("The URL has expired");
		}
		
		if(urlEntity.isPresent()) {
			
			String fullUrl = urlEntity.get().getDestinationUrl();
			
			RedirectView redirect = new RedirectView();
			
			redirect.setUrl(fullUrl);
			
			return redirect;
			
		}
		else {
			throw new Exception("Please Provide a valid URL");
		}
	}
	
	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteExpiredUrls() {
		List<UrlEntity> expiredUrls;
		do {
			expiredUrls = urlRepo.findUrlsToDelete(LocalDateTime.now(), BATCH_SIZE);
			
			urlRepo.deleteAll(expiredUrls);
		}
		while(expiredUrls.size() == BATCH_SIZE);
	}
	
	public boolean updateExpiry(String shortUrl, int daysToAdd) throws Exception {
		
		Optional<UrlEntity> urlEntity = urlRepo.getByShortUrl(shortUrl);
		
		if(urlEntity.isPresent()) {
			
			LocalDateTime addDays = urlEntity.get().getExpiryDate().plusDays(daysToAdd);
			
			urlEntity.get().setExpiryDate(addDays);
			
			urlRepo.save(urlEntity.get());
			
			return true;
		}
		else {
			throw new Exception("Please Provide a valid URL");
		}
	}
	
	
}
