package net.minecraft.network.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.NetHandler;
import net.minecraft.world.level.World;
import net.minecraft.world.level.chunk.Chunk;

public class Packet52MultiBlockChange extends Packet {
	public int xPosition;
	public int zPosition;
	public byte[] encodedBlocks;
	public int size;

	public Packet52MultiBlockChange() {
		this.isChunkDataPacket = true;
	}

	// This had to be adapted to encode 8 bit block ID and 8 bit metadata
	public Packet52MultiBlockChange(int xPos, int zPos, short[] encodedData, int numBlocksToUpdate, World world) {
		this.isChunkDataPacket = true;
		this.xPosition = xPos;
		this.zPosition = zPos;
		this.size = numBlocksToUpdate;
		
		// Using 4 bytes per block: 2 for encoded XZY, 2 for encoded ID/meta
		int byteSize = 4 * numBlocksToUpdate;

		Chunk chunk = world.getChunkFromChunkCoords(xPos, zPos);

		try {
			if(numBlocksToUpdate > 64) {
				// As it is, this should not happen
				System.out.println("Something wrong - ChunkTilesUpdatePacket compress " + numBlocksToUpdate);
			} else {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(byteSize);
				DataOutputStream outputStream = new DataOutputStream(byteArrayOutputStream);

				for(int i = 0; i < numBlocksToUpdate; ++i) {
					
					// Encoded data is XXXXZZZZ YYYYYYYY

					int x = encodedData[i] >> 12 & 15;
					int z = encodedData[i] >> 8 & 15;
					int y = encodedData[i] & 255;

					outputStream.writeShort(encodedData[i]);
					
					// Now encode IIIIIIIIMMMMMMMM (ID, meta)
					outputStream.writeShort((short)((chunk.getBlockID(x, y, z) & 255) << 8 | chunk.getBlockMetadata(x, y, z) & 255));
				}

				this.encodedBlocks = byteArrayOutputStream.toByteArray();
				if(this.encodedBlocks.length != byteSize) {
					throw new RuntimeException("Expected length " + byteSize + " doesn\'t match received length " + this.encodedBlocks.length);
				}
			}
		} catch (IOException iOException14) {
			System.err.println(iOException14.getMessage());
			this.encodedBlocks = null;
		}

	}


	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.xPosition = dataInputStream1.readInt();
		this.zPosition = dataInputStream1.readInt();
		this.size = dataInputStream1.readShort() & 65535;
		int i2 = dataInputStream1.readInt();
		if(i2 > 0) {
			this.encodedBlocks = new byte[i2];
			dataInputStream1.readFully(this.encodedBlocks);
		}

	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.xPosition);
		dataOutputStream1.writeInt(this.zPosition);
		dataOutputStream1.writeShort((short)this.size);
		if(this.encodedBlocks != null) {
			dataOutputStream1.writeInt(this.encodedBlocks.length);
			dataOutputStream1.write(this.encodedBlocks);
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