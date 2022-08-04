package ro.sync.search;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.settings.IndexSettings;

/**
 * Class that handles Algolia API calls.
 * 
 * @author Artiom Bozieac
 */
public class AlgoliaClient {
	/**
	 * Logger to inform user about certain actions like errors and others.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AlgoliaClient.class);
	/**
	 * Algolia application's id.
	 */
	private String appId;
	/**
	 * Algolia admin's key to perform actions.
	 */
	private String adminApiKey;
	/**
	 * Client that performs operations such as indices management.
	 */
	private SearchClient client;
	/**
	 * Index that stores the current index performing actions on;
	 */
	private SearchIndex<Page> index;

	/**
	 * Constructor with URL to get data from.
	 * 
	 * @throws IOException if a problem with loading config properties occured.
	 * 
	 */
	public AlgoliaClient() throws IOException {
		try (InputStream input = new FileInputStream("config.properties")) {
			Properties properties = new Properties();

			// load a properties file
			properties.load(input);
			appId = properties.getProperty("algolia.appId");
			adminApiKey = properties.getProperty("algolia.adminApiKey");

			client = DefaultSearchClient.create(appId, adminApiKey);
		} catch (IOException e) {
			logger.error("An error occured when trying to load properties");
			logger.error(Arrays.toString(e.getStackTrace()));
			throw e;
		}
	}

	/**
	 * Initialize a new index containing all the pages and sets it to "index"
	 * property. If indexName exists then it selects it without recreating it.
	 * 
	 * @param indexName is the name to be assigned to index.
	 */
	public void initIndex(final String indexName) {
		index = client.initIndex(indexName, Page.class);
		index.setSettings(new IndexSettings()
				.setSearchableAttributes(Arrays.asList("title", "shortDescription", "keywords", "contents"))
				.setCustomRanking(
						Arrays.asList("desc(title)", "desc(keywords)", "desc(shortDescription)", "desc(contents)"))
				.setAttributesToHighlight(Arrays.asList("title", "shortDescription", "contents"))
				.setAttributesToSnippet(Arrays.asList("contents:30")).setAttributesForFaceting(Arrays.asList("title")));
		logger.info("Index {} succesfully created/selected!", indexName);
	}

	/**
	 * Adds crawled pages from Crawler object to index.
	 * 
	 * @throws IOException if Crawler was failed to initiate or the HTML File
	 *                     couldn't be read.
	 */
	public void addObjectToIndex(final String url, final String baseUrl) throws IOException {
		try {
			Crawler crawler = new Crawler(url, baseUrl);
			crawler.crawl();
			index.saveObjects(crawler.getCrawledPages());
			logger.info("{} Page object(s) successfully added to {} index!", crawler.getCrawledPages().size(),
					index.getUrlEncodedIndexName());
		} catch (MalformedURLException e) {
			logger.error("An error occured when crawling URL: {}", url);
			logger.error(Arrays.toString(e.getStackTrace()));
			throw e;
		}
	}

	/**
	 * Main method that crawls data and stores it into Algolia Index.
	 * 
	 * @param args - URL and Base URl to be crawled.
	 */
	public static void main(String[] args) {
		AlgoliaClient client;
		try {
			client = new AlgoliaClient();
			client.initIndex("webhelp-search-service-publishing-template");

			if (args.length < 2)
				logger.error("There are no arguments passed to main function!");
			else
				client.addObjectToIndex(args[0], args[1]);

		} catch (IOException e) {
			logger.error("An error occurred when initializing AlgoliaClient, check your properties file! {}",
					Arrays.asList(e.getStackTrace()));
		}
	}
}
