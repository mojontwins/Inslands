package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet93FiniteWorldSettings extends Packet {
	public int themeID;
	public int sizeID;

	public Packet93FiniteWorldSettings() {
	}
	
	public Packet93FiniteWorldSettings(int themeID, int sizeID) {
		this.themeID = themeID;
		this.sizeID = sizeID;
	}
	
	@Override
	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.themeID = dataInputStream1.readByte();
		this.sizeID = dataInputStream1.readByte();
	}

	@Override
	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeByte(this.themeID);
		dataOutputStream1.writeByte(this.sizeID);
	}

	@Override
	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleFiniteWorldSettings(this);
	}

	@Override
	public int getPacketSize() {
		return 2;
	}

}
