package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class Packet40EntityMetadata extends Packet {
	public int entityId;
	private List metadata;

	public Packet40EntityMetadata() {
	}

	public Packet40EntityMetadata(int i1, DataWatcher dataWatcher2) {
		this.entityId = i1;
		this.metadata = dataWatcher2.getChangedObjects();
	}

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
}
