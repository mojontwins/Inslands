package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeEntrance extends ComponentNetherBridgePiece {
	public ComponentNetherBridgeEntrance(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
		this.getNextComponentNormal((ComponentNetherBridgeStartPiece)structureComponent1, list2, random3, 5, 3, true);
	}

	public static ComponentNetherBridgeEntrance createValidComponent(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, -5, -3, 0, 13, 14, 13, i5);
		return isAboveGround(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentNetherBridgeEntrance(i6, random1, structureBoundingBox7, i5) : null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 3, 0, 12, 4, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 5, 0, 12, 13, 12, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 5, 0, 1, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 11, 5, 0, 12, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 2, 5, 11, 4, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 8, 5, 11, 10, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 5, 9, 11, 7, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 2, 5, 0, 4, 12, 1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 8, 5, 0, 10, 12, 1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 5, 9, 0, 7, 12, 1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 2, 11, 2, 10, 12, 10, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 5, 8, 0, 7, 8, 0, Block.netherFence.blockID, Block.netherFence.blockID, false);

		int i4;
		for(i4 = 1; i4 <= 11; i4 += 2) {
			this.fillWithBlocks(world1, structureBoundingBox3, i4, 10, 0, i4, 11, 0, Block.netherFence.blockID, Block.netherFence.blockID, false);
			this.fillWithBlocks(world1, structureBoundingBox3, i4, 10, 12, i4, 11, 12, Block.netherFence.blockID, Block.netherFence.blockID, false);
			this.fillWithBlocks(world1, structureBoundingBox3, 0, 10, i4, 0, 11, i4, Block.netherFence.blockID, Block.netherFence.blockID, false);
			this.fillWithBlocks(world1, structureBoundingBox3, 12, 10, i4, 12, 11, i4, Block.netherFence.blockID, Block.netherFence.blockID, false);
			this.placeBlockAtCurrentPosition(world1, Block.netherBrick.blockID, 0, i4, 13, 0, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.netherBrick.blockID, 0, i4, 13, 12, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.netherBrick.blockID, 0, 0, 13, i4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.netherBrick.blockID, 0, 12, 13, i4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.netherFence.blockID, 0, i4 + 1, 13, 0, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.netherFence.blockID, 0, i4 + 1, 13, 12, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.netherFence.blockID, 0, 0, 13, i4 + 1, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.netherFence.blockID, 0, 12, 13, i4 + 1, structureBoundingBox3);
		}

		this.placeBlockAtCurrentPosition(world1, Block.netherFence.blockID, 0, 0, 13, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.netherFence.blockID, 0, 0, 13, 12, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.netherFence.blockID, 0, 0, 13, 0, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.netherFence.blockID, 0, 12, 13, 0, structureBoundingBox3);

		for(i4 = 3; i4 <= 9; i4 += 2) {
			this.fillWithBlocks(world1, structureBoundingBox3, 1, 7, i4, 1, 8, i4, Block.netherFence.blockID, Block.netherFence.blockID, false);
			this.fillWithBlocks(world1, structureBoundingBox3, 11, 7, i4, 11, 8, i4, Block.netherFence.blockID, Block.netherFence.blockID, false);
		}

		this.fillWithBlocks(world1, structureBoundingBox3, 4, 2, 0, 8, 2, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 2, 4, 12, 2, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 0, 0, 8, 1, 3, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 0, 9, 8, 1, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 0, 4, 3, 1, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 9, 0, 4, 12, 1, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);

		int i5;
		for(i4 = 4; i4 <= 8; ++i4) {
			for(i5 = 0; i5 <= 2; ++i5) {
				this.fillCurrentPositionBlocksDownwards(world1, Block.netherBrick.blockID, 0, i4, -1, i5, structureBoundingBox3);
				this.fillCurrentPositionBlocksDownwards(world1, Block.netherBrick.blockID, 0, i4, -1, 12 - i5, structureBoundingBox3);
			}
		}

		for(i4 = 0; i4 <= 2; ++i4) {
			for(i5 = 4; i5 <= 8; ++i5) {
				this.fillCurrentPositionBlocksDownwards(world1, Block.netherBrick.blockID, 0, i4, -1, i5, structureBoundingBox3);
				this.fillCurrentPositionBlocksDownwards(world1, Block.netherBrick.blockID, 0, 12 - i4, -1, i5, structureBoundingBox3);
			}
		}

		this.fillWithBlocks(world1, structureBoundingBox3, 5, 5, 5, 7, 5, 7, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 6, 1, 6, 6, 4, 6, 0, 0, false);
		this.placeBlockAtCurrentPosition(world1, Block.netherBrick.blockID, 0, 6, 0, 6, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.lavaMoving.blockID, 0, 6, 5, 6, structureBoundingBox3);
		i4 = this.getXWithOffset(6, 6);
		i5 = this.getYWithOffset(5);
		int i6 = this.getZWithOffset(6, 6);
		if(structureBoundingBox3.isVecInside(i4, i5, i6)) {
			world1.scheduledUpdatesAreImmediate = true;
			Block.blocksList[Block.lavaMoving.blockID].updateTick(world1, i4, i5, i6, random2);
			world1.scheduledUpdatesAreImmediate = false;
		}

		return true;
	}
}
