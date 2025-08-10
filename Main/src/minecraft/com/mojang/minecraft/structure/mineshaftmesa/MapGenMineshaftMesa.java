package com.mojang.minecraft.structure.mineshaftmesa;

import com.mojang.minecraft.structure.MapGenStructure;
import com.mojang.minecraft.structure.StructureStart;

import net.minecraft.src.World;
import net.minecraft.world.level.biome.BiomeGenMesa;

public class MapGenMineshaftMesa extends MapGenStructure {
	public MapGenMineshaftMesa(World world) {
		this.world = world;
	}
	
	@Override
	protected boolean canSpawnStructureAtCoords(World world, int cX, int cZ) {
		return world.getWorldChunkManager().getBiomeGenAt((cX << 4) + 8, (cZ << 4) + 8) instanceof BiomeGenMesa && this.rand.nextInt(50) == 0;
	}

	@Override
	protected StructureStart getStructureStart(int cX, int cZ) {
		return new StructureMineshaftMesaStart(this.world, this.rand, cX, cZ);
	}

}
