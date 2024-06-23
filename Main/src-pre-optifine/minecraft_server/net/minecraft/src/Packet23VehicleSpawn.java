package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet23VehicleSpawn extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int motionXencoded;
	public int motionYencoded;
	public int motionZencoded;
	public int type;
	public int throwerEntityId;
	public int metadata;

	public Packet23VehicleSpawn() {
	}

	public Packet23VehicleSpawn(Entity entity1, int i2) {
		this(entity1, i2, 0);
	}

	public Packet23VehicleSpawn(Entity entity1, int i2, int i3) {
		this(entity1, i2, i3, 0);
	}
	
	public Packet23VehicleSpawn(Entity entity1, int i2, int i3, int metadata) {
		this.entityId = entity1.entityId;
		this.xPosition = MathHelper.floor_double(entity1.posX * 32.0D);
		this.yPosition = MathHelper.floor_double(entity1.posY * 32.0D);
		this.zPosition = MathHelper.floor_double(entity1.posZ * 32.0D);
		this.type = i2;
		this.throwerEntityId = i3;
		if(i3 > 0) {
			double d4 = entity1.motionX;
			double d6 = entity1.motionY;
			double d8 = entity1.motionZ;
			double d10 = 3.9D;
			if(d4 < -d10) {
				d4 = -d10;
			}

			if(d6 < -d10) {
				d6 = -d10;
			}

			if(d8 < -d10) {
				d8 = -d10;
			}

			if(d4 > d10) {
				d4 = d10;
			}

			if(d6 > d10) {
				d6 = d10;
			}

			if(d8 > d10) {
				d8 = d10;
			}

			this.motionXencoded = (int)(d4 * 8000.0D);
			this.motionYencoded = (int)(d6 * 8000.0D);
			this.motionZencoded = (int)(d8 * 8000.0D);
		}

		this.metadata = metadata;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
		this.type = dataInputStream1.readByte();
		this.xPosition = dataInputStream1.readInt();
		this.yPosition = dataInputStream1.readInt();
		this.zPosition = dataInputStream1.readInt();
		this.throwerEntityId = dataInputStream1.readInt();
		this.metadata = dataInputStream1.readInt();
		if(this.throwerEntityId > 0) {
			this.motionXencoded = dataInputStream1.readShort();
			this.motionYencoded = dataInputStream1.readShort();
			this.motionZencoded = dataInputStream1.readShort();
		}

	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
		dataOutputStream1.writeByte(this.type);
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.writeInt(this.yPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.writeInt(this.throwerEntityId);
		dataOutputStream1.writeInt(this.metadata);
		if(this.throwerEntityId > 0) {
			dataOutputStream1.writeShort(this.motionXencoded);
			dataOutputStream1.writeShort(this.motionYencoded);
			dataOutputStream1.writeShort(this.motionZencoded);
		}

	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleVehicleSpawn(this);
	}

	public int getPacketSize() {
		return 21 + 4 + (this.throwerEntityId > 0 ? 6 : 0);
	}
}
