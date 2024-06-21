package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet52MultiBlockChange extends Packet {
	public int xPosition;
	public int zPosition;
	public byte[] metadataArray;
	public int size;
	private static byte[] field_48168_e = new byte[0];

	public Packet52MultiBlockChange() {
		this.isChunkDataPacket = true;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.xPosition = dataInputStream1.readInt();
		this.zPosition = dataInputStream1.readInt();
		this.size = dataInputStream1.readShort() & 65535;
		int i2 = dataInputStream1.readInt();
		if(i2 > 0) {
			this.metadataArray = new byte[i2];
			dataInputStream1.readFully(this.metadataArray);
		}

	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.writeShort((short)this.size);
		if(this.metadataArray != null) {
			dataOutputStream1.writeInt(this.metadataArray.length);
			dataOutputStream1.write(this.metadataArray);
		} else {
			dataOutputStream1.writeInt(0);
		}

	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleMultiBlockChange(this);
	}

	public int getPacketSize() {
		return 10 + this.size * 4;
	}
}
