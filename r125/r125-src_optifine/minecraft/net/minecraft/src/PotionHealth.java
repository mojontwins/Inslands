package net.minecraft.src;

public class PotionHealth extends Potion {
	public PotionHealth(int i1, boolean z2, int i3) {
		super(i1, z2, i3);
	}

	public boolean isInstant() {
		return true;
	}

	public boolean isReady(int i1, int i2) {
		return i1 >= 1;
	}
}
