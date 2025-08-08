package net.minecraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.NetHandler;

public class Packet98UpdateWeather extends Packet {
	public boolean raining;
	public boolean snowing;
	public boolean thundering;
	
	public Packet98UpdateWeather () {	
	}
	
	public Packet98UpdateWeather (boolean raining, boolean snowing, boolean thundering) {
		this.raining = raining;
		this.snowing = snowing;
		this.thundering = thundering;
	}

	@Override
	public void readPacketData(DataInputStream var1) throws IOException {
		this.raining = (var1.read() == 1);
		this.snowing = (var1.read() == 1);
		this.thundering = (var1.read() == 1);
	}

	@Override
	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.write(this.raining ? 1 : 0);
		var1.write(this.snowing ? 1 : 0);
		var1.write(this.thundering ? 1 : 0);
	}

	@Override
	public void processPacket(NetHandler var1) {
		var1.handleUpdateWeather(this);
	}

	@Override
	public int getPacketSize() {
		return 3;
	}

}

