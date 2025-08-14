package net.minecraft.world.level.levelgen.structure.stronghold;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.level.ChunkPosition;
import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.structure.StructureComponent;

public class ComponentStrongholdStairs2 extends ComponentStrongholdStairs {
	public StructureStrongholdPieceWeight field_35038_a;
	public ArrayList<StructureComponent> field_35037_b = new ArrayList<StructureComponent>();

	public ComponentStrongholdStairs2(World world, int i1, Random random2, int i3, int i4) {
		super(world, 0, random2, i3, i4);
	}

	public ChunkPosition getCenter() {
		return super.getCenter();
	}
}
