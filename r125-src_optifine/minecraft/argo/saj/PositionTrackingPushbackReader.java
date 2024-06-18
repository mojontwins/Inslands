package argo.saj;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

final class PositionTrackingPushbackReader implements ThingWithPosition {
	private final PushbackReader pushbackReader;
	private int characterCount = 0;
	private int lineCount = 1;
	private boolean lastCharacterWasCarriageReturn = false;

	public PositionTrackingPushbackReader(Reader reader1) {
		this.pushbackReader = new PushbackReader(reader1);
	}

	public void unread(char c1) throws IOException {
		--this.characterCount;
		if(this.characterCount < 0) {
			this.characterCount = 0;
		}

		this.pushbackReader.unread(c1);
	}

	public void uncount(char[] c1) {
		this.characterCount -= c1.length;
		if(this.characterCount < 0) {
			this.characterCount = 0;
		}

	}

	public int read() throws IOException {
		int i1 = this.pushbackReader.read();
		this.updateCharacterAndLineCounts(i1);
		return i1;
	}

	public int read(char[] c1) throws IOException {
		int i2 = this.pushbackReader.read(c1);
		char[] c3 = c1;
		int i4 = c1.length;

		for(int i5 = 0; i5 < i4; ++i5) {
			char c6 = c3[i5];
			this.updateCharacterAndLineCounts(c6);
		}

		return i2;
	}

	private void updateCharacterAndLineCounts(int i1) {
		if(13 == i1) {
			this.characterCount = 0;
			++this.lineCount;
			this.lastCharacterWasCarriageReturn = true;
		} else {
			if(10 == i1 && !this.lastCharacterWasCarriageReturn) {
				this.characterCount = 0;
				++this.lineCount;
			} else {
				++this.characterCount;
			}

			this.lastCharacterWasCarriageReturn = false;
		}

	}

	public int getColumn() {
		return this.characterCount;
	}

	public int getRow() {
		return this.lineCount;
	}
}
