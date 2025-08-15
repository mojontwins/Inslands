package net.minecraft.world.stats;

final class StatTypeSimple implements IStatType {
	public String format(int i1) {
		return StatBase.getNumberFormat().format((long)i1);
	}
}
