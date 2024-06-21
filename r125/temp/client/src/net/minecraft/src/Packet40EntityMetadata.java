package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class Packet40EntityMetadata extends Packet {
	public int entityId;
	private List metadata;

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.entityId = dataInputStream1.readInt();
		this.metadata = DataWatcher.readWatchableObjects(dataInputStream1);
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeInt(this.entityId);
		DataWatcher.writeObjectsInListToStream(this.metadata, dataOutputStream1);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.handleEntityMetadata(this);
	}

	public int getPacketSize() {
		return 5;
	}

	public List getMetadata() {
		return this.metadata;
	}
}
