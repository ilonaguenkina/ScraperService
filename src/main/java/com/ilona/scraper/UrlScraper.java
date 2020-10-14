package com.ilona.scraper;

import org.jsoup.nodes.Document;

public class UrlScraper implements Runnable {
	private Document doc;
	private OGMetaData metadata;
	private ScraperServiceImpl service;

	public UrlScraper(Document doc, OGMetaData metadata, ScraperServiceImpl service) {
		this.doc = doc;
		this.metadata = metadata;
		this.service = service;
	}

	@Override
	public void run() {
		service.scrapeMetaData(doc, metadata);
	}

}
