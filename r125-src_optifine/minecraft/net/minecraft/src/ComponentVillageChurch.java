package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageChurch extends ComponentVillage {
	private int averageGroundLevel = -1;

	public ComponentVillageChurch(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
	}

	public static ComponentVillageChurch findValidPlacement(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, 0, 0, 0, 5, 12, 9, i5);
		return canVillageGoDeeper(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentVillageChurch(i6, random1, structureBoundingBox7, i5) : null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		if(this.averageGroundLevel < 0) {
			this.averageGroundLevel = this.getAverageGroundLevel(world1, structureBoundingBox3);
			if(this.averageGroundLevel < 0) {
				return true;
			}

			this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 12 - 1, 0);
		}

		this.fillWithBlocks(world1, structureBoundingBox3, 1, 1, 1, 3, 3, 7, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 5, 1, 3, 9, 3, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 0, 0, 3, 0, 8, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 1, 0, 3, 10, 0, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 1, 1, 0, 10, 3, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 1, 1, 4, 10, 3, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 0, 4, 0, 4, 7, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 0, 4, 4, 4, 7, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 1, 8, 3, 4, 8, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 5, 4, 3, 10, 4, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 5, 5, 3, 5, 7, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 9, 0, 4, 9, 4, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 4, 0, 4, 4, 4, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 0, 11, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 4, 11, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 2, 11, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 2, 11, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 1, 1, 6, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 1, 1, 7, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 2, 1, 7, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 3, 1, 6, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 3, 1, 7, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairCompactCobblestone.blockID, this.getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 1, 1, 5, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairCompactCobblestone.blockID, this.getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 2, 1, 6, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairCompactCobblestone.blockID, this.getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 3, 1, 5, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairCompactCobblestone.blockID, this.getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 1), 1, 2, 7, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairCompactCobblestone.blockID, this.getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 0), 3, 2, 7, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 0, 2, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 0, 3, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 4, 2, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 4, 3, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 0, 6, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 0, 7, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 4, 6, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 4, 7, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 2, 6, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 2, 7, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 2, 6, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 2, 7, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 0, 3, 6, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 4, 3, 6, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 2, 3, 8, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.torchWood.blockID, 0, 2, 4, 7, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.torchWood.blockID, 0, 1, 4, 6, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.torchWood.blockID, 0, 3, 4, 6, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.torchWood.blockID, 0, 2, 4, 5, structureBoundingBox3);
		int i4 = this.getMetadataWithOffset(Block.ladder.blockID, 4);

		int i5;
		for(i5 = 1; i5 <= 9; ++i5) {
			this.placeBlockAtCurrentPosition(world1, Block.ladder.blockID, i4, 3, i5, 3, structureBoundingBox3);
		}

		this.placeBlockAtCurrentPosition(world1, 0, 0, 2, 1, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, 0, 0, 2, 2, 0, structureBoundingBox3);
		this.placeDoorAtCurrentPosition(world1, structureBoundingBox3, random2, 2, 1, 0, this.getMetadataWithOffset(Block.doorWood.blockID, 1));
		if(this.getBlockIdAtCurrentPosition(world1, 2, 0, -1, structureBoundingBox3) == 0 && this.getBlockIdAtCurrentPosition(world1, 2, -1, -1, structureBoundingBox3) != 0) {
			this.placeBlockAtCurrentPosition(world1, Block.stairCompactCobblestone.blockID, this.getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 2, 0, -1, structureBoundingBox3);
		}

		for(i5 = 0; i5 < 9; ++i5) {
			for(int i6 = 0; i6 < 5; ++i6) {
				this.clearCurrentPositionBlocksUpwards(world1, i6, 12, i5, structureBoundingBox3);
				this.fillCurrentPositionBlocksDownwards(world1, Block.cobblestone.blockID, 0, i6, -1, i5, structureBoundingBox3);
			}
		}

		this.spawnVillagers(world1, structureBoundingBox3, 2, 1, 2, 1);
		return true;
	}

	protected int getVillagerType(int i1) {
		return 2;
	}
}
