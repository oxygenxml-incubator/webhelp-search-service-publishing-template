package ro.sync.search;

import java.util.List;

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
public class AlgoliaHandler {
	/**
	 * Logger to inform user about certain actions like errors and others.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AlgoliaHandler.class);
	/**
	 * Algolia application's id.
	 */
	private static String appId = "KLFWXPOEHY";
	/**
	 * Algolia admin's key to perform actions.
	 */
	private static String adminApiKey = "87c939ac9269c88a17beeaacca28567a";
	/**
	 * Client that performs operations such as indices management.
	 */
	private static SearchClient client = DefaultSearchClient.create(appId, adminApiKey);

	/**
	 * Private constructor in order to not instantiate objects.
	 */
	private AlgoliaHandler() {

	}

	/**
	 * Creates a new index containing all the pages.
	 * 
	 * @param indexName is the name to be assigned to index.
	 * @param pages     is the list with all the pages that should be stored under
	 *                  the index.
	 */
	public static void addIndex(final String indexName, final List<Page> pages) {
		SearchIndex<Page> index = client.initIndex(indexName, Page.class);
		index.saveObjects(pages);
		logger.info("Index {} succesfully created!", indexName);
	}
}
