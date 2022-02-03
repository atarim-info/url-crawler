package com.vladimir.patt.urlcrawler.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vladimir.patt.urlcrawler.entity.UrlToTitleMap;
import com.vladimir.patt.urlcrawler.repository.UrlRepository;

@Service
public class UrlCrawlerService {
	@Autowired
	private UrlRepository urlRepository;
	
	public void saveUrl(UrlToTitleMap urlToTitleMap) {
		urlRepository.save(urlToTitleMap);
	}

	public List<UrlToTitleMap> getAllUrls() {
		return urlRepository.findAll();
	}

	public Optional<UrlToTitleMap> getUrlById(Long id) {
		return urlRepository.findById(id);
	}
	
	public Optional<UrlToTitleMap> getUrlByLink(String url) {
		return urlRepository.findByUrl(url);
	}

}
