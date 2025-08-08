package net.minecraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.NetHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLightningBolt;
import net.minecraft.src.MathHelper;

public class Packet71Weather extends Packet {
	public int entityID;
	public int encodedPosX;
	public int encodedPosY;
	public int encodedPosZ;
	public int lightning;

	public Packet71Weather() {
	}

	public Packet71Weather(Entity entity1) {
		this.entityID = entity1.entityId;
		this.encodedPosX = MathHelper.floor_double(entity1.posX * 32.0D);
		this.encodedPosY = MathHelper.floor_double(entity1.posY * 32.0D);
		this.encodedPosZ = MathHelper.floor_double(entity1.posZ * 32.0D);
		if(entity1 instanceof EntityLightningBolt) {
			this.lightning = 1;
		}

	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityID = dataInputStream1.readInt();
		this.lightning = dataInputStream1.readByte();
		this.encodedPosX = dataInputStream1.readInt();
		this.encodedPosY = dataInputStream1.readInt();
		this.encodedPosZ = dataInputStream1.readInt();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityID);
		dataOutputStream1.writeByte(this.lightning);
		dataOutputStream1.writeInt(this.encodedPosX);
		dataOutputStream1.writeInt(this.encodedPosY);
		dataOutputStream1.writeInt(this.encodedPosZ);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleWeather(this);
	}

	public int getPacketSize() {
		return 17;
	}
}
