package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet61DoorChange extends Packet {
	public int sfxID;
	public int auxData;
	public int posX;
	public int posY;
	public int posZ;

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.sfxID = dataInputStream1.readInt();
		this.posX = dataInputStream1.readInt();
		this.posY = dataInputStream1.readByte() & 255;
		this.posZ = dataInputStream1.readInt();
		this.auxData = dataInputStream1.readInt();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.sfxID);
		dataOutputStream1.writeInt(this.posX);
		dataOutputStream1.writeByte(this.posY & 255);
		dataOutputStream1.writeInt(this.posZ);
		dataOutputStream1.writeInt(this.auxData);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleDoorChange(this);
	}

	public int getPacketSize() {
		return 20;
	}
}
