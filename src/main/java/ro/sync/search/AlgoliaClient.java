package ro.sync.search;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
	public AlgoliaClient(final String indexName) throws IOException {
		try (InputStream input = new FileInputStream("config.properties")) {
			Properties properties = new Properties();

			// Load a properties file.
			properties.load(input);

			appId = properties.getProperty("algolia.appId");
			adminApiKey = properties.getProperty("algolia.adminApiKey");

			client = DefaultSearchClient.create(appId, adminApiKey);

			index = client.initIndex(indexName, Page.class);
			index.setSettings(new IndexSettings()
					.setSearchableAttributes(Arrays.asList("title", "shortDescription", "keywords", "contents"))
					.setCustomRanking(
							Arrays.asList("desc(title)", "desc(keywords)", "desc(shortDescription)", "desc(contents)"))
					.setAttributesToHighlight(Arrays.asList("contents"))
					.setAttributesToSnippet(Arrays.asList("contents:30"))
					.setAttributesForFaceting(Arrays.asList("title")));

			logger.info("Index {} succesfully created/selected!", indexName);
		}
	}

	/**
	 * Adds crawled pages from Crawler object to index.
	 * 
	 * @throws IOException if Crawler was failed to initiate or the HTML File
	 *                     couldn't be read.
	 */
	public void populateIndex(final String url, final String baseUrl) throws IOException {
		Crawler crawler = new Crawler(url, baseUrl);
		crawler.crawl();

		index.clearObjects();
		index.saveObjects(crawler.getCrawledPages());
		logger.info("{} Page object(s) successfully added to {} index!", crawler.getCrawledPages().size(),
				index.getUrlEncodedIndexName());
	}

	/**
	 * Main method that crawls data and stores it into Algolia Index.
	 * 
	 * @param args - URL and Base URl to be crawled.
	 */
	public static void main(String[] args) {
		AlgoliaClient client;
		try {
			client = new AlgoliaClient(args[0]);

			if (args.length < 3) {
				logger.error("There are no arguments passed to main function!");

				// TODO print how to use a program
				// --url=URL --baseURL=BASEURL --indexName=name
				// Print git in a command line for instance
			} else {
				client.populateIndex(args[1], args[2]);
			}

		} catch (IOException e) {
			logger.error("An error occurred when initializing AlgoliaClient, check your properties file or passed arguments!", e);
		}
	}
}
