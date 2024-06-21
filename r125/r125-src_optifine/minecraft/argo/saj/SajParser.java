package argo.saj;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public final class SajParser {
	public void parse(Reader reader1, JsonListener jsonListener2) throws InvalidSyntaxException, IOException {
		PositionTrackingPushbackReader positionTrackingPushbackReader3 = new PositionTrackingPushbackReader(reader1);
		char c4 = (char)positionTrackingPushbackReader3.read();
		switch(c4) {
		case '[':
			positionTrackingPushbackReader3.unread(c4);
			jsonListener2.startDocument();
			this.arrayString(positionTrackingPushbackReader3, jsonListener2);
			break;
		case '{':
			positionTrackingPushbackReader3.unread(c4);
			jsonListener2.startDocument();
			this.objectString(positionTrackingPushbackReader3, jsonListener2);
			break;
		default:
			throw new InvalidSyntaxException("Expected either [ or { but got [" + c4 + "].", positionTrackingPushbackReader3);
		}

		int i5 = this.readNextNonWhitespaceChar(positionTrackingPushbackReader3);
		if(i5 != -1) {
			throw new InvalidSyntaxException("Got unexpected trailing character [" + (char)i5 + "].", positionTrackingPushbackReader3);
		} else {
			jsonListener2.endDocument();
		}
	}

	private void arrayString(PositionTrackingPushbackReader positionTrackingPushbackReader1, JsonListener jsonListener2) throws InvalidSyntaxException, IOException {
		char c3 = (char)this.readNextNonWhitespaceChar(positionTrackingPushbackReader1);
		if(c3 != 91) {
			throw new InvalidSyntaxException("Expected object to start with [ but got [" + c3 + "].", positionTrackingPushbackReader1);
		} else {
			jsonListener2.startArray();
			char c4 = (char)this.readNextNonWhitespaceChar(positionTrackingPushbackReader1);
			positionTrackingPushbackReader1.unread(c4);
			if(c4 != 93) {
				this.aJsonValue(positionTrackingPushbackReader1, jsonListener2);
			}

			boolean z5 = false;

			while(!z5) {
				char c6 = (char)this.readNextNonWhitespaceChar(positionTrackingPushbackReader1);
				switch(c6) {
				case ',':
					this.aJsonValue(positionTrackingPushbackReader1, jsonListener2);
					break;
				case ']':
					z5 = true;
					break;
				default:
					throw new InvalidSyntaxException("Expected either , or ] but got [" + c6 + "].", positionTrackingPushbackReader1);
				}
			}

			jsonListener2.endArray();
		}
	}

	private void objectString(PositionTrackingPushbackReader positionTrackingPushbackReader1, JsonListener jsonListener2) throws InvalidSyntaxException, IOException {
		char c3 = (char)this.readNextNonWhitespaceChar(positionTrackingPushbackReader1);
		if(c3 != 123) {
			throw new InvalidSyntaxException("Expected object to start with { but got [" + c3 + "].", positionTrackingPushbackReader1);
		} else {
			jsonListener2.startObject();
			char c4 = (char)this.readNextNonWhitespaceChar(positionTrackingPushbackReader1);
			positionTrackingPushbackReader1.unread(c4);
			if(c4 != 125) {
				this.aFieldToken(positionTrackingPushbackReader1, jsonListener2);
			}

			boolean z5 = false;

			while(!z5) {
				char c6 = (char)this.readNextNonWhitespaceChar(positionTrackingPushbackReader1);
				switch(c6) {
				case ',':
					this.aFieldToken(positionTrackingPushbackReader1, jsonListener2);
					break;
				case '}':
					z5 = true;
					break;
				default:
					throw new InvalidSyntaxException("Expected either , or } but got [" + c6 + "].", positionTrackingPushbackReader1);
				}
			}

			jsonListener2.endObject();
		}
	}

	private void aFieldToken(PositionTrackingPushbackReader positionTrackingPushbackReader1, JsonListener jsonListener2) throws InvalidSyntaxException, IOException {
		char c3 = (char)this.readNextNonWhitespaceChar(positionTrackingPushbackReader1);
		if(34 != c3) {
			throw new InvalidSyntaxException("Expected object identifier to begin with [\"] but got [" + c3 + "].", positionTrackingPushbackReader1);
		} else {
			positionTrackingPushbackReader1.unread(c3);
			jsonListener2.startField(this.stringToken(positionTrackingPushbackReader1));
			char c4 = (char)this.readNextNonWhitespaceChar(positionTrackingPushbackReader1);
			if(c4 != 58) {
				throw new InvalidSyntaxException("Expected object identifier to be followed by : but got [" + c4 + "].", positionTrackingPushbackReader1);
			} else {
				this.aJsonValue(positionTrackingPushbackReader1, jsonListener2);
				jsonListener2.endField();
			}
		}
	}

	private void aJsonValue(PositionTrackingPushbackReader positionTrackingPushbackReader1, JsonListener jsonListener2) throws InvalidSyntaxException, IOException {
		char c3 = (char)this.readNextNonWhitespaceChar(positionTrackingPushbackReader1);
		switch(c3) {
		case '\"':
			positionTrackingPushbackReader1.unread(c3);
			jsonListener2.stringValue(this.stringToken(positionTrackingPushbackReader1));
			break;
		case '-':
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			positionTrackingPushbackReader1.unread(c3);
			jsonListener2.numberValue(this.numberToken(positionTrackingPushbackReader1));
			break;
		case '[':
			positionTrackingPushbackReader1.unread(c3);
			this.arrayString(positionTrackingPushbackReader1, jsonListener2);
			break;
		case 'f':
			char[] c6 = new char[4];
			int i7 = positionTrackingPushbackReader1.read(c6);
			if(i7 == 4 && c6[0] == 97 && c6[1] == 108 && c6[2] == 115 && c6[3] == 101) {
				jsonListener2.falseValue();
				break;
			}

			positionTrackingPushbackReader1.uncount(c6);
			throw new InvalidSyntaxException("Expected \'f\' to be followed by [[a, l, s, e]], but got [" + Arrays.toString(c6) + "].", positionTrackingPushbackReader1);
		case 'n':
			char[] c8 = new char[3];
			int i9 = positionTrackingPushbackReader1.read(c8);
			if(i9 != 3 || c8[0] != 117 || c8[1] != 108 || c8[2] != 108) {
				positionTrackingPushbackReader1.uncount(c8);
				throw new InvalidSyntaxException("Expected \'n\' to be followed by [[u, l, l]], but got [" + Arrays.toString(c8) + "].", positionTrackingPushbackReader1);
			}

			jsonListener2.nullValue();
			break;
		case 't':
			char[] c4 = new char[3];
			int i5 = positionTrackingPushbackReader1.read(c4);
			if(i5 != 3 || c4[0] != 114 || c4[1] != 117 || c4[2] != 101) {
				positionTrackingPushbackReader1.uncount(c4);
				throw new InvalidSyntaxException("Expected \'t\' to be followed by [[r, u, e]], but got [" + Arrays.toString(c4) + "].", positionTrackingPushbackReader1);
			}

			jsonListener2.trueValue();
			break;
		case '{':
			positionTrackingPushbackReader1.unread(c3);
			this.objectString(positionTrackingPushbackReader1, jsonListener2);
			break;
		default:
			throw new InvalidSyntaxException("Invalid character at start of value [" + c3 + "].", positionTrackingPushbackReader1);
		}

	}

	private String numberToken(PositionTrackingPushbackReader positionTrackingPushbackReader1) throws IOException, InvalidSyntaxException {
		StringBuilder stringBuilder2 = new StringBuilder();
		char c3 = (char)positionTrackingPushbackReader1.read();
		if(45 == c3) {
			stringBuilder2.append('-');
		} else {
			positionTrackingPushbackReader1.unread(c3);
		}

		stringBuilder2.append(this.nonNegativeNumberToken(positionTrackingPushbackReader1));
		return stringBuilder2.toString();
	}

	private String nonNegativeNumberToken(PositionTrackingPushbackReader positionTrackingPushbackReader1) throws IOException, InvalidSyntaxException {
		StringBuilder stringBuilder2 = new StringBuilder();
		char c3 = (char)positionTrackingPushbackReader1.read();
		if(48 == c3) {
			stringBuilder2.append('0');
			stringBuilder2.append(this.possibleFractionalComponent(positionTrackingPushbackReader1));
			stringBuilder2.append(this.possibleExponent(positionTrackingPushbackReader1));
		} else {
			positionTrackingPushbackReader1.unread(c3);
			stringBuilder2.append(this.nonZeroDigitToken(positionTrackingPushbackReader1));
			stringBuilder2.append(this.digitString(positionTrackingPushbackReader1));
			stringBuilder2.append(this.possibleFractionalComponent(positionTrackingPushbackReader1));
			stringBuilder2.append(this.possibleExponent(positionTrackingPushbackReader1));
		}

		return stringBuilder2.toString();
	}

	private char nonZeroDigitToken(PositionTrackingPushbackReader positionTrackingPushbackReader1) throws IOException, InvalidSyntaxException {
		char c3 = (char)positionTrackingPushbackReader1.read();
		switch(c3) {
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return c3;
		default:
			throw new InvalidSyntaxException("Expected a digit 1 - 9 but got [" + c3 + "].", positionTrackingPushbackReader1);
		}
	}

	private char digitToken(PositionTrackingPushbackReader positionTrackingPushbackReader1) throws IOException, InvalidSyntaxException {
		char c3 = (char)positionTrackingPushbackReader1.read();
		switch(c3) {
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return c3;
		default:
			throw new InvalidSyntaxException("Expected a digit 1 - 9 but got [" + c3 + "].", positionTrackingPushbackReader1);
		}
	}

	private String digitString(PositionTrackingPushbackReader positionTrackingPushbackReader1) throws IOException {
		StringBuilder stringBuilder2 = new StringBuilder();
		boolean z3 = false;

		while(!z3) {
			char c4 = (char)positionTrackingPushbackReader1.read();
			switch(c4) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				stringBuilder2.append(c4);
				break;
			default:
				z3 = true;
				positionTrackingPushbackReader1.unread(c4);
			}
		}

		return stringBuilder2.toString();
	}

	private String possibleFractionalComponent(PositionTrackingPushbackReader positionTrackingPushbackReader1) throws IOException, InvalidSyntaxException {
		StringBuilder stringBuilder2 = new StringBuilder();
		char c3 = (char)positionTrackingPushbackReader1.read();
		if(c3 == 46) {
			stringBuilder2.append('.');
			stringBuilder2.append(this.digitToken(positionTrackingPushbackReader1));
			stringBuilder2.append(this.digitString(positionTrackingPushbackReader1));
		} else {
			positionTrackingPushbackReader1.unread(c3);
		}

		return stringBuilder2.toString();
	}

	private String possibleExponent(PositionTrackingPushbackReader positionTrackingPushbackReader1) throws IOException, InvalidSyntaxException {
		StringBuilder stringBuilder2 = new StringBuilder();
		char c3 = (char)positionTrackingPushbackReader1.read();
		if(c3 != 46 && c3 != 69) {
			positionTrackingPushbackReader1.unread(c3);
		} else {
			stringBuilder2.append('E');
			stringBuilder2.append(this.possibleSign(positionTrackingPushbackReader1));
			stringBuilder2.append(this.digitToken(positionTrackingPushbackReader1));
			stringBuilder2.append(this.digitString(positionTrackingPushbackReader1));
		}

		return stringBuilder2.toString();
	}

	private String possibleSign(PositionTrackingPushbackReader positionTrackingPushbackReader1) throws IOException {
		StringBuilder stringBuilder2 = new StringBuilder();
		char c3 = (char)positionTrackingPushbackReader1.read();
		if(c3 != 43 && c3 != 45) {
			positionTrackingPushbackReader1.unread(c3);
		} else {
			stringBuilder2.append(c3);
		}

		return stringBuilder2.toString();
	}

	private String stringToken(PositionTrackingPushbackReader positionTrackingPushbackReader1) throws InvalidSyntaxException, IOException {
		StringBuilder stringBuilder2 = new StringBuilder();
		char c3 = (char)positionTrackingPushbackReader1.read();
		if(34 != c3) {
			throw new InvalidSyntaxException("Expected [\"] but got [" + c3 + "].", positionTrackingPushbackReader1);
		} else {
			boolean z4 = false;

			while(!z4) {
				char c5 = (char)positionTrackingPushbackReader1.read();
				switch(c5) {
				case '\"':
					z4 = true;
					break;
				case '\\':
					char c6 = this.escapedStringChar(positionTrackingPushbackReader1);
					stringBuilder2.append(c6);
					break;
				default:
					stringBuilder2.append(c5);
				}
			}

			return stringBuilder2.toString();
		}
	}

	private char escapedStringChar(PositionTrackingPushbackReader positionTrackingPushbackReader1) throws IOException, InvalidSyntaxException {
		char c3 = (char)positionTrackingPushbackReader1.read();
		char c2;
		switch(c3) {
		case '\"':
			c2 = 34;
			break;
		case '/':
			c2 = 47;
			break;
		case '\\':
			c2 = 92;
			break;
		case 'b':
			c2 = 8;
			break;
		case 'f':
			c2 = 12;
			break;
		case 'n':
			c2 = 10;
			break;
		case 'r':
			c2 = 13;
			break;
		case 't':
			c2 = 9;
			break;
		case 'u':
			c2 = (char)this.hexadecimalNumber(positionTrackingPushbackReader1);
			break;
		default:
			throw new InvalidSyntaxException("Unrecognised escape character [" + c3 + "].", positionTrackingPushbackReader1);
		}

		return c2;
	}

	private int hexadecimalNumber(PositionTrackingPushbackReader positionTrackingPushbackReader1) throws IOException, InvalidSyntaxException {
		char[] c2 = new char[4];
		int i3 = positionTrackingPushbackReader1.read(c2);
		if(i3 != 4) {
			throw new InvalidSyntaxException("Expected a 4 digit hexidecimal number but got only [" + i3 + "], namely [" + String.valueOf(c2, 0, i3) + "].", positionTrackingPushbackReader1);
		} else {
			try {
				int i4 = Integer.parseInt(String.valueOf(c2), 16);
				return i4;
			} catch (NumberFormatException numberFormatException6) {
				positionTrackingPushbackReader1.uncount(c2);
				throw new InvalidSyntaxException("Unable to parse [" + String.valueOf(c2) + "] as a hexidecimal number.", numberFormatException6, positionTrackingPushbackReader1);
			}
		}
	}

	private int readNextNonWhitespaceChar(PositionTrackingPushbackReader positionTrackingPushbackReader1) throws IOException {
		boolean z3 = false;

		int i2;
		do {
			i2 = positionTrackingPushbackReader1.read();
			switch(i2) {
			case 9:
			case 10:
			case 13:
			case 32:
				break;
			default:
				z3 = true;
			}
		} while(!z3);

		return i2;
	}
}
