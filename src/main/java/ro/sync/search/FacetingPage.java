package ro.sync.search;

import java.util.List;
import java.util.Map;

/**
 * Page class that is used for CrawlerFaceting to store data for Algolia index.
 * @author Bozieac Artiom
 *
 */
public class FacetingPage extends BasicPage {
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
	 * The page's breadcrumb which is a list of entries that contains Title and
	 * Relative path.
	 */
	private List<Map.Entry<String, String>> breadcrumb;

	/**
	 * @param product is the page's profiling condition of product.
	 * @return reference to the current instance.
	 */
	public FacetingPage setProduct(final List<String> product) {
		this.product = product;
		return this;
	}

	/**
	 * @param platform is the page's profiling condition of platform.
	 * @return reference to the current instance.
	 */
	public FacetingPage setPlatform(final List<String> platform) {
		this.platform = platform;
		return this;
	}

	/**
	 * @param audience is the page's profiling condition of audience.
	 * @return reference to the current instance.
	 */
	public FacetingPage setAudience(final List<String> audience) {
		this.audience = audience;
		return this;
	}

	/**
	 * @param rev is the page's profiling condition of rev.
	 * @return reference to the current instance.
	 */
	public FacetingPage setRev(final List<String> rev) {
		this.rev = rev;
		return this;
	}

	/**
	 * @param props is the page's profiling condition of props.
	 * @return reference to the current instance.
	 */
	public FacetingPage setProps(final List<String> props) {
		this.props = props;
		return this;
	}

	/**
	 * @param otherprops is the page's profiling condition of otherprops.
	 * @return reference to the current instance.
	 */
	public FacetingPage setOtherprops(final List<String> otherprops) {
		this.otherprops = otherprops;
		return this;
	}

	/**
	 * 
	 * @param breadcrumb is the page's breadcrumb which is a list of entries with
	 *                   Title and Relative path.
	 * @return reference to the current instance.
	 */
	public FacetingPage setBreadcrumb(final List<Map.Entry<String, String>> breadcrumb) {
		this.breadcrumb = breadcrumb;
		return this;
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
	 * @return Page's collected breadcrumb.
	 */
	public List<Map.Entry<String, String>> getBreadcrumb() {
		return this.breadcrumb;
	}
}
