package ro.sync.search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.algolia.search.SearchIndex;
import com.algolia.search.models.settings.IndexSettings;

/**
 * Class that uses multiple documentations given via a config and pushes crawled
 * data to the Algolia index. It collects title, keywords, short description,
 * content, breadcrumb and creates a new attribute to specify each topic's
 * documentation. This class is used for Webhelp template with React results
 * page.
 * 
 * @author Bozieac Artiom
 *
 */
public class AlgoliaMultipleDocumentations extends AlgoliaBase {
	/**
	 * Logger to inform user about certain actions like errors and others.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AlgoliaMultipleDocumentations.class);
	/**
	 * Index that stores the current index performing actions on;
	 */
	protected SearchIndex<PageMultipleDocumentations> index;

	/**
	 * Constructor to set up necessary stuff like properties for Algolia connection.
	 * 
	 * @throws IOException if a problem with loading config properties occured.
	 * 
	 */
	public AlgoliaMultipleDocumentations() throws IOException {
		super();
	}

	/**
	 * Sets settings for the index.
	 */
	private void prepareIndex() {
		index.setSettings(new IndexSettings()
				.setSearchableAttributes(Arrays.asList("title", "shortDescription", "content"))
				.setCustomRanking(Arrays.asList("desc(title)", "desc(shortDescription)", "desc(content)"))
				.setAttributesToHighlight(Arrays.asList("title", "shortDescription", "content"))
				.setAttributesToSnippet(Arrays.asList("content:30")).setAttributesForFaceting(Arrays.asList("_tags")));
		index.clearObjects();
	}

	@Override
	public void useArguments(final String... args) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Use config in order to crawl documentations and store them in Algolia index.
	 * 
	 * @param configPath is the path to the crawler-config.json
	 * @throws JSONException if JSON couldn't be parsed.
	 * @throws IOException   if config file is not found.
	 */
	public void useConfig(final String configPath) throws JSONException, IOException {
		JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(configPath))));
		// Map that stores documentation url and name.
		Map<String, String> documentations = new HashMap<>();

		// Extract array with documentations.
		JSONArray documentationsJson = jsonObject.getJSONArray("publications");

		// Put documentations into the map.
		for (int i = 0; i < documentationsJson.length(); i++)
			documentations.put(documentationsJson.getJSONObject(i).getString("Name"),
					documentationsJson.getJSONObject(i).getString("Publication URL"));

		index = client.initIndex(jsonObject.getString("indexName"), PageMultipleDocumentations.class);
		prepareIndex();

		// Crawl every single documentation and store it in Algolia index.
		for (Entry<String, String> documentation : documentations.entrySet()) {
			CrawlerMultipleDocumentations crawler = new CrawlerMultipleDocumentations(documentation.getValue(),
					documentation.getValue(), false).setDocumentationName(documentation.getKey());
			crawler.crawl();

			index.saveObjects(crawler.getCrawledPages());

			logger.info("{} Page object(s) successfully added to {} index!", crawler.getCrawledPages().size(),
					index.getUrlEncodedIndexName());
		}
	}
}
