package net.minecraft.world.level.levelgen.structure.mineshaftmesa;

import net.minecraft.src.World;
import net.minecraft.world.level.biome.BiomeGenMesa;
import net.minecraft.world.level.levelgen.MapGenStructure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

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
