package net.minecraft.src;

public class SpawnListEntry extends WeightedRandomChoice {
	public Class entityClass;
	public int minGroupCount;
	public int maxGroupCount;

	public SpawnListEntry(Class class1, int i2, int i3, int i4) {
		super(i2);
		this.entityClass = class1;
		this.minGroupCount = i3;
		this.maxGroupCount = i4;
	}
}
