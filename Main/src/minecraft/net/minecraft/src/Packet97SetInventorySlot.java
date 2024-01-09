package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet97SetInventorySlot extends Packet {
	
	// Used by the creative inventory to force a new itemstack into an inventory slot
	public int slot;
	public short itemID;
	public byte itemAmount;
	public short itemDamage;
	
	public Packet97SetInventorySlot () {	
	}
	
	public Packet97SetInventorySlot (int slot, short itemID, byte itemAmount, short itemDamage) {
		this.slot = slot;
		this.itemID = itemID;
		this.itemAmount = itemAmount;
		this.itemDamage = itemDamage;
	}
	@Override
	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.slot = dataInputStream1.readInt();
		this.itemID = dataInputStream1.readShort();
		this.itemAmount = dataInputStream1.readByte();
		this.itemDamage = dataInputStream1.readShort();
	}

	@Override
	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.slot);
		dataOutputStream1.writeShort(this.itemID);
		dataOutputStream1.writeByte(this.itemAmount);
		dataOutputStream1.writeShort(this.itemDamage);
	}

	@Override
	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleSetInventorySlot(this);
	}

	@Override
	public int getPacketSize() {
		return 9;
	}

}
