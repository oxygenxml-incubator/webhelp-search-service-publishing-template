package ro.sync.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

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
		Crawler crawler = new Crawler(Path.of("target/test-classes/recursion/index.html").toUri().toURL().toString(),
				Path.of("target/test-classes/recursion/").toUri().toURL().toString());
		crawler.crawl();

		List<URL> expected = new ArrayList<>();
		expected.add(new URL(crawler.getBaseUrl() + "recursion.html"));
		expected.add(new URL(crawler.getBaseUrl() + "index.html"));

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
		Crawler crawler = new Crawler(
				Path.of("target/test-classes/multipleLines/index.html").toUri().toURL().toString(),
				Path.of("target/test-classes/multipleLines/").toUri().toURL().toString());
		crawler.crawl();

		List<URL> expected = new ArrayList<>();
		expected.add(new URL(crawler.getBaseUrl() + "other.html"));

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
		Crawler crawler = new Crawler(
				Path.of("target/test-classes/relativeHref/index/index.html").toUri().toURL().toString(),
				Path.of("target/test-classes/relativeHref/index/").toUri().toURL().toString());
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
		Crawler crawler = new Crawler(
				Path.of("target/test-classes/relativeHref/index/index.html").toUri().toURL().toString(),
				Path.of("target/test-classes/relativeHref/index/").toUri().toURL().toString());

		assertEquals(
				"<!DOCTYPE html><html lang=\"en\">    <head>        <title>Index</title>    </head>    <body>        <h1><a href=\"../relative.html\">Relative href</a></h1>    </body></html>",
				crawler.readHtml(crawler.getUrl()));
	}

	/**
	 * Tests if the findUrls() method finds the correct urls and adds them to
	 * visitedUrls list.
	 * 
	 * @throws IOException when a problem with reading HTML code occurred.
	 */
	@Test
	void findUrlsTest() throws IOException {
		Crawler crawler = new Crawler(
				Path.of("target/test-classes/multipleLines/index.html").toUri().toURL().toString(),
				Path.of("target/test-classes/multipleLines/").toUri().toURL().toString());

		crawler.findUrls(crawler.pattern.matcher(crawler.readHtml(crawler.getUrl())));

		List<URL> expected = new ArrayList<>();
		expected.add(new URL(crawler.getBaseUrl() + "other.html"));

		assertEquals(expected, crawler.getVisitedUrls());
	}
}
