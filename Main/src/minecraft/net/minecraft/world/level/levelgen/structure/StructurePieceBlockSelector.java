package net.minecraft.world.level.levelgen.structure;

import java.util.Random;

public abstract class StructurePieceBlockSelector {
	protected int selectedblockID;
	protected int selectedBlockMetaData;

	public abstract void selectBlocks(Random random1, int i2, int i3, int i4, boolean z5);

	public int getSelectedblockID() {
		return this.selectedblockID;
	}

	public int getSelectedBlockMetaData() {
		return this.selectedBlockMetaData;
	}
}
