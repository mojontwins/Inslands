package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public final class J_JdomParser {
	public J_JsonRootNode parseFromReader(Reader reader1) throws IOException, J_InvalidSyntaxException {
		J_JsonListenerToJdomAdapter j_JsonListenerToJdomAdapter2 = new J_JsonListenerToJdomAdapter();
		(new J_SajParser()).func_27463_a(reader1, j_JsonListenerToJdomAdapter2);
		return j_JsonListenerToJdomAdapter2.func_27208_a();
	}

	public J_JsonRootNode parse(String string1) throws J_InvalidSyntaxException {
		try {
			J_JsonRootNode j_JsonRootNode2 = this.parseFromReader(new StringReader(string1));
			return j_JsonRootNode2;
		} catch (IOException iOException4) {
			throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", iOException4);
		}
	}

	public J_JsonRootNode parseFromInputStream(InputStream inputStream) throws J_InvalidSyntaxException {
		Reader reader = null;
		try {
			reader = new InputStreamReader(inputStream);
			J_JsonRootNode j_JsonRootNode2 = this.parseFromReader(reader);
			return j_JsonRootNode2;
		} catch (IOException iOException4) {
			throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", iOException4);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
