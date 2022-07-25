package ro.sync.search;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.Query;

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
			JsonHandler jsonHandler = new JsonHandler(crawler.getCrawledPages());
		} catch (MalformedURLException e) {
			logger.error("An error occured when initializing URLs!");
		}

		try (SearchClient client = DefaultSearchClient.create("KLFWXPOEHY", "87c939ac9269c88a17beeaacca28567a");) {
			SearchIndex<Page> index = client.initIndex("Syncro pages", Page.class);
			index.saveObjects(crawler.getCrawledPages());

			logger.info(index.search(new Query("Internship")).toString());
		} catch (Exception e) {
			logger.error("An error occured when performing Algolia API calls!");
			e.printStackTrace();
		}

	}
}
