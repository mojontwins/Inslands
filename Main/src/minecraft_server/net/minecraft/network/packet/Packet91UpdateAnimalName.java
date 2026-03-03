package net.minecraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.NetHandler;

public class Packet91UpdateAnimalName extends Packet {
	public int entityId;
	public String name;

	public Packet91UpdateAnimalName() {
		this.shouldDelay = true;
	}
	
	public Packet91UpdateAnimalName(int entityId, String name) {
		this.shouldDelay = true;
		this.entityId = entityId;
		this.name = name;
	}

	@Override
	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
		this.name = readString(dataInputStream1, 32);
	}

	@Override
	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(entityId);
		writeString(this.name, dataOutputStream1);
	}

	@Override
	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleUpdateAnimalName(this);
	}

	@Override
	public int getPacketSize() {
		return 4 + 2 + this.name.length();
	}

}
