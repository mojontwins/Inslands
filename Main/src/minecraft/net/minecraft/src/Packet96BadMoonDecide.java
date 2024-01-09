package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet96BadMoonDecide extends Packet {
	public boolean badMoonDecide = false;
	
	public Packet96BadMoonDecide() {
	}
	
	public Packet96BadMoonDecide(boolean badMoonDecide) {
		this.badMoonDecide = badMoonDecide;
	}

	@Override
	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.badMoonDecide = (dataInputStream1.read() == 1);
	}

	@Override
	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.write(this.badMoonDecide ? 1 : 0);
	}

	@Override
	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleBadMoonDecide (this);
	}

	@Override
	public int getPacketSize() {
		return 1;
	}

}
