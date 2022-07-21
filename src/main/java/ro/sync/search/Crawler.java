package ro.sync.search;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class that crawls an HTML website and looks for data.
 * 
 * @author Artiom Bozieac
 *
 */
public class Crawler {
	/**
	 * Logger to inform user about certain actions like errors and others.
	 */
	private static final Logger logger = LoggerFactory.getLogger(Crawler.class);
	/**
	 * String with JSON format used in order to send data to Algolia.
	 */
	private StringBuilder json;
	/**
	 * The url to be crawled.
	 */
	private String url;
	/**
	 * The base url of url to be crawled. It is used in order to not leave the
	 * website and crawl data to infinite. For example if the base url is
	 * "https://google.com/search/index.html" then it won't go to any other sites
	 * that don't start with "https://google.com/search".
	 */
	private String baseUrl;

	/**
	 * Represents the state of URL. If URL has "http:// or "https://" protcol then
	 * it's a website, if "file://" then it's a file.
	 */
	private boolean isFile;

	/**
	 * List that stores all the visited urls in order to not crawl them more than
	 * one time.
	 */
	private List<String> visitedUrls = new ArrayList<>();

	/**
	 * List that serves as a queue that is used to perform BFS algorithm.
	 */
	private List<String> queue = new ArrayList<>();

	/**
	 * List that stores all crawled pages
	 */
	private List<Page> pages = new ArrayList<>();

	/**
	 * Constructor with url and baseUrl parameters.
	 * 
	 * @param url     is the page that should be crawled for data.
	 * @param baseUrl is the parent that is used to not go out of bounds.
	 * 
	 * @throws MalformedURLException if problems with initialization of URL
	 *                               occurred.
	 */
	public Crawler(final String url, final String baseUrl) throws MalformedURLException {
		this(url, baseUrl, false);
	}

	/**
	 * Constructor with url, baseUrl and isFile parameters.
	 * 
	 * @param url     is the page that should be crawled for data.
	 * @param baseUrl is the parent that is used to not go out of bounds.
	 * @param isFile  is the state of URL
	 * 
	 * @throws MalformedURLException if problems with initialization of URL
	 *                               occurred.
	 */
	public Crawler(final String url, final String baseUrl, final boolean isFile) throws MalformedURLException {
		this.url = url;
		this.baseUrl = baseUrl;
		this.isFile = isFile;
	}

	/**
	 * Creates JSON record of the page with given index.
	 * 
	 * @return The json model with data prepared for Algolia.
	 */
	public String getJsonRecord(final int index) {
		createJsonRecord(index);
		return this.json.toString();
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
	 * @return true if URL is a file and false otherwise.
	 */
	public boolean isFile() {
		return isFile;
	}

	/**
	 * @param the value to set to "isFile" flag.
	 */
	public void setIsFile(final boolean isFile) {
		this.isFile = isFile;
	}

	/**
	 * @return list of visited urls after the crawl.
	 */
	public List<String> getVisitedUrls() {
		return this.visitedUrls;
	}

	/**
	 * @return list of crawled pages
	 */
	public List<Page> getCrawledPages() {
		return this.pages;
	}

	/**
	 * Using the given url in the constructor it visits every resource that haves
	 * the same host and crawls its data.
	 * 
	 */
	public void crawl() {
		visitedUrls.clear();

		// Add to the queue the starting url so it starts with it
		queue.add(url);

		while (!queue.isEmpty()) {
			try {
				String currentUrl = queue.remove(0);
				findUrls(readHtml(currentUrl), currentUrl);
				collectData(readHtml(currentUrl));
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("An error with reading HTML file occured!");
			}
		}
	}

	/**
	 * Reads HTML code from an URL.
	 * 
	 * @param url is page's url whose HTML code should be extracted.
	 * @return a HTML document.
	 * @throws IOException when a problem with reading the HTML code occurred.
	 */
	Document readHtml(final String url) throws IOException {
		return this.isFile ? Jsoup.parse(new File(url.substring(6)), "UTF-8") : Jsoup.connect(url).get();
	}

	/**
	 * Finds appropriate urls among all the matches and adds them to queue.
	 * 
	 * @param page that stores html code
	 * @throws MalformedURLException when a problem with initialization of URL
	 *                               occurred.
	 */
	void findUrls(final Document page, final String pageUrl) throws MalformedURLException {
		// Select all "a" tags
		Elements links = page.select("a");
		// Search for ".html" hrefs
		for (Element link : links) {
			if (!link.attr("href").endsWith(".html"))
				continue;

			String currentUrl = new URL(new URL(pageUrl), link.attr("href")).toString();

			if (!visitedUrls.contains(currentUrl) && currentUrl.startsWith(this.baseUrl)) {
				visitedUrls.add(currentUrl);
				queue.add(currentUrl);
			}
		}
	}

	/**
	 * Collects all the data(titles, keywords and contents) from visited urls.
	 */
	private void collectData(final Document page) {
		final String title = collectTitle(page);
		final List<String> keywords = collectKeywords(page);
		final String contents = collectContents(page);
		final String pageUrl = page.baseUri();

		pages.add(new Page(title, keywords, contents, pageUrl));
	}

	/**
	 * Page's collected title from metadata.
	 */
	private String collectTitle(final Document page) {
		return page.title();
	}

	/**
	 * @return Page's collected keywords from metadata.
	 */
	private List<String> collectKeywords(final Document page) {
		Element element = page.select("meta[name=keywords]").first();
		List<String> keywords = new ArrayList<>();

		if (page.select("meta[name=keywords]").first() != null)
			keywords.add(element.attr("content"));

		return keywords;
	}

	/**
	 * @return Page's collected contents from body section.
	 */
	private String collectContents(final Document page) {
		return page.body().text();
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
}
