package net.minecraft.client.gui;

public final class ScaledResolution {
	private int scaledWidth;
	private int scaledHeight;

	public ScaledResolution(int i1, int i2) {
		this.scaledWidth = i1;
		this.scaledHeight = i2;

		for(i1 = 1; this.scaledWidth / (i1 + 1) >= 320 && this.scaledHeight / (i1 + 1) >= 240; ++i1) {
		}

		this.scaledWidth /= i1;
		this.scaledHeight /= i1;
	}

	public final int getScaledWidth() {
		return this.scaledWidth;
	}

	public final int getScaledHeight() {
		return this.scaledHeight;
	}
}