package net.minecraft.world.entity.mob.humanoid;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.IAngryAtPlayer;
import net.minecraft.world.entity.IMob;
import net.minecraft.world.entity.ISentient;
import net.minecraft.world.entity.mob.EntityMob;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.projectile.EntityArrowWithEffect;
import net.minecraft.world.entity.status.Status;
import net.minecraft.world.entity.status.StatusEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.stats.AchievementList;

public class EntityPirateArcher extends EntityMob implements IMob, ISentient, IAngryAtPlayer {
	private static final ItemStack defaultHeldItem = new ItemStack(Item.bow, 1);
	private boolean angryAtPlayer = false;

	public EntityPirateArcher(World world) {
		super(world);
		this.attackStrength = this.worldObj == null ? 7 : 5 + this.worldObj.difficultySetting;
		this.texture = "/mob/pirate.png";
	}

	public int getFullHealth() {
		return this.worldObj == null ? 27 : 25 + this.worldObj.difficultySetting;
	}

	protected String getLivingSound() {
		return "mob.zombie";
	}

	protected String getHurtSound() {
		return "mob.zombiehurt";
	}

	protected String getDeathSound() {
		return "mob.zombiedeath";
	}

	protected void attackEntity(Entity target, float distance) {
		if(distance < 10.0F) {
			double dx = target.posX - this.posX;
			double dz = target.posZ - this.posZ;
			if(this.attackTime == 0) {
				EntityArrow arrow = this.selectArrow();

				++arrow.posY;
				double eyeOffset = target.posY + (double)target.getEyeHeight() - 0.2D - arrow.posY;
				float lift = MathHelper.sqrt_double(dx * dx + dz * dz) * 0.2F;
				this.worldObj.spawnEntityInWorld(arrow);
				arrow.setArrowHeading(dx, eyeOffset + (double)lift, dz, 0.6F, 12.0F);

				this.worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));

				this.attackTime = 100;
			}

			this.rotationYaw = (float)(Math.atan2(dz, dx) * 180.0D / Math.PI) - 90.0F;
			this.hasAttacked = true;
		}

	}
	
	public EntityArrow selectArrow() {
		return new EntityArrowWithEffect(this.worldObj, this)
				.withStatusEffect(new StatusEffect(Status.statusBlind.id, 100, 1));
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
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("AngryAtPlayer", this.angryAtPlayer);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.angryAtPlayer = compound.getBoolean("AngryAtPlayer");
	}

	protected int getDropItemId() {
		return Item.arrow.shiftedIndex;
	}

	protected void dropFewItems(boolean flag, int i) {
		int count = this.rand.nextInt(3 + i);

		for(int l = 0; l < count; ++l) {
			this.dropItem(Item.arrow.shiftedIndex, 1);
		}

		count = this.rand.nextInt(3 + i);

		for(int l = 0; l < count; ++l) {
			this.dropItem(Item.ingotGold.shiftedIndex, 2);
		}

	}

	@Override
	public boolean getCanSpawnHere() {
		int x = MathHelper.floor_double(this.posX);
		int y = MathHelper.floor_double(this.boundingBox.minY);
		int z = MathHelper.floor_double(this.posZ);
		return this.posY > 60.0D && this.worldObj.isBlockOpaqueCube(x, y, z)
				|| (this.rand.nextInt(4) == 1 && super.getCanSpawnHere());
	}

	@Override
	public ItemStack getHeldItem() {
		return defaultHeldItem;
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
