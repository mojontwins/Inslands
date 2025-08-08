package net.minecraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.NetHandler;

public class Packet99SetCreativeMode extends Packet {
	public boolean isCreative;
	
	public Packet99SetCreativeMode () {	
	}
	
	public Packet99SetCreativeMode (boolean isCreative) {
		this.isCreative = isCreative;
	}

	@Override
	public void readPacketData(DataInputStream var1) throws IOException {
		this.isCreative = (var1.read() == 1);

	}

	@Override
	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.write(this.isCreative ? 1 : 0);
	}

	@Override
	public void processPacket(NetHandler var1) {
		var1.handleSetCreative (this);
	}

	@Override
	public int getPacketSize() {
		return 1;
	}

}
