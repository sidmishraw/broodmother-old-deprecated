/**
 * Project: BroodMother
 * Package: io.github.sidmishraw.broodmother.core
 * File: Spiderling.java
 * 
 * @author sidmishraw
 *         Last modified: Jun 6, 2017 9:04:54 PM
 */
package io.github.sidmishraw.broodmother.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author sidmishraw
 *
 *         Qualified Name: io.github.sidmishraw.broodmother.core.Spiderling
 *
 */
public class Spiderling implements Runnable {
	
	/**
	 * 
	 * @author sidmishraw
	 *
	 *         Qualified Name:
	 *         io.github.sidmishraw.broodmother.core.SpiderlingConstants
	 * 
	 *         The constants defined for use by the Spiderling instance
	 *
	 */
	private static class SpiderlingConstants {
		
		private static final String	ANCHOR_TAG	= "a";
		private static final String	HREF		= "href";
	}
	
	/**
	 * The set of all the links that have been visited by the spiderling.
	 */
	private Set<String>	visitedURLs	= new HashSet<>();
	
	/**
	 * The base URL from which the Spiderling starts crawling
	 */
	private String		baseURL;
	
	/**
	 * The pattern that is used by the spiderling to filter out the outgoing
	 * links.
	 */
	private String		crawlableURLPattern;
	
	/**
	 * Initializes a spiderling with baseURL as baseURL and a crawlable filter
	 * pattern.
	 * 
	 * @param baseURL
	 *            - The base URL from which the spiderling starts crawling
	 * @param crawlableURLPattern
	 *            - The URL pattern that the spiderling uses to filter out other
	 *            crawlable URLs.
	 */
	public Spiderling(String baseURL, String crawlableURLPattern) {
		
		this.baseURL = baseURL;
		this.crawlableURLPattern = crawlableURLPattern;
	}
	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~DO NOT USE FOR LIVE CODE~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * 
	 * 
	 * Dummy method for writing out to a file for debugging purposes
	 */
	private void writeToFile(String s) {
		
		try (BufferedWriter br = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(new File("output.txt"), true)))) {
			
			br.write(s);
			br.write("\n");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * The spiderling crawls the URL passed into this method.
	 * 
	 * @param urlString
	 *            - The URL to crawl on
	 * @throws Exception
	 */
	private void crawl(String urlString) throws Exception {
		
		System.out.println("Crawling : " + urlString);
		writeToFile(urlString);
		
		this.visitedURLs.add(urlString);
		
		Document htmlDoc = Jsoup.connect(urlString).get();
		
		// ----------------------
		// TODO: Index the document here -- call to the indexing service
		// ----------------------
		
		Elements outgoingLinks = htmlDoc.select(
				SpiderlingConstants.ANCHOR_TAG + "[" + SpiderlingConstants.HREF + "]");
		
		// build he willVisitURLs
		for (Element link : outgoingLinks) {
			
			String linkURL = link.absUrl(SpiderlingConstants.HREF).toLowerCase();
			
			if ((!this.visitedURLs.contains(linkURL))
					&& linkURL.matches(this.getFilterPattern())
					&& validCrawlableURL(linkURL)) {
				
				try {
					
					crawl(linkURL);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
			
		}
	}
	
	/**
	 * Checks if the URL string is valid. That is, the page can be downloaded
	 * and viewed in the browser.
	 * 
	 * @param urlString
	 *            - the URL that is to be checked
	 * @return true if it is valid else false
	 */
	private boolean validCrawlableURL(String urlString) {
		
		if (urlString.matches(".*\\.(py|zip|xls|pdf)")) {
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * Generates the filter pattern
	 * 
	 * @return The pattern for filtering the next set of URLs to visit
	 */
	private String getFilterPattern() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(".*")
				.append(this.crawlableURLPattern)
				.append(".*");
		
		return buffer.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		System.out.println("Started Crawl");
		
		try {
			
			this.crawl(this.baseURL.toLowerCase());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		System.out.println("Finished Crawl");
	}
	
	/**
	 * @return the baseURL
	 */
	public String getBaseURL() {
		
		return this.baseURL;
	}
	
	/**
	 * @param baseURL
	 *            the baseURL to set
	 */
	public void setBaseURL(String baseURL) {
		
		this.baseURL = baseURL;
	}
	
	/**
	 * @return the crawlableURLPattern
	 */
	public String getCrawlableURLPattern() {
		
		return this.crawlableURLPattern;
	}
	
	/**
	 * @param crawlableURLPattern
	 *            the crawlableURLPattern to set
	 */
	public void setCrawlableURLPattern(String crawlableURLPattern) {
		
		this.crawlableURLPattern = crawlableURLPattern;
	}
	
	/**
	 * @return the visitedURLs
	 */
	public Set<String> getVisitedURLs() {
		
		return this.visitedURLs;
	}
}
