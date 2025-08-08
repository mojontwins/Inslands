package net.minecraft.network.packet;

class PacketCounter {
	private int totalPackets;
	private long totalBytes;

	public PacketCounter() {
	}

	public void addPacket(int i1) {
		++this.totalPackets;
		this.totalBytes += (long)i1;
	}

	public int getTotalPackets() {
		return totalPackets;
	}

	public void setTotalPackets(int totalPackets) {
		this.totalPackets = totalPackets;
	}

	public long getTotalBytes() {
		return totalBytes;
	}

	public void setTotalBytes(long totalBytes) {
		this.totalBytes = totalBytes;
	}
}
