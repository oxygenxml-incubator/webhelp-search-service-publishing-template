package ro.sync.search;

import java.net.MalformedURLException;

/**
 * The main class of the search service where all the necessary methods are
 * called
 * 
 * @author Artiom Bozieac
 *
 */
public class Main {
	public static void main(String[] args) {
		Crawler crawler;
		try {
			crawler = new Crawler("https://w3schools.com", "https://w3schools.com", false);
			crawler.crawl();
		} catch (MalformedURLException e) {
			System.out.println("An error occured when initializing URLs!");
		}
	}
}
