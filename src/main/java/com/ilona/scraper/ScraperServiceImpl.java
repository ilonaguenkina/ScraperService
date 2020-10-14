package com.ilona.scraper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class ScraperServiceImpl implements ScraperService {
	private static Map<String, Integer> idByCanonicalUrl = new HashMap<String, Integer>();
	private static Map<Integer, OGMetaData> metaDatasById  = new HashMap<Integer, OGMetaData>();
	private static Object lock = new Object();
	private static ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
	
	public int setUrlId(String url) throws FailureException {
		try {
			Document doc = Jsoup.connect(url).get();
			String canonicalUrl = getCanonicalUrl(doc, url);
			Integer id = idByCanonicalUrl.get(canonicalUrl);
			if (id == null) {
				id = createUrlId(doc, canonicalUrl);
			}
			
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FailureException(e);
		}
	}
	
	public OGMetaData getMetaData(int id) {
		return metaDatasById.get(id);
	}
	
	public void scrapeMetaData(Document doc, OGMetaData metadata) {
		Element head = doc.head();
		if (head != null) {
			Elements elements = head.select("meta[property=og:title]");
			if (!elements.isEmpty()) {
				String title = elements.first().attr("content");
				metadata.setTitle(title);
			}
			elements = head.select("meta[property=og:type]");
			if (!elements.isEmpty()) {
				String type = elements.first().attr("content");
				metadata.setType(type);
			}
			elements = head.select("meta[property=og:updated_time]");
			if (!elements.isEmpty()) {
				String updated_time = elements.first().attr("content");
				metadata.setUpdated_time(updated_time);
			}
			elements = head.select("meta[property=og:image]");
			if (!elements.isEmpty()) {
				for (int i = 0; i < elements.size(); i++) {
					Element element = elements.get(i);
					Image image = new Image(element.attr("content"));
					metadata.getImages().add(image);
					Element sibling = element.nextElementSibling();
					while (sibling != null && sibling.attr("property").startsWith("og:image:")) {
						String name = sibling.attr("property");
						name = name.substring(9);
						switch (name) {
							case "type":
								image.setType(sibling.attr("content"));
								break;
							case "width":
								image.setType(sibling.attr("content"));
								break;
							case "height":
								image.setType(sibling.attr("content"));
								break;
							case "alt":
								image.setType(sibling.attr("content"));
								break;
							default:
								break;
						}
						sibling = sibling.nextElementSibling();
					}
				}
			}
		}
		metadata.setScrape_status("Done");
		metaDatasById.put(metadata.getId(), metadata);
	}
	
	private String getCanonicalUrl(Document doc, String url) {
		String canonicalUrl = null;
		Element head = doc.head();
		if (head != null) {
			Elements elements = head.select("link[rel=canonical]");
			if (!elements.isEmpty()) {
				canonicalUrl = elements.first().attr("href");
			}
			
			if (canonicalUrl == null || canonicalUrl.isEmpty()) {
				elements = head.select("meta[property=og:url]");
				if (!elements.isEmpty()) {
					canonicalUrl = elements.first().attr("content");
				}
			}
		}
		
		if (canonicalUrl == null || canonicalUrl.isEmpty()) {
			canonicalUrl = url;
		}
		
		return canonicalUrl;
	}
	
	private int createUrlId(Document doc, String canonicalUrl) {
		synchronized (lock) {
			Integer id = idByCanonicalUrl.get(canonicalUrl);
			if (id != null) {
				return id;
			}
			
			id = canonicalUrl.hashCode();
			idByCanonicalUrl.put(canonicalUrl, id);
			OGMetaData metaData = new OGMetaData(id, canonicalUrl);
			metaDatasById.put(id, metaData);
			executor.submit(new UrlScraper(doc, metaData, this));
			return id;
		}
	}
}
