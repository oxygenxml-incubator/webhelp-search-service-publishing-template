package ro.sync.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.javatuples.Pair;
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
	
}
