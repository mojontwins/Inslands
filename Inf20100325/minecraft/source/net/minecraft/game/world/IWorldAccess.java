package net.minecraft.game.world;

public interface IWorldAccess {
	void markBlockAndNeighborsNeedsUpdate(int var1, int var2, int var3);

	void playSound(String var1, double var2, double var4, double var6, float var8, float var9);

	void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12);
}
