package net.minecraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.NetHandler;

public class Packet70Bed extends Packet {
	public static final String[] errorMessageArr = new String[]{"tile.bed.notValid", null, null};
	public int setRainingAction;
	public boolean raining;
	public boolean snowing;
	public boolean thundering;

	public Packet70Bed() {
	}

	public Packet70Bed(boolean raining, boolean snowing, boolean thundering) {
		this.raining = raining;
		this.snowing = snowing;
		this.thundering = thundering;
		this.setRainingAction = 1;
	}
	
	public Packet70Bed(int i1) {
		this.setRainingAction = i1;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.setRainingAction = dataInputStream1.readByte();
		this.raining = (dataInputStream1.read() == 1);
		this.snowing = (dataInputStream1.read() == 1);
		this.thundering = (dataInputStream1.read() == 1);
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeByte(this.setRainingAction);
		dataOutputStream1.write(this.raining ? 1 : 0);
		dataOutputStream1.write(this.snowing ? 1 : 0);
		dataOutputStream1.write(this.thundering ? 1 : 0);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleBed(this);
	}

	public int getPacketSize() {
		return 4;
	}
}
