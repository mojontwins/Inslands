package net.minecraft.game.entity.misc;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

import util.MathHelper;

public class EntityItem extends Entity {
	public ItemStack item;
	private int unusedInt;
	public int age = 0;
	public int delayBeforeCanPickup;
	private int health = 5;
	public float hoverStart = (float)(Math.random() * Math.PI * 2.0D);

	public EntityItem(World world1, double d2, double d4, double d6, ItemStack itemStack8) {
		super(world1);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(d2, d4, d6);
		this.item = itemStack8;
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.motionX = (double)((float)(Math.random() * (double)0.2F - (double)0.1F));
		this.motionY = (double)0.2F;
		this.motionZ = (double)((float)(Math.random() * (double)0.2F - (double)0.1F));
		this.entityWalks = false;
	}

	public EntityItem(World world1) {
		super(world1);
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
		if(this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) == Material.lava) {
			this.motionY = (double)0.2F;
			this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}

		double d6 = this.posZ;
		double d4 = this.posY;
		double d2 = this.posX;
		int i8 = MathHelper.floor_double(d2);
		int i9 = MathHelper.floor_double(d4);
		int i10 = MathHelper.floor_double(d6);
		double d11 = d2 - (double)i8;
		double d13 = d4 - (double)i9;
		double d15 = d6 - (double)i10;
		if(Block.opaqueCubeLookup[this.worldObj.getBlockId(i8, i9, i10)]) {
			boolean z26 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i8 - 1, i9, i10)];
			boolean z3 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i8 + 1, i9, i10)];
			boolean z28 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i8, i9 - 1, i10)];
			boolean z5 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i8, i9 + 1, i10)];
			boolean z29 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i8, i9, i10 - 1)];
			boolean z7 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i8, i9, i10 + 1)];
			byte b30 = -1;
			double d24 = 9999.0D;
			if(z26 && d11 < 9999.0D) {
				d24 = d11;
				b30 = 0;
			}

			if(z3 && 1.0D - d11 < d24) {
				d24 = 1.0D - d11;
				b30 = 1;
			}

			if(z28 && d13 < d24) {
				d24 = d13;
				b30 = 2;
			}

			if(z5 && 1.0D - d13 < d24) {
				d24 = 1.0D - d13;
				b30 = 3;
			}

			if(z29 && d15 < d24) {
				d24 = d15;
				b30 = 4;
			}

			if(z7 && 1.0D - d15 < d24) {
				b30 = 5;
			}

			float f27 = this.rand.nextFloat() * 0.2F + 0.1F;
			if(b30 == 0) {
				this.motionX = (double)(-f27);
			}

			if(b30 == 1) {
				this.motionX = (double)f27;
			}

			if(b30 == 2) {
				this.motionY = (double)(-f27);
			}

			if(b30 == 3) {
				this.motionY = (double)f27;
			}

			if(b30 == 4) {
				this.motionZ = (double)(-f27);
			}

			if(b30 == 5) {
				this.motionZ = (double)f27;
			}
		}

		boolean z10000 = false;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= (double)0.98F;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)0.98F;
		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
			this.motionY *= -0.5D;
		}

		++this.unusedInt;
		++this.age;
		if(this.age >= 6000) {
			super.isDead = true;
		}

	}

	protected final void dealFireDamage(int i1) {
		this.attackEntityFrom((Entity)null, 1);
	}

	public final boolean attackEntityFrom(Entity entity1, int i2) {
		this.health -= i2;
		if(this.health <= 0) {
			super.isDead = true;
		}

		return false;
	}

	public final void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setShort("Health", (byte)this.health);
		nBTTagCompound1.setShort("Age", (short)this.age);
		nBTTagCompound1.setCompoundTag("Item", this.item.writeToNBT(new NBTTagCompound()));
	}

	public final void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		this.health = nBTTagCompound1.getShort("Health") & 255;
		this.age = nBTTagCompound1.getShort("Age");
		nBTTagCompound1 = nBTTagCompound1.getCompoundTag("Item");
		this.item = new ItemStack(nBTTagCompound1);
	}

	public final void onCollideWithPlayer(EntityPlayer entityPlayer1) {
		if(this.delayBeforeCanPickup == 0 && entityPlayer1.inventory.storePartialItemStack(this.item)) {
			this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			entityPlayer1.onItemPickup(this);
			super.isDead = true;
		}

	}
}