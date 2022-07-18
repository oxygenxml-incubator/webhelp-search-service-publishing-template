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
	 * "https://google.com/search/index.html" then it won't go to any other sites
	 * that don't start with "https://google.com/search".
	 */
	private URL baseUrl;

	/**
	 * List that stores all the visited urls in order to not crawl them more than
	 * one time
	 */
	private List<URL> visitedUrls = new ArrayList<URL>();

	/**
	 * List that serves as a queue that is used to perform BFS algorithm
	 */
	private List<URL> queue = new ArrayList<URL>();

	/**
	 * Regex pattern that finds html references
	 */
	private Pattern pattern = Pattern.compile("<a.href=([\\\"'])([^#\"]*?\\.html)");

	/**
	 * Constructor with url and baseUrl argument"
	 * 
	 * @param url - URL to be crawled
	 * @param baseUrl = base url to stay in bounds
	 */
	public Crawler(final String url, final String baseUrl) {
		try {
			this.url = new URL(url);
			this.baseUrl = new URL(baseUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public URL getUrl() {
		return this.url;
	}

	public URL getBaseUrl() {
		return this.baseUrl;
	}

	public List<URL> getVisitedUrls() {
		return this.visitedUrls;
	}

	/**
	 * Using the given url in the constructor it visits every resource that haves
	 * the same host and crawls its data
	 */
	public void crawl() {
		visitedUrls.clear();

		// Add to the queue the starting url so it starts with it
		queue.add(url);

		try {
			while (!queue.isEmpty()) {
				String htmlCode = extractHtmlCode(queue.remove(0));

				// Matcher that finds all the matches in a text using the pattern
				Matcher matcher = pattern.matcher(htmlCode);

				findUrls(matcher);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String extractHtmlCode(final URL currentUrl) {
		// A string that will store raw HTML code from the input stream
		String htmlCode = "";

		// Use a BufferedReader and get the stream from url to crawl the data
		try (BufferedReader input = new BufferedReader(new InputStreamReader(currentUrl.openStream()));) {
			String inputLine = input.readLine();

			while (inputLine != null) {
				htmlCode += inputLine;

				inputLine = input.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return htmlCode;
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
				currentUrl = new URL(baseUrl, matcher.group(2));
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
