package argo.jdom;

import java.util.Arrays;

public final class JsonNodeSelectors {
	public static JsonNodeSelector func_27349_a(Object... object0) {
		return chainOn(object0, new JsonNodeSelector(new JsonNodeSelectors_String()));
	}

	public static JsonNodeSelector func_27346_b(Object... object0) {
		return chainOn(object0, new JsonNodeSelector(new JsonNodeSelectors_Array()));
	}

	public static JsonNodeSelector func_27353_c(Object... object0) {
		return chainOn(object0, new JsonNodeSelector(new JsonNodeSelectors_Object()));
	}

	public static JsonNodeSelector func_27348_a(String string0) {
		return func_27350_a(JsonNodeFactories.aJsonString(string0));
	}

	public static JsonNodeSelector func_27350_a(JsonStringNode jsonStringNode0) {
		return new JsonNodeSelector(new JsonNodeSelectors_Field(jsonStringNode0));
	}

	public static JsonNodeSelector func_27351_b(String string0) {
		return func_27353_c(new Object[0]).with(func_27348_a(string0));
	}

	public static JsonNodeSelector func_27347_a(int i0) {
		return new JsonNodeSelector(new JsonNodeSelectors_Element(i0));
	}

	public static JsonNodeSelector func_27354_b(int i0) {
		return func_27346_b(new Object[0]).with(func_27347_a(i0));
	}

	private static JsonNodeSelector chainOn(Object[] object0, JsonNodeSelector jsonNodeSelector1) {
		JsonNodeSelector jsonNodeSelector2 = jsonNodeSelector1;

		for(int i3 = object0.length - 1; i3 >= 0; --i3) {
			if(object0[i3] instanceof Integer) {
				jsonNodeSelector2 = chainedJsonNodeSelector(func_27354_b(((Integer)object0[i3]).intValue()), jsonNodeSelector2);
			} else {
				if(!(object0[i3] instanceof String)) {
					throw new IllegalArgumentException("Element [" + object0[i3] + "] of path elements" + " [" + Arrays.toString(object0) + "] was of illegal type [" + object0[i3].getClass().getCanonicalName() + "]; only Integer and String are valid.");
				}

				jsonNodeSelector2 = chainedJsonNodeSelector(func_27351_b((String)object0[i3]), jsonNodeSelector2);
			}
		}

		return jsonNodeSelector2;
	}

	private static JsonNodeSelector chainedJsonNodeSelector(JsonNodeSelector jsonNodeSelector0, JsonNodeSelector jsonNodeSelector1) {
		return new JsonNodeSelector(new ChainedFunctor(jsonNodeSelector0, jsonNodeSelector1));
	}
}
