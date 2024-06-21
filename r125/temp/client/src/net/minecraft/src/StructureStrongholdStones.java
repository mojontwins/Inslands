package net.minecraft.src;

import java.util.Random;

class StructureStrongholdStones extends StructurePieceBlockSelector {
	private StructureStrongholdStones() {
	}

	public void selectBlocks(Random random1, int i2, int i3, int i4, boolean z5) {
		if(!z5) {
			this.selectedBlockId = 0;
			this.selectedBlockMetaData = 0;
		} else {
			this.selectedBlockId = Block.stoneBrick.blockID;
			float f6 = random1.nextFloat();
			if(f6 < 0.2F) {
				this.selectedBlockMetaData = 2;
			} else if(f6 < 0.5F) {
				this.selectedBlockMetaData = 1;
			} else if(f6 < 0.55F) {
				this.selectedBlockId = Block.silverfish.blockID;
				this.selectedBlockMetaData = 2;
			} else {
				this.selectedBlockMetaData = 0;
			}
		}

	}

	StructureStrongholdStones(StructureStrongholdPieceWeight2 structureStrongholdPieceWeight21) {
		this();
	}
}
