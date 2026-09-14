package net.minecraft.world.entity.mob.humanoid;

import com.mojang.nbt.NBTTagCompound;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.IAngryAtPlayer;
import net.minecraft.world.entity.ISentient;
import net.minecraft.world.entity.mob.EntityHumanBase;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.World;
import net.minecraft.world.stats.AchievementList;

public class EntityPirate extends EntityHumanBase implements ISentient, IAngryAtPlayer {
	private static final Set<Integer> SPAWN_BLOCKS = new HashSet<Integer>();

	static {
		SPAWN_BLOCKS.add(Block.planks.blockID);
		SPAWN_BLOCKS.add(Block.stone.blockID);
		SPAWN_BLOCKS.add(Block.wood.blockID);
		SPAWN_BLOCKS.add(Block.stoneBricks.blockID);
		SPAWN_BLOCKS.add(Block.dirt.blockID);
		SPAWN_BLOCKS.add(Block.grass.blockID);
	}

	private boolean angryAtPlayer = false;
	
	public EntityPirate(World world) {
		super(world);
		this.armorIndex = 4;
		this.attackStrength = 8;
		this.texture = "/mob/pirate.png";
	}

	public int getFullHealth() {
		return 25 + this.armorIndex * 4;
	}

	@Override
	public void onLivingUpdate() {
		if(this.rand.nextInt(1000) == 1) {
			this.spawnPuffs("smoke");
		}

		super.onLivingUpdate();
	}

	@Override
	public boolean attackEntityFrom(Entity source, int damage) {
		if(this.rand.nextInt(10) == 0) {
			this.spawnPuffs("splash");
		}
		
		if(source instanceof EntityPlayer) {
			this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(32.0D, 32.0D, 32.0D))
				.stream()
				.filter(e -> e instanceof IAngryAtPlayer)
				.forEach(e -> ((IAngryAtPlayer)e).setAngryAtPlayer(true));
			this.setAngryAtPlayer(true);
		}

		return super.attackEntityFrom(source, damage);
	}

	private void spawnPuffs(String particle) {
		for(int r = 0; r < 10; ++r) {
			this.worldObj.spawnParticle(particle, this.posX + (double)this.rand.nextFloat() - 0.5D, this.posY + 1.0D + (double)this.rand.nextFloat(), this.posZ + (double)this.rand.nextFloat() - 0.5D, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean getCanSpawnHere() {
		int x = MathHelper.floor_double(this.posX);
		int y = MathHelper.floor_double(this.boundingBox.minY);
		int z = MathHelper.floor_double(this.posZ);
		return this.posY > 60.0D && SPAWN_BLOCKS.contains(this.worldObj.getBlockID(x, y, z))
				|| (this.rand.nextInt(4) == 1 && super.getCanSpawnHere());
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("AngryAtPlayer", this.angryAtPlayer);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.angryAtPlayer = compound.getBoolean("AngryAtPlayer");
		this.configureAttributesBasedOnLevel();
	}

	@Override
	public boolean isAngryAtPlayer() {
		return this.angryAtPlayer;
	}

	@Override
	public void setAngryAtPlayer(boolean angry) {
		this.angryAtPlayer = angry;
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
			player.triggerAchievement(AchievementList.pirateKill);
		}
		
		super.onDeath(killer);
	}
}
