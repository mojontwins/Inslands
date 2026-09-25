package net.minecraft.world.level.tile;

import java.io.File;
import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldInfo;
import net.minecraft.world.level.WorldSettings;
import net.minecraft.world.level.WorldNameGen;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.WorldType;
import net.minecraft.world.level.PortalRegistry;
import net.minecraft.world.level.chunk.storage.SaveOldDir;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;
import net.minecraft.world.level.theme.LevelThemeSettings;

public class BlockWorldPortal extends Block {

	public BlockWorldPortal(int id, int textureIndex) {
		super(id, textureIndex, Material.rock);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return true;
	}

	public int getRenderType() {
		return 113;
	}

	public int getRenderBlockPass() {
		return 1;
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public int idDropped(int meta, Random random) {
		return 0;
	}

	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
	}

	public void onBlockRemoval(World world, int x, int y, int z) {
	}

	/**
	 * Places a portal at (x,y,z) pointing at a brand-new world reserved with the
	 * given (already-resolved) parameters; returns the allocated world id, or -1
	 * if refused. Parameters are used verbatim — no forced-terrain override and
	 * no randomisation (callers resolve any defaults themselves). Refuses in the
	 * nether (like the item). The destination world is an ordinary linked world:
	 * first travel generates it and builds the return portal.
	 */
	public static int placeNewWorldPortal(World world, int x, int y, int z, int themeId, int sizeId, int worldTypeId) {
		if(world == null || world.worldProvider.isNether) return -1;
		if(themeId < 0 || themeId >= LevelThemeSettings.allThemeSettings.size()) return -1;

		File saveDirectory = PortalRegistry.getBaseSaveDirectory(world);
		if(saveDirectory == null) return -1;

		PortalRegistry registry = new PortalRegistry(saveDirectory);
		int worldId = registry.allocateWorldId();
		boolean brandNew = worldId >= 0;
		if(!brandNew) {
			worldId = registry.getRandomExistingWorldId(themeId);
			if(worldId < 0) return -1;
		}

		if(brandNew) {
			long baseSeed = PortalRegistry.getBaseSeed(world);
			long seed = WorldNameGen.deriveSeed(baseSeed, worldId);
			String name = WorldNameGen.getName(baseSeed, worldId);

			WorldType terrainType = worldTypeId >= 0 && worldTypeId < WorldType.worldTypes.length ? WorldType.worldTypes[worldTypeId] : WorldType.DEFAULT;
			if(terrainType == null) terrainType = WorldType.DEFAULT;

			WorldSettings settings = new WorldSettings(seed, 0, true, false, true, world.getWorldInfo().isLayeredSand(), terrainType);
			WorldInfo stub = new WorldInfo(settings, name);
			stub.setDimension(worldId);
			stub.setSaveVersion(19132);
			stub.setGenerated(false);

			// The stub level.dat freezes the theme (ThemeId), size (Width/LengthInChunks)
			// and terrain (generatorName) globals at write time; save the running world's
			// globals and restore them so a mid-game placement does not corrupt it.
			int prevTheme = LevelThemeGlobalSettings.themeID;
			int prevX = WorldSize.xChunks;
			int prevZ = WorldSize.zChunks;
			try {
				LevelThemeGlobalSettings.loadThemeById(themeId);
				stub.setThemeId(themeId);
				if(sizeId >= 0 && sizeId <= WorldSize.maxPortalSizeID) {
					WorldSize.setSizeById(sizeId);
					stub.setWorldSizeInChunks(WorldSize.xChunks, WorldSize.zChunks);
				}
				SaveOldDir handler = new SaveOldDir(saveDirectory, "DIM-" + worldId, false);
				handler.saveWorldInfo(stub);
			} finally {
				LevelThemeGlobalSettings.loadThemeById(prevTheme);
				WorldSize.setSize(prevX, prevZ);
			}
		}

		world.setBlockAndMetadataWithNotify(x, y, z, Block.worldPortal.blockID, worldId);
		return worldId;
	}
}