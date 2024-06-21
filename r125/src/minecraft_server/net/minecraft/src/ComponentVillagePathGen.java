package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillagePathGen extends ComponentVillageRoadPiece {
	private int averageGroundLevel;

	public ComponentVillagePathGen(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
		this.averageGroundLevel = Math.max(structureBoundingBox3.getXSize(), structureBoundingBox3.getZSize());
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
		boolean z4 = false;

		int i5;
		StructureComponent structureComponent6;
		for(i5 = random3.nextInt(5); i5 < this.averageGroundLevel - 8; i5 += 2 + random3.nextInt(5)) {
			structureComponent6 = this.getNextComponentNN((ComponentVillageStartPiece)structureComponent1, list2, random3, 0, i5);
			if(structureComponent6 != null) {
				i5 += Math.max(structureComponent6.boundingBox.getXSize(), structureComponent6.boundingBox.getZSize());
				z4 = true;
			}
		}

		for(i5 = random3.nextInt(5); i5 < this.averageGroundLevel - 8; i5 += 2 + random3.nextInt(5)) {
			structureComponent6 = this.getNextComponentPP((ComponentVillageStartPiece)structureComponent1, list2, random3, 0, i5);
			if(structureComponent6 != null) {
				i5 += Math.max(structureComponent6.boundingBox.getXSize(), structureComponent6.boundingBox.getZSize());
				z4 = true;
			}
		}

		if(z4 && random3.nextInt(3) > 0) {
			switch(this.coordBaseMode) {
			case 0:
				StructureVillagePieces.getNextStructureComponentVillagePath((ComponentVillageStartPiece)structureComponent1, list2, random3, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, 1, this.getComponentType());
				break;
			case 1:
				StructureVillagePieces.getNextStructureComponentVillagePath((ComponentVillageStartPiece)structureComponent1, list2, random3, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, this.getComponentType());
				break;
			case 2:
				StructureVillagePieces.getNextStructureComponentVillagePath((ComponentVillageStartPiece)structureComponent1, list2, random3, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, 1, this.getComponentType());
				break;
			case 3:
				StructureVillagePieces.getNextStructureComponentVillagePath((ComponentVillageStartPiece)structureComponent1, list2, random3, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, this.getComponentType());
			}
		}

		if(z4 && random3.nextInt(3) > 0) {
			switch(this.coordBaseMode) {
			case 0:
				StructureVillagePieces.getNextStructureComponentVillagePath((ComponentVillageStartPiece)structureComponent1, list2, random3, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, 3, this.getComponentType());
				break;
			case 1:
				StructureVillagePieces.getNextStructureComponentVillagePath((ComponentVillageStartPiece)structureComponent1, list2, random3, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, this.getComponentType());
				break;
			case 2:
				StructureVillagePieces.getNextStructureComponentVillagePath((ComponentVillageStartPiece)structureComponent1, list2, random3, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, 3, this.getComponentType());
				break;
			case 3:
				StructureVillagePieces.getNextStructureComponentVillagePath((ComponentVillageStartPiece)structureComponent1, list2, random3, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, this.getComponentType());
			}
		}

	}

	public static StructureBoundingBox func_35378_a(ComponentVillageStartPiece componentVillageStartPiece0, List list1, Random random2, int i3, int i4, int i5, int i6) {
		for(int i7 = 7 * MathHelper.getRandomIntegerInRange(random2, 3, 5); i7 >= 7; i7 -= 7) {
			StructureBoundingBox structureBoundingBox8 = StructureBoundingBox.getComponentToAddBoundingBox(i3, i4, i5, 0, 0, 0, 3, 3, i7, i6);
			if(StructureComponent.findIntersecting(list1, structureBoundingBox8) == null) {
				return structureBoundingBox8;
			}
		}

		return null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		for(int i4 = this.boundingBox.minX; i4 <= this.boundingBox.maxX; ++i4) {
			for(int i5 = this.boundingBox.minZ; i5 <= this.boundingBox.maxZ; ++i5) {
				if(structureBoundingBox3.isVecInside(i4, 64, i5)) {
					int i6 = world1.getTopSolidOrLiquidBlock(i4, i5) - 1;
					world1.setBlock(i4, i6, i5, Block.gravel.blockID);
				}
			}
		}

		return true;
	}
}
