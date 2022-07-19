package ro.sync.search;

/**
 * The main class of the search service where all the necessary methods are
 * called
 * 
 * @author Artiom Bozieac
 *
 */
public class Main {
	public static void main(String[] args) {
		Crawler crawler = new Crawler(
				"https://www.oxygenxml.com/doc/versions/24.1/ug-ope/topics/ope-getting_started.html",
				"https://www.oxygenxml.com/doc/versions/24.1/ug-ope/topics/");
		crawler.crawl();
	}
}
