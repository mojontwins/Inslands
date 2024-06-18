package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeCorridor4 extends ComponentNetherBridgePiece {
	public ComponentNetherBridgeCorridor4(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
		byte b4 = 1;
		if(this.coordBaseMode == 1 || this.coordBaseMode == 2) {
			b4 = 5;
		}

		this.getNextComponentX((ComponentNetherBridgeStartPiece)structureComponent1, list2, random3, 0, b4, random3.nextInt(8) > 0);
		this.getNextComponentZ((ComponentNetherBridgeStartPiece)structureComponent1, list2, random3, 0, b4, random3.nextInt(8) > 0);
	}

	public static ComponentNetherBridgeCorridor4 createValidComponent(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, -3, 0, 0, 9, 7, 9, i5);
		return isAboveGround(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentNetherBridgeCorridor4(i6, random1, structureBoundingBox7, i5) : null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 0, 0, 8, 1, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 2, 0, 8, 5, 8, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 6, 0, 8, 6, 5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 2, 0, 2, 5, 0, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 6, 2, 0, 8, 5, 0, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 3, 0, 1, 4, 0, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 7, 3, 0, 7, 4, 0, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 2, 4, 8, 2, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 1, 4, 2, 2, 4, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 6, 1, 4, 7, 2, 4, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 3, 8, 8, 3, 8, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 3, 6, 0, 3, 7, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 8, 3, 6, 8, 3, 7, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 3, 4, 0, 5, 5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 8, 3, 4, 8, 5, 5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 3, 5, 2, 5, 5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 6, 3, 5, 7, 5, 5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 4, 5, 1, 5, 5, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 7, 4, 5, 7, 5, 5, Block.netherFence.blockID, Block.netherFence.blockID, false);

		for(int i4 = 0; i4 <= 5; ++i4) {
			for(int i5 = 0; i5 <= 8; ++i5) {
				this.fillCurrentPositionBlocksDownwards(world1, Block.netherBrick.blockID, 0, i5, -1, i4, structureBoundingBox3);
			}
		}

		return true;
	}
}
