package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet70Bed extends Packet {
	public static final String[] errorMessageArr = new String[]{"tile.bed.notValid", null, null};
	public int setRainingAction;

	public Packet70Bed() {
	}

	public Packet70Bed(int i1) {
		this.setRainingAction = i1;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.setRainingAction = dataInputStream1.readByte();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeByte(this.setRainingAction);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleBed(this);
	}

	public int getPacketSize() {
		return 1;
	}
}
