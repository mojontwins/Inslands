package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet9Respawn extends Packet {
	public byte dimension;

	public Packet9Respawn() {
	}

	public Packet9Respawn(byte b1) {
		this.dimension = b1;
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleRespawnPacket(this);
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.dimension = dataInputStream1.readByte();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeByte(this.dimension);
	}

	public int getPacketSize() {
		return 1;
	}
}
