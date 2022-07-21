package ro.sync.search;

import java.util.List;

/**
 * The class that represents a page model. It contains all the data crawled from
 * a certain URL.
 * 
 * @author Artiom Bozieac
 *
 */
public class Page {
	/**
	 * URL from whom the data was collected.
	 */
	private String url;
	/**
	 * Page's title collected from metadata.
	 */
	private String title;
	/**
	 * Page's collected keywords from metadata.
	 */
	private List<String> keywords;
	/**
	 * Page's collected contents from body section.
	 */
	private String contents;

	/**
	 * @return URL from whom the data was collected.
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @return Page's collected title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @return Page's collected keywords
	 */
	public List<String> getKeywords() {
		return this.keywords;
	}

	/**
	 * @return Page's collected contents from body section.
	 */
	public String getContents() {
		return this.contents;
	}
}
