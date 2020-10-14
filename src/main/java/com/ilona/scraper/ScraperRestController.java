package com.ilona.scraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScraperRestController {
	
	@Autowired
	ScraperService service;
	
	@PostMapping("/stories")
	public int setUrlId(@RequestParam String url) throws FailureException {
		return service.setUrlId(url);
	}

	@GetMapping("/stories/{urlId}")
	public OGMetaData getOGMetaData(@PathVariable  int urlId) {
		return service.getMetaData(urlId);
	}
}
