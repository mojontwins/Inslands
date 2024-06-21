package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageWoodHut extends ComponentVillage {
	private int averageGroundLevel = -1;
	private final boolean isTallHouse;
	private final int tablePosition;

	public ComponentVillageWoodHut(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
		this.isTallHouse = random2.nextBoolean();
		this.tablePosition = random2.nextInt(3);
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
	}

	public static ComponentVillageWoodHut findValidPlacement(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, 0, 0, 0, 4, 6, 5, i5);
		return canVillageGoDeeper(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentVillageWoodHut(i6, random1, structureBoundingBox7, i5) : null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		if(this.averageGroundLevel < 0) {
			this.averageGroundLevel = this.getAverageGroundLevel(world1, structureBoundingBox3);
			if(this.averageGroundLevel < 0) {
				return true;
			}

			this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 6 - 1, 0);
		}

		this.fillWithBlocks(world1, structureBoundingBox3, 1, 1, 1, 3, 5, 4, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 0, 0, 3, 0, 4, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 0, 1, 2, 0, 3, Block.dirt.blockID, Block.dirt.blockID, false);
		if(this.isTallHouse) {
			this.fillWithBlocks(world1, structureBoundingBox3, 1, 4, 1, 2, 4, 3, Block.wood.blockID, Block.wood.blockID, false);
		} else {
			this.fillWithBlocks(world1, structureBoundingBox3, 1, 5, 1, 2, 5, 3, Block.wood.blockID, Block.wood.blockID, false);
		}

		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 1, 4, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 2, 4, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 1, 4, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 2, 4, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 0, 4, 1, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 0, 4, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 0, 4, 3, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 3, 4, 1, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 3, 4, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 3, 4, 3, structureBoundingBox3);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 1, 0, 0, 3, 0, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 3, 1, 0, 3, 3, 0, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 1, 4, 0, 3, 4, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 3, 1, 4, 3, 3, 4, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 1, 1, 0, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 3, 1, 1, 3, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 1, 0, 2, 3, 0, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 1, 4, 2, 3, 4, Block.planks.blockID, Block.planks.blockID, false);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 0, 2, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 3, 2, 2, structureBoundingBox3);
		if(this.tablePosition > 0) {
			this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, this.tablePosition, 1, 3, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.pressurePlatePlanks.blockID, 0, this.tablePosition, 2, 3, structureBoundingBox3);
		}

		this.placeBlockAtCurrentPosition(world1, 0, 0, 1, 1, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, 0, 0, 1, 2, 0, structureBoundingBox3);
		this.placeDoorAtCurrentPosition(world1, structureBoundingBox3, random2, 1, 1, 0, this.getMetadataWithOffset(Block.doorWood.blockID, 1));
		if(this.getBlockIdAtCurrentPosition(world1, 1, 0, -1, structureBoundingBox3) == 0 && this.getBlockIdAtCurrentPosition(world1, 1, -1, -1, structureBoundingBox3) != 0) {
			this.placeBlockAtCurrentPosition(world1, Block.stairCompactCobblestone.blockID, this.getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 1, 0, -1, structureBoundingBox3);
		}

		for(int i4 = 0; i4 < 5; ++i4) {
			for(int i5 = 0; i5 < 4; ++i5) {
				this.clearCurrentPositionBlocksUpwards(world1, i5, 6, i4, structureBoundingBox3);
				this.fillCurrentPositionBlocksDownwards(world1, Block.cobblestone.blockID, 0, i5, -1, i4, structureBoundingBox3);
			}
		}

		this.spawnVillagers(world1, structureBoundingBox3, 1, 1, 2, 1);
		return true;
	}
}
