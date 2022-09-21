package ro.sync.search;

import java.util.List;
import java.util.Map;

/**
 * Page class for CrawlerMultipleDocumentations.
 * @author Bozieac Artiom
 *
 */
public class PageMultipleDocumentations extends PageBase {
	/**
	 * The page's breadcrumb which is a list of entries that contains Title and
	 * Relative path.
	 */
	private List<Map.Entry<String, String>> breadcrumb;
	/**
	 * Documentation to which the page belongs.
	 */
	private String documentation;

	/**
	 * 
	 * @param breadcrumb is the page's breadcrumb which is a list of entries with
	 *                   Title and Relative path.
	 * @return reference to the current instance.
	 */
	public PageMultipleDocumentations setBreadcrumb(final List<Map.Entry<String, String>> breadcrumb) {
		this.breadcrumb = breadcrumb;
		return this;
	}

	/**
	 * @param documentation is the documentation to which the page belongs.
	 * @return reference to the current instance.
	 */
	public PageMultipleDocumentations setDocumentation(final String documentation) {
		this.documentation = documentation;
		return this;
	}

	/**
	 * @return Page's collected breadcrumb.
	 */
	public List<Map.Entry<String, String>> getBreadcrumb() {
		return this.breadcrumb;
	}

	/**
	 * @return Page's collected documentation.
	 */
	public String getDocumentation() {
		return this.documentation;
	}
}
