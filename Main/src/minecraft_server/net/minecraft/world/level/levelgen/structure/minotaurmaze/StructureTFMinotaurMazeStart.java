package net.minecraft.world.level.levelgen.structure.minotaurmaze;

import java.util.Random;

import net.minecraft.world.GlobalVars;
import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.structure.StructureStart;

public class StructureTFMinotaurMazeStart extends StructureStart {
	public StructureTFMinotaurMazeStart(World world, Random rand, int chunkX, int chunkZ) {
		int x = (chunkX << 4) + rand.nextInt(16) - 8;
		int z = (chunkZ << 4) + rand.nextInt(16) - 8;

		int y = world.getWorldInfo().getTerrainType().getSeaLevel(world);
		
		System.out.println ("Minotaur maze " + x + " " + y + " " + z);
		GlobalVars.hasCorrectMinoshroomMaze = true;
		
		ComponentTFMazeRuins lair = new ComponentTFMazeRuins(world, rand, 0, x, y, z);
		this.components.add(lair);
		lair.buildComponent(lair, this.components, rand);
		this.updateBoundingBox();
	}
}
