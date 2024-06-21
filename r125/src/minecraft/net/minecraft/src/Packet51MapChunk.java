package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class Packet51MapChunk extends Packet {
	public int xCh;
	public int zCh;
	public int yChMin;
	public int yChMax;
	public byte[] chunkData;
	public boolean includeInitialize;
	private int tempLength;
	private int field_48178_h;
	private static byte[] temp = new byte[0];

	public Packet51MapChunk() {
		this.isChunkDataPacket = true;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.xCh = dataInputStream1.readInt();
		this.zCh = dataInputStream1.readInt();
		this.includeInitialize = dataInputStream1.readBoolean();
		this.yChMin = dataInputStream1.readShort();
		this.yChMax = dataInputStream1.readShort();
		this.tempLength = dataInputStream1.readInt();
		this.field_48178_h = dataInputStream1.readInt();
		if(temp.length < this.tempLength) {
			temp = new byte[this.tempLength];
		}

		dataInputStream1.readFully(temp, 0, this.tempLength);
		int i2 = 0;

		int i3;
		for(i3 = 0; i3 < 16; ++i3) {
			i2 += this.yChMin >> i3 & 1;
		}

		i3 = 12288 * i2;
		if(this.includeInitialize) {
			i3 += 256;
		}

		this.chunkData = new byte[i3];
		Inflater inflater4 = new Inflater();
		inflater4.setInput(temp, 0, this.tempLength);

		try {
			inflater4.inflate(this.chunkData);
		} catch (DataFormatException dataFormatException9) {
			throw new IOException("Bad compressed data format");
		} finally {
			inflater4.end();
		}

	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.xCh);
		dataOutputStream1.writeInt(this.zCh);
		dataOutputStream1.writeBoolean(this.includeInitialize);
		dataOutputStream1.writeShort((short)(this.yChMin & 65535));
		dataOutputStream1.writeShort((short)(this.yChMax & 65535));
		dataOutputStream1.writeInt(this.tempLength);
		dataOutputStream1.writeInt(this.field_48178_h);
		dataOutputStream1.write(this.chunkData, 0, this.tempLength);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.func_48487_a(this);
	}

	public int getPacketSize() {
		return 17 + this.tempLength;
	}
}
