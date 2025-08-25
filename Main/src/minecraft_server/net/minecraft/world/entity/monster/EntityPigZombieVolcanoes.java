package net.minecraft.world.entity.monster;

import net.minecraft.util.MathHelper;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;

public class EntityPigZombieVolcanoes extends EntityPigZombie {

	public EntityPigZombieVolcanoes(World world1) {
		super(world1);
	}

	@Override
	public boolean getCanSpawnHere() {
		int x = MathHelper.floor_double(this.posX);
		int y = MathHelper.floor_double(this.boundingBox.minY);
		int z = MathHelper.floor_double(this.posZ);
		
		boolean s = super.getCanSpawnHere();
		boolean b = this.worldObj.getblockID(x, y - 1, z) == Block.bloodStone.blockID;
		
		return b && s;
	}
	
}
