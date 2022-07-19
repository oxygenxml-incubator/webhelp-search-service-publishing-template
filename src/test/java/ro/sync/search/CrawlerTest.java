package ro.sync.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

/**
 * The class that tests crawler's main functional.
 * 
 * @author Artiom Bozieac
 *
 */
class CrawlerTest {
	/**
	 * Tests the case when an HTML page depends on another one. For example
	 * index.html references to other.html and vice versa.
	 * 
	 * @throws MalformedURLException if problems with initialization of URL
	 *                               occurred.
	 */
	@Test
	void recursionTest() throws MalformedURLException {
		Crawler crawler = new Crawler("file://src/test/resources/recursion/index.html",
				"file://src/test/resources/recursion/", true);
		crawler.crawl();

		List<String> expected = new ArrayList<>();
		expected.add(crawler.getBaseUrl() + "recursion.html");
		expected.add(crawler.getBaseUrl() + "index.html");

		assertEquals(expected, crawler.getVisitedUrls());
	}

	/**
	 * Tests the case when tags in HTML code are written on multiple lines.
	 * 
	 * @throws MalformedURLException if problems with initialization of URL
	 *                               occurred.
	 */
	@Test
	void multipleLinesTest() throws MalformedURLException {
		Crawler crawler = new Crawler("file://src/test/resources/multipleLines/index.html",
				"file://src/test/resources/multipleLines/", true);
		crawler.crawl();

		List<String> expected = new ArrayList<>();
		expected.add(crawler.getBaseUrl() + "other.html");

		assertEquals(expected, crawler.getVisitedUrls());
	}

	/**
	 * Tests the case when HREF attribute has relative path like this "../". This
	 * href should not be added to visited links because it is out of parents'
	 * bound.
	 * 
	 * @throws MalformedURLException if problems with initialization of URL
	 *                               occurred.
	 */
	@Test
	void relativeHrefTest() throws MalformedURLException {
		Crawler crawler = new Crawler("file://src/test/resources/relativeHref/index.html",
				"file://src/test/resources/relativeHref/", true);
		crawler.crawl();

		assertEquals(new ArrayList<>(), crawler.getVisitedUrls());
	}

	/**
	 * Tests if the readHtml() method returns the HTML code correctly.
	 * 
	 * @throws IOException when a problem with reading HTML code occurred.
	 */
	@Test
	void readHtmlTest() throws IOException {
		Crawler crawler = new Crawler("file://src/test/resources/multipleLines/index.html",
				"file://src/test/resources/multipleLines/", true);

		System.out.println(crawler.readHtml(crawler.getUrl()).toString());

		assertEquals(Jsoup.parse(new File("src/test/resources/multipleLines/index.html"), "UTF-8").toString(),
				crawler.readHtml(crawler.getUrl()).toString());
	}

	/**
	 * Tests if the findUrls() method finds the correct urls and adds them to
	 * visitedUrls list.
	 * 
	 * @throws IOException when a problem with reading HTML code occurred.
	 */
	@Test
	void findUrlsTest() throws IOException {
		Crawler crawler = new Crawler("file://src/test/resources/multipleLines/index.html",
				"file://src/test/resources/multipleLines/", true);

		crawler.findUrls(crawler.readHtml(crawler.getUrl()));

		List<String> expected = new ArrayList<>();
		expected.add(crawler.getBaseUrl() + "other.html");

		assertEquals(expected, crawler.getVisitedUrls());
	}
}
