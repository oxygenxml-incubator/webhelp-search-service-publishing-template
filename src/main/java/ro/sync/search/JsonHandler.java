package ro.sync.search;

/**
 * Class that handles all the actions related to JSON files.
 * 
 * @author Artiom Bozieac
 *
 */
public class JsonHandler {
	/**
	 * Crawler that holds all the crawled data from URLs.
	 */
	private Crawler crawler;

	/**
	 * String with JSON format used in order to send data to Algolia.
	 */
	private StringBuilder json;

	/**
	 * Constructor with crawler parameter.
	 * 
	 * @param crawler is the crawler that stores all the crawled data.
	 */
	public JsonHandler(final Crawler crawler) {
		this.crawler = crawler;
	}

	/*
	 * /** Creates JSON record of the page with given index.
	 * 
	 * @return The json model with data prepared for Algolia.
	 */
	public String getJson() {
		createJson();
		return this.json.toString();
	}

	/**
	 * Creates a JSON record of the page with index "index".
	 * 
	 * @param index is the index of the page whose data should be serialized.
	 */
	private void createJsonRecord(final int index) {
		// Start JSON record.
		this.json.append("\t{\n");

		// Add title key with page's title as the value.
		this.json.append("\t\t\"title\": \"" + crawler.getCrawledPages().get(index).getTitle() + "\",\n");

		// Add keywords as a JSON array.
		this.json.append("\t\t\"keywords\": [");
		if (!crawler.getCrawledPages().get(index).getKeywords().isEmpty()) {
			for (String keyword : crawler.getCrawledPages().get(index).getKeywords()) {
				// If this is the last keyword to add, then don't add the whitespace and comma.
				if (keyword.equals(crawler.getCrawledPages().get(index).getKeywords()
						.get(crawler.getCrawledPages().get(index).getKeywords().size() - 1)))
					this.json.append("\"" + keyword + "\"],\n");
				else
					this.json.append("\"" + keyword + "\", ");
			}
		} else {
			// Else close the aray add comma and newline.
			this.json.append("],\n");
		}

		// Add contents
		this.json.append("\t\t\"contents\": \"" + crawler.getCrawledPages().get(index).getContents() + "\",\n");

		// Add URL of the HTML document.
		this.json.append("\t\t\"url\": \"" + crawler.getCrawledPages().get(index).getUrl() + "\"\n");

		// Close the record
		this.json.append("\t}\n");
	}

	private void createJson() {
		this.json = new StringBuilder();

		// Start JSON file.
		this.json.append("{\n");

		for (int i = 0; i < crawler.getCrawledPages().size(); i++)
			createJsonRecord(i);

		// Close the file.
		this.json.append("}\n");
	}
}
