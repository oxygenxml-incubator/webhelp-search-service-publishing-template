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

/**
 * The class that crawls an HTML website and looks for data.
 * 
 * @author Artiom Bozieac
 *
 */
public class Crawler {
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
				findUrls(readHtml(queue.remove(0)));
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("An error with reading HTML data occurred!");
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
		return this.isFile ? Jsoup.parse(new File(url.substring(7)), "UTF-8") : Jsoup.connect(url).get();
	}

	/**
	 * Finds appropriate urls among all the matches and adds them to queue.
	 * 
	 * @param page that stores html code
	 * @throws MalformedURLException when a problem with initialization of URL
	 *                               occurred.
	 */
	void findUrls(final Document page) throws MalformedURLException {
		// Select all "a" tags
		Elements links = page.select("a");
		// Search for ".html" hrefs
		for (Element link : links) {
			if (!link.attr("href").endsWith(".html"))
				continue;

			String currentUrl = new URL(new URL(baseUrl), link.attr("href")).toString();

			if (!visitedUrls.contains(currentUrl) && currentUrl.startsWith(baseUrl)) {
				System.out.println("Website with URL: " + currentUrl);
				visitedUrls.add(currentUrl);
				queue.add(currentUrl);
			}
		}
	}
}
