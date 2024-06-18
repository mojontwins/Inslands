package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdPortalRoom extends ComponentStronghold {
	private boolean hasSpawner;

	public ComponentStrongholdPortalRoom(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
		if(structureComponent1 != null) {
			((ComponentStrongholdStairs2)structureComponent1).portalRoom = this;
		}

	}

	public static ComponentStrongholdPortalRoom findValidPlacement(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, -4, -1, 0, 11, 8, 16, i5);
		return canStrongholdGoDeeper(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentStrongholdPortalRoom(i6, random1, structureBoundingBox7, i5) : null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		this.fillWithRandomizedBlocks(world1, structureBoundingBox3, 0, 0, 0, 10, 7, 15, false, random2, StructureStrongholdPieces.getStrongholdStones());
		this.placeDoor(world1, random2, structureBoundingBox3, EnumDoor.GRATES, 4, 1, 0);
		byte b4 = 6;
		this.fillWithRandomizedBlocks(world1, structureBoundingBox3, 1, b4, 1, 1, b4, 14, false, random2, StructureStrongholdPieces.getStrongholdStones());
		this.fillWithRandomizedBlocks(world1, structureBoundingBox3, 9, b4, 1, 9, b4, 14, false, random2, StructureStrongholdPieces.getStrongholdStones());
		this.fillWithRandomizedBlocks(world1, structureBoundingBox3, 2, b4, 1, 8, b4, 2, false, random2, StructureStrongholdPieces.getStrongholdStones());
		this.fillWithRandomizedBlocks(world1, structureBoundingBox3, 2, b4, 14, 8, b4, 14, false, random2, StructureStrongholdPieces.getStrongholdStones());
		this.fillWithRandomizedBlocks(world1, structureBoundingBox3, 1, 1, 1, 2, 1, 4, false, random2, StructureStrongholdPieces.getStrongholdStones());
		this.fillWithRandomizedBlocks(world1, structureBoundingBox3, 8, 1, 1, 9, 1, 4, false, random2, StructureStrongholdPieces.getStrongholdStones());
		this.fillWithBlocks(world1, structureBoundingBox3, 1, 1, 1, 1, 1, 3, Block.lavaMoving.blockID, Block.lavaMoving.blockID, false);
		this.fillWithBlocks(world1, structureBoundingBox3, 9, 1, 1, 9, 1, 3, Block.lavaMoving.blockID, Block.lavaMoving.blockID, false);
		this.fillWithRandomizedBlocks(world1, structureBoundingBox3, 3, 1, 8, 7, 1, 12, false, random2, StructureStrongholdPieces.getStrongholdStones());
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 1, 9, 6, 1, 11, Block.lavaMoving.blockID, Block.lavaMoving.blockID, false);

		int i5;
		for(i5 = 3; i5 < 14; i5 += 2) {
			this.fillWithBlocks(world1, structureBoundingBox3, 0, 3, i5, 0, 4, i5, Block.fenceIron.blockID, Block.fenceIron.blockID, false);
			this.fillWithBlocks(world1, structureBoundingBox3, 10, 3, i5, 10, 4, i5, Block.fenceIron.blockID, Block.fenceIron.blockID, false);
		}

		for(i5 = 2; i5 < 9; i5 += 2) {
			this.fillWithBlocks(world1, structureBoundingBox3, i5, 3, 15, i5, 4, 15, Block.fenceIron.blockID, Block.fenceIron.blockID, false);
		}

		i5 = this.getMetadataWithOffset(Block.stairsStoneBrickSmooth.blockID, 3);
		this.fillWithRandomizedBlocks(world1, structureBoundingBox3, 4, 1, 5, 6, 1, 7, false, random2, StructureStrongholdPieces.getStrongholdStones());
		this.fillWithRandomizedBlocks(world1, structureBoundingBox3, 4, 2, 6, 6, 2, 7, false, random2, StructureStrongholdPieces.getStrongholdStones());
		this.fillWithRandomizedBlocks(world1, structureBoundingBox3, 4, 3, 7, 6, 3, 7, false, random2, StructureStrongholdPieces.getStrongholdStones());

		for(int i6 = 4; i6 <= 6; ++i6) {
			this.placeBlockAtCurrentPosition(world1, Block.stairsStoneBrickSmooth.blockID, i5, i6, 1, 4, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stairsStoneBrickSmooth.blockID, i5, i6, 2, 5, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stairsStoneBrickSmooth.blockID, i5, i6, 3, 6, structureBoundingBox3);
		}

		byte b14 = 2;
		byte b7 = 0;
		byte b8 = 3;
		byte b9 = 1;
		switch(this.coordBaseMode) {
		case 0:
			b14 = 0;
			b7 = 2;
			break;
		case 1:
			b14 = 1;
			b7 = 3;
			b8 = 0;
			b9 = 2;
		case 2:
		default:
			break;
		case 3:
			b14 = 3;
			b7 = 1;
			b8 = 0;
			b9 = 2;
		}

		this.placeBlockAtCurrentPosition(world1, Block.endPortalFrame.blockID, b14 + (random2.nextFloat() > 0.9F ? 4 : 0), 4, 3, 8, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.endPortalFrame.blockID, b14 + (random2.nextFloat() > 0.9F ? 4 : 0), 5, 3, 8, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.endPortalFrame.blockID, b14 + (random2.nextFloat() > 0.9F ? 4 : 0), 6, 3, 8, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.endPortalFrame.blockID, b7 + (random2.nextFloat() > 0.9F ? 4 : 0), 4, 3, 12, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.endPortalFrame.blockID, b7 + (random2.nextFloat() > 0.9F ? 4 : 0), 5, 3, 12, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.endPortalFrame.blockID, b7 + (random2.nextFloat() > 0.9F ? 4 : 0), 6, 3, 12, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.endPortalFrame.blockID, b8 + (random2.nextFloat() > 0.9F ? 4 : 0), 3, 3, 9, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.endPortalFrame.blockID, b8 + (random2.nextFloat() > 0.9F ? 4 : 0), 3, 3, 10, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.endPortalFrame.blockID, b8 + (random2.nextFloat() > 0.9F ? 4 : 0), 3, 3, 11, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.endPortalFrame.blockID, b9 + (random2.nextFloat() > 0.9F ? 4 : 0), 7, 3, 9, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.endPortalFrame.blockID, b9 + (random2.nextFloat() > 0.9F ? 4 : 0), 7, 3, 10, structureBoundingBox3);
		this.placeBlockAtCurrentPosition(world1, Block.endPortalFrame.blockID, b9 + (random2.nextFloat() > 0.9F ? 4 : 0), 7, 3, 11, structureBoundingBox3);
		if(!this.hasSpawner) {
			int i13 = this.getYWithOffset(3);
			int i10 = this.getXWithOffset(5, 6);
			int i11 = this.getZWithOffset(5, 6);
			if(structureBoundingBox3.isVecInside(i10, i13, i11)) {
				this.hasSpawner = true;
				world1.setBlockWithNotify(i10, i13, i11, Block.mobSpawner.blockID);
				TileEntityMobSpawner tileEntityMobSpawner12 = (TileEntityMobSpawner)world1.getBlockTileEntity(i10, i13, i11);
				if(tileEntityMobSpawner12 != null) {
					tileEntityMobSpawner12.setMobID("Silverfish");
				}
			}
		}

		return true;
	}
}
