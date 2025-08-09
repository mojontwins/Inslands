package net.minecraft.world.entity.animal;

import net.minecraft.world.entity.EntityLiving;

public interface EntityTameable {

	boolean isTamed();

	EntityLiving getOwner();

}
