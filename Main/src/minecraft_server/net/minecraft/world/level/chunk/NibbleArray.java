package net.minecraft.world.level.chunk;

public class NibbleArray {
	public final byte[] data;

	public NibbleArray(int i1) {
		this.data = new byte[i1 >> 1];
	}

	public NibbleArray(byte[] b1) {
		this.data = b1;
	}

	public int getNibble(int i1, int i2, int i3) {
		return this.getNibble(i1 << 11 | i3 << 7 | i2);
	}

	public int getNibble(int flatIndex) {
		int byteIndex = flatIndex >> 1;
		return (flatIndex & 1) == 0 ? this.data[byteIndex] & 15 : this.data[byteIndex] >> 4 & 15;
	}

	public void setNibble(int i1, int i2, int i3, int i4) {
		this.setNibble(i1 << 11 | i3 << 7 | i2, i4);
	}

	public void setNibble(int flatIndex, int value) {
		int byteIndex = flatIndex >> 1;
		if((flatIndex & 1) == 0) {
			this.data[byteIndex] = (byte)(this.data[byteIndex] & 240 | value & 15);
		} else {
			this.data[byteIndex] = (byte)(this.data[byteIndex] & 15 | (value & 15) << 4);
		}
	}

	public boolean isValid() {
		return this.data != null;
	}
	
	public byte[] asByteArray() {
		byte[] result = new byte[this.data.length * 2];
		int idx = 0;
		for(int i = 0; i < this.data.length; i ++) {
			result[idx++] = (byte)(this.data[i] & 15);
			result[idx++] = (byte)((this.data[i] >> 4) & 15);
		}
		
		return result;
	}
}
