package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet108EnchantItem extends Packet {
	public int windowId;
	public int enchantment;

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleEnchantItem(this);
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.windowId = dataInputStream1.readByte();
		this.enchantment = dataInputStream1.readByte();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeByte(this.windowId);
		dataOutputStream1.writeByte(this.enchantment);
	}

	public int getPacketSize() {
		return 2;
	}
}
