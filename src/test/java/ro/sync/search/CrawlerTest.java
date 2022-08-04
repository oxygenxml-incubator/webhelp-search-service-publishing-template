package ro.sync.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
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
	 * @throws IOException if problems with initialization of URL or reading HTML
	 *                     File occurred.
	 */
	@Test
	void recursionTest() throws IOException {
		Crawler crawler = new Crawler(Path.of("src/test/resources/recursion/index.html").toUri().toURL().toString(),
				Path.of("src/test/resources/recursion/").toUri().toURL().toString(), true);
		crawler.crawl();

		List<String> expected = new ArrayList<>();
		expected.add(crawler.getBaseUrl() + "recursion.html");

		assertEquals(expected, crawler.getVisitedUrls());
	}

	/**
	 * Tests the case when tags in HTML code are written on multiple lines.
	 * 
	 * @throws IOException if problems with initialization of URL or reading HTML
	 *                     File occurred.
	 */
	@Test
	void multipleLinesTest() throws IOException {
		Crawler crawler = new Crawler(Path.of("src/test/resources/multipleLines/index.html").toUri().toURL().toString(),
				Path.of("src/test/resources/multipleLines/").toUri().toURL().toString(), true);
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
	 * @throws IOException if problems with initialization of URL or reading HTML
	 *                     File occurred.
	 */
	@Test
	void relativeHrefTest() throws IOException {
		Crawler crawler = new Crawler(
				Path.of("src/test/resources/relativeHref/index/index.html").toUri().toURL().toString(),
				Path.of("src/test/resources/relativeHref/index/").toUri().toURL().toString(), true);
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
		Crawler crawler = new Crawler(Path.of("src/test/resources/multipleLines/index.html").toUri().toURL().toString(),
				Path.of("src/test/resources/multipleLines/").toUri().toURL().toString(), true);

		crawler.readHtml(crawler.getUrl()).toString();

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
		Crawler crawler = new Crawler(Path.of("src/test/resources/multipleLines/index.html").toUri().toURL().toString(),
				Path.of("src/test/resources/multipleLines/").toUri().toURL().toString(), true);

		crawler.findUrls(crawler.readHtml(crawler.getUrl()), crawler.getUrl());

		List<String> expected = new ArrayList<>();
		expected.add(crawler.getBaseUrl() + "other.html");

		assertEquals(expected, crawler.getVisitedUrls());
	}

	/**
	 * Tests what happens if an "a" tag doesn't have "href" attribute.
	 * 
	 * @throws IOException when a problem with reading HTML code occurred.
	 */
	@Test
	void noHrefTest() throws IOException {
		Crawler crawler = new Crawler(Path.of("src/test/resources/noHref/index.html").toUri().toURL().toString(),
				Path.of("src/test/resources/noHref/").toUri().toURL().toString(), true);

		crawler.findUrls(crawler.readHtml(crawler.getUrl()), crawler.getUrl());

		List<String> expected = new ArrayList<>();
		expected.add(crawler.getBaseUrl() + "other.html");

		assertEquals(expected, crawler.getVisitedUrls());
	}

	/**
	 * Tests if relative paths works correctly with base url.
	 * 
	 * @throws IOException when a problem with reading HTML code occurred.
	 */
	@Test
	void baseUrlTest() throws IOException {
		Crawler crawler = new Crawler(Path.of("src/test/resources/baseUrl/t1/t2/other.html").toUri().toURL().toString(),
				Path.of("src/test/resources/baseUrl/").toUri().toURL().toString(), true);

		crawler.findUrls(crawler.readHtml(crawler.getUrl()), crawler.getUrl());

		List<String> expected = new ArrayList<>();
		expected.add(crawler.getBaseUrl() + "other.html");

		assertEquals(expected, crawler.getVisitedUrls());
	}

	/**
	 * Tests if titles are extracted correctly.
	 * 
	 * @throws IOException when a problem with reading HTML code occurred.
	 */
	@Test
	void getTitleTest() throws IOException {
		Crawler crawler = new Crawler(Path.of("src/test/resources/data/index.html").toUri().toURL().toString(),
				Path.of("src/test/resources/data/").toUri().toURL().toString(), true);

		crawler.crawl();

		assertEquals("Publishing your documentation with WebHelp Responsive using GitHub Actions",
				crawler.getCrawledPages().get(0).getTitle());
	}

	/**
	 * Tests if keywords are extracted correctly.
	 * 
	 * @throws IOException when a problem with reading HTML code occurred.
	 */
	@Test
	void getKeywordsTest() throws IOException {
		Crawler crawler = new Crawler(Path.of("src/test/resources/data/index.html").toUri().toURL().toString(),
				Path.of("src/test/resources/data/").toUri().toURL().toString(), true);

		crawler.crawl();

		List<String> expected = new ArrayList<>();
		expected.add("Test");
		expected.add("Search");
		expected.add("Service");

		assertEquals(expected.toString(), crawler.getCrawledPages().get(0).getKeywords().toString());
	}

	/**
	 * Tests if contents are extracted correctly.
	 * 
	 * @throws IOException when a problem with reading HTML code or output.txt
	 *                     occurred.
	 */
	@Test
	void getContentsTest() throws IOException {
		Crawler crawler = new Crawler(
				Path.of("src/test/resources/data/index.html").toUri().toURL().toString(),
				Path.of("src/test/resources/data/").toUri().toURL().toString(), true);

		crawler.crawl();
		
		// TODO: Use a string inside test. It is easier to read
		StringBuilder expected = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/data/output.txt"))) {
			String line = reader.readLine();

			while (line != null) {
				expected.append(line + "\n");
				line = reader.readLine();
			}
		}

		assertTrue(expected.compareTo(new StringBuilder(crawler.getCrawledPages().get(0).getContents())) == 1);
	}
}
