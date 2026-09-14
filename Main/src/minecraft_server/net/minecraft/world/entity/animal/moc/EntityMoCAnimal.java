package net.minecraft.world.entity.animal.moc;

import java.util.Comparator;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Datawatchers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.entity.animal.wild.EntityWolf;
import net.minecraft.world.entity.mob.EntityMob;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemFood;
import net.minecraft.world.item.ItemSeeds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.pathfinder.PathEntity;

public class EntityMoCAnimal extends EntityAnimal {
	protected int temper;
	protected boolean isEntityJumping;
	public EntityLiving roper;

	public EntityMoCAnimal(World world) {
		super(world);
		this.setEdad(0);
		this.setIsAdult(false);
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataWatcher.addObject(Datawatchers.DW_EDAD, 0); // int ageTicks / "edad"
		this.dataWatcher.addObject(Datawatchers.DW_TYPE, 0); // int type
		this.dataWatcher.addObject(Datawatchers.DW_ISADULT, (byte) 0); // isAdult - 0 false 1 true
	}

	public boolean getIsAdult() {
		return this.dataWatcher.getWatchableObjectByte(Datawatchers.DW_ISADULT) == 1;
	}

	public void setIsAdult(boolean adult) {
		this.dataWatcher.updateObject(Datawatchers.DW_ISADULT, (byte) (adult ? 1 : 0));
	}

	public int getEdad() {
		return this.dataWatcher.getWatchableObjectInt(Datawatchers.DW_EDAD);
	}

	public void setEdad(int edad) {
		this.dataWatcher.updateObject(Datawatchers.DW_EDAD, edad);
	}

	public int getType() {
		return this.dataWatcher.getWatchableObjectInt(Datawatchers.DW_TYPE);
	}

	public void setType(int type) {
		this.dataWatcher.updateObject(Datawatchers.DW_TYPE, type);
	}

	public boolean getIsJumping() {
		return this.isEntityJumping;
	}

	public int getTemper() {
		return this.temper;
	}

	public void setTemper(int temper) {
		this.temper = temper;
	}

	public EntityLiving getRoper() {
		return this.roper;
	}

	public void setRoper(EntityLiving roper) {
		this.roper = roper;
	}

	public boolean isItemEdible(Item item) {
		return item instanceof ItemFood || item instanceof ItemSeeds || item == Item.wheat || item == Item.sugar
				|| item == Item.cake || item == Item.egg;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Adult", this.getIsAdult());
		nbttagcompound.setInteger("Edad", this.getEdad());
		nbttagcompound.setInteger("Type", this.getType());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setIsAdult(nbttagcompound.getBoolean("Adult"));
		this.setEdad(nbttagcompound.getInteger("Edad"));
		this.setType(nbttagcompound.getInteger("Type"));
	}

	public void faceLocation(int x, int y, int z, float maxTurn) {
		double dx = x + 0.5D - this.posX;
		double dz = z + 0.5D - this.posZ;
		double dy = y + 0.5D - this.posY;
		double horizontalDistance = MathHelper.sqrt_double(dx * dx + dz * dz);
		float yaw = (float) (Math.atan2(dz, dx) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) (-(Math.atan2(dy, horizontalDistance) * 180.0D / Math.PI));
		this.rotationPitch = -this.updateRotation(this.rotationPitch, pitch, maxTurn);
		this.rotationYaw = this.updateRotation(this.rotationYaw, yaw, maxTurn);
	}

	private float updateRotation(float current, float target, float maxChange) {
		float delta = target - current;
		for (; delta < -180.0F; delta += 360.0F) {
		}
		while (delta >= 180.0F) {
			delta -= 360.0F;
		}
		if (delta > maxChange) {
			delta = maxChange;
		}
		if (delta < -maxChange) {
			delta = -maxChange;
		}
		return current + delta;
	}

	public void getMyOwnPath(Entity entity, float distance) {
		PathEntity path = this.worldObj.getPathEntityToEntity(this, entity, 16F, true, false, false, true);
		if (path != null) {
			this.setPathToEntity(path);
		}
	}

	protected EntityLiving getClosestEntityLiving(Entity origin, double range) {
		double rangeSq = range * range;
		return this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(range, range, range)).stream()
				.filter(entity -> !this.entitiesToIgnore(entity))
				.filter(entity -> range < 0.0D || entity.getDistanceSq(origin.posX, origin.posY, origin.posZ) < rangeSq)
				.filter(entity -> ((EntityLiving) entity).canEntityBeSeen(origin))
				.min(Comparator.comparingDouble(entity -> entity.getDistanceSq(origin.posX, origin.posY, origin.posZ)))
				.map(EntityLiving.class::cast)
				.orElse(null);
	}

	public boolean entitiesToIgnore(Entity entity) {
		return !(entity instanceof EntityLiving) || entity instanceof EntityMob || entity instanceof EntityPlayer
				|| entity instanceof EntityWolf || entity.width >= this.width || entity.height >= this.height;
	}

	public boolean isMyFavoriteFood(ItemStack itemStack) {
		return false;
	}

	public boolean isNotScared() {
		return false;
	}
}
