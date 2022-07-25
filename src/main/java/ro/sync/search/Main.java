package ro.sync.search;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class of the search service where all the necessary methods are
 * called
 * 
 * @author Artiom Bozieac
 *
 */
public class Main {
	/**
	 * Logger to inform user about certain actions like errors and others.
	 */
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		Crawler crawler = null;
		try {
			crawler = new Crawler("https://www.sync.ro", "https://www.sync.ro", false);
			crawler.crawl();
			AlgoliaHandler.addIndex("Syncro pages", crawler.getCrawledPages());
		} catch (MalformedURLException e) {
			logger.error("An error occured when initializing URLs!");
		}
	}
}
