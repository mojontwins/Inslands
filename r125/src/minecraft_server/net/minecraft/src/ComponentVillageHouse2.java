package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageHouse2 extends ComponentVillage {
	private static final StructurePieceTreasure[] chestLoot = new StructurePieceTreasure[]{new StructurePieceTreasure(Item.diamond.shiftedIndex, 0, 1, 3, 3), new StructurePieceTreasure(Item.ingotIron.shiftedIndex, 0, 1, 5, 10), new StructurePieceTreasure(Item.ingotGold.shiftedIndex, 0, 1, 3, 5), new StructurePieceTreasure(Item.bread.shiftedIndex, 0, 1, 3, 15), new StructurePieceTreasure(Item.appleRed.shiftedIndex, 0, 1, 3, 15), new StructurePieceTreasure(Item.pickaxeSteel.shiftedIndex, 0, 1, 1, 5), new StructurePieceTreasure(Item.swordSteel.shiftedIndex, 0, 1, 1, 5), new StructurePieceTreasure(Item.plateSteel.shiftedIndex, 0, 1, 1, 5), new StructurePieceTreasure(Item.helmetSteel.shiftedIndex, 0, 1, 1, 5), new StructurePieceTreasure(Item.legsSteel.shiftedIndex, 0, 1, 1, 5), new StructurePieceTreasure(Item.bootsSteel.shiftedIndex, 0, 1, 1, 5), new StructurePieceTreasure(Block.obsidian.blockID, 0, 3, 7, 5), new StructurePieceTreasure(Block.sapling.blockID, 0, 3, 7, 5)};
	private int averageGroundLevel = -1;
	private boolean hasMadeChest;

	public ComponentVillageHouse2(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
	}

	public static ComponentVillageHouse2 findValidPlacement(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, 0, 0, 0, 10, 6, 7, i5);
		return canVillageGoDeeper(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentVillageHouse2(i6, random1, structureBoundingBox7, i5) : null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		if(this.averageGroundLevel < 0) {
			this.averageGroundLevel = this.getAverageGroundLevel(world1, structureBoundingBox3);
			if(this.averageGroundLevel < 0) {
				return true;
			}

			this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 6 - 1, 0);
		}

		this.fillWithBlocks(world1, structureBoundingBox3, 0, 1, 0, 9, 4, 6, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 0, 0, 9, 0, 6, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 4, 0, 9, 4, 6, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 5, 0, 9, 5, 6, Block.stairSingle.blockID, Block.stairSingle.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 5, 1, 8, 5, 5, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 1, 0, 2, 3, 0, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 1, 0, 0, 4, 0, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 3, 1, 0, 3, 4, 0, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 1, 6, 0, 4, 6, Block.wood.blockID, Block.wood.blockID, false);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 3, 3, 1, structureBoundingBox3);
		this.fillWithBlocks(world1, structureBoundingBox3, 3, 1, 2, 3, 3, 2, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 1, 3, 5, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 1, 1, 0, 3, 5, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 1, 6, 5, 3, 6, Block.planks.blockID, Block.planks.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 5, 1, 0, 5, 3, 0, Block.fence.blockID, Block.fence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 9, 1, 0, 9, 3, 0, Block.fence.blockID, Block.fence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 6, 1, 4, 9, 4, 6, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
		this.placeBlockAtCurrentPosition(world1, Block.lavaMoving.blockID, 0, 7, 1, 5, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.lavaMoving.blockID, 0, 8, 1, 5, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.fenceIron.blockID, 0, 9, 2, 5, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.fenceIron.blockID, 0, 9, 2, 4, structureBoundingBox3);
		this.fillWithBlocks(world1, structureBoundingBox3, 7, 2, 4, 8, 2, 5, 0, 0, false);
		this.placeBlockAtCurrentPosition(world1, Block.cobblestone.blockID, 0, 6, 1, 3, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stoneOvenIdle.blockID, 0, 6, 2, 3, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stoneOvenIdle.blockID, 0, 6, 3, 3, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairDouble.blockID, 0, 8, 1, 1, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 0, 2, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 0, 2, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 2, 2, 6, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.thinGlass.blockID, 0, 4, 2, 6, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 2, 1, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.pressurePlatePlanks.blockID, 0, 2, 2, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.planks.blockID, 0, 1, 1, 5, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairCompactPlanks.blockID, this.getMetadataWithOffset(Block.stairCompactPlanks.blockID, 3), 2, 1, 5, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairCompactPlanks.blockID, this.getMetadataWithOffset(Block.stairCompactPlanks.blockID, 1), 1, 1, 4, structureBoundingBox3);
		int i4;
		int i5;
		if(!this.hasMadeChest) {
			i4 = this.getYWithOffset(1);
			i5 = this.getXWithOffset(5, 5);
			int i6 = this.getZWithOffset(5, 5);
			if(structureBoundingBox3.isVecInside(i5, i4, i6)) {
				this.hasMadeChest = true;
				this.createTreasureChestAtCurrentPosition(world1, structureBoundingBox3, random2, 5, 1, 5, chestLoot, 3 + random2.nextInt(6));
			}
		}

		for(i4 = 6; i4 <= 8; ++i4) {
			if(this.getBlockIdAtCurrentPosition(world1, i4, 0, -1, structureBoundingBox3) == 0 && this.getBlockIdAtCurrentPosition(world1, i4, -1, -1, structureBoundingBox3) != 0) {
				this.placeBlockAtCurrentPosition(world1, Block.stairCompactCobblestone.blockID, this.getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), i4, 0, -1, structureBoundingBox3);
			}
		}

		for(i4 = 0; i4 < 7; ++i4) {
			for(i5 = 0; i5 < 10; ++i5) {
				this.clearCurrentPositionBlocksUpwards(world1, i5, 6, i4, structureBoundingBox3);
				this.fillCurrentPositionBlocksDownwards(world1, Block.cobblestone.blockID, 0, i5, -1, i4, structureBoundingBox3);
			}
		}

		this.spawnVillagers(world1, structureBoundingBox3, 7, 1, 1, 1);
		return true;
	}

	protected int getVillagerType(int i1) {
		return 3;
	}
}
