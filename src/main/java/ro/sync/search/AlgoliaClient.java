package ro.sync.search;

import java.net.MalformedURLException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;

/**
 * Class that handles Algolia API calls.
 * 
 * @author Artiom Bozieac
 *
 */
public class AlgoliaClient {
	/**
	 * Logger to inform user about certain actions like errors and others.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AlgoliaClient.class);
	/**
	 * Crawler that crawls a URL and extracts data from it.
	 */
	private Crawler crawler;
	/**
	 * Algolia application's id.
	 */
	private String appId = "KLFWXPOEHY";
	/**
	 * Algolia admin's key to perform actions.
	 */
	private String adminApiKey = "87c939ac9269c88a17beeaacca28567a";
	/**
	 * Client that performs operations such as indices management.
	 */
	private SearchClient client = DefaultSearchClient.create(appId, adminApiKey);
	/**
	 * Index that stores the current index performing actions on;
	 */
	private SearchIndex<Page> index;

	/**
	 * Constructor with URL to get data from.
	 * 
	 * @param url     is the URL of the file to get data from.
	 * @param baseUrl is the base URl to not go out of bounds.
	 */
	public AlgoliaClient(final String url, final String baseUrl) {
		try {
			crawler = new Crawler(url, baseUrl);
			crawler.crawl();
		} catch (MalformedURLException e) {
			logger.error("An error occured when trying to connect {}", url);
			logger.error(Arrays.toString(e.getStackTrace()));
		}
	}

	/**
	 * Initialize a new index containing all the pages.
	 * 
	 * @param indexName is the name to be assigned to index.
	 */
	public void initIndex(final String indexName) {
		index = client.initIndex(indexName, Page.class);
		logger.info("Index {} succesfully created!", indexName);
	}

	/**
	 * Adds crawled pages from Crawler object to index.
	 */
	public void addObjectToIndex() {
		index.saveObjects(crawler.getCrawledPages());
		logger.info("{} Page objects successfully added to {} index!", crawler.getCrawledPages().size(),
				index.getUrlEncodedIndexName());
	}
}
