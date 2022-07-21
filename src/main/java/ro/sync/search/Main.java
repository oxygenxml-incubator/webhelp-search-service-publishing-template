package ro.sync.search;

import java.net.MalformedURLException;
import java.util.List;

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

		Crawler crawler;
		try {
			crawler = new Crawler("https://www.sync.ro", "https://www.sync.ro", false);
			crawler.crawl();
			List<Page> pages = crawler.getCrawledPages();

			for (Page page : pages) {
				logger.info(page.getTitle());
				logger.info(page.getKeywords().toString());
				logger.info(page.getContents());
			}
		} catch (MalformedURLException e) {
			logger.error("An error occured when initializing URLs!");
		}
	}
}
