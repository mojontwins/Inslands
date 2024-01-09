package net.minecraft.src;

public class EntityGlowdustFX extends EntityReddustFX {
	public EntityGlowdustFX(World world1, double d2, double d4, double d6) {
		this(world1, d2, d4, d6, 1.0F);
	}
	
	public EntityGlowdustFX(World world1, double d2, double d4, double d6, float f8) {
		super(world1, d2, d4, d6, f8, 1.0F, 1.0F, 1.0F);
		this.particleRed = this.particleGreen = (float)(Math.random() * (double)0.3F) + 0.7F;
		this.particleBlue = (float)(Math.random() * (double)0.1F);
	}

}
