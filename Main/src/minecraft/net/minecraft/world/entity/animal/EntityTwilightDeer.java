package net.minecraft.world.entity.animal;

import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.World;

public class EntityTwilightDeer extends EntityCow {
	public EntityTwilightDeer(World world) {
		super(world);
		this.texture = "/mob/wilddeer.png";
		this.setSize(0.7F, 2.3F);
	}

	protected String getLivingSound() {
		return null;
	}

	public boolean interact(EntityPlayer entityplayer) {
		return false;
	}
	
	public boolean needsLitBlockToSpawn() {
		return false;
	}
}
