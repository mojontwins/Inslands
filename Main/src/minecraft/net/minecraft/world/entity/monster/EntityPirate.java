package net.minecraft.world.entity.monster;

import java.util.List;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.src.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.IMobWithLevel;
import net.minecraft.world.entity.ISentient;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.stats.AchievementList;

public class EntityPirate extends EntityHumanBase implements IMobWithLevel, ISentient {
	protected boolean angryAtPlayer = false;
	
	public EntityPirate(World world) {
		super(world);
		this.armorIndex = 4;
		this.attackStrength = 8;
		this.texture = "/mob/pirate.png";
	}

	public void configureAttributesBasedOnLevel() {
		super.configureAttributesBasedOnLevel();
	}
	
	public int getFullHealth() {
		return 25 + this.armorIndex * 4;
	}

	@Override
	public void onLivingUpdate() {
		if(this.rand.nextInt(1000) == 1) {
			for(int r = 0; r < 10; ++r) {
				this.worldObj.spawnParticle("smoke", this.posX + (double)this.rand.nextFloat() - 0.5D, this.posY + 1.0D + (double)this.rand.nextFloat(), this.posZ + (double)this.rand.nextFloat() - 0.5D, 0.0D, 0.0D, 0.0D);
			}
		}

		super.onLivingUpdate();
	}

	@Override
	public boolean attackEntityFrom(Entity entity, int i) {
		if(this.rand.nextInt(10) == 0) {
			for(int r = 0; r < 10; ++r) {
				this.worldObj.spawnParticle("splash", this.posX + (double)this.rand.nextFloat() - 0.5D, this.posY + 1.0D + (double)this.rand.nextFloat(), this.posZ + (double)this.rand.nextFloat() - 0.5D, 0.0D, 0.0D, 0.0D);
			}
		}
		
		if(entity instanceof EntityPlayer) {
			List<Entity> list4 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(32.0D, 32.0D, 32.0D));

			for(int i5 = 0; i5 < list4.size(); ++i5) {
				Entity entity6 = (Entity)list4.get(i5);
				if(entity6 instanceof EntityPirate) {
					EntityPirate entityPirate = (EntityPirate)entity6;
					entityPirate.angryAtPlayer = true;
				} else if(entity6 instanceof EntityPirateArcher) {
					EntityPirateArcher entityPirate = (EntityPirateArcher)entity6;
					entityPirate.angryAtPlayer = true;
				} else if(entity6 instanceof EntityPirateBoss) {
					EntityPirateBoss entityPirate = (EntityPirateBoss)entity6;
					entityPirate.angryAtPlayer = true;
				}
			}

			this.angryAtPlayer = true;
		}

		return super.attackEntityFrom(entity, i);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		return super.attackEntityAsMob(entity);
	}

	@Override
	public boolean getCanSpawnHere() {
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.boundingBox.minY);
		int k = MathHelper.floor_double(this.posZ);
		return this.posY > 60.0D && 
				(this.worldObj.getBlockId(i, j, k) == Block.planks.blockID || this.worldObj.getBlockId(i, j, k) == Block.stone.blockID || this.worldObj.getBlockId(i, j, k) == Block.wood.blockID || this.worldObj.getBlockId(i, j, k) == Block.stoneBricks.blockID || this.worldObj.getBlockId(i, j, k) == Block.dirt.blockID || this.worldObj.getBlockId(i, j, k) == Block.grass.blockID) ? true : (this.rand.nextInt(4) == 1 ? super.getCanSpawnHere() : false);
	}

	@Override
	protected String getLivingSound() {
		return "mob.zombie";
	}

	@Override
	protected String getHurtSound() {
		return "mob.zombiehurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.zombiedeath";
	}

	@Override
	public void dropFewItems() {
		int j = this.rand.nextInt(3);

		int l;
		for(l = 0; l < j; ++l) {
			this.dropItem(Item.appleRed.shiftedIndex, 1);
		}

		if(this.getLvl() == 2) {
			j = this.rand.nextInt(3);

			for(l = 0; l < j; ++l) {
				this.dropItem(Item.ingotIron.shiftedIndex, 1);
			}
		}

		if(this.getLvl() == 3) {
			j = this.rand.nextInt(3);

			for(l = 0; l < j; ++l) {
				this.dropItem(Item.ingotGold.shiftedIndex, 1);
			}
		}

		if(this.getLvl() == 4) {
			j = this.rand.nextInt(3);

			for(l = 0; l < j; ++l) {
				this.dropItem(Item.diamond.shiftedIndex, 1);
			}
		}

	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("AngryAtPlayer", this.angryAtPlayer);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.angryAtPlayer = nbttagcompound.getBoolean("AntryAtPlayer");
		this.configureAttributesBasedOnLevel();
	}

	@Override
	protected int getDropItemId() {
		return Item.feather.shiftedIndex;
	}
	
	@Override
	public ItemStack getHeldItem() {
		switch(this.getLvl()) {
		case 0:
			return new ItemStack(Item.axeWood, 1);
		case 1:
			return new ItemStack(Item.axeSteel, 1);
		case 2:
			return new ItemStack(Item.axeSteel, 1);
		case 3:
			return new ItemStack(Item.axeGold, 1);
		case 4:
			return new ItemStack(Item.axeDiamond, 1);
		default:
			return new ItemStack(Item.axeGold, 1);
		}
	}
	
	@Override
	protected Entity findPlayerToAttack() {
		// New logic - if player is disguised and pirates are not angry at player, don't attack.
		
		EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		return 
				entityPlayer1 != null && 
				!entityPlayer1.isCreative && 
				this.canEntityBeSeen(entityPlayer1) && 
				(!entityPlayer1.dressedAsAPirate() || this.angryAtPlayer) ? 
						entityPlayer1 
					: 
						null;
	}

	@Override
	public void onDeath(Entity entity) {
		if(entity instanceof EntityPlayer) {
			EntityPlayer entityPlayer = (EntityPlayer) entity;
			entityPlayer.triggerAchievement(AchievementList.pirateKill);
		}
		
		super.onDeath(entity);
	}
}
