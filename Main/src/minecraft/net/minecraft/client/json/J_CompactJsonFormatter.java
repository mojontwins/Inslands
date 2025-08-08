package net.minecraft.client.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.TreeSet;

public final class J_CompactJsonFormatter implements J_JsonFormatter {
	public String func_27327_a(J_JsonRootNode j_JsonRootNode1) {
		StringWriter stringWriter2 = new StringWriter();

		try {
			this.func_27329_a(j_JsonRootNode1, stringWriter2);
		} catch (IOException iOException4) {
			throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", iOException4);
		}

		return stringWriter2.toString();
	}

	public void func_27329_a(J_JsonRootNode j_JsonRootNode1, Writer writer2) throws IOException {
		this.func_27328_a(j_JsonRootNode1, writer2);
	}

	private void func_27328_a(J_JsonNode j_JsonNode1, Writer writer2) throws IOException {
		boolean z3 = true;
		Iterator<J_JsonNode> iterator4;
		switch(j_JsonNode1.getType()) {
		case ARRAY:
			writer2.append('[');
			iterator4 = j_JsonNode1.getElements().iterator();

			while(iterator4.hasNext()) {
				J_JsonNode j_JsonNode6 = (J_JsonNode)iterator4.next();
				if(!z3) {
					writer2.append(',');
				}

				z3 = false;
				this.func_27328_a(j_JsonNode6, writer2);
			}

			writer2.append(']');
			break;
		case OBJECT:
			writer2.append('{');
			iterator4 = (new TreeSet<J_JsonNode>(j_JsonNode1.getFields().keySet())).iterator();

			while(iterator4.hasNext()) {
				J_JsonStringNode j_JsonStringNode5 = (J_JsonStringNode)iterator4.next();
				if(!z3) {
					writer2.append(',');
				}

				z3 = false;
				this.func_27328_a(j_JsonStringNode5, writer2);
				writer2.append(':');
				this.func_27328_a((J_JsonNode)j_JsonNode1.getFields().get(j_JsonStringNode5), writer2);
			}

			writer2.append('}');
			break;
		case STRING:
			writer2.append('\"').append((new J_JsonEscapedString(j_JsonNode1.getText())).toString()).append('\"');
			break;
		case NUMBER:
			writer2.append(j_JsonNode1.getText());
			break;
		case FALSE:
			writer2.append("false");
			break;
		case TRUE:
			writer2.append("true");
			break;
		case NULL:
			writer2.append("null");
			break;
		default:
			throw new RuntimeException("Coding failure in Argo:  Attempt to format a JsonNode of unknown type [" + j_JsonNode1.getType() + "];");
		}

	}


}
