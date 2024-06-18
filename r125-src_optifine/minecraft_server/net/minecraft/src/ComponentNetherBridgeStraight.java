package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeStraight extends ComponentNetherBridgePiece {
	public ComponentNetherBridgeStraight(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
		this.getNextComponentNormal((ComponentNetherBridgeStartPiece)structureComponent1, list2, random3, 1, 3, false);
	}

	public static ComponentNetherBridgeStraight createValidComponent(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, -1, -3, 0, 5, 10, 19, i5);
		return isAboveGround(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentNetherBridgeStraight(i6, random1, structureBoundingBox7, i5) : null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 3, 0, 4, 4, 18, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 5, 0, 3, 7, 18, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 5, 0, 0, 5, 18, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 5, 0, 4, 5, 18, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 2, 0, 4, 2, 5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 2, 13, 4, 2, 18, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 0, 0, 4, 1, 3, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 0, 15, 4, 1, 18, Block.netherBrick.blockID, Block.netherBrick.blockID, false);

		for(int i4 = 0; i4 <= 4; ++i4) {
			for(int i5 = 0; i5 <= 2; ++i5) {
				this.fillCurrentPositionBlocksDownwards(world1, Block.netherBrick.blockID, 0, i4, -1, i5, structureBoundingBox3);
				this.fillCurrentPositionBlocksDownwards(world1, Block.netherBrick.blockID, 0, i4, -1, 18 - i5, structureBoundingBox3);
			}
		}

		this.fillWithBlocks(world1, structureBoundingBox3, 0, 1, 1, 0, 4, 1, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 3, 4, 0, 4, 4, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 3, 14, 0, 4, 14, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 1, 17, 0, 4, 17, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 1, 1, 4, 4, 1, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 3, 4, 4, 4, 4, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 3, 14, 4, 4, 14, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 1, 17, 4, 4, 17, Block.netherFence.blockID, Block.netherFence.blockID, false);
		return true;
	}
}
