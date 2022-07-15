package ro.sync.search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class that crawls an HTML website and looks for data
 * 
 * @author Artiom Bozieac
 *
 */
public class Crawler {
	/**
	 * The url to be crawled
	 */
	private URL url;
	/**
	 * The base url of url to be crawled. It is used in order to not leave the
	 * website and crawl data to infinite. For example if the base url is
	 * "https://google.com" then it won't go to any other sites that don't start
	 * with "https://google.com".
	 */
	private URL baseUrl;

	/**
	 * List that stores all the visited urls in order to not crawl them more than
	 * one time
	 */
	private List<URL> visitedUrls;

	/**
	 * List that serves as a queue that is used to perform BFS algorithm
	 */
	private List<URL> queue;

	/**
	 * Constructor with no arguments. If no arguments are passed then "url" and
	 * "baseUrl" are set to null.
	 */
	public Crawler() {
		url = null;
		baseUrl = null;

		visitedUrls = new ArrayList<URL>();
		queue = new ArrayList<URL>();
	}

	/**
	 * Constructor with url argument. If url is "https://google.com/search" then
	 * baseUrl will be set to "https://google.com"
	 * 
	 * @param url - URL to be crawled
	 */
	public Crawler(final String url) {
		visitedUrls = new ArrayList<URL>();
		queue = new ArrayList<URL>();

		try {
			this.url = new URL(url);

			// Extract the protocol from URL and add the host
			this.baseUrl = new URL(this.url.getProtocol() + "://" + this.url.getHost());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("The baseUrl could not be extracted");
		}
	}

	public void setUrl(final URL url) {
		this.url = url;
	}

	public URL getUrl() {
		return this.url;
	}

	public URL getBaseUrl() {
		return this.baseUrl;
	}

	/**
	 * Using the given url in the constructor it visits every resource that haves
	 * the same host and crawls its data
	 */
	public void crawl() {
		visitedUrls.clear();
		
		// Add to the queue the starting url so it starts with it
		queue.add(url);
		// Add to the visited url the starting url so it won't be visited twice and more
		visitedUrls.add(url);

		try {
			while (!queue.isEmpty()) {
				// Get the front url
				URL currentUrl = queue.remove(0);

				// A string that will store raw HTML code from the input stream
				String htmlCode = "";

				// Use a BufferedReader and get the stream from url to crawl the data
				BufferedReader input = new BufferedReader(new InputStreamReader(currentUrl.openStream()));
				String inputLine = input.readLine();

				while (inputLine != null) {
					htmlCode += inputLine;

					inputLine = input.readLine();
				}

				input.close();

				// Pattern that uses regex in order to find urls in a text
				Pattern pattern = Pattern.compile("<a.href=([\"'])([^#]*?.html)");
				// Matcher that finds all the matches in a text using the pattern
				Matcher matcher = pattern.matcher(htmlCode);

				findUrls(matcher);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Finds appropriate urls among all the matches and adds them to queue
	 * 
	 * @param matcher - Matcher that stores all the matches
	 */
	private void findUrls(final Matcher matcher) {
		while (matcher.find()) {
			URL currentUrl;
			try {
				// If url starts with "http" then don't add the baseUrl to it
				if (matcher.group(2).toString().startsWith("http")) {
					currentUrl = new URL(matcher.group(2));
				} else {
					// If url starts doesn't start with "/" then add one to access it
					if (!matcher.group(2).toString().startsWith("/")) {
						currentUrl = new URL(baseUrl + "/" + matcher.group(2));
					} else {
						currentUrl = new URL(baseUrl + matcher.group(2));
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				currentUrl = null;
			}

			if (currentUrl != null && !visitedUrls.contains(currentUrl)
					&& currentUrl.toString().startsWith(baseUrl.toString())) {
				System.out.println("Website with URL: " + currentUrl.toString());
				visitedUrls.add(currentUrl);
				queue.add(currentUrl);
			}
		}
	}
}
