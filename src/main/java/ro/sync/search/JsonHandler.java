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
	 * /** Creates JSON record of all the pages.
	 * 
	 * @return The json model with data prepared for Algolia.
	 */
	public String getJson() {
		createJson();
		return this.json.toString();
	}

	/*
	 * /** Creates JSON record of the page with given index.
	 * 
	 * @return The json model with data prepared for Algolia.
	 */
	public String getJsonRecord(final int index) {
		createJsonRecord(index);
		return this.json.toString();
	}

	/**
	 * Creates a JSON record of the page with index "index".
	 * 
	 * @param index is the index of the page whose data should be serialized.
	 */
	private void createJsonRecord(final int index) {
		this.json = new StringBuilder();

		// Start JSON record.
		this.json.append("{\n");

		// Add title key with page's title as the value.
		this.json.append("\t\"title\": \"" + crawler.getCrawledPages().get(index).getTitle() + "\",\n");

		// Add keywords as a JSON array.
		this.json.append("\t\"keywords\": [");
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
		this.json.append("\t\"contents\": \"" + crawler.getCrawledPages().get(index).getContents() + "\",\n");

		// Add URL of the HTML document.
		this.json.append("\t\"url\": \"" + crawler.getCrawledPages().get(index).getUrl() + "\"\n");

		// Close the record
		this.json.append("}\n");
	}

	private void createJson() {
		this.json = new StringBuilder();

		// Start JSON file.
		this.json.append("{\n");

		for (int i = 0; i < crawler.getCrawledPages().size(); i++) {
			// Start JSON record.
			this.json.append("\t{\n");

			// Add title key with page's title as the value.
			this.json.append("\t\t\"title\": \"" + crawler.getCrawledPages().get(i).getTitle() + "\",\n");

			// Add keywords as a JSON array.
			this.json.append("\t\t\"keywords\": [");
			if (!crawler.getCrawledPages().get(i).getKeywords().isEmpty()) {
				for (String keyword : crawler.getCrawledPages().get(i).getKeywords()) {
					// If this is the last keyword to add, then don't add the whitespace and comma.
					if (keyword.equals(crawler.getCrawledPages().get(i).getKeywords()
							.get(crawler.getCrawledPages().get(i).getKeywords().size() - 1)))
						this.json.append("\"" + keyword + "\"],\n");
					else
						this.json.append("\"" + keyword + "\", ");
				}
			} else {
				// Else close the aray add comma and newline.
				this.json.append("],\n");
			}

			// Add contents
			this.json.append("\t\t\"contents\": \"" + crawler.getCrawledPages().get(i).getContents() + "\",\n");

			// Add URL of the HTML document.
			this.json.append("\t\t\"url\": \"" + crawler.getCrawledPages().get(i).getUrl() + "\"\n");

			// Close the record
			if (i == crawler.getCrawledPages().size() - 1)
				this.json.append("\t}\n");
			else
				this.json.append("\t},\n");
		}

		// Close the file.
		this.json.append("}\n");
	}
}
