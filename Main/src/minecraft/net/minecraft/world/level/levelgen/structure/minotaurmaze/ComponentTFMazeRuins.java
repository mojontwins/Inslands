package net.minecraft.world.level.levelgen.structure.minotaurmaze;

import java.util.List;
import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureComponent;

public class ComponentTFMazeRuins extends StructureTFComponent {
	public ComponentTFMazeRuins(World var1, Random var2, int var3, int var4, int var5, int var6) {
		super(var3);
		this.setCoordBaseMode(0);
		this.boundingBox = StructureTFComponent.getComponentToAddBoundingBox(var4, var5, var6, -30, -3, -30, 60, 10, 60, 0);
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current
	 * Location of StructGen
	 */
	public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
		super.buildComponent(var1, var2, var3);
		ComponentTFMinotaurMaze var4 = new ComponentTFMinotaurMaze(
				1,
				this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2, 
				this.boundingBox.minY - 14,
				this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, 
				1
			);
		var2.add(var4);
		var4.buildComponent(this, var2, var3);
		ComponentTFMazeRoomEntrance var5 = new ComponentTFMazeRoomEntrance(
				2, 
				var3, 
				this.boundingBox.minX + 30,
				this.boundingBox.minY, 
				this.boundingBox.minZ + 30
			);
		var2.add(var5);
		var5.buildComponent(this, var2, var3);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		return true;
	}
}
