package com.mojang.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class NBTTagDouble extends NBTBase {
	public double doubleValue;

	public NBTTagDouble() {
	}

	public NBTTagDouble(double d1) {
		this.doubleValue = d1;
	}

	final void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeDouble(this.doubleValue);
	}

	final void readTagContents(DataInput dataInput1) throws IOException {
		this.doubleValue = dataInput1.readDouble();
	}

	public final byte getType() {
		return (byte)6;
	}

	public final String toString() {
		return "" + this.doubleValue;
	}
}