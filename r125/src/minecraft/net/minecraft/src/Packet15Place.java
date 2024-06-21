package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet15Place extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int direction;
	public ItemStack itemStack;

	public Packet15Place() {
	}

	public Packet15Place(int i1, int i2, int i3, int i4, ItemStack itemStack5) {
		this.xPosition = i1;
		this.yPosition = i2;
		this.zPosition = i3;
		this.direction = i4;
		this.itemStack = itemStack5;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.xPosition = dataInputStream1.readInt();
		this.yPosition = dataInputStream1.read();
		this.zPosition = dataInputStream1.readInt();
		this.direction = dataInputStream1.read();
		this.itemStack = this.readItemStack(dataInputStream1);
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.write(this.yPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.write(this.direction);
		this.writeItemStack(this.itemStack, dataOutputStream1);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handlePlace(this);
	}

	public int getPacketSize() {
		return 15;
	}
}
