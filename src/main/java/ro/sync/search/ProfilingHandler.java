package ro.sync.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class that handles the profiling informatian and creates data to push to
 * Algolia index.
 * 
 * @author Artiom Bozieac
 *
 */
public class ProfilingHandler {
	/**
	 * Logger to inform user about certain actions like errors and others.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ProfilingHandler.class);
	/**
	 * Path to the file with profiling information.
	 */
	private String path = "";
	/**
	 * Map that stores key as a facet value and list of strings as all the possible
	 * values for the facet.
	 */
	private Map<String, List<String>> profilingValues = new HashMap<>();

	/**
	 * Public constructor with path parameter.
	 * 
	 * @param path is the path to the XML document with profiling information.
	 */
	public ProfilingHandler(final String path) {
		this.path = path;
	}

	/**
	 * @return extracted profiling values.
	 */
	public Map<String, List<String>> getProflingValues() {
		extractProfilingValues();
		return this.profilingValues;
	}

	/**
	 * Extracts profiling values from XML document and stores them into a
	 * Map<String, List<String>>, where String is the facet, and List<String> is the
	 * possible values for the facet.
	 */
	public void extractProfilingValues() {
		try {
			File file = new File(this.path);
			FileInputStream fileInputStream = new FileInputStream(file);

			Document doc = Jsoup.parse(fileInputStream, null, "", Parser.xmlParser());

			// A list with all facets names.
			List<String> facetsNames = new ArrayList<>();
			// A list with all possible facets values.
			List<List<String>> possibleFacetsValues = new ArrayList<>();

			// Select all facets names.
			for (Element el : doc.select("name"))
				facetsNames.add(el.text());

			// Select all values nodes.
			for (Element el : doc.select("values")) {
				// Create a temporary list for extracted possible facet values.
				List<String> possibleFacetValues = new ArrayList<>();

				// Extract possbile facet values.
				for (Element value : el.children())
					possibleFacetValues.add(value.attr("key"));

				// Store extracted possible values in list with all possible facets values.
				possibleFacetsValues.add(possibleFacetValues);
			}

			// Put extracted facets names and values into the map.
			for (int i = 0; i < facetsNames.size(); i++)
				this.profilingValues.put(facetsNames.get(i), possibleFacetsValues.get(i));

		} catch (FileNotFoundException e) {
			logger.error("The given path is not valid and hence profiling values cannot be extracted", e);
		} catch (IOException e) {
			logger.error("The given path could not be parsed, check your contents!", e);
		}
	}

	public static void main(String[] args) {
		ProfilingHandler pHandler = new ProfilingHandler(
				"doc/mobile-phone/out/webhelp-responsive/subject-scheme-values.xml");
		
		logger.info(pHandler.getProflingValues().toString());
	}
}
