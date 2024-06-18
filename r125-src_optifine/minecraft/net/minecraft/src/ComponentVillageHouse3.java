package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageHouse3 extends ComponentVillage {
	private int averageGroundLevel = -1;

	public ComponentVillageHouse3(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
	}

	public static ComponentVillageHouse3 findValidPlacement(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, 0, 0, 0, 9, 7, 12, i5);
		return canVillageGoDeeper(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentVillageHouse3(i6, random1, structureBoundingBox7, i5) : null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		if(this.averageGroundLevel < 0) {
			this.averageGroundLevel = this.getAverageGroundLevel(world1, structureBoundingBox3);
			if(this.averageGroundLevel < 0) {
				return true;
			}

			this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 7 - 1, 0);
		}

		this.fillWithBlocks(world1, structureBoundingBox3, 1, 1, 1, 7, 4, 4, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 2, 1, 6, 8, 4, 10, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 2, 0, 5, 8, 0, 10, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 0, 1, 7, 0, 4, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 0, 0, 0, 3, 5, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 8, 0, 0, 8, 3, 10, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 0, 0, 7, 2, 0, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 0, 5, 2, 1, 5, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 2, 0, 6, 2, 3, 10, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 3, 0, 10, 7, 3, 10, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 2, 0, 7, 3, 0, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 2, 5, 2, 3, 5, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 4, 1, 8, 4, 1, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 4, 4, 3, 4, 4, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 5, 2, 8, 5, 3, Block.planks.blockID, Block.planks.blockID, false);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 0, 4, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 0, 4, 3, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 8, 4, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 8, 4, 3, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 8, 4, 4, structureBoundingBox3);
		int i4 = this.getMetadataWithOffset(Block.stairCompactPlanks.blockID, 3);
		int i5 = this.getMetadataWithOffset(Block.stairCompactPlanks.blockID, 2);

		int i6;
		int i7;
		for(i6 = -1; i6 <= 2; ++i6) {
			for(i7 = 0; i7 <= 8; ++i7) {
				this.placeBlockAtCurrentPosition(world1, Block.stairCompactPlanks.blockID, i4, i7, 4 + i6, i6, structureBoundingBox3);
				if((i6 > -1 || i7 <= 1) && (i6 > 0 || i7 <= 3) && (i6 > 1 || i7 <= 4 || i7 >= 6)) {
					this.placeBlockAtCurrentPosition(world1, Block.stairCompactPlanks.blockID, i5, i7, 4 + i6, 5 - i6, structureBoundingBox3);
				}
			}
		}

		this.fillWithBlocks(world1, structureBoundingBox3, 3, 4, 5, 3, 4, 10, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 7, 4, 2, 7, 4, 10, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 5, 4, 4, 5, 10, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 6, 5, 4, 6, 5, 10, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 5, 6, 3, 5, 6, 10, Block.planks.blockID, Block.planks.blockID, false);
		i6 = this.getMetadataWithOffset(Block.stairCompactPlanks.blockID, 0);

		int i8;
		for(i7 = 4; i7 >= 1; --i7) {
			this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, i7, 2 + i7, 7 - i7, structureBoundingBox3);

			for(i8 = 8 - i7; i8 <= 10; ++i8) {
				this.placeBlockAtCurrentPosition(world1, Block.stairCompactPlanks.blockID, i6, i7, 2 + i7, i8, structureBoundingBox3);
			}
		}

		i7 = this.getMetadataWithOffset(Block.stairCompactPlanks.blockID, 1);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 6, 6, 3, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 7, 5, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairCompactPlanks.blockID, i7, 6, 6, 4, structureBoundingBox3);

		int i9;
		for(i8 = 6; i8 <= 8; ++i8) {
			for(i9 = 5; i9 <= 10; ++i9) {
				this.placeBlockAtCurrentPosition(world1, Block.stairCompactPlanks.blockID, i7, i8, 12 - i8, i9, structureBoundingBox3);
			}
		}

		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 0, 2, 1, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 0, 2, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 0, 2, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 0, 2, 3, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 4, 2, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 5, 2, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 6, 2, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 8, 2, 1, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 8, 2, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 8, 2, 3, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 8, 2, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 8, 2, 5, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 8, 2, 6, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 8, 2, 7, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 8, 2, 8, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 8, 2, 9, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 2, 2, 6, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 2, 2, 7, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 2, 2, 8, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 2, 2, 9, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 4, 4, 10, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 5, 4, 10, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.wood.blockID, 0, 6, 4, 10, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 5, 5, 10, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, 0, 0, 2, 1, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, 0, 0, 2, 2, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.torchWood.blockID, 0, 2, 3, 1, structureBoundingBox3);
		this.placeDoorAtCurrentPosition(world1, structureBoundingBox3, random2, 2, 1, 0, this.getMetadataWithOffset(Block.doorWood.blockID, 1));
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 0, -1, 3, 2, -1, 0, 0, false);
		if(this.getBlockIdAtCurrentPosition(world1, 2, 0, -1, structureBoundingBox3) == 0 && this.getBlockIdAtCurrentPosition(world1, 2, -1, -1, structureBoundingBox3) != 0) {
			this.placeBlockAtCurrentPosition(world1, Block.stairCompactCobblestone.blockID, this.getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 2, 0, -1, structureBoundingBox3);
		}

		for(i8 = 0; i8 < 5; ++i8) {
			for(i9 = 0; i9 < 9; ++i9) {
				this.clearCurrentPositionBlocksUpwards(world1, i9, 7, i8, structureBoundingBox3);
				this.fillCurrentPositionBlocksDownwards(world1, Block.cobblestone.blockID, 0, i9, -1, i8, structureBoundingBox3);
			}
		}

		for(i8 = 5; i8 < 11; ++i8) {
			for(i9 = 2; i9 < 9; ++i9) {
				this.clearCurrentPositionBlocksUpwards(world1, i9, 7, i8, structureBoundingBox3);
				this.fillCurrentPositionBlocksDownwards(world1, Block.cobblestone.blockID, 0, i9, -1, i8, structureBoundingBox3);
			}
		}

		this.spawnVillagers(world1, structureBoundingBox3, 4, 1, 2, 2);
		return true;
	}
}
