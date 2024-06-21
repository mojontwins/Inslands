package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet43Experience extends Packet {
	public float experience;
	public int experienceTotal;
	public int experienceLevel;

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.experience = dataInputStream1.readFloat();
		this.experienceLevel = dataInputStream1.readShort();
		this.experienceTotal = dataInputStream1.readShort();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeFloat(this.experience);
		dataOutputStream1.writeShort(this.experienceLevel);
		dataOutputStream1.writeShort(this.experienceTotal);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleExperience(this);
	}

	public int getPacketSize() {
		return 4;
	}
}
