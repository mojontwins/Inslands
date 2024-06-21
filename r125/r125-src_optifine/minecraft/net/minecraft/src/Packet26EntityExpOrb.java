package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet26EntityExpOrb extends Packet {
	public int entityId;
	public int posX;
	public int posY;
	public int posZ;
	public int xpValue;

	public Packet26EntityExpOrb() {
	}

	public Packet26EntityExpOrb(EntityXPOrb entityXPOrb1) {
		this.entityId = entityXPOrb1.entityId;
		this.posX = MathHelper.floor_double(entityXPOrb1.posX * 32.0D);
		this.posY = MathHelper.floor_double(entityXPOrb1.posY * 32.0D);
		this.posZ = MathHelper.floor_double(entityXPOrb1.posZ * 32.0D);
		this.xpValue = entityXPOrb1.getXpValue();
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
		this.posX = dataInputStream1.readInt();
		this.posY = dataInputStream1.readInt();
		this.posZ = dataInputStream1.readInt();
		this.xpValue = dataInputStream1.readShort();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
		dataOutputStream1.writeInt(this.posX);
		dataOutputStream1.writeInt(this.posY);
		dataOutputStream1.writeInt(this.posZ);
		dataOutputStream1.writeShort(this.xpValue);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleEntityExpOrb(this);
	}

	public int getPacketSize() {
		return 18;
	}
}
