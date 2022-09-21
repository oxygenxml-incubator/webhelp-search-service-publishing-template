package ro.sync.search;

import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Document;

/**
 * Base class for Crawler that crawls an URL for its data.
 * 
 * @author Bozieac Artiom
 *
 */
public class CrawlerBase extends CrawlerAbstract<PageBase> {
	/**
	 * Constructor with url and baseUrl parameters.
	 * 
	 * @param url     is the page that should be crawled for data.
	 * @param baseUrl is the parent that is used to not go out of bounds.
	 * 
	 * @throws IOException if problems with initaliztion of URL or accessing the
	 *                     nodesToIgnore.csv file occurred.
	 */
	protected CrawlerBase(String url, String baseUrl, boolean isFile) throws IOException {
		super(url, baseUrl, isFile);
	}

	/**
	 * @return list of crawled pages
	 */
	@Override
	public List<PageBase> getCrawledPages() {
		return this.pages;
	}

	/**
	 * Collects all the data(titles, keywords and content) from visited urls and
	 * creates a new Page object.
	 * 
	 * @param page is the desired document whose data should be collected.
	 */
	protected void collectData(final Document page) {
		pages.add(new PageBase().setTitle(collectTitle(page)).setShortDescription(collectShortDescription(page))
				.setKeywords(collectKeywords(page)).setContent(collectContent(page)).setUrl(page.baseUri()));
	}
}
