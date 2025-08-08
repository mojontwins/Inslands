package net.minecraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.NetHandler;

public class Packet61DoorChange extends Packet {
	public int field_28050_a;
	public int field_28049_b;
	public int field_28053_c;
	public int field_28052_d;
	public int field_28051_e;

	public Packet61DoorChange() {
	}

	public Packet61DoorChange(int i1, int i2, int i3, int i4, int i5) {
		this.field_28050_a = i1;
		this.field_28053_c = i2;
		this.field_28052_d = i3;
		this.field_28051_e = i4;
		this.field_28049_b = i5;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.field_28050_a = dataInputStream1.readInt();
		this.field_28053_c = dataInputStream1.readInt();
		this.field_28052_d = dataInputStream1.readByte();
		this.field_28051_e = dataInputStream1.readInt();
		this.field_28049_b = dataInputStream1.readInt();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.field_28050_a);
		dataOutputStream1.writeInt(this.field_28053_c);
		dataOutputStream1.writeByte(this.field_28052_d);
		dataOutputStream1.writeInt(this.field_28051_e);
		dataOutputStream1.writeInt(this.field_28049_b);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleDoorChange(this);
	}

	public int getPacketSize() {
		return 20;
	}
}
