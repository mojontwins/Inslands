package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet103SetSlot extends Packet {
	public int windowId;
	public int itemSlot;
	public ItemStack myItemStack;

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleSetSlot(this);
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.windowId = dataInputStream1.readByte();
		this.itemSlot = dataInputStream1.readShort();
		this.myItemStack = this.readItemStack(dataInputStream1);
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeByte(this.windowId);
		dataOutputStream1.writeShort(this.itemSlot);
		this.writeItemStack(this.myItemStack, dataOutputStream1);
	}

	public int getPacketSize() {
		return 8;
	}
}
