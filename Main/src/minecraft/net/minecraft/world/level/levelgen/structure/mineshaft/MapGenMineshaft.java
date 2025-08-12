package net.minecraft.world.level.levelgen.structure.mineshaft;

import net.minecraft.src.World;
import net.minecraft.world.level.levelgen.MapGenStructure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

public class MapGenMineshaft extends MapGenStructure {
	public MapGenMineshaft(World world) {
		this.world = world;
	}
	
	protected boolean canSpawnStructureAtCoords(World world, int cX, int cZ) {
		return this.rand.nextInt(100) == 0 ; //&& this.rand.nextInt(80) < Math.max(Math.abs(cX), Math.abs(cZ));
	}

	protected StructureStart getStructureStart(int cX, int cZ) {
		return new StructureMineshaftStart(this.world, this.rand, cX, cZ);
	}
}
