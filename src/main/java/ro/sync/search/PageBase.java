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
public class PageBase {
	/**
	 * URL from whom the data was collected.
	 */
	@JsonProperty("objectID")
	protected String url;
	/**
	 * Page's title collected from metadata.
	 */
	protected String title;
	/**
	 * Page's short description
	 */
	protected String shortDescription;
	/**
	 * Page's collected keywords from metadata.
	 */
	@JsonProperty("_tags")
	protected List<String> keywords;
	/**
	 * Page's collected content from body section.
	 */
	protected String content;

	/**
	 * @param url is the URL from whom the data should be collected.
	 * @return reference to the current instance.
	 */
	protected PageBase setUrl(final String url) {
		this.url = url;
		return this;
	}

	/**
	 * @param title is the page's title.
	 * @return reference to the current instance.
	 */
	protected PageBase setTitle(final String title) {
		this.title = title;
		return this;
	}

	/**
	 * @param shortDescription is the page's short description.
	 * @return reference to the current instance.
	 */
	protected PageBase setShortDescription(final String shortDescription) {
		this.shortDescription = shortDescription;
		return this;
	}

	/**
	 * @param keywords is the page's collected keywords from metadata.
	 * @return reference to the current instance.
	 */
	protected PageBase setKeywords(final List<String> keywords) {
		this.keywords = keywords;
		return this;
	}

	/**
	 * @param content is the page's content that represents the body.
	 * @return reference to the current instance.
	 */
	protected PageBase setContent(final String content) {
		this.content = content;
		return this;
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
	 * @return Page's collected content from body section.
	 */
	public String getContent() {
		return this.content;
	}
}
