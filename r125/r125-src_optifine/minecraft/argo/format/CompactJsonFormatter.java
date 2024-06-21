package argo.format;

import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.TreeSet;

public final class CompactJsonFormatter implements JsonFormatter {
	public String format(JsonRootNode jsonRootNode1) {
		StringWriter stringWriter2 = new StringWriter();

		try {
			this.format(jsonRootNode1, stringWriter2);
		} catch (IOException iOException4) {
			throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", iOException4);
		}

		return stringWriter2.toString();
	}

	public void format(JsonRootNode jsonRootNode1, Writer writer2) throws IOException {
		this.formatJsonNode(jsonRootNode1, writer2);
	}

	private void formatJsonNode(JsonNode jsonNode1, Writer writer2) throws IOException {
		boolean z3 = true;
		Iterator iterator4;
		switch(CompactJsonFormatter_JsonNodeType.enumJsonNodeTypeMappingArray[jsonNode1.getType().ordinal()]) {
		case 1:
			writer2.append('[');
			iterator4 = jsonNode1.getElements().iterator();

			while(iterator4.hasNext()) {
				JsonNode jsonNode6 = (JsonNode)iterator4.next();
				if(!z3) {
					writer2.append(',');
				}

				z3 = false;
				this.formatJsonNode(jsonNode6, writer2);
			}

			writer2.append(']');
			break;
		case 2:
			writer2.append('{');
			iterator4 = (new TreeSet(jsonNode1.getFields().keySet())).iterator();

			while(iterator4.hasNext()) {
				JsonStringNode jsonStringNode5 = (JsonStringNode)iterator4.next();
				if(!z3) {
					writer2.append(',');
				}

				z3 = false;
				this.formatJsonNode(jsonStringNode5, writer2);
				writer2.append(':');
				this.formatJsonNode((JsonNode)jsonNode1.getFields().get(jsonStringNode5), writer2);
			}

			writer2.append('}');
			break;
		case 3:
			writer2.append('\"').append((new JsonEscapedString(jsonNode1.getText())).toString()).append('\"');
			break;
		case 4:
			writer2.append(jsonNode1.getText());
			break;
		case 5:
			writer2.append("false");
			break;
		case 6:
			writer2.append("true");
			break;
		case 7:
			writer2.append("null");
			break;
		default:
			throw new RuntimeException("Coding failure in Argo:  Attempt to format a JsonNode of unknown type [" + jsonNode1.getType() + "];");
		}

	}
}
