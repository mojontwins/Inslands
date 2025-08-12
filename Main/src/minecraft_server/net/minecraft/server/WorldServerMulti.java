package net.minecraft.server;

import net.minecraft.src.WorldSettings;
import net.minecraft.world.level.chunk.storage.ISaveHandler;

public class WorldServerMulti extends WorldServer {
	public WorldServerMulti(MinecraftServer minecraftServer1, ISaveHandler iSaveHandler2, String string3, int i4, WorldSettings worldSettings5, WorldServer worldServer7) {
		super(minecraftServer1, iSaveHandler2, string3, i4, worldSettings5);
		this.mapStorage = worldServer7.mapStorage;
	}
}
