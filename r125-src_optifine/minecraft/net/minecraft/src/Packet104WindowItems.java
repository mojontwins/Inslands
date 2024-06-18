package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet104WindowItems extends Packet {
	public int windowId;
	public ItemStack[] itemStack;

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.windowId = dataInputStream1.readByte();
		short s2 = dataInputStream1.readShort();
		this.itemStack = new ItemStack[s2];

		for(int i3 = 0; i3 < s2; ++i3) {
			this.itemStack[i3] = this.readItemStack(dataInputStream1);
		}

	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeByte(this.windowId);
		dataOutputStream1.writeShort(this.itemStack.length);

		for(int i2 = 0; i2 < this.itemStack.length; ++i2) {
			this.writeItemStack(this.itemStack[i2], dataOutputStream1);
		}

	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleWindowItems(this);
	}

	public int getPacketSize() {
		return 3 + this.itemStack.length * 5;
	}
}
