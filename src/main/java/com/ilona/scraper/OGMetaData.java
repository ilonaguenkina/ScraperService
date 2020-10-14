package com.ilona.scraper;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class OGMetaData implements Serializable {
	private int id;
	private String url;
	private String type;
	private String title;
	private List<Image> images = new LinkedList<Image>();
	private String updated_time;
	private String scrape_status;
	
	public OGMetaData() {
	}

	public OGMetaData(int id, String url) {
		this.id = id;
		this.url = url;
		this.scrape_status = "Pending";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getScrape_status() {
		return scrape_status;
	}

	public void setScrape_status(String scrape_status) {
		this.scrape_status = scrape_status;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public String getUpdated_time() {
		return updated_time;
	}

	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}
}
