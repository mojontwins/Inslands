package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdStairs extends ComponentStronghold {
	private final boolean field_35036_a;
	private final EnumDoor doorType;

	public ComponentStrongholdStairs(int i1, Random random2, int i3, int i4) {
		super(i1);
		this.field_35036_a = true;
		this.coordBaseMode = random2.nextInt(4);
		this.doorType = EnumDoor.OPENING;
		switch(this.coordBaseMode) {
		case 0:
		case 2:
			this.boundingBox = new StructureBoundingBox(i3, 64, i4, i3 + 5 - 1, 74, i4 + 5 - 1);
			break;
		default:
			this.boundingBox = new StructureBoundingBox(i3, 64, i4, i3 + 5 - 1, 74, i4 + 5 - 1);
		}

	}

	public ComponentStrongholdStairs(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.field_35036_a = false;
		this.coordBaseMode = i4;
		this.doorType = this.getRandomDoor(random2);
		this.boundingBox = structureBoundingBox3;
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
		if(this.field_35036_a) {
			StructureStrongholdPieces.setComponentType(ComponentStrongholdCrossing.class);
		}

		this.getNextComponentNormal((ComponentStrongholdStairs2)structureComponent1, list2, random3, 1, 1);
	}

	public static ComponentStrongholdStairs getStrongholdStairsComponent(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, -1, -7, 0, 5, 11, 5, i5);
		return canStrongholdGoDeeper(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentStrongholdStairs(i6, random1, structureBoundingBox7, i5) : null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		if(this.isLiquidInStructureBoundingBox(world1, structureBoundingBox3)) {
			return false;
		} else {
			if(this.field_35036_a) {
				;
			}

			this.fillWithRandomizedBlocks(world1, structureBoundingBox3, 0, 0, 0, 4, 10, 4, true, random2, StructureStrongholdPieces.getStrongholdStones());
			this.placeDoor(world1, random2, structureBoundingBox3, this.doorType, 1, 7, 0);
			this.placeDoor(world1, random2, structureBoundingBox3, EnumDoor.OPENING, 1, 1, 4);
			this.placeBlockAtCurrentPosition(world1, Block.stoneBrick.blockID, 0, 2, 6, 1, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stoneBrick.blockID, 0, 1, 5, 1, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stairSingle.blockID, 0, 1, 6, 1, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stoneBrick.blockID, 0, 1, 5, 2, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stoneBrick.blockID, 0, 1, 4, 3, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stairSingle.blockID, 0, 1, 5, 3, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stoneBrick.blockID, 0, 2, 4, 3, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stoneBrick.blockID, 0, 3, 3, 3, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stairSingle.blockID, 0, 3, 4, 3, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stoneBrick.blockID, 0, 3, 3, 2, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stoneBrick.blockID, 0, 3, 2, 1, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stairSingle.blockID, 0, 3, 3, 1, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stoneBrick.blockID, 0, 2, 2, 1, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stoneBrick.blockID, 0, 1, 1, 1, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stairSingle.blockID, 0, 1, 2, 1, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stoneBrick.blockID, 0, 1, 1, 2, structureBoundingBox3);
			this.placeBlockAtCurrentPosition(world1, Block.stairSingle.blockID, 0, 1, 1, 3, structureBoundingBox3);
			return true;
		}
	}
}
