package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageHouse4_Garden extends ComponentVillage {
	private int averageGroundLevel = -1;
	private final boolean isRoofAccessible;

	public ComponentVillageHouse4_Garden(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
		this.isRoofAccessible = random2.nextBoolean();
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
	}

	public static ComponentVillageHouse4_Garden findValidPlacement(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, 0, 0, 0, 5, 6, 5, i5);
		return StructureComponent.findIntersecting(list0, structureBoundingBox7) != null ? null : new ComponentVillageHouse4_Garden(i6, random1, structureBoundingBox7, i5);
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		if(this.averageGroundLevel < 0) {
			this.averageGroundLevel = this.getAverageGroundLevel(world1, structureBoundingBox3);
			if(this.averageGroundLevel < 0) {
				return true;
			}

			this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 6 - 1, 0);
		}

		this.fillWithBlocks(world1, structureBoundingBox3, 0, 0, 0, 4, 0, 4, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 4, 0, 4, 4, 4, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 4, 1, 3, 4, 3, Block.planks.blockID, Block.planks.blockID, false);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 0, 1, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 0, 2, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 0, 3, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 4, 1, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 4, 2, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 4, 3, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 0, 1, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 0, 2, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 0, 3, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 4, 1, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 4, 2, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 4, 3, 4, structureBoundingBox3);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 1, 1, 0, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 1, 1, 4, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 1, 4, 3, 3, 4, Block.planks.blockID, Block.planks.blockID, false);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 0, 2, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 2, 2, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 4, 2, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 1, 1, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 1, 2, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 1, 3, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 2, 3, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 3, 3, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 3, 2, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 3, 1, 0, structureBoundingBox3);
		if(this.getBlockIdAtCurrentPosition(world1, 2, 0, -1, structureBoundingBox3) == 0 && this.getBlockIdAtCurrentPosition(world1, 2, -1, -1, structureBoundingBox3) != 0) {
			this.placeBlockAtCurrentPosition(world1, Block.stairCompactCobblestone.blockID, this.getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 2, 0, -1, structureBoundingBox3);
		}

		this.fillWithBlocks(world1, structureBoundingBox3, 1, 1, 1, 3, 3, 3, 0, 0, false);
		if(this.isRoofAccessible) {
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 0, 5, 0, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 1, 5, 0, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 2, 5, 0, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 3, 5, 0, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 4, 5, 0, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 0, 5, 4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 1, 5, 4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 2, 5, 4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 3, 5, 4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 4, 5, 4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 4, 5, 1, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 4, 5, 2, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 4, 5, 3, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 0, 5, 1, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 0, 5, 2, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 0, 5, 3, structureBoundingBox3);
		}

		int i4;
		if(this.isRoofAccessible) {
			i4 = this.getMetadataWithOffset(Block.ladder.blockID, 3);
			this.placeBlockAtCurrentPosition(world1, Block.ladder.blockID, i4, 3, 1, 3, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.ladder.blockID, i4, 3, 2, 3, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.ladder.blockID, i4, 3, 3, 3, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.ladder.blockID, i4, 3, 4, 3, structureBoundingBox3);
		}

		this.placeBlockAtCurrentPosition(world1, Block.torchWood.blockID, 0, 2, 3, 1, structureBoundingBox3);

		for(i4 = 0; i4 < 5; ++i4) {
			for(int i5 = 0; i5 < 5; ++i5) {
				this.clearCurrentPositionBlocksUpwards(world1, i5, 6, i4, structureBoundingBox3);
				this.fillCurrentPositionBlocksDownwards(world1, Block.cobblestone.blockID, 0, i5, -1, i4, structureBoundingBox3);
			}
		}

		this.spawnVillagers(world1, structureBoundingBox3, 1, 1, 2, 1);
		return true;
	}
}
