package ro.sync.search;

import java.io.IOException;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class of the application that is used to crawl URLs and push data to the
 * Algolia index.
 * 
 * @author Bozieac Artiom
 *
 */
public class Starter {
	/**
	 * Logger to inform user about certain actions like errors and others.
	 */
	private static final Logger logger = LoggerFactory.getLogger(Starter.class);

	/**
	 * Use base version of crawler.
	 * 
	 * @param args is the passed arguments.
	 */
	private static void useBasicCrawler(String... args) {
		try {
			BasicAlgolia algolia = new BasicAlgolia();
			algolia.useArguments(args);
		} catch (IllegalArgumentException e) {
			logger.error("Invalid arguments passed!");
		} catch (IOException e) {
			logger.error(
					"An error occurred when reading Algolia credentials from config.properties or your subject-scheme-values.json path!",
					e);
		}
	}

	/**
	 * Use basic version of crawler.
	 * 
	 * @param args is the passed arguments.
	 */
	private static void useFacetingCrawler(String... args) {
		try {
			FacetingAlgolia algolia = new FacetingAlgolia();
			algolia.useArguments(args);
		} catch (IllegalArgumentException e) {
			logger.error("Invalid arguments passed!");
		} catch (IOException e) {
			logger.error("An error occurred when reading Algolia credentials from config.properties!", e);
		}
	}

	/**
	 * Use base version of crawler.
	 * 
	 * @param args is the passed arguments.
	 */
	private static void useMultipleDocumentationsCrawler(String... args) {
		try {
			MultipleDocumentationsAlgolia algolia = new MultipleDocumentationsAlgolia();
			algolia.useConfig(args[0].substring(12));
		} catch (JSONException e) {
			logger.error("An error while parsing JSON config occured!", e);
		} catch (IOException e) {
			logger.error(
					"An error occurred when reading Algolia credentials from config.properties or the path to the crawler config is invalid!",
					e);
		}
	}

	/**
	 * Main method that crawls data and stores it into Algolia Index.
	 * 
	 * @param args - URL, Base URl, indexName to be crawled. You can pass only
	 *             configPath if you want to use a JSON config. If you want to use
	 *             profilig information when you must pass profilingConditionsPath
	 *             argument.
	 */
	public static void main(String[] args) {
		if (args.length == 1) {
			useMultipleDocumentationsCrawler(args);
		} else if (args.length == 4) {
			useFacetingCrawler(args);
		} else if (args.length == 3) {
			useBasicCrawler(args);
		} else {
			logger.info("Invalid Arguments!");
			logger.info("To use base crawler pass -url=PATH -baseURL=PATH -indexName=INDEXNAME");
			logger.info(
					"To use faceting crawler pass -url=PATH -baseURL=PATH -indexName=INDEXNAME -profilingConditionsPath=PATH");
			logger.info("To use multiple documentations crawler pass -configPath=PATH");
		}
	}
}
