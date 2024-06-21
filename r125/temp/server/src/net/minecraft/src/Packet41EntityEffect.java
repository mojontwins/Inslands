package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet41EntityEffect extends Packet {
	public int entityId;
	public byte effectId;
	public byte effectAmp;
	public short duration;

	public Packet41EntityEffect() {
	}

	public Packet41EntityEffect(int i1, PotionEffect potionEffect2) {
		this.entityId = i1;
		this.effectId = (byte)(potionEffect2.getPotionID() & 255);
		this.effectAmp = (byte)(potionEffect2.getAmplifier() & 255);
		this.duration = (short)potionEffect2.getDuration();
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
		this.effectId = dataInputStream1.readByte();
		this.effectAmp = dataInputStream1.readByte();
		this.duration = dataInputStream1.readShort();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
		dataOutputStream1.writeByte(this.effectId);
		dataOutputStream1.writeByte(this.effectAmp);
		dataOutputStream1.writeShort(this.duration);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleEntityEffect(this);
	}

	public int getPacketSize() {
		return 8;
	}
}
