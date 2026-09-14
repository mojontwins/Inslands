package net.minecraft.world.entity.mob.boss;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.IAngryAtPlayer;
import net.minecraft.world.entity.IMob;
import net.minecraft.world.entity.mob.EntityMob;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.World;
import net.minecraft.world.stats.AchievementList;

public class EntityPirateBoss extends EntityMob implements IMob, IAngryAtPlayer {
	private int invisibleCooldown = 10;
	private boolean angryAtPlayer = false;

	public EntityPirateBoss(World world) {
		super(world);
		this.texture = "/mob/pirateBoss.png";
		this.attackStrength = 10;
		this.health = this.getFullHealth();
	}
	
	protected void entityInit() {
		super.entityInit();
		
		// Store "invisible" here to make this work in SMP
		this.dataWatcher.addObject(19, Byte.valueOf((byte) 0));
	}
	
	public void setInvisible(boolean invisible) {
		this.dataWatcher.updateObject(19, (byte) (invisible ? 1 : 0));
	}

	public boolean isInvisible() {
		return this.dataWatcher.getWatchableObjectByte(19) == 1;
	}

	@Override
	protected void despawnEntity() {
		if(this.isDead) {
			super.despawnEntity();
		}
	}

	@Override
	public String getEntityTexture() {
		return this.isInvisible() ? "/mob/invisible.png" : "/mob/pirateBoss.png";
	}

	@Override
	protected void attackEntity(Entity target, float distance) {
		super.attackEntity(target, distance);
		if(this.attackTime <= 0 && distance < 2.0F && target.boundingBox.maxY > this.boundingBox.minY && target.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			this.attackEntityAsMob(target);
		}

		if(this.health < 200 && !this.isInvisible() && this.invisibleCooldown < 0) {
			this.setInvisible(true);

			for(int n = 0; n < 3; ++n) {
				this.worldObj.spawnParticle("spell", this.posX + (double)this.rand.nextFloat() - 0.5D, this.posY + 1.0D, this.posZ + (double)this.rand.nextFloat() - 0.5D, 1.0D, 1.0D, 1.0D);
			}

			this.castTeleport(target);
		}

		--this.invisibleCooldown;
	}

	@Override
	public boolean isAngryAtPlayer() {
		return this.angryAtPlayer;
	}

	@Override
	public void setAngryAtPlayer(boolean angry) {
		this.angryAtPlayer = angry;
	}

	public int getFullHealth() {
		return 100;
	}

	@Override
	public boolean attackEntityFrom(Entity source, int damage) {
		if(this.isInvisible()) {
			this.setInvisible(false);
			this.invisibleCooldown = 30;
		}

		return super.attackEntityFrom(source, damage);
	}

	private boolean castTeleport(Entity target) {
		double offsetX, offsetZ;
		do {
			offsetX = (this.rand.nextDouble() - 0.5) * 16.0D;
			offsetZ = (this.rand.nextDouble() - 0.5) * 16.0D;
		} while((offsetX >= -4 && offsetX <= 4) || (offsetZ >= -4 && offsetZ <= 4));
		
		double destX = target.posX + offsetX;
		double destZ = target.posZ + offsetZ;
		double destY = target.posY;
		
		// Currently here
		double originX = this.posX;
		double originY = this.posY;
		double originZ = this.posZ;
		
		boolean hasTeleported = false;
		
		int x = MathHelper.floor_double(destX);
		int y = MathHelper.floor_double(destY);
		int z = MathHelper.floor_double(destZ);

		if(this.worldObj.blockExists(x, y, z)) {
			boolean foundSolid = false;

			while(!foundSolid && y > 0) {
				Block block = Block.blocksList[this.worldObj.getBlockID(x, y - 1, z)];
				if(block != null && block.blockMaterial.getIsSolid()) {
					foundSolid = true;
				} else {
					--this.posY;
					--y;
				}
			}

			if(foundSolid) {
				this.setLocationAndAngles(destX, destY, destZ, this.worldObj.rand.nextFloat() * 360.0F, 0.0F);
				
				if(this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox)) {
					double divisor = 2.0D;
					
					for(int i = 0; i < 10; ++i) {
						this.worldObj.spawnParticle("largesmoke", originX + (double)this.rand.nextFloat() - 0.5D, originY, originZ + (double)this.rand.nextFloat() - 0.5D, (destX - originX) / divisor, (destY - originY) / divisor , (destZ - originZ) / divisor);
					}

					hasTeleported = true;
				}
			}
		}

		if(!hasTeleported) {
			this.setLocationAndAngles(originX, originY, originZ, this.worldObj.rand.nextFloat() * 360.0F, 0.0F);
			return false;
		} else {
			this.worldObj.playSoundEffect(originX, originY, originZ, "random.breath", 1.0F, 1.0F);
			this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.0F);
			return true;
		}
	}

	@Override
	public void dropFewItems() {
		super.dropFewItems();
		if(this.rand.nextInt(5) == 0 || this.rand.nextInt(2) > 0) {
			this.dropItem(Item.diamond.shiftedIndex, 2);
		}
	}

	@Override
	public ItemStack getHeldItem() {
		return this.isInvisible() ? new ItemStack(Item.knifeDiamond, 1) : new ItemStack(Item.swordDiamond, 1);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("AngryAtPlayer", this.angryAtPlayer);
		compound.setBoolean("Invisible", this.isInvisible());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.angryAtPlayer = compound.getBoolean("AngryAtPlayer");
		this.setInvisible(compound.getBoolean("Invisible"));
	}
	
	@Override
	protected Entity findPlayerToAttack() {
		// New logic - if player is disguised and pirates are not angry at player, don't attack.
		
		EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		return 
				player != null && 
				!player.isCreative && 
				this.canEntityBeSeen(player) && 
				(!player.dressedAsAPirate() || this.angryAtPlayer) ? 
						player 
					: 
						null;
	}
	
	@Override
	public void onDeath(Entity killer) {
		if(killer instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) killer;
			player.triggerAchievement(AchievementList.pirateKing);
			
			if(!this.worldObj.isRemote) {
				this.dropItem(Item.pirateSigil.shiftedIndex, 1);
			}
		}
		
		super.onDeath(killer);
	}
}
