package argo.format;

import argo.jdom.JsonNodeType;

class CompactJsonFormatter_JsonNodeType {
	static final int[] enumJsonNodeTypeMappingArray = new int[JsonNodeType.values().length];

	static {
		try {
			enumJsonNodeTypeMappingArray[JsonNodeType.ARRAY.ordinal()] = 1;
		} catch (NoSuchFieldError noSuchFieldError7) {
		}

		try {
			enumJsonNodeTypeMappingArray[JsonNodeType.OBJECT.ordinal()] = 2;
		} catch (NoSuchFieldError noSuchFieldError6) {
		}

		try {
			enumJsonNodeTypeMappingArray[JsonNodeType.STRING.ordinal()] = 3;
		} catch (NoSuchFieldError noSuchFieldError5) {
		}

		try {
			enumJsonNodeTypeMappingArray[JsonNodeType.NUMBER.ordinal()] = 4;
		} catch (NoSuchFieldError noSuchFieldError4) {
		}

		try {
			enumJsonNodeTypeMappingArray[JsonNodeType.FALSE.ordinal()] = 5;
		} catch (NoSuchFieldError noSuchFieldError3) {
		}

		try {
			enumJsonNodeTypeMappingArray[JsonNodeType.TRUE.ordinal()] = 6;
		} catch (NoSuchFieldError noSuchFieldError2) {
		}

		try {
			enumJsonNodeTypeMappingArray[JsonNodeType.NULL.ordinal()] = 7;
		} catch (NoSuchFieldError noSuchFieldError1) {
		}

	}
}
