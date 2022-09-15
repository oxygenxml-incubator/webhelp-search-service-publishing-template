package ro.sync.search;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
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

			index.setSettings(new IndexSettings()
					.setSearchableAttributes(Arrays.asList("title", "shortDescription", "content"))
					.setCustomRanking(Arrays.asList("desc(title)", "desc(shortDescription)", "desc(content)"))
					.setAttributesToHighlight(Arrays.asList("title", "shortDescription", "content"))
					.setAttributesToSnippet(Arrays.asList("content:30")).setAttributesForFaceting(Arrays.asList("_tags",
							"product", "platform", "audience", "rev", "props", "otherProps", "documentation")));

			logger.info("Index {} successfully created/selected!", indexName);
		}
	}

	/**
	 * Adds crawled pages from Crawler object to index.
	 * 
	 * @param url               is the URL whose pages should be added to index.
	 * @param baseUrl           is the base URL that is used to not go out of
	 *                          bounds.
	 * @param facetsPath        is the path to the file with profiling conditions of
	 *                          the documentation.
	 * @param clearIndex        is the flag that indicates if index should be
	 *                          cleared before populating it or not.
	 * @param documentationName is the name to be set for documentation, you can
	 *                          leave it empty.
	 * 
	 * @throws IOException if Crawler was failed to initiate or the HTML File
	 *                     couldn't be read.
	 */
	public void populateIndex(final String url, final String baseUrl, final String facetsPath, final boolean clearIndex,
			final String documentationName) throws IOException {
		Crawler crawler = new Crawler(url, baseUrl, facetsPath);
		crawler.setDocumentationName(documentationName).crawl();

		if (clearIndex)
			index.clearObjects();

		index.saveObjects(crawler.getCrawledPages());
		logger.info("{} Page object(s) successfully added to {} index!", crawler.getCrawledPages().size(),
				index.getUrlEncodedIndexName());
	}

	/**
	 * Clears the objects from index.
	 */
	private void clearIndex() {
		index.clearObjects();
	}

	/**
	 * Outputs an example of correct usage of class. Is used when user didn't pass
	 * the arguments correctly.
	 */
	private static void informUserAboutArguments() {
		logger.error("There are no enough arguments passed!");
		logger.info(
				"To use it correctly you should specify arguments, for example: java AlgoliaClient -url=URL -baseUrl=BASE_URL -indexName=INDEX_NAME");
		logger.info("Optional parameters are: -profilingValuesPath=PATH");
	}

	/**
	 * Main method that crawls data and stores it into Algolia Index. If there is a
	 * crawler-config.json, there is no need to specify arguments as the program
	 * will automatically use the config.
	 * 
	 * @param args - URL, Base URl, indexName and optionally profilingValuesPath to
	 *             be crawled.
	 */
	public static void main(String[] args) {
		try {
			String contents = new String((Files.readAllBytes(Paths.get("crawler-config.json"))));
			JSONObject jsonObject = new JSONObject(contents);
			// Map that stores documentation url and name.
			Map<String, String> documentations = new HashMap<>();

			// Extract array with documentations.
			JSONArray documentationsJson = jsonObject.getJSONArray("documentations");

			// Put documentations into the map.
			for (int i = 0; i < documentationsJson.length(); i++)
				documentations.put(documentationsJson.getJSONObject(i).getString("name"),
						documentationsJson.getJSONObject(i).getString("url"));

			AlgoliaClient client = new AlgoliaClient(jsonObject.getString("indexName"));
			client.clearIndex();

			// Crawl every single documentation and store it in Algolia index.
			for (Entry<String, String> documentation : documentations.entrySet())
				client.populateIndex(documentation.getValue(), documentation.getValue(),
						jsonObject.getString("profilingValuesPath"), false, documentation.getKey());

		} catch (IOException e) {
			logger.error("No crawler-config.json found! Using passed arguments!");

			if (args.length < 3) {
				informUserAboutArguments();
			} else {
				String url = "";
				String baseUrl = "";
				String indexName = "";
				String profilingValuesPath = "";

				for (String arg : args) {
					if (arg.startsWith("-url=")) {
						url = arg.substring(5, arg.length());
					} else if (arg.startsWith("-baseUrl=")) {
						baseUrl = arg.substring(9, arg.length());
					} else if (arg.startsWith("-indexName=")) {
						indexName = arg.substring(11, arg.length());
					} else if (arg.startsWith("-profilingValuesPath=")) {
						profilingValuesPath = arg.substring(21, arg.length());
					}
				}

				if (url.isEmpty() || baseUrl.isEmpty() || indexName.isEmpty()) {
					informUserAboutArguments();
					return;
				}

				try {
					AlgoliaClient client = new AlgoliaClient(indexName);
					client.populateIndex(url, baseUrl, profilingValuesPath, true, "");
				} catch (IOException ex) {
					logger.error(
							"An error occurred when initializing AlgoliaClient, check your properties file or passed arguments!",
							e);
				}
			}
		}
	}
}
