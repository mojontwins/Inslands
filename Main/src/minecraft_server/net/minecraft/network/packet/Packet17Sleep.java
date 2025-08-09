package net.minecraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.NetHandler;
import net.minecraft.world.entity.Entity;

public class Packet17Sleep extends Packet {
	public int field_22045_a;
	public int field_22044_b;
	public int field_22048_c;
	public int field_22047_d;
	public int field_22046_e;

	public Packet17Sleep() {
	}

	public Packet17Sleep(Entity entity1, int i2, int i3, int i4, int i5) {
		this.field_22046_e = i2;
		this.field_22044_b = i3;
		this.field_22048_c = i4;
		this.field_22047_d = i5;
		this.field_22045_a = entity1.entityId;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.field_22045_a = dataInputStream1.readInt();
		this.field_22046_e = dataInputStream1.readByte();
		this.field_22044_b = dataInputStream1.readInt();
		this.field_22048_c = dataInputStream1.readByte();
		this.field_22047_d = dataInputStream1.readInt();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.field_22045_a);
		dataOutputStream1.writeByte(this.field_22046_e);
		dataOutputStream1.writeInt(this.field_22044_b);
		dataOutputStream1.writeByte(this.field_22048_c);
		dataOutputStream1.writeInt(this.field_22047_d);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleSleep(this);
	}

	public int getPacketSize() {
		return 14;
	}
}
