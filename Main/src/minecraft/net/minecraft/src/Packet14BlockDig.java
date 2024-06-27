package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet14BlockDig extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int face;
	public int status;
	public ItemStack itemStack;
	public float xWithinFace;
	public float yWithinFace;
	public float zWithinFace;

	public Packet14BlockDig() {
	}

	public Packet14BlockDig(int i1, int i2, int i3, int i4, int i5, ItemStack itemStack, float xWithinFace, float yWithinFace, float zWithinFace) {
		this.status = i1;
		this.xPosition = i2;
		this.yPosition = i3;
		this.zPosition = i4;
		this.face = i5;
		this.itemStack = itemStack;
		this.xWithinFace = xWithinFace;
		this.yWithinFace = yWithinFace;
		this.zWithinFace = zWithinFace;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.status = dataInputStream1.read();
		this.xPosition = dataInputStream1.readInt();
		this.yPosition = dataInputStream1.read();
		this.zPosition = dataInputStream1.readInt();
		this.face = dataInputStream1.read();
		this.xWithinFace = dataInputStream1.readFloat();
		this.yWithinFace = dataInputStream1.readFloat();
		this.zWithinFace = dataInputStream1.readFloat();
		
		short s2 = dataInputStream1.readShort();
		if(s2 >= 0) {
			byte b3 = dataInputStream1.readByte();
			short s4 = dataInputStream1.readShort();
			this.itemStack = new ItemStack(s2, b3, s4);
		} else {
			this.itemStack = null;
		}
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.write(this.status);
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.write(this.yPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.write(this.face);
		dataOutputStream1.writeFloat(this.xWithinFace);
		dataOutputStream1.writeFloat(this.yWithinFace);
		dataOutputStream1.writeFloat(this.zWithinFace);
		
		if(this.itemStack == null) {
			dataOutputStream1.writeShort(-1);
		} else {
			dataOutputStream1.writeShort(this.itemStack.itemID);
			dataOutputStream1.writeByte(this.itemStack.stackSize);
			dataOutputStream1.writeShort(this.itemStack.getItemDamage());
		}
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleBlockDig(this);
	}

	public int getPacketSize() {
		return 16 + 12;
	}
}
