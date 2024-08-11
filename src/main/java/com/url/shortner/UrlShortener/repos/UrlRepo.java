package com.url.shortner.UrlShortener.repos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.url.shortner.UrlShortener.entities.UrlEntity;

public interface UrlRepo extends JpaRepository<UrlEntity, Long> {
	
	
	@Query(value = "select count(*) from url_entity where short_url = :x", nativeQuery = true)
	long countByShortUrl(@Param("x") String ShortUrl);
	
	@Query(value = "select * from url_entity where short_url = :x", nativeQuery = true)
	Optional<UrlEntity> getByShortUrl(@Param("x") String ShortUrl);
	
	@Query(value = "select * from url_entity where expiry_date < :x LIMIT :y", nativeQuery = true)
	List<UrlEntity> findUrlsToDelete(@Param("x") LocalDateTime expiryDate,@Param("y") long limit);

}
