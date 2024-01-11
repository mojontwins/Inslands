package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet250CustomPayload extends Packet {
	public String channel;
	public int length;
	public byte[] data;
	
	public Packet250CustomPayload() {}
	
	public Packet250CustomPayload(String channel, byte[] data) {
		this.channel = channel;
		this.data = data;
		
		if(data != null) {
			this.length = data.length;
			if(this.length > 32767) {
				throw new IllegalArgumentException("Payload too big (max 32K)");
			}
		}
	}

	@Override
	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.channel = readString(dataInputStream, 20);
		this.length = dataInputStream.readShort();
		
		if(this.length > 0 && this.length < 32767) {
			this.data = new byte[this.length];
			dataInputStream.readFully(this.data);
		}
		
	}

	@Override
	public void writePacketData(DataOutputStream dataOutputStream) throws IOException {
		writeString(this.channel, dataOutputStream);
		dataOutputStream.writeShort((short)this.length);
		
		if(this.data != null) {
			dataOutputStream.write(this.data);
		}
	}

	@Override
	public void processPacket(NetHandler netHandler) {
		netHandler.handleCustomPayload(this);
	}

	@Override
	public int getPacketSize() {
		return 2 + this.channel.length() * 2 + 2 + this.length;
	}

}
