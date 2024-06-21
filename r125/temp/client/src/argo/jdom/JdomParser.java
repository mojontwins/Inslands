package argo.jdom;

import argo.saj.InvalidSyntaxException;
import argo.saj.SajParser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public final class JdomParser {
	public JsonRootNode parse(Reader reader1) throws InvalidSyntaxException, IOException {
		JsonListenerToJdomAdapter jsonListenerToJdomAdapter2 = new JsonListenerToJdomAdapter();
		(new SajParser()).parse(reader1, jsonListenerToJdomAdapter2);
		return jsonListenerToJdomAdapter2.getDocument();
	}

	public JsonRootNode parse(String string1) throws InvalidSyntaxException {
		try {
			JsonRootNode jsonRootNode2 = this.parse((Reader)(new StringReader(string1)));
			return jsonRootNode2;
		} catch (IOException iOException4) {
			throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", iOException4);
		}
	}
}
