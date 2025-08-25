package net.minecraft.world.entity.animal;

import java.util.Random;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockCloth;

public class EntitySheepuff extends EntityAetherAnimal {
	public static final float[][] fleeceColorTable = new float[][]{{1.0F, 1.0F, 1.0F}, {0.975F, 0.85F, 0.6F}, {0.95F, 0.75F, 0.925F}, {0.8F, 0.85F, 0.975F}, {0.95F, 0.95F, 0.6F}, {0.75F, 0.9F, 0.55F}, {0.975F, 0.85F, 0.9F}, {0.65F, 0.65F, 0.65F}, {0.8F, 0.8F, 0.8F}, {0.65F, 0.8F, 0.85F}, {0.85F, 0.7F, 0.95F}, {0.6F, 0.7F, 0.9F}, {0.75F, 0.7F, 0.65F}, {0.7F, 0.75F, 0.6F}, {0.9F, 0.65F, 0.65F}, {0.55F, 0.55F, 0.55F}};
	private int amountEaten;

	public EntitySheepuff(World world) {
		super(world);
		this.texture = "/mob/sheepuff.png";
		this.setSize(0.9F, 1.3F);
		this.setFleeceColor(getRandomFleeceColor(this.rand));
		this.amountEaten = 0;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
	}

	protected void dropFewItems() {
		if(!this.getSheared()) {
			this.entityDropItem(new ItemStack(Block.cloth.blockID, 1 + this.rand.nextInt(2), this.getFleeceColor()), 0.0F);
		}

	}

	public boolean interact(EntityPlayer entityplayer) {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		int colour;
		if(itemstack != null && itemstack.itemID == Item.shears.shiftedIndex && !this.getSheared()) {
			if(!this.worldObj.isRemote) {
				int j;
				EntityItem entityitem;
				if(this.getPuffed()) {
					this.setPuffed(false);
					colour = 2 + this.rand.nextInt(3);

					for(j = 0; j < colour; ++j) {
						entityitem = this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 1.0F);
						entityitem.motionY += (double)(this.rand.nextFloat() * 0.05F);
						entityitem.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
						entityitem.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
					}
				} else {
					this.setSheared(true);
					colour = 2 + this.rand.nextInt(3);

					for(j = 0; j < colour; ++j) {
						entityitem = this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 1.0F);
						entityitem.motionY += (double)(this.rand.nextFloat() * 0.05F);
						entityitem.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
						entityitem.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
					}
				}
			}

			itemstack.damageItem(1, entityplayer);
		}

		if(itemstack != null && itemstack.itemID == Item.dyePowder.shiftedIndex && !this.getSheared()) {
			colour = BlockCloth.getDyeFromBlock(itemstack.getItemDamage());
			if(this.getFleeceColor() != colour) {
				if(this.getPuffed() && itemstack.stackSize >= 2) {
					this.setFleeceColor(colour);
					itemstack.stackSize -= 2;
				} else if(!this.getPuffed()) {
					this.setFleeceColor(colour);
					--itemstack.stackSize;
				}
			}
		}

		return false;
	}

	protected void jump() {
		if(this.getPuffed()) {
			this.motionY = 1.8D;
			this.motionX += this.rand.nextGaussian() * 0.5D;
			this.motionZ += this.rand.nextGaussian() * 0.5D;
		} else {
			this.motionY = (double)0.42F;
		}

	}

	public void onUpdate() {
		super.onUpdate();
		if(this.getPuffed()) {
			this.fallDistance = 0.0F;
			if(this.motionY < -0.05D) {
				this.motionY = -0.05D;
			}
		}

		if(this.rand.nextInt(100) == 0) {
			int x = MathHelper.floor_double(this.posX);
			int y = MathHelper.floor_double(this.posY);
			int z = MathHelper.floor_double(this.posZ);
			if(this.worldObj.getblockID(x, y - 1, z) == Block.grass.blockID) {
				this.worldObj.setBlockWithNotify(x, y - 1, z, Block.dirt.blockID);
				++this.amountEaten;
			}
		}

		if(this.amountEaten == 5 && !this.getSheared() && !this.getPuffed()) {
			this.setPuffed(true);
			this.amountEaten = 0;
		}

		if(this.amountEaten == 10 && this.getSheared() && !this.getPuffed()) {
			this.setSheared(false);
			this.setFleeceColor(0);
			this.amountEaten = 0;
		}

	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Sheared", this.getSheared());
		nbttagcompound.setBoolean("Puffed", this.getPuffed());
		nbttagcompound.setByte("Color", (byte)this.getFleeceColor());
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setSheared(nbttagcompound.getBoolean("Sheared"));
		this.setPuffed(nbttagcompound.getBoolean("Puffed"));
		this.setFleeceColor(nbttagcompound.getByte("Color"));
	}

	protected String getLivingSound() {
		return "mob.sheep";
	}

	protected String getHurtSound() {
		return "mob.sheep";
	}

	protected String getDeathSound() {
		return "mob.sheep";
	}

	public int getFleeceColor() {
		return this.dataWatcher.getWatchableObjectByte(16) & 15;
	}

	public void setFleeceColor(int i) {
		byte byte0 = this.dataWatcher.getWatchableObjectByte(16);
		this.dataWatcher.updateObject(16, (byte)(byte0 & 240 | i & 15));
	}

	public boolean getSheared() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 16) != 0;
	}

	public void setSheared(boolean flag) {
		byte byte0 = this.dataWatcher.getWatchableObjectByte(16);
		if(flag) {
			this.dataWatcher.updateObject(16, (byte)(byte0 | 16));
		} else {
			this.dataWatcher.updateObject(16, (byte)(byte0 & -17));
		}

	}

	public boolean getPuffed() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 32) != 0;
	}

	public void setPuffed(boolean flag) {
		byte byte0 = this.dataWatcher.getWatchableObjectByte(16);
		if(flag) {
			this.dataWatcher.updateObject(16, (byte)(byte0 | 32));
		} else {
			this.dataWatcher.updateObject(16, (byte)(byte0 & -33));
		}

	}

	public static int getRandomFleeceColor(Random random) {
		int i = random.nextInt(100);
		return i < 5 ? 3 : (i < 10 ? 9 : (i < 15 ? 5 : (i < 18 ? 6 : (random.nextInt(500) != 0 ? 0 : 10))));
	}
}
