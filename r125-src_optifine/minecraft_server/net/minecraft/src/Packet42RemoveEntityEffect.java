package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet42RemoveEntityEffect extends Packet {
	public int entityId;
	public byte effectId;

	public Packet42RemoveEntityEffect() {
	}

	public Packet42RemoveEntityEffect(int i1, PotionEffect potionEffect2) {
		this.entityId = i1;
		this.effectId = (byte)(potionEffect2.getPotionID() & 255);
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
		this.effectId = dataInputStream1.readByte();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
		dataOutputStream1.writeByte(this.effectId);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleRemoveEntityEffect(this);
	}

	public int getPacketSize() {
		return 5;
	}
}
