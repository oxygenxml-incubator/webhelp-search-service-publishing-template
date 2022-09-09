package ro.sync.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * The class that tests profiling handler's functional.
 * 
 * @author Artiom Bozieac
 *
 */
class ProfilingHandlerTest {

	@Test
	void extractProfilingValuesTest() {
		ProfilingHandler pHandler = new ProfilingHandler(
				"doc/mobile-phone/out/webhelp-responsive/subject-scheme-values.json");

		Map<String, List<String>> expected = pHandler.getProflingValues();

		Map<String, List<String>> actual = new HashMap<>();
		actual.put("product", Arrays.asList("X2000", "X1000"));
		actual.put("audience", Arrays.asList("Technician", "BasicUser"));

		assertEquals(expected, actual);
	}
}
