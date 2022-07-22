package ro.sync.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that tests JsonHandler's functional
 * 
 * @author Artiom Bozieac
 *
 */
class JsonHandlerTest {
	/**
	 * Logger to inform user about certain actions like errors and others.
	 */
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	/**
	 * List that stores the crawled pages
	 */
	private static List<Page> pages;

	@BeforeAll
	public static void setup() {
		try {
			Crawler crawler = new Crawler(Path.of("src/test/resources/data/index.html").toUri().toURL().toString(),
					Path.of("src/test/resources/data/").toUri().toURL().toString(), true);
			crawler.crawl();
			pages = crawler.getCrawledPages();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger.error("An error occured when initializing URLs!");
		}
	}

	/**
	 * Tests if getJsonRecord() method works correctly. It should return a record of
	 * a crawled page in JSON format.
	 */
	@Test
	void getJsonRecordTest() {
		JsonHandler jsonHandler = new JsonHandler(pages);

		StringBuilder expected = new StringBuilder();

		// Start JSON record.
		expected.append("{\n");

		// Add title key with page's title as the value.
		expected.append("\t\"title\": \"" + pages.get(0).getTitle() + "\",\n");

		// Add keywords as a JSON array.
		expected.append("\t\"keywords\": [");
		if (!pages.get(0).getKeywords().isEmpty()) {
			for (String keyword : pages.get(0).getKeywords()) {
				// If this is the last keyword to add, then don't add the whitespace and comma.
				if (keyword.equals(pages.get(0).getKeywords().get(pages.get(0).getKeywords().size() - 1)))
					expected.append("\"" + keyword + "\"],\n");
				else
					expected.append("\"" + keyword + "\", ");
			}
		} else {
			// Else close the aray add comma and newline.
			expected.append("],\n");
		}

		// Add contents
		expected.append("\t\"contents\": \"" + pages.get(0).getContents() + "\",\n");

		// Add URL of the HTML document.
		expected.append("\t\"url\": \"" + pages.get(0).getUrl() + "\"\n");

		// Close the record
		expected.append("}\n");

		assertEquals(expected.toString(), jsonHandler.getJsonRecord(0));
	}

	/**
	 * Tests if getJson() method works correctly. It should return a record of a
	 * crawled page in JSON format.
	 */
	@Test
	void getJsonTest() {
		JsonHandler jsonHandler = new JsonHandler(pages);

		StringBuilder expected = new StringBuilder();

		// Start JSON file.
		expected.append("{\n");

		for (int i = 0; i < pages.size(); i++) {
			// Start JSON record.
			expected.append("\t{\n");

			// Add title key with page's title as the value.
			expected.append("\t\t\"title\": \"" + pages.get(i).getTitle() + "\",\n");

			// Add keywords as a JSON array.
			expected.append("\t\t\"keywords\": [");
			if (!pages.get(i).getKeywords().isEmpty()) {
				for (String keyword : pages.get(i).getKeywords()) {
					// If this is the last keyword to add, then don't add the whitespace and comma.
					if (keyword.equals(pages.get(i).getKeywords().get(pages.get(i).getKeywords().size() - 1)))
						expected.append("\"" + keyword + "\"],\n");
					else
						expected.append("\"" + keyword + "\", ");
				}
			} else {
				// Else close the aray add comma and newline.
				expected.append("],\n");
			}

			// Add contents
			expected.append("\t\t\"contents\": \"" + pages.get(i).getContents() + "\",\n");

			// Add URL of the HTML document.
			expected.append("\t\t\"url\": \"" + pages.get(i).getUrl() + "\"\n");

			// Close the record
			if (i == pages.size() - 1)
				expected.append("\t}\n");
			else
				expected.append("\t},\n");
		}

		// Close the file.
		expected.append("}\n");

		assertEquals(expected.toString(), jsonHandler.getJson());
	}
}
