package ro.sync.search;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
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
	 * Map that stores key as a profiling condition and list of strings as all the
	 * possible values for the profiling condition.
	 */
	private Map<String, List<String>> profilingValues = new HashMap<>();

	/**
	 * Public constructor with path parameter.
	 * 
	 * @param path is the path to the JSON document with profiling information.
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
	 * Extracts profiling values from JSON document and stores them into a
	 * Map<String, List<String>>, where String is the profiling condition, and
	 * List<String> is the possible values for the profiling condition.
	 */
	private void extractProfilingValues() {
		try {
			String contents = new String((Files.readAllBytes(Paths.get(this.path))));
			JSONObject jsonObject = new JSONObject(contents);

			// A list with all profiling conditions names.
			List<String> profilingConditionsNames = new ArrayList<>();
			// A list with all possible profiling conditions values.
			List<List<String>> profilingConditionsValues = new ArrayList<>();

			JSONArray profilingConditionsArr = jsonObject.getJSONObject("subjectScheme").getJSONArray("attrValues");

			// Extract profiling information from JSON.
			for (int i = 0; i < profilingConditionsArr.length(); i++) {
				// Get profiling conditions's names.
				profilingConditionsNames.add(profilingConditionsArr.getJSONObject(i).getString("name"));

				List<String> profilingConditionValues = new ArrayList<>();
				// Select array with all the values for profiling condition.
				for (int j = 0; j < profilingConditionsArr.getJSONObject(i).getJSONArray("values").length(); j++) {
					profilingConditionValues.add(profilingConditionsArr.getJSONObject(i).getJSONArray("values")
							.getJSONObject(j).getString("key"));
				}

				profilingConditionsValues.add(profilingConditionValues);
			}

			// Put extracted profiling conditions names and values into the map.
			for (int i = 0; i < profilingConditionsNames.size(); i++)
				this.profilingValues.put(profilingConditionsNames.get(i), profilingConditionsValues.get(i));

		} catch (FileNotFoundException e) {
			logger.error("The given path is not valid and hence profiling values cannot be extracted", e);
		} catch (IOException e) {
			logger.error("The given path could not be parsed, check your contents!", e);
		}
	}

	public static void main(String[] args) {
		ProfilingHandler pHandler = new ProfilingHandler(
				"doc/mobile-phone/out/webhelp-responsive/subject-scheme-values.json");

		logger.info(pHandler.getProflingValues().toString());
	}
}
