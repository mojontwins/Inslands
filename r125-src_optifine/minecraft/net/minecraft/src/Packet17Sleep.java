package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet17Sleep extends Packet {
	public int entityID;
	public int bedX;
	public int bedY;
	public int bedZ;
	public int field_22046_e;

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityID = dataInputStream1.readInt();
		this.field_22046_e = dataInputStream1.readByte();
		this.bedX = dataInputStream1.readInt();
		this.bedY = dataInputStream1.readByte();
		this.bedZ = dataInputStream1.readInt();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityID);
		dataOutputStream1.writeByte(this.field_22046_e);
		dataOutputStream1.writeInt(this.bedX);
		dataOutputStream1.writeByte(this.bedY);
		dataOutputStream1.writeInt(this.bedZ);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleSleep(this);
	}

	public int getPacketSize() {
		return 14;
	}
}
