package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdStairsStraight extends ComponentStronghold {
	private final EnumDoor doorType;

	public ComponentStrongholdStairsStraight(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.doorType = this.getRandomDoor(random2);
		this.boundingBox = structureBoundingBox3;
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
		this.getNextComponentNormal((ComponentStrongholdStairs2)structureComponent1, list2, random3, 1, 1);
	}

	public static ComponentStrongholdStairsStraight findValidPlacement(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, -1, -7, 0, 5, 11, 8, i5);
		return canStrongholdGoDeeper(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentStrongholdStairsStraight(i6, random1, structureBoundingBox7, i5) : null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		if(this.isLiquidInStructureBoundingBox(world1, structureBoundingBox3)) {
			return false;
		} else {
			this.fillWithRandomizedBlocks(world1, structureBoundingBox3, 0, 0, 0, 4, 10, 7, true, random2, StructureStrongholdPieces.getStrongholdStones());
			this.placeDoor(world1, random2, structureBoundingBox3, this.doorType, 1, 7, 0);
			this.placeDoor(world1, random2, structureBoundingBox3, EnumDoor.OPENING, 1, 1, 7);
			int i4 = this.getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 2);

			for(int i5 = 0; i5 < 6; ++i5) {
				this.placeBlockAtCurrentPosition(world1, Block.stairCompactCobblestone.blockID, i4, 1, 6 - i5, 1 + i5, structureBoundingBox3);
				this.placeBlockAtCurrentPosition(world1, Block.stairCompactCobblestone.blockID, i4, 2, 6 - i5, 1 + i5, structureBoundingBox3);
				this.placeBlockAtCurrentPosition(world1, Block.stairCompactCobblestone.blockID, i4, 3, 6 - i5, 1 + i5, structureBoundingBox3);
				if(i5 < 5) {
					this.placeBlockAtCurrentPosition(world1, Block.stoneBrick.blockID, 0, 1, 5 - i5, 1 + i5, structureBoundingBox3);
					this.placeBlockAtCurrentPosition(world1, Block.stoneBrick.blockID, 0, 2, 5 - i5, 1 + i5, structureBoundingBox3);
					this.placeBlockAtCurrentPosition(world1, Block.stoneBrick.blockID, 0, 3, 5 - i5, 1 + i5, structureBoundingBox3);
				}
			}

			return true;
		}
	}
}
