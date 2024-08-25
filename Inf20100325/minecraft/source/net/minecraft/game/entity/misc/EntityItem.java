package net.minecraft.game.entity.misc;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

public class EntityItem extends Entity {
	public ItemStack item;
	private int age2;
	public int age = 0;
	public int delayBeforeCanPickup;
	private int health = 5;
	public float hoverStart = (float)(Math.random() * Math.PI * 2.0D);

	public EntityItem(World var1, double var2, double var4, double var6, ItemStack var8) {
		super(var1);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(var2, var4, var6);
		this.item = var8;
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.motionX = (double)((float)(Math.random() * (double)0.2F - (double)0.1F));
		this.motionY = (double)0.2F;
		this.motionZ = (double)((float)(Math.random() * (double)0.2F - (double)0.1F));
		this.canTriggerWalking = false;
	}

	public final void onUpdate() {
		super.onUpdate();
		if(this.delayBeforeCanPickup > 0) {
			--this.delayBeforeCanPickup;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)0.04F;
		if(this.worldObj.getBlockMaterial((int)this.posX, (int)this.posY, (int)this.posZ) == Material.lava) {
			this.motionY = (double)0.2F;
			this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}

		double var6 = this.posZ;
		double var4 = this.posY;
		double var2 = this.posX;
		int var8 = (int)var2;
		int var9 = (int)var4;
		int var10 = (int)var6;
		double var11 = var2 - (double)var8;
		double var13 = var4 - (double)var9;
		double var15 = var6 - (double)var10;
		if(Block.opaqueCubeLookup[this.worldObj.getBlockId(var8, var9, var10)]) {
			boolean var26 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(var8 - 1, var9, var10)];
			boolean var3 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(var8 + 1, var9, var10)];
			boolean var28 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(var8, var9 - 1, var10)];
			boolean var5 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(var8, var9 + 1, var10)];
			boolean var29 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(var8, var9, var10 - 1)];
			boolean var7 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(var8, var9, var10 + 1)];
			byte var30 = -1;
			double var24 = 9999.0D;
			if(var26 && var11 < 9999.0D) {
				var24 = var11;
				var30 = 0;
			}

			if(var3 && 1.0D - var11 < var24) {
				var24 = 1.0D - var11;
				var30 = 1;
			}

			if(var28 && var13 < var24) {
				var24 = var13;
				var30 = 2;
			}

			if(var5 && 1.0D - var13 < var24) {
				var24 = 1.0D - var13;
				var30 = 3;
			}

			if(var29 && var15 < var24) {
				var24 = var15;
				var30 = 4;
			}

			if(var7 && 1.0D - var15 < var24) {
				var30 = 5;
			}

			float var27 = this.rand.nextFloat() * 0.2F + 0.1F;
			if(var30 == 0) {
				this.motionX = (double)(-var27);
			}

			if(var30 == 1) {
				this.motionX = (double)var27;
			}

			if(var30 == 2) {
				this.motionY = (double)(-var27);
			}

			if(var30 == 3) {
				this.motionY = (double)var27;
			}

			if(var30 == 4) {
				this.motionZ = (double)(-var27);
			}

			if(var30 == 5) {
				this.motionZ = (double)var27;
			}
		}

		boolean var10000 = false;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= (double)0.98F;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)0.98F;
		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
			this.motionY *= -0.5D;
		}

		++this.age2;
		++this.age;
		if(this.age >= 6000) {
			this.setEntityDead();
		}

	}

	protected final void dealFireDamage(int var1) {
		this.attackEntityFrom((Entity)null, 1);
	}

	public final boolean attackEntityFrom(Entity var1, int var2) {
		this.health -= var2;
		if(this.health <= 0) {
			this.setEntityDead();
		}

		return false;
	}
}
