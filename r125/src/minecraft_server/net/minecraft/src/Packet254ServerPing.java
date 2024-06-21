package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet254ServerPing extends Packet {
	public void readPacketData(DataInputStream dataInputStream1) {
	}

	public void writePacketData(DataOutputStream dataOutputStream1) {
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleServerPing(this);
	}

	public int getPacketSize() {
		return 0;
	}
}
