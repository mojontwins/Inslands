package net.minecraft.game.world.chunk;

final class NibbleArray {
	public final byte[] data;

	public NibbleArray(int i1) {
		this.data = new byte[i1 >> 1];
	}

	public NibbleArray(byte[] b1) {
		this.data = b1;
	}

	public final int get(int i1, int i2, int i3) {
		i2 = (i1 = i1 << 11 | i3 << 7 | i2) >> 1;
		return (i1 &= 1) == 0 ? this.data[i2] & 15 : this.data[i2] >> 4 & 15;
	}

	public final void set(int i1, int i2, int i3, int i4) {
		i2 = (i1 = i1 << 11 | i3 << 7 | i2) >> 1;
		if((i1 &= 1) == 0) {
			this.data[i2] = (byte)(this.data[i2] & 240 | i4 & 15);
		} else {
			this.data[i2] = (byte)(this.data[i2] & 15 | (i4 & 15) << 4);
		}
	}

	public final boolean isValid() {
		return this.data != null;
	}
}