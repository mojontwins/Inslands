package argo.saj;

public interface JsonListener {
	void startDocument();

	void endDocument();

	void startArray();

	void endArray();

	void startObject();

	void endObject();

	void startField(String string1);

	void endField();

	void stringValue(String string1);

	void numberValue(String string1);

	void trueValue();

	void falseValue();

	void nullValue();
}
