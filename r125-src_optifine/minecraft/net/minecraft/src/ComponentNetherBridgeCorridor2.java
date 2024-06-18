package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeCorridor2 extends ComponentNetherBridgePiece {
	public ComponentNetherBridgeCorridor2(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
		this.getNextComponentZ((ComponentNetherBridgeStartPiece)structureComponent1, list2, random3, 0, 1, true);
	}

	public static ComponentNetherBridgeCorridor2 createValidComponent(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, -1, 0, 0, 5, 7, 5, i5);
		return isAboveGround(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentNetherBridgeCorridor2(i6, random1, structureBoundingBox7, i5) : null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 0, 0, 4, 1, 4, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 2, 0, 4, 5, 4, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 2, 0, 0, 5, 4, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 3, 1, 0, 4, 1, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 3, 3, 0, 4, 3, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 2, 0, 4, 5, 0, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 2, 4, 4, 5, 4, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 3, 4, 1, 4, 4, Block.netherFence.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 3, 3, 4, 3, 4, 4, Block.netherFence.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 6, 0, 4, 6, 4, Block.netherBrick.blockID, Block.netherBrick.blockID, false);

		for(int i4 = 0; i4 <= 4; ++i4) {
			for(int i5 = 0; i5 <= 4; ++i5) {
				this.fillCurrentPositionBlocksDownwards(world1, Block.netherBrick.blockID, 0, i4, -1, i5, structureBoundingBox3);
			}
		}

		return true;
	}
}
