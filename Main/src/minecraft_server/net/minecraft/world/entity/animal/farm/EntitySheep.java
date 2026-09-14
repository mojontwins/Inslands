package net.minecraft.world.entity.animal.farm;

import com.mojang.nbt.NBTTagCompound;
import java.util.Random;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;

public class EntitySheep extends EntityAnimal {
	public static final float[][] fleeceColorTable = new float[][]{{1.0F, 1.0F, 1.0F}, {0.95F, 0.7F, 0.2F}, {0.9F, 0.5F, 0.85F}, {0.6F, 0.7F, 0.95F}, {0.9F, 0.9F, 0.2F}, {0.5F, 0.8F, 0.1F}, {0.95F, 0.7F, 0.8F}, {0.3F, 0.3F, 0.3F}, {0.6F, 0.6F, 0.6F}, {0.3F, 0.6F, 0.7F}, {0.7F, 0.4F, 0.9F}, {0.2F, 0.4F, 0.8F}, {0.5F, 0.4F, 0.3F}, {0.4F, 0.5F, 0.2F}, {0.8F, 0.3F, 0.3F}, {0.1F, 0.1F, 0.1F}};

	public EntitySheep(World world) {
		super(world);
		this.texture = "/mob/sheep.png";
		this.setSize(0.9F, 1.3F);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, (byte) 0);
	}

	public boolean attackEntityFrom(Entity source, int damage) {
		// Reinstated for b1.6.6 - punch sheep to get cloth. {
		if (!this.worldObj.isRemote && !this.getSheared() && source instanceof EntityLiving) {
			this.setSheared(true);
			int count = 1 + this.rand.nextInt(3);

			for (int i = 0; i < count; ++i) {
				EntityItem item = this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 1.0F);
				item.motionY += (double) (this.rand.nextFloat() * 0.05F);
				item.motionX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				item.motionZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
			}
		}
		// }

		return super.attackEntityFrom(source, damage);
	}

	protected void dropFewItems() {
		if (!this.getSheared()) {
			this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 0.0F);
		}
	}

	protected int getDropItemId() {
		return Block.cloth.blockID;
	}

	public boolean interact(EntityPlayer player) {
		if (super.interact(player)) {
			return false;
		}

		ItemStack heldItem = player.inventory.getCurrentItem();
		if (heldItem != null && heldItem.itemID == Item.shears.shiftedIndex && !this.getSheared()) {
			if (!this.worldObj.isRemote) {
				this.setSheared(true);
				int count = 2 + this.rand.nextInt(3);

				for (int i = 0; i < count; ++i) {
					EntityItem item = this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 1.0F);
					item.motionY += (double) (this.rand.nextFloat() * 0.05F);
					item.motionX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
					item.motionZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				}
			}

			heldItem.damageItem(1, player);
		}

		return false;
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Sheared", this.getSheared());
		nbttagcompound.setByte("Color", (byte) this.getFleeceColor());
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setSheared(nbttagcompound.getBoolean("Sheared"));
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

	public void setFleeceColor(int fleeceColor) {
		byte dataByte = this.dataWatcher.getWatchableObjectByte(16);
		this.dataWatcher.updateObject(16, (byte) (dataByte & 240 | fleeceColor & 15));
	}

	public boolean getSheared() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 16) != 0;
	}

	public void setSheared(boolean sheared) {
		byte dataByte = this.dataWatcher.getWatchableObjectByte(16);
		if (sheared) {
			this.dataWatcher.updateObject(16, (byte) (dataByte | 16));
		} else {
			this.dataWatcher.updateObject(16, (byte) (dataByte & -17));
		}
	}

	public static int getRandomFleeceColorForReal(Random random) {
		return random.nextInt(16);
	}

	public static int getRandomFleeceColor(Random random) {
		int color = random.nextInt(100);
		return color < 5 ? 15 : (color < 10 ? 7 : (color < 15 ? 8 : (color < 18 ? 12 : (random.nextInt(500) == 0 ? 6 : 0))));
	}

	public void onLivingUpdate() {
		// Eat grass / tall grass to regrow wool
		super.onLivingUpdate();

		if (this.rand.nextInt(1000) == 0 && !this.worldObj.isRemote) {
			int x = MathHelper.floor_double(this.posX);
			int y = MathHelper.floor_double(this.posY);
			int z = MathHelper.floor_double(this.posZ);
			if (this.worldObj.getBlockID(x, y, z) == Block.tallGrass.blockID) {
				this.worldObj.playAuxSFX(2001, x, y, z, Block.tallGrass.blockID + 4096);
				this.worldObj.setBlockWithNotify(x, y, z, 0);
				this.regrowWool();
			} else if (this.worldObj.getBlockID(x, y - 1, z) == Block.grass.blockID) {
				this.worldObj.playAuxSFX(2001, x, y - 1, z, Block.grass.blockID);
				this.worldObj.setBlockWithNotify(x, y - 1, z, Block.dirt.blockID);
				this.regrowWool();
			}
		}
	}

	public void regrowWool() {
		this.health = this.getFullHealth();
		this.setSheared(false);
	}
}
