package ro.sync.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Crawler that crawls an URL for data and uses DITA profiling conditions.
 * 
 * @author artio
 *
 */
public class FacetingCrawler extends AbstractCrawler<FacetingPage> {
	/**
	 * Path that leads to the JSON file where all profiling values and conditions
	 * are stored.
	 */
	private String profilingConditionsPath;

	/**
	 * Constructor with url and baseUrl parameters.
	 * 
	 * @param url                     is the page that should be crawled for data.
	 * @param baseUrl                 is the parent that is used to not go out of
	 *                                bounds.
	 * @param profilingConditionsPath is the path for the JSON file that stores all
	 *                                the profiling information.
	 * @throws IOException if problems with initaliztion of URL or accessing the
	 *                     nodesToIgnore.csv file occurred.
	 */
	protected FacetingCrawler(String url, String baseUrl, boolean isFile, final String profilingConditionsPath)
			throws IOException {
		super(url, baseUrl, isFile);

		this.profilingConditionsPath = profilingConditionsPath;
	}

	/**
	 * @return list of crawled pages
	 */
	@Override
	public List<FacetingPage> getCrawledPages() {
		return this.pages;
	}

	/**
	 * Collects all the data(titles, keywords and content) from visited urls and
	 * creates a new Page object. It also collects profiling values from DITA.
	 * 
	 * @param page is the desired document whose data should be collected.
	 */
	protected void collectData(final Document page) {
		pages.add(((FacetingPage) ((FacetingPage) new FacetingPage().setTitle(collectTitle(page))
				.setShortDescription(collectShortDescription(page)).setKeywords(collectKeywords(page)))
				.setBreadcrumb(collectBreadcrumb(page)).setContent(collectContent(page)).setUrl(page.baseUri()))
				.setProduct(collectProfilingCondition(page, "product"))
				.setPlatform(collectProfilingCondition(page, "platform"))
				.setAudience(collectProfilingCondition(page, "audience")).setRev(collectProfilingCondition(page, "rev"))
				.setProps(collectProfilingCondition(page, "props"))
				.setOtherprops(collectProfilingCondition(page, "otherprops")));

		logger.info("Page {} was crawled!", page.title());
	}

	/**
	 * Collects an profiling condition of Page from metadata.
	 * 
	 * @param page               is the Document whose data should be collected.
	 * @param profilingCondition is the profiling condition's name whose values
	 *                           should to be returned.
	 * @return Page's collected profiling condition of passed argument.
	 */
	private List<String> collectProfilingCondition(final Document page, final String profilingCondition) {
		List<String> profilingValues = new ArrayList<>();

		if (this.profilingConditionsPath.isEmpty())
			return new ArrayList<>();

		ProfilingHandler pHandler = new ProfilingHandler(this.profilingConditionsPath);

		Element value = page.select(String.format("meta[name=\"wh-data-%s\"]", profilingCondition)).first();

		if (value != null)
			// If the value is present in the DOM put it in the facets values.
			profilingValues.add(value.attr("content"));
		else {
			// If the value isn't present then put every possible value for the facet.
			if (pHandler.getProflingValues().get(profilingCondition) != null)
				for (String facetValue : pHandler.getProflingValues().get(profilingCondition)) {
					profilingValues.add(facetValue);
				}
		}

		return profilingValues;
	}

	/**
	 * Collects page's breadcrumb from the top of the page.
	 * 
	 * @param page is the page whose breadcrumb should be collected.
	 * @return page's breadcrumb that is a list of entries with title and relative
	 *         path.
	 */
	private List<Map.Entry<String, String>> collectBreadcrumb(final Document page) {
		List<Map.Entry<String, String>> breadcrumb = new ArrayList<>();

		// Remove the short description in the breadcrumb that is not visible to the
		// user.
		page.select("div.wh-tooltip").remove();

		// Extract every category of the breadcrumb.
		for (Element el : page.select("div.wh_breadcrumb > ol > li")) {
			if (el.text().equals("Home"))
				breadcrumb.add(Map.entry(el.text(), el.child(0).child(0).absUrl("href")));
			else
				breadcrumb.add(Map.entry(el.text(), el.child(0).child(0).child(0).absUrl("href")));
		}

		return breadcrumb;
	}

}
