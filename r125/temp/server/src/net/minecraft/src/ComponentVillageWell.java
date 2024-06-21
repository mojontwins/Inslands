package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageWell extends ComponentVillage {
	private final boolean field_35385_a = true;
	private int averageGroundLevel = -1;

	public ComponentVillageWell(int i1, Random random2, int i3, int i4) {
		super(i1);
		this.coordBaseMode = random2.nextInt(4);
		switch(this.coordBaseMode) {
		case 0:
		case 2:
			this.boundingBox = new StructureBoundingBox(i3, 64, i4, i3 + 6 - 1, 78, i4 + 6 - 1);
			break;
		default:
			this.boundingBox = new StructureBoundingBox(i3, 64, i4, i3 + 6 - 1, 78, i4 + 6 - 1);
		}

	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
		StructureVillagePieces.getNextStructureComponentVillagePath((ComponentVillageStartPiece)structureComponent1, list2, random3, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, 1, this.getComponentType());
		StructureVillagePieces.getNextStructureComponentVillagePath((ComponentVillageStartPiece)structureComponent1, list2, random3, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, 3, this.getComponentType());
		StructureVillagePieces.getNextStructureComponentVillagePath((ComponentVillageStartPiece)structureComponent1, list2, random3, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, 2, this.getComponentType());
		StructureVillagePieces.getNextStructureComponentVillagePath((ComponentVillageStartPiece)structureComponent1, list2, random3, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, 0, this.getComponentType());
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		if(this.averageGroundLevel < 0) {
			this.averageGroundLevel = this.getAverageGroundLevel(world1, structureBoundingBox3);
			if(this.averageGroundLevel < 0) {
				return true;
			}

			this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 3, 0);
		}

		if(this.field_35385_a) {
			;
		}

		this.fillWithBlocks(world1, structureBoundingBox3, 1, 0, 1, 4, 12, 4, Block.cobblestone.blockID, Block.waterMoving.blockID, false);
		this.placeBlockAtCurrentPosition(world1, 0, 0, 2, 12, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, 0, 0, 3, 12, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, 0, 0, 2, 12, 3, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, 0, 0, 3, 12, 3, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 1, 13, 1, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 1, 14, 1, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 4, 13, 1, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 4, 14, 1, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 1, 13, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 1, 14, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 4, 13, 4, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.fence.blockID, 0, 4, 14, 4, structureBoundingBox3);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 15, 1, 4, 15, 4, Block.cobblestone.blockID, Block.cobblestone.blockID, false);

		for(int i4 = 0; i4 <= 5; ++i4) {
			for(int i5 = 0; i5 <= 5; ++i5) {
				if(i5 == 0 || i5 == 5 || i4 == 0 || i4 == 5) {
					this.placeBlockAtCurrentPosition(world1, Block.gravel.blockID, 0, i5, 11, i4, structureBoundingBox3);
					this.clearCurrentPositionBlocksUpwards(world1, i5, 12, i4, structureBoundingBox3);
				}
			}
		}

		return true;
	}
}
