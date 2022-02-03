package com.vladimir.patt.urlcrawler.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vladimir.patt.urlcrawler.entity.UrlToTitleMap;


@Repository
public interface UrlRepository extends JpaRepository<UrlToTitleMap, Long>{

	Optional<UrlToTitleMap> findByUrl(String url);

}

