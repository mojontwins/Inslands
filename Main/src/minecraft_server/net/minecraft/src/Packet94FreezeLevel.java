package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet94FreezeLevel extends Packet {
	public int freezeLevel = 0;
	
	public Packet94FreezeLevel() {
	}
	
	public Packet94FreezeLevel(int dayOfTheYear) {
		this.freezeLevel = dayOfTheYear;
	}

	@Override
	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.freezeLevel = dataInputStream1.readInt();
	}

	@Override
	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.freezeLevel);
	}

	@Override
	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleFreezeLevel(this);
	}

	@Override
	public int getPacketSize() {
		return 4;
	}

}
