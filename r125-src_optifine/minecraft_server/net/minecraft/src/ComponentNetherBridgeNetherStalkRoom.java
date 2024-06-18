package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeNetherStalkRoom extends ComponentNetherBridgePiece {
	public ComponentNetherBridgeNetherStalkRoom(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
		this.getNextComponentNormal((ComponentNetherBridgeStartPiece)structureComponent1, list2, random3, 5, 3, true);
		this.getNextComponentNormal((ComponentNetherBridgeStartPiece)structureComponent1, list2, random3, 5, 11, true);
	}

	public static ComponentNetherBridgeNetherStalkRoom createValidComponent(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, -5, -3, 0, 13, 14, 13, i5);
		return isAboveGround(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentNetherBridgeNetherStalkRoom(i6, random1, structureBoundingBox7, i5) : null;
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

		i4 = this.getMetadataWithOffset(Block.stairsNetherBrick.blockID, 3);

		int i5;
		int i6;
		int i7;
		for(i5 = 0; i5 <= 6; ++i5) {
			i6 = i5 + 4;

			for(i7 = 5; i7 <= 7; ++i7) {
				this.placeBlockAtCurrentPosition(world1, Block.stairsNetherBrick.blockID, i4, i7, 5 + i5, i6, structureBoundingBox3);
			}

			if(i6 >= 5 && i6 <= 8) {
				this.fillWithBlocks(world1, structureBoundingBox3, 5, 5, i6, 7, i5 + 4, i6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
			} else if(i6 >= 9 && i6 <= 10) {
				this.fillWithBlocks(world1, structureBoundingBox3, 5, 8, i6, 7, i5 + 4, i6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
			}

			if(i5 >= 1) {
				this.fillWithBlocks(world1, structureBoundingBox3, 5, 6 + i5, i6, 7, 9 + i5, i6, 0, 0, false);
			}
		}

		for(i5 = 5; i5 <= 7; ++i5) {
			this.placeBlockAtCurrentPosition(world1, Block.stairsNetherBrick.blockID, i4, i5, 12, 11, structureBoundingBox3);
		}

		this.fillWithBlocks(world1, structureBoundingBox3, 5, 6, 7, 5, 7, 7, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 7, 6, 7, 7, 7, 7, Block.netherFence.blockID, Block.netherFence.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 5, 13, 12, 7, 13, 12, 0, 0, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 2, 5, 2, 3, 5, 3, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 2, 5, 9, 3, 5, 10, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 2, 5, 4, 2, 5, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 9, 5, 2, 10, 5, 3, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 9, 5, 9, 10, 5, 10, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 10, 5, 4, 10, 5, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		i5 = this.getMetadataWithOffset(Block.stairsNetherBrick.blockID, 0);
		i6 = this.getMetadataWithOffset(Block.stairsNetherBrick.blockID, 1);
		this.placeBlockAtCurrentPosition(world1, Block.stairsNetherBrick.blockID, i6, 4, 5, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairsNetherBrick.blockID, i6, 4, 5, 3, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairsNetherBrick.blockID, i6, 4, 5, 9, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairsNetherBrick.blockID, i6, 4, 5, 10, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairsNetherBrick.blockID, i5, 8, 5, 2, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairsNetherBrick.blockID, i5, 8, 5, 3, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairsNetherBrick.blockID, i5, 8, 5, 9, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.stairsNetherBrick.blockID, i5, 8, 5, 10, structureBoundingBox3);
		this.fillWithBlocks(world1, structureBoundingBox3, 3, 4, 4, 4, 4, 8, Block.slowSand.blockID, Block.slowSand.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 8, 4, 4, 9, 4, 8, Block.slowSand.blockID, Block.slowSand.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 3, 5, 4, 4, 5, 8, Block.netherStalk.blockID, Block.netherStalk.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 8, 5, 4, 9, 5, 8, Block.netherStalk.blockID, Block.netherStalk.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 2, 0, 8, 2, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 2, 4, 12, 2, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 0, 0, 8, 1, 3, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 0, 9, 8, 1, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 0, 4, 3, 1, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 9, 0, 4, 12, 1, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);

		int i8;
		for(i7 = 4; i7 <= 8; ++i7) {
			for(i8 = 0; i8 <= 2; ++i8) {
				this.fillCurrentPositionBlocksDownwards(world1, Block.netherBrick.blockID, 0, i7, -1, i8, structureBoundingBox3);
				this.fillCurrentPositionBlocksDownwards(world1, Block.netherBrick.blockID, 0, i7, -1, 12 - i8, structureBoundingBox3);
			}
		}

		for(i7 = 0; i7 <= 2; ++i7) {
			for(i8 = 4; i8 <= 8; ++i8) {
				this.fillCurrentPositionBlocksDownwards(world1, Block.netherBrick.blockID, 0, i7, -1, i8, structureBoundingBox3);
				this.fillCurrentPositionBlocksDownwards(world1, Block.netherBrick.blockID, 0, 12 - i7, -1, i8, structureBoundingBox3);
			}
		}

		return true;
	}
}
