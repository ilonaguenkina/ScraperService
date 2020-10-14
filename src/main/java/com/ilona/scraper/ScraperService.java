package com.ilona.scraper;

public interface ScraperService {
	public int setUrlId(String url) throws FailureException;
	public OGMetaData getMetaData(int id);
}
