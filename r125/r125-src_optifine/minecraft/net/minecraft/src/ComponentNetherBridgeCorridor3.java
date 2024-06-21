package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeCorridor3 extends ComponentNetherBridgePiece {
	public ComponentNetherBridgeCorridor3(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
		this.getNextComponentNormal((ComponentNetherBridgeStartPiece)structureComponent1, list2, random3, 1, 0, true);
	}

	public static ComponentNetherBridgeCorridor3 createValidComponent(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, -1, -7, 0, 5, 14, 10, i5);
		return isAboveGround(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentNetherBridgeCorridor3(i6, random1, structureBoundingBox7, i5) : null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		int i4 = this.getMetadataWithOffset(Block.stairsNetherBrick.blockID, 2);

		for(int i5 = 0; i5 <= 9; ++i5) {
			int i6 = Math.max(1, 7 - i5);
			int i7 = Math.min(Math.max(i6 + 5, 14 - i5), 13);
			int i8 = i5;
			this.fillWithBlocks(world1, structureBoundingBox3, 0, 0, i5, 4, i6, i5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
			this.fillWithBlocks(world1, structureBoundingBox3, 1, i6 + 1, i5, 3, i7 - 1, i5, 0, 0, false);
			if(i5 <= 6) {
				this.placeBlockAtCurrentPosition(world1, Block.stairsNetherBrick.blockID, i4, 1, i6 + 1, i5, structureBoundingBox3);
				this.placeBlockAtCurrentPosition(world1, Block.stairsNetherBrick.blockID, i4, 2, i6 + 1, i5, structureBoundingBox3);
				this.placeBlockAtCurrentPosition(world1, Block.stairsNetherBrick.blockID, i4, 3, i6 + 1, i5, structureBoundingBox3);
			}

			this.fillWithBlocks(world1, structureBoundingBox3, 0, i7, i5, 4, i7, i5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
			this.fillWithBlocks(world1, structureBoundingBox3, 0, i6 + 1, i5, 0, i7 - 1, i5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
			this.fillWithBlocks(world1, structureBoundingBox3, 4, i6 + 1, i5, 4, i7 - 1, i5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
			if((i5 & 1) == 0) {
				this.fillWithBlocks(world1, structureBoundingBox3, 0, i6 + 2, i5, 0, i6 + 3, i5, Block.netherFence.blockID, Block.netherFence.blockID, false);
				this.fillWithBlocks(world1, structureBoundingBox3, 4, i6 + 2, i5, 4, i6 + 3, i5, Block.netherFence.blockID, Block.netherFence.blockID, false);
			}

			for(int i9 = 0; i9 <= 4; ++i9) {
				this.fillCurrentPositionBlocksDownwards(world1, Block.netherBrick.blockID, 0, i9, -1, i8, structureBoundingBox3);
			}
		}

		return true;
	}
}
