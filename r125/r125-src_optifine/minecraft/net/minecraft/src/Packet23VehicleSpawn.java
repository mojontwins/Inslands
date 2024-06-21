package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet23VehicleSpawn extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int speedX;
	public int speedY;
	public int speedZ;
	public int type;
	public int throwerEntityId;

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
		this.type = dataInputStream1.readByte();
		this.xPosition = dataInputStream1.readInt();
		this.yPosition = dataInputStream1.readInt();
		this.zPosition = dataInputStream1.readInt();
		this.throwerEntityId = dataInputStream1.readInt();
		if(this.throwerEntityId > 0) {
			this.speedX = dataInputStream1.readShort();
			this.speedY = dataInputStream1.readShort();
			this.speedZ = dataInputStream1.readShort();
		}

	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
		dataOutputStream1.writeByte(this.type);
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.writeInt(this.yPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.writeInt(this.throwerEntityId);
		if(this.throwerEntityId > 0) {
			dataOutputStream1.writeShort(this.speedX);
			dataOutputStream1.writeShort(this.speedY);
			dataOutputStream1.writeShort(this.speedZ);
		}

	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleVehicleSpawn(this);
	}

	public int getPacketSize() {
		return 21 + this.throwerEntityId > 0 ? 6 : 0;
	}
}
