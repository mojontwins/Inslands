package net.minecraft.src;

import java.awt.Canvas;

import net.minecraft.client.MinecraftApplet;

public class CanvasMinecraftApplet extends Canvas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7925850237633896902L;
	final MinecraftApplet mcApplet;

	public CanvasMinecraftApplet(MinecraftApplet minecraftApplet1) {
		this.mcApplet = minecraftApplet1;
	}

	public synchronized void addNotify() {
		super.addNotify();
		this.mcApplet.startMainThread();
	}

	public synchronized void removeNotify() {
		this.mcApplet.shutdown();
		super.removeNotify();
	}
}
