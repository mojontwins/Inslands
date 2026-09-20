package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet70Bed extends Packet {
	public static final String[] errorMessageArr = new String[]{"tile.bed.notValid", null, null};
	public int field_25019_b;

	public Packet70Bed() {
	}

	public Packet70Bed(int i1) {
		this.field_25019_b = i1;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.field_25019_b = dataInputStream1.readByte();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeByte(this.field_25019_b);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleBed(this);
	}

	public int getPacketSize() {
		return 1;
	}
}
