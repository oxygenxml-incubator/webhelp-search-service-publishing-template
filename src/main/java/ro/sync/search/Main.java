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
		Crawler crawler = new Crawler("https://sync.ro");
		crawler.crawl();
	}
}
