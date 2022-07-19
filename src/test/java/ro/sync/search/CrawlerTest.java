package ro.sync.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class CrawlerTest {
	private static Crawler crawler;

	@Test
	void crawlSyncro() throws MalformedURLException {
		crawler = new Crawler("https://sync.ro", "https://sync.ro");
		crawler.crawl();
		List<URL> expected = new ArrayList<>() {
			{
				add(new URL("https://sync.ro/index.html"));
				add(new URL("https://sync.ro/company.html"));
				add(new URL("https://sync.ro/products.html"));
				add(new URL("https://sync.ro/jobs.html"));
				add(new URL("https://sync.ro/impact.html"));
				add(new URL("https://sync.ro/contact.html"));
				add(new URL("https://sync.ro/internship.html"));
				add(new URL("https://sync.ro/terms_of_use.html"));
				add(new URL("https://sync.ro/privacy_policy.html"));
			}
		};

		assertEquals(crawler.getVisitedUrls(), expected);
	}

	@Test
	void recursionTest() throws MalformedURLException {
		crawler = new Crawler("https://snazzy-dusk-c2b3e5.netlify.app", "https://snazzy-dusk-c2b3e5.netlify.app");
		crawler.crawl();
		List<URL> expected = new ArrayList<>() {
			{
				add(new URL("https://snazzy-dusk-c2b3e5.netlify.app/index.html"));
				add(new URL(
						"https://snazzy-dusk-c2b3e5.netlify.app/topics/publishing_your_documentation_with_webhelp_responsive_using_github_actions.html"));
				add(new URL("https://snazzy-dusk-c2b3e5.netlify.app/../index.html"));
				add(new URL(
						"https://snazzy-dusk-c2b3e5.netlify.app/../topics/publishing_your_documentation_with_webhelp_responsive_using_github_actions.html"));
			}
		};

		assertEquals(crawler.getVisitedUrls(), expected);
	}

	@Test
	void crawlWebsiteWithoutHtmlReferences() {
		crawler = new Crawler("https://www.w3schools.com/html/html_styles.asp", "https://www.w3schools.com/html/");
		crawler.crawl();
		List<URL> expected = new ArrayList<>();
		assertEquals(crawler.getVisitedUrls(), expected);
	}
}
