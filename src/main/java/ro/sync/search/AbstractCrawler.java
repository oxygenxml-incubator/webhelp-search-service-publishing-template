package ro.sync.search;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class of Crawler that provides basic functional.
 * 
 * @author Bozieac Artiom
 *
 * @param <T> is the version of page to be used. PageBase, PageFaceting or
 *            PageMultipleDocumentations.
 */
public abstract class AbstractCrawler<T extends BasicPage> {
	/**
	 * Logger to inform user about certain actions like errors and others.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(AbstractCrawler.class);
	/**
	 * The url to be crawled.
	 */
	protected String url;
	/**
	 * The base url of url to be crawled. It is used in order to not leave the
	 * website and crawl data to infinite. For example if the base url is
	 * "https://google.com/search/index.html" then it won't go to any other sites
	 * that don't start with "https://google.com/search".
	 */
	protected String baseUrl;
	/**
	 * Represents the state of URL. If URL has "http:// or "https://" protocol then
	 * it's a website, if "file://" then it's a file.
	 */
	protected boolean isFile;
	/**
	 * Class and attribute that represents short description in DOM.
	 */
	static final String SHORT_DESCRIPTION_SELECTOR = "p[class=\"- topic/shortdesc shortdesc\"]";
	/**
	 * File that represents class and attributes that should be ignored for
	 * collection.
	 */
	static final String NODES_TO_IGNORE_PATH = "nodesToIgnore.csv";
	/**
	 * A list of strings that represents selectors of elements that should be ignore
	 * during the crawling process.
	 */
	protected final List<String> nodesToIgnore = new ArrayList<>();

	/**
	 * List that stores all the visited urls in order to not crawl them more than
	 * one time.
	 */
	protected List<String> visitedUrls = new ArrayList<>();

	/**
	 * List that serves as a queue that is used to perform BFS algorithm.
	 */
	protected List<String> queue = new ArrayList<>();
	/**
	 * List that stores all crawled pages
	 */
	protected List<T> pages = new ArrayList<>();

	/**
	 * Constructor with url and baseUrl parameters.
	 * 
	 * @param url     is the page that should be crawled for data.
	 * @param baseUrl is the parent that is used to not go out of bounds.
	 * @param isFile  is the flag that indicates if you passed and URL to the file
	 *                or a website.
	 * 
	 * @throws IOException if problems with initaliztion of URL or accessing the
	 *                     nodesToIgnore.csv file occurred.
	 */
	protected AbstractCrawler(final String url, final String baseUrl, final boolean isFile) throws IOException {
		this.url = url;
		this.baseUrl = baseUrl;
		this.isFile = isFile;

		StringTokenizer tokenizer = new StringTokenizer(Files.readString(Path.of(NODES_TO_IGNORE_PATH)), ",");
		while (tokenizer.hasMoreTokens()) {
			nodesToIgnore.add(tokenizer.nextToken());
		}
	}

	/**
	 * @return list of crawled pages
	 */
	public List<T> getCrawledPages() {
		return this.pages;
	}

	/**
	 * @return start url that should be crawled for data.
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @return base url that is used to not go out of parent's bounds.
	 */
	public String getBaseUrl() {
		return this.baseUrl;
	}

	/**
	 * @return list of visited urls after the crawl.
	 */
	public List<String> getVisitedUrls() {
		return this.visitedUrls;
	}

	/**
	 * Using the given url in the constructor it visits every resource that haves
	 * the same host and crawls its data.
	 * 
	 * @throws IOException if a problem with reading HTML File occured.
	 * 
	 */
	public void crawl() throws IOException {
		visitedUrls.clear();

		// Add to the queue the starting url so it starts with it
		queue.add(url);

		while (!queue.isEmpty()) {
			String currentUrl = queue.remove(0);
			Document page = readHtml(currentUrl);
			
			findUrls(page, currentUrl);

			if (!(currentUrl.endsWith("index.html") || currentUrl.equals(this.url)))
				collectData(page);
		}

		logger.info("The crawling went successfully! {} page(s) has/have been crawled!", getCrawledPages().size());
	}

	/**
	 * Reads HTML code from an URL.
	 * 
	 * @param url is page's url whose HTML code should be extracted.
	 * @return a HTML document.
	 * @throws IOException when a problem with reading the HTML code occurred.
	 */
	protected Document readHtml(final String url) throws IOException {
		return this.isFile ? Jsoup.parse(new File(url.substring(5)), "UTF-8") : Jsoup.connect(url).get();
	}

	/**
	 * Finds appropriate urls among all the matches and adds them to queue.
	 * 
	 * @param page    is the Document whose hrefs should be collected.
	 * @param pageUrl is the current page's url that is used to construct the next
	 *                URLs.
	 * @throws MalformedURLException when a problem with initialization of URL
	 *                               occurred.
	 */
	protected void findUrls(final Document page, final String pageUrl) throws MalformedURLException {
		// Select all "a" tags
		Elements links = page.select("a");
		// Search for ".html" hrefs
		for (Element link : links) {
			if (link.attr("href").endsWith(".html")) {

				String currentUrl = new URL(new URL(pageUrl), link.attr("href")).toString();

				if (!visitedUrls.contains(currentUrl) && currentUrl.startsWith(this.baseUrl)
						&& !(currentUrl.endsWith("index.html") || currentUrl.equals(this.url))) {
					visitedUrls.add(currentUrl);
					queue.add(currentUrl);
				}
			}
		}
	}

	/**
	 * Collects all the data(titles, keywords and content) from visited urls and
	 * creates a new Page object.
	 * 
	 * @param page is the desired document whose data should be collected.
	 */
	protected abstract void collectData(final Document page);

	/**
	 * Collects the title of the page.
	 * 
	 * @param page is the desired document whose data should be collected. Page's
	 *             collected title from metadata.
	 * @return page's collected title.
	 */
	protected String collectTitle(final Document page) {
		return page.title();
	}

	/**
	 * Collects the short description of the page.
	 * 
	 * @param page is the desired document whose data should be collected.
	 * @return Short description of the page
	 */
	protected String collectShortDescription(final Document page) {
		return page.select(SHORT_DESCRIPTION_SELECTOR).text();
	}

	/**
	 * Collects the keywords of the page from metadata.
	 * 
	 * @param page is the desired document whose data should be collected.
	 * @return Page's collected keywords from metadata.
	 */
	protected List<String> collectKeywords(final Document page) {
		Element element = page.select("meta[name=keywords]").first();

		if (element != null)
			return Arrays.asList(element.attr("content").split(","));

		return new ArrayList<>();
	}

	/**
	 * Collects the content of Page from body. The content are texts, titles,
	 * paragraphs and others.
	 * 
	 * @param page is the desired document whose data should be collected.
	 * @return Page's collected content from body section.
	 */
	protected String collectContent(final Document page) {
		// Delete from DOM every selector from file "nodesToIgnore.csv".
		for (String selector : this.nodesToIgnore)
			page.select(selector).remove();

		// Return remaining text from body.
		return page.body().text();
	}
}
