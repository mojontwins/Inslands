package net.minecraft.client.gui;

import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.packet.Packet0KeepAlive;
import net.minecraft.util.StringTranslate;

public class GuiDownloadTerrain extends GuiScreen {
	private NetClientHandler netHandler;
	private WorldClient worldClient;
	private int updateCounter = 0;

	public GuiDownloadTerrain(NetClientHandler netClientHandler1, WorldClient worldClient1) {
		this.netHandler = netClientHandler1;
		this.worldClient = worldClient1;
	}

	protected void keyTyped(char c1, int i2) {
	}

	public void initGui() {
		this.controlList.clear();
	}

	public void updateScreen() {
		++this.updateCounter;
		if(this.updateCounter % 20 == 0) {
			this.netHandler.addToSendQueue(new Packet0KeepAlive());
		}

		if(this.netHandler != null) {
			this.netHandler.processReadPackets();
		}

	}

	protected void actionPerformed(GuiButton guiButton1) {
	}

	public void drawScreen(int i1, int i2, float f3) {
		this.drawBackground(0);
		StringTranslate stringTranslate4 = StringTranslate.getInstance();
		this.drawCenteredString(this.fontRenderer, stringTranslate4.translateKey("multiplayer.downloadingTerrain"), this.width / 2, this.height / 2 - 50, 0xFFFFFF);
		int total = this.worldClient != null ? this.worldClient.terrainChunksTotal : 0;
		int received = this.worldClient != null ? this.worldClient.terrainChunksReceived : 0;
		if(total > 0) {
			int percent = Math.min(received * 100 / total, 100);
			int barWidth = 208;
			int barHeight = 6;
			int barX = (this.width - barWidth) / 2;
			int barY = this.height / 2 - 20;
			this.drawRect(barX - 1, barY - 1, barX + barWidth + 1, barY + barHeight + 1, 0xFF000000);
			this.drawRect(barX, barY, barX + barWidth * percent / 100, barY + barHeight, 0xFF70FF70);
			this.drawCenteredString(this.fontRenderer, percent + "% (" + received + " / " + total + ")", this.width / 2, barY + barHeight + 4, 0xA0A0A0);
		} else {
			this.drawCenteredString(this.fontRenderer, "...", this.width / 2, this.height / 2 - 10, 0xA0A0A0);
		}

		super.drawScreen(i1, i2, f3);
	}
}
