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
	 * @param indexName is the name that should be given to index.
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

			index.setSettings(
					new IndexSettings().setSearchableAttributes(Arrays.asList("title", "shortDescription", "contents"))
							.setCustomRanking(Arrays.asList("desc(title)", "desc(shortDescription)", "desc(contents)"))
							.setAttributesToHighlight(Arrays.asList("title", "shortDescription", "contents"))
							.setAttributesToSnippet(Arrays.asList("contents:30"))
							.setAttributesForFaceting(Arrays.asList("_tags", "facets")));

			logger.info("Index {} succesfully created/selected!", indexName);
		}
	}

	/**
	 * Adds crawled pages from Crawler object to index.
	 * 
	 * @param url     is the URL whose pages should be added to index.
	 * @param baseUrl is the base URL that is used to not go out of bounds.
	 * 
	 * @throws IOException if Crawler was failed to initiate or the HTML File
	 *                     couldn't be read.
	 */
	public void populateIndex(final String url, final String baseUrl, final String facetsPath) throws IOException {
		Crawler crawler = new Crawler(url, baseUrl, facetsPath);
		crawler.crawl();

		index.clearObjects();
		index.saveObjects(crawler.getCrawledPages());
		logger.info("{} Page object(s) successfully added to {} index!", crawler.getCrawledPages().size(),
				index.getUrlEncodedIndexName());
	}

	/**
	 * Outputs an example of correct usage of class. Is used when user didn't pass
	 * the arguments correctly.
	 */
	private static void informUserAboutArguments() {
		logger.error("There are no enough arguments passed!");
		logger.info(
				"To use it correctly you should specify arguments, for example: java AlgoliaClient -url=URL -baseUrl=BASE_URL -indexName=INDEX_NAME");
	}

	/**
	 * Main method that crawls data and stores it into Algolia Index.
	 * 
	 * @param args - URL, Base URl, indexName and optionally facetsPath to be
	 *             crawled.
	 */
	public static void main(String[] args) {
		try {
			if (args.length < 3) {
				informUserAboutArguments();
			} else {
				String url = "";
				String baseUrl = "";
				String indexName = "";
				String facetsPath = "";

				for (String arg : args) {
					if (arg.startsWith("-url=")) {
						url = arg.substring(5, arg.length());
					} else if (arg.startsWith("-baseUrl=")) {
						baseUrl = arg.substring(9, arg.length());
					} else if (arg.startsWith("-indexName=")) {
						indexName = arg.substring(11, arg.length());
					} else if (arg.startsWith("-facetsPath=")) {
						facetsPath = arg.substring(12, arg.length());
					}
				}

				if (url.isEmpty() || baseUrl.isEmpty() || indexName.isEmpty()) {
					informUserAboutArguments();
					return;
				}

				AlgoliaClient client = new AlgoliaClient(indexName);
				client.populateIndex(url, baseUrl, facetsPath);
			}
		} catch (IOException e) {
			logger.error(
					"An error occurred when initializing AlgoliaClient, check your properties file or passed arguments!",
					e);
		}
	}
}
