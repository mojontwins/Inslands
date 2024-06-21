package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageField extends ComponentVillage {
	private int averageGroundLevel = -1;

	public ComponentVillageField(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
	}

	public static ComponentVillageField findValidPlacement(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, 0, 0, 0, 13, 4, 9, i5);
		return canVillageGoDeeper(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentVillageField(i6, random1, structureBoundingBox7, i5) : null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		if(this.averageGroundLevel < 0) {
			this.averageGroundLevel = this.getAverageGroundLevel(world1, structureBoundingBox3);
			if(this.averageGroundLevel < 0) {
				return true;
			}

			this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 4 - 1, 0);
		}

		this.fillWithBlocks(world1, structureBoundingBox3, 0, 1, 0, 12, 4, 8, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 0, 1, 2, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 0, 1, 5, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 7, 0, 1, 8, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 10, 0, 1, 11, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 0, 0, 0, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 6, 0, 0, 6, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 12, 0, 0, 12, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 0, 0, 11, 0, 0, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 0, 8, 11, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 3, 0, 1, 3, 0, 7, Block.waterMoving.blockID, Block.waterMoving.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 9, 0, 1, 9, 0, 7, Block.waterMoving.blockID, Block.waterMoving.blockID, false);

		int i4;
		for(i4 = 1; i4 <= 7; ++i4) {
			this.placeBlockAtCurrentPosition(world1, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random2, 2, 7), 1, 1, i4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random2, 2, 7), 2, 1, i4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random2, 2, 7), 4, 1, i4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random2, 2, 7), 5, 1, i4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random2, 2, 7), 7, 1, i4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random2, 2, 7), 8, 1, i4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random2, 2, 7), 10, 1, i4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.crops.blockID, MathHelper.getRandomIntegerInRange(random2, 2, 7), 11, 1, i4, structureBoundingBox3);
		}

		for(i4 = 0; i4 < 9; ++i4) {
			for(int i5 = 0; i5 < 13; ++i5) {
				this.clearCurrentPositionBlocksUpwards(world1, i5, 4, i4, structureBoundingBox3);
				this.fillCurrentPositionBlocksDownwards(world1, Block.dirt.blockID, 0, i5, -1, i4, structureBoundingBox3);
			}
		}

		return true;
	}
}
