package com.vladimir.patt.urlcrawler.crawl;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vladimir.patt.urlcrawler.entity.UrlToTitleMap;
import com.vladimir.patt.urlcrawler.service.UrlCrawlerService;

@Service
public class UrlCrawlerWorker {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private Map<String, UrlToTitleMap> pagesVisited = new HashMap<>();
	private Set<String> pagesToVisit = new TreeSet<>();

	@Autowired
	private UrlCrawlerService urlCrawlerService;

	public static class WorkerLeg {
		private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
		private final Logger log = LoggerFactory.getLogger(this.getClass());
		private List<String> links = new LinkedList<>(); // Just a list of URLs
		private Document htmlDocument; // This is our web page, or in other words, our document

		public boolean crawl(String url, int maxLinks) {
			try {
				Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
				Document htmlDocument = connection.get();
				this.htmlDocument = htmlDocument;
				if (connection.response().statusCode() == 200) {
					// 200 is the HTTP OK status code
					// indicating that everything is great.
					log.info("\n**Visiting** Received web page at " + url);
				}
				if (!connection.response().contentType().contains("text/html")) {
					log.info("**Failure** Retrieved something other than HTML");
					return false;
				}

				Elements linksOnPage = htmlDocument.select("a[href]");

				log.info("Found (" + linksOnPage.size() + ") links");
				for (Element link : linksOnPage) {
					if (maxLinks-- == 0) {
						break;
					}
					this.links.add(link.absUrl("href"));
				}
				return true;
			} catch (IOException ioe) {
				// We were not successful in our HTTP request
				return false;
			}
		}

		public String getTitle() {
			String title = this.htmlDocument.title();
			log.info("Title :: " + title);
			return title;
		}

		public List<String> getLinks() { // Returns a list of all the URLs on the page
			return this.links;
		}

	}

	private String nextUrl() {
		String nextUrl;
		do {
			nextUrl = this.pagesToVisit.iterator().next();
			this.pagesToVisit.remove(nextUrl);
		} while (this.pagesVisited.containsKey(nextUrl));
		return nextUrl;
	}

	public Collection<UrlToTitleMap> crawl(String url, int maxRef) {
		this.pagesToVisit.add(url);
		while (this.pagesVisited.size() < maxRef) {
			if (!pagesToVisit.isEmpty()) {
				int pagesToVisitNum = this.pagesToVisit.size();
				String currentUrl = this.nextUrl();
				Optional<UrlToTitleMap> urlToTitleMapOpt = urlCrawlerService.getUrlByLink(currentUrl);
				WorkerLeg leg = new WorkerLeg();
				leg.crawl(currentUrl, maxRef - (pagesToVisitNum + this.pagesVisited.size()));
				UrlToTitleMap urlToTitleMap;
				if (urlToTitleMapOpt.isPresent()) {
					urlToTitleMap = urlToTitleMapOpt.get();
				}
				else {
					String title = leg.getTitle();
					urlToTitleMap = new UrlToTitleMap(currentUrl, title);
					urlCrawlerService.saveUrl(urlToTitleMap);
				}
				this.pagesToVisit.addAll(leg.getLinks());
				this.pagesVisited.put(currentUrl, urlToTitleMap);	
			}
			else {
				break;
			}
		}
		log.info(String.format("**Done** Visited %s web page(s)", this.pagesVisited.size()));
		return this.pagesVisited.values();
	}
}
