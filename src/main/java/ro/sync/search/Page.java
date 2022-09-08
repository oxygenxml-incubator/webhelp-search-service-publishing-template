package ro.sync.search;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	@JsonProperty("objectID")
	private String url;
	/**
	 * Page's title collected from metadata.
	 */
	private String title;
	/**
	 * Page's short description
	 */
	private String shortDescription;
	/**
	 * Page's collected keywords from metadata.
	 */
	@JsonProperty("_tags")
	private List<String> keywords;
	/**
	 * Page's collected contents from body section.
	 */
	private String contents;
	/**
	 * Page's collected facets from metadata.
	 */
	private List<String> facets;

	/**
	 * Constructor with title, keywords, contents and url parameters.
	 * 
	 * @param title            is the page's title from metadata.
	 * @param keywords         is the page's keywords from metadata.
	 * @param contents         is the page's contents from body section.
	 * @param url              is the HTML document's url.
	 * @param shortDescription is the page's short descrption.
	 * @param facets           is the page's facets from metadata.
	 */
	public Page(final String title, final String shortDescription, final List<String> keywords, final String contents,
			final String url, final List<String> facets) {
		this.title = title;
		this.shortDescription = shortDescription;
		this.keywords = keywords;
		this.contents = contents;
		this.url = url;
		this.facets = facets;
	}

	/**
	 * @return URL from whom the data was collected.
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @return Page's collected title.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @return Page's short description.
	 */
	public String getShortDescription() {
		return this.shortDescription;
	}

	/**
	 * @return Page's collected keywords.
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

	/**
	 * @return Page's collected facets from metadata.
	 */
	public List<String> getFacets() {
		return this.facets;
	}
}
