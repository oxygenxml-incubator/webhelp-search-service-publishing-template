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
	 * Page's collected content from body section.
	 */
	private String content;
	/**
	 * Page's collected profiling condition of product.
	 */
	private List<String> product;
	/**
	 * Page's collected profiling condition of platform.
	 */
	private List<String> platform;
	/**
	 * Page's collected profiling condition of audience.
	 */
	private List<String> audience;
	/**
	 * Page's collected profiling condition of rev.
	 */
	private List<String> rev;
	/**
	 * Page's collected profiling condition of props.
	 */
	private List<String> props;
	/**
	 * Page's collected profiling condition of otherprops.
	 */
	private List<String> otherprops;
	/**
	 * Documentation to which the page belongs.
	 */
	private String documentation;

	/**
	 * @param url is the URL from whom the data should be collected.
	 * @return reference to the current instance.
	 */
	public Page setUrl(final String url) {
		this.url = url;
		return this;
	}

	/**
	 * @param title is the page's title.
	 * @return reference to the current instance.
	 */
	public Page setTitle(final String title) {
		this.title = title;
		return this;
	}

	/**
	 * @param shortDescription is the page's short description.
	 * @return reference to the current instance.
	 */
	public Page setShortDescription(final String shortDescription) {
		this.shortDescription = shortDescription;
		return this;
	}

	/**
	 * @param keywords is the page's collected keywords from metadata.
	 * @return reference to the current instance.
	 */
	public Page setKeywords(final List<String> keywords) {
		this.keywords = keywords;
		return this;
	}

	/**
	 * @param content is the page's content that represents the body.
	 * @return reference to the current instance.
	 */
	public Page setContent(final String content) {
		this.content = content;
		return this;
	}

	/**
	 * @param product is the page's profiling condition of product.
	 * @return reference to the current instance.
	 */
	public Page setProduct(final List<String> product) {
		this.product = product;
		return this;
	}

	/**
	 * @param platform is the page's profiling condition of platform.
	 * @return reference to the current instance.
	 */
	public Page setPlatform(final List<String> platform) {
		this.platform = platform;
		return this;
	}

	/**
	 * @param audience is the page's profiling condition of audience.
	 * @return reference to the current instance.
	 */
	public Page setAudience(final List<String> audience) {
		this.audience = audience;
		return this;
	}

	/**
	 * @param rev is the page's profiling condition of rev.
	 * @return reference to the current instance.
	 */
	public Page setRev(final List<String> rev) {
		this.rev = rev;
		return this;
	}

	/**
	 * @param props is the page's profiling condition of props.
	 * @return reference to the current instance.
	 */
	public Page setProps(final List<String> props) {
		this.props = props;
		return this;
	}

	/**
	 * @param otherprops is the page's profiling condition of otherprops.
	 * @return reference to the current instance.
	 */
	public Page setOtherprops(final List<String> otherprops) {
		this.otherprops = otherprops;
		return this;
	}

	/**
	 * @param documentation is the documentation to which the page belongs.
	 * @return reference to the current instance.
	 */
	public Page setDocumentation(final String documentation) {
		this.documentation = documentation;
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

	/**
	 * @return Page's collected profiling conditions of product.
	 */
	public List<String> getProduct() {
		return this.product;
	}

	/**
	 * @return Page's collected profiling condition of platform.
	 */
	public List<String> getPlatform() {
		return this.platform;
	}

	/**
	 * @return Page's collected profiling condition of audience.
	 */
	public List<String> getAudience() {
		return this.audience;
	}

	/**
	 * @return Page's collected profiling condition of rev.
	 */
	public List<String> getRev() {
		return this.rev;
	}

	/**
	 * @return Page's collected profiling condition of props.
	 */
	public List<String> getProps() {
		return this.props;
	}

	/**
	 * @return Page's collected profiling condition of otherprops.
	 */
	public List<String> getOtherprops() {
		return this.otherprops;
	}

	/**
	 * @return Page's collected documentation.
	 */
	public String getDocumentation() {
		return this.documentation;
	}
}
