package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet202PlayerAbilities extends Packet {
	public boolean field_50007_a = false;
	public boolean field_50005_b = false;
	public boolean field_50006_c = false;
	public boolean field_50004_d = false;

	public Packet202PlayerAbilities() {
	}

	public Packet202PlayerAbilities(PlayerCapabilities playerCapabilities1) {
		this.field_50007_a = playerCapabilities1.disableDamage;
		this.field_50005_b = playerCapabilities1.isFlying;
		this.field_50006_c = playerCapabilities1.allowFlying;
		this.field_50004_d = playerCapabilities1.isCreativeMode;
	}

	public void readPacketData(DataInputStream dataInputStream1) throws IOException {
		this.field_50007_a = dataInputStream1.readBoolean();
		this.field_50005_b = dataInputStream1.readBoolean();
		this.field_50006_c = dataInputStream1.readBoolean();
		this.field_50004_d = dataInputStream1.readBoolean();
	}

	public void writePacketData(DataOutputStream dataOutputStream1) throws IOException {
		dataOutputStream1.writeBoolean(this.field_50007_a);
		dataOutputStream1.writeBoolean(this.field_50005_b);
		dataOutputStream1.writeBoolean(this.field_50006_c);
		dataOutputStream1.writeBoolean(this.field_50004_d);
	}

	public void processPacket(NetHandler netHandler1) {
		netHandler1.func_50003_a(this);
	}

	public int getPacketSize() {
		return 1;
	}
}
