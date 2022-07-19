package ro.sync.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private URL url;
	/**
	 * The base url of url to be crawled. It is used in order to not leave the
	 * website and crawl data to infinite. For example if the base url is
	 * "https://google.com/search/index.html" then it won't go to any other sites
	 * that don't start with "https://google.com/search".
	 */
	private URL baseUrl;

	/**
	 * List that stores all the visited urls in order to not crawl them more than
	 * one time.
	 */
	private List<URL> visitedUrls = new ArrayList<>();

	/**
	 * List that serves as a queue that is used to perform BFS algorithm.
	 */
	private List<URL> queue = new ArrayList<>();

	/**
	 * The Regex pattern that finds html references in text.
	 */
	Pattern pattern = Pattern.compile("<a.*href=([\"'])([^#\"]*?\\.html)");

	/**
	 * Constructor with url and baseUrl parameters
	 * 
	 * @param url     is the page that should be crawled for data.
	 * @param baseUrl is the parent that is used to not go out of bounds.
	 * 
	 * @throws MalformedURLException if problems with initialization of URL
	 *                               occurred.
	 */
	public Crawler(final String url, final String baseUrl) throws MalformedURLException {
		this.url = new URL(url);
		this.baseUrl = new URL(baseUrl);
	}

	/**
	 * 
	 * @return start url that should be crawled for data.
	 */
	public URL getUrl() {
		return this.url;
	}

	/**
	 * @return base url that is used to not go out of parent's bounds
	 */
	public URL getBaseUrl() {
		return this.baseUrl;
	}

	/**
	 * @return list of visited urls after the crawl
	 */
	public List<URL> getVisitedUrls() {
		return this.visitedUrls;
	}

	/**
	 * Using the given url in the constructor it visits every resource that haves
	 * the same host and crawls its data.
	 */
	public void crawl() {
		visitedUrls.clear();

		// Add to the queue the starting url so it starts with it
		queue.add(url);

		while (!queue.isEmpty()) {
			String htmlCode;
			try {
				htmlCode = readHtml(queue.remove(0));
			} catch (IOException e) {
				System.out.println(e.getMessage());
				htmlCode = "";
			}

			// Matcher that finds all the matches in a text using the pattern
			Matcher matcher = pattern.matcher(htmlCode);

			try {
				findUrls(matcher);
			} catch (MalformedURLException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	/**
	 * Reads HTML code from an URL.
	 * 
	 * @param url is page's url whose HTML code should be extracted.
	 * @return extracted HTML code.
	 * @throws IOException when a problem with reading the HTML code occurred.
	 */
	String readHtml(final URL url) throws IOException {
		// A string that will store raw HTML code from the input stream
		StringBuilder htmlCode = new StringBuilder();

		// Use a BufferedReader and get the stream from url to crawl the data
		try (BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));) {
			String inputLine = input.readLine();

			while (inputLine != null) {
				htmlCode.append(inputLine);

				inputLine = input.readLine();
			}
		} catch (IOException e) {
			throw new IOException("A problem with reading your HTML page occured! " + e);
		}

		return htmlCode.toString();
	}

	/**
	 * Finds appropriate urls among all the matches and adds them to queue.
	 * 
	 * @param matcher that stores all the found matches.
	 * @throws MalformedURLException when a problem with initialization of URL
	 *                               occurred.
	 */
	void findUrls(final Matcher matcher) throws MalformedURLException {
		while (matcher.find()) {
			URL currentUrl;
			try {
				currentUrl = new URL(baseUrl, matcher.group(2));

				if (!visitedUrls.contains(currentUrl) && currentUrl.toString().startsWith(baseUrl.toString())) {
					System.out.println("Website with URL: " + currentUrl.toString());
					visitedUrls.add(currentUrl);
					queue.add(currentUrl);
				}
			} catch (MalformedURLException e) {
				throw new MalformedURLException("A problem with creating the URL occured! " + e);
			}
		}
	}
}
