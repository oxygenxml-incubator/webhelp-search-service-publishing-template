package ro.sync.search;

import java.util.List;

/**
 * Class that handles all the actions related to JSON files.
 * 
 * @author Artiom Bozieac
 *
 */
public class JsonHandler {
	/**
	 * Pages that holds all the crawled pages by the crawler.
	 */
	private List<Page> pages;
	/**
	 * String with JSON format used in order to send data to Algolia.
	 */
	private StringBuilder json;
	/**
	 * Constructor with pages parameter.
	 * 
	 * @param pages is the list that stores all the crawled pages.
	 */
	public JsonHandler(final List<Page> pages) {
		this.pages = pages;
	}

	/**
	 * Creates JSON record of all the pages.
	 * 
	 * TODO: Get methods should not trigger methods that builds something...
	 * 
	 * @return The json model with data prepared for Algolia.
	 */
	public String getJson() {
		createJson();
		return this.json.toString();
	}

	/**
	 * Creates JSON record of the page with given index.
	 * 
	 * TODO: Get methods should not trigger methods that builds something...
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
		this.json.append("\t\"title\": \"" + pages.get(index).getTitle() + "\",\n");

		// Add keywords as a JSON array.
		this.json.append("\t\"keywords\": [");
		if (!pages.get(index).getKeywords().isEmpty()) {
			for (String keyword : pages.get(index).getKeywords()) {
				// If this is the last keyword to add, then don't add the whitespace and comma.
				if (keyword.equals(pages.get(index).getKeywords().get(pages.get(index).getKeywords().size() - 1)))
					this.json.append("\"" + keyword + "\"],\n");
				else
					this.json.append("\"" + keyword + "\", ");
			}
		} else {
			// Else close the aray add comma and newline.
			this.json.append("],\n");
		}

		// Add contents
		this.json.append("\t\"contents\": \"" + pages.get(index).getContents() + "\",\n");

		// Add URL of the HTML document.
		this.json.append("\t\"url\": \"" + pages.get(index).getUrl() + "\"\n");

		// Close the record
		this.json.append("}\n");
	}

	/**
	 * TODO private methods should have comments
	 */
	private void createJson() {
		this.json = new StringBuilder();

		// Start JSON file.
		this.json.append("{\n");

		for (int i = 0; i < pages.size(); i++) {
			// Start JSON record.
			this.json.append("\t{\n");

			// Add title key with page's title as the value.
			this.json.append("\t\t\"title\": \"" + pages.get(i).getTitle() + "\",\n");

			// Add keywords as a JSON array.
			this.json.append("\t\t\"keywords\": [");
			if (!pages.get(i).getKeywords().isEmpty()) {
				for (String keyword : pages.get(i).getKeywords()) {
					// If this is the last keyword to add, then don't add the whitespace and comma.
					if (keyword.equals(pages.get(i).getKeywords().get(pages.get(i).getKeywords().size() - 1)))
						this.json.append("\"" + keyword + "\"],\n");
					else
						this.json.append("\"" + keyword + "\", ");
				}
			} else {
				// Else close the aray add comma and newline.
				this.json.append("],\n");
			}

			// Add contents
			this.json.append("\t\t\"contents\": \"" + pages.get(i).getContents() + "\",\n");

			// Add URL of the HTML document.
			this.json.append("\t\t\"url\": \"" + pages.get(i).getUrl() + "\"\n");

			// Close the record
			if (i == pages.size() - 1)
				this.json.append("\t}\n");
			else
				this.json.append("\t},\n");
		}

		// Close the file.
		this.json.append("}\n");
	}
}
