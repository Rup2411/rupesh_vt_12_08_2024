package com.url.shortner.UrlShortener.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "url_entity", indexes = @Index(name = "idx_expiry_date", columnList = "expiry_date"))
public class UrlEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "short_url", unique = true, nullable = false)
	private String shortUrl;
	
	@Column(name = "long_url", nullable = false)
	private String destinationUrl;
	
	@Column(name = "expiry_date", nullable = false)
	private LocalDateTime expiryDate;
}
