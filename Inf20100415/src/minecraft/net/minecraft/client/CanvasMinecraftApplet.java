package net.minecraft.client;

import java.awt.Canvas;

final class CanvasMinecraftApplet extends Canvas {
	private MinecraftApplet mcApplet;

	CanvasMinecraftApplet(MinecraftApplet minecraftApplet1) {
		this.mcApplet = minecraftApplet1;
	}

	public final synchronized void addNotify() {
		super.addNotify();
		this.mcApplet.startMainThread();
	}

	public final synchronized void removeNotify() {
		this.mcApplet.shutdown();
		super.removeNotify();
	}
}