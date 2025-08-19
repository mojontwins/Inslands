package net.minecraft.game.entity.animal;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class EntitySheep extends EntityAnimal {
	public boolean sheared = false;

	public EntitySheep(World world1) {
		super(world1);
		this.texture = "/mob/sheep.png";
		this.setSize(0.9F, 1.3F);
	}

	public final boolean attackEntityFrom(Entity entity1, int i2) {
		if(!this.sheared && entity1 instanceof EntityLiving) {
			this.sheared = true;
			int i3 = 1 + this.rand.nextInt(3);

			for(int i4 = 0; i4 < i3; ++i4) {
				EntityItem entityItem5;
				EntityItem entityItem10000 = entityItem5 = this.entityDropItem(Block.clothGray.blockID, 1, 1.0F);
				entityItem10000.motionY += (double)(this.rand.nextFloat() * 0.05F);
				entityItem5.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				entityItem5.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
			}
		}

		return super.attackEntityFrom(entity1, i2);
	}

	public final void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
		nBTTagCompound1.setBoolean("Sheared", this.sheared);
	}

	public final void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
		this.sheared = nBTTagCompound1.getBoolean("Sheared");
	}

	protected final String getLivingSound() {
		return "mob.sheep";
	}

	protected final String getHurtSound() {
		return "mob.sheep";
	}

	protected final String getDeathSound() {
		return "mob.sheep";
	}
}