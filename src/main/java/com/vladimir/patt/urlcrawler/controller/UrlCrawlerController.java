package com.vladimir.patt.urlcrawler.controller;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vladimir.patt.urlcrawler.crawl.UrlCrawlerWorker;
import com.vladimir.patt.urlcrawler.entity.UrlToTitleMap;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UrlCrawlerController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UrlCrawlerWorker urlCrawlerWorker;
	
	@GetMapping(value = {"/", "/home"})
	public String homePage() {
		return "index";
	}
	
	@GetMapping(value = {"/crawl"})
	public Collection<UrlToTitleMap> crawlPage(@RequestParam("url") String url, @RequestParam("number") Integer maxLinks, 
			HttpServletResponse response, Optional<UrlToTitleMap> urlToTitleMap) {
		log.info("URL :: " + url);
		log.info("number :: " + maxLinks);
			
		Collection<UrlToTitleMap> urls =  urlCrawlerWorker.crawl(url.replaceAll("’", ""), maxLinks);
		return urls;
	}
}
