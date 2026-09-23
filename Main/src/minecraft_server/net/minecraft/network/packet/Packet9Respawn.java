package net.minecraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.NetHandler;

public class Packet9Respawn extends Packet {
	public int dimension;

	public Packet9Respawn() {
	}

	public Packet9Respawn(int i1) {
		this.dimension = i1;
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleRespawnPacket(this);
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.dimension = dataInputStream1.readUnsignedByte();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeByte(this.dimension);
	}

	public int getPacketSize() {
		return 1;
	}
}