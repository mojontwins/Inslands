package net.minecraft.world.level.levelgen.structure.stronghold;

import java.util.Random;

import net.minecraft.world.level.levelgen.structure.StructurePieceBlockSelector;

class StructureStrongholdStones extends StructurePieceBlockSelector {
	private StructureStrongholdStones() {
	}

	public void selectBlocks(Random rand, int x, int y, int z, boolean hollow) {
		if(!hollow) {
			this.selectedblockID = 0;
			this.selectedBlockMetaData = 0;
		} else {
			this.selectedblockID = MapGenStronghold.bricksId;
			float f6 = rand.nextFloat();
			if(f6 < 0.2F) {
				this.selectedblockID = MapGenStronghold.bricksAlt1Id;
				this.selectedBlockMetaData = MapGenStronghold.bricksAlt1Meta;
			} else if(f6 < 0.5F) {
				this.selectedblockID = MapGenStronghold.bricksAlt2Id;
				this.selectedBlockMetaData = MapGenStronghold.bricksAlt2Meta;
			} else {
				this.selectedblockID = MapGenStronghold.bricksId;
				this.selectedBlockMetaData = MapGenStronghold.bricksMeta;
			}
		}

	}

	StructureStrongholdStones(StructureStrongholdPieceWeight2 structureStrongholdPieceWeight21) {
		this();
	}
}
