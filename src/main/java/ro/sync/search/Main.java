package ro.sync.search;

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
	 * 
	 * TODO: Delete this class. We have AlgoliaClient.main() with same functionality.
	 */
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		AlgoliaClient client = new AlgoliaClient();
		client.initIndex("webhelp-search-service-publishing-template");
		client.addObjectToIndex("https://sweet-beignet-8a9e20.netlify.app",
				"https://sweet-beignet-8a9e20.netlify.app");
	}
}
