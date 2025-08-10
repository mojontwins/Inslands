package net.minecraft.world.entity.animal;

import java.util.Random;

import net.minecraft.src.World;

public class EntityTwilightBighorn extends EntitySheep {
	public EntityTwilightBighorn(World world) {
		super(world);
		this.texture = "/mob/bighorn.png";
		this.setSize(0.9F, 1.3F);
	}

	public static int getRandomFleeceColorForReal(Random random) {
		return random.nextInt(2) == 0 ? 12 : random.nextInt(16);
	}
	
	public boolean needsLitBlockToSpawn() {
		return false;
	}
}
