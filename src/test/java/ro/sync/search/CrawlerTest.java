package ro.sync.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class CrawlerTest {
	private static Crawler crawler;

	@Test
	void recursionTest() throws MalformedURLException {
		crawler = new Crawler(Path.of("target/test-classes/recursion/index.html").toUri().toURL().toString(),
				Path.of("target/test-classes/recursion/").toUri().toURL().toString());
		crawler.crawl();
		List<URL> expected = new ArrayList<>() {
			{
				add(new URL(crawler.getBaseUrl() + "recursion.html"));
				add(new URL(crawler.getBaseUrl() + "index.html"));
			}
		};

		assertEquals(crawler.getVisitedUrls(), expected);
	}

	@Test
	void multipleLinesTest() throws MalformedURLException {
		crawler = new Crawler(Path.of("target/test-classes/multipleLines/index.html").toUri().toURL().toString(),
				Path.of("target/test-classes/multipleLines/").toUri().toURL().toString());
		crawler.crawl();
		List<URL> expected = new ArrayList<>() {
			{
				add(new URL(crawler.getBaseUrl() + "other.html"));
			}
		};

		assertEquals(crawler.getVisitedUrls(), expected);
	}

	@Test
	void relativeHrefTest() throws MalformedURLException {
		crawler = new Crawler(Path.of("target/test-classes/relativeHref/index/index.html").toUri().toURL().toString(),
				Path.of("target/test-classes/relativeHref/index/").toUri().toURL().toString());
		crawler.crawl();

		assertEquals(crawler.getVisitedUrls(), new ArrayList<>());
	}
}
