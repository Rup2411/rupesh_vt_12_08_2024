package com.url.shortner.UrlShortener.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlResponseDto {

	private String shortUrl;
	
	@NotBlank(message = "Need to provide a destination url")
	private String destinationUrl;
	
	private LocalDateTime expiryDate;
}
