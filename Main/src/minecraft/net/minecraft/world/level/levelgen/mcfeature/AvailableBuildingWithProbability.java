package net.minecraft.world.level.levelgen.mcfeature;

public class AvailableBuildingWithProbability {
	public Class<?> buildingClass;
	public int probability;
	
	public AvailableBuildingWithProbability(Class<?> buildingClass, int probability) {
		this.buildingClass = buildingClass;
		this.probability = probability;
	}
}
