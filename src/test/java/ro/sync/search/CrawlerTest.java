package ro.sync.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class CrawlerTest {
	private static Crawler crawler;

	private static Stream<Pair<String, String>> provideParameters() {
		return Stream.of(
				new Pair<String, String>("https://www.google.com/search?q=something", "https://www.google.com"),
				new Pair<String, String>("https://translate.google.com/?sl=en&tl=ru&text=something&op=translate",
						"https://translate.google.com"),
				new Pair<String, String>("https://www.news.com.au/lifestyle/real-life/news-life",
						"https://www.news.com.au"));
	}

	@ParameterizedTest
	@MethodSource("provideParameters")
	void testBaseUrls(final Pair<String, String> urls) {
		crawler = new Crawler(urls.getValue0());
		assertEquals(crawler.getBaseUrl().toString(), urls.getValue1());
	}

	@Test
	void crawlSyncro() throws MalformedURLException {
		crawler = new Crawler("https://sync.ro");
		crawler.crawl();
		List<URL> expected = new ArrayList<URL>() {
			{
				add(new URL("https://sync.ro"));
				add(new URL("https://sync.ro/index.html"));
				add(new URL("https://sync.ro/company.html"));
				add(new URL("https://sync.ro/products.html"));
				add(new URL("https://sync.ro/jobs.html"));
				add(new URL("https://sync.ro/impact.html"));
				add(new URL("https://sync.ro/contact.html"));
				add(new URL("https://sync.ro/internship.html"));
				add(new URL("https://sync.ro/privacy_policy.html"));
			}
		};

		assertEquals(crawler.getVisitedUrls(), expected);
	}

}
