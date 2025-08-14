package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.WeightedRandomChoice;

public class StructurePieceTreasure extends WeightedRandomChoice {
	public int itemID;
	public int itemMetadata;
	public int minItemStack;
	public int maxItemStack;

	public StructurePieceTreasure(int i1, int i2, int i3, int i4, int i5) {
		super(i5);
		this.itemID = i1;
		this.itemMetadata = i2;
		this.minItemStack = i3;
		this.maxItemStack = i4;
	}
}
