package net.minecraft.world.entity.ai;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityPlayer;

public class EntityAINearestAttackableTarget extends EntityAITarget {
	EntityLiving targetEntity;
	Class<?> targetClass;
	int targetChance;
	private EntityAINearestAttackableTargetSorter sorter;

	public EntityAINearestAttackableTarget(EntityLiving entityLiving, Class<?> targetClass, float targetDistance, int targetChance, boolean shouldCheckSight) {
		this(entityLiving, targetClass, targetDistance, targetChance, shouldCheckSight, false);
	}

	public EntityAINearestAttackableTarget(EntityLiving entityLiving, Class<?> targetClass, float targetDistance, int targetChance, boolean shouldCheckSight, boolean needsPathCheck) {
		super(entityLiving, targetDistance, shouldCheckSight, needsPathCheck);
		this.targetClass = targetClass;
		this.targetDistance = targetDistance;
		this.targetChance = targetChance;
		this.sorter = new EntityAINearestAttackableTargetSorter(this, entityLiving);
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
			return false;
		} else {
			if (this.targetClass == EntityPlayer.class) {
				EntityPlayer player = this.taskOwner.worldObj.getClosestPlayerToEntity(this.taskOwner, (double) this.targetDistance);
				if (this.isSuitableTarget(player, false)) {
					this.targetEntity = player;
					return true;
				}
			} else {
				List<?> candidates = this.taskOwner.worldObj.getEntitiesWithinAABB(this.targetClass, this.taskOwner.boundingBox.expand((double) this.targetDistance, 4.0D, (double) this.targetDistance));
				Collections.sort(candidates, this.sorter);
				Iterator<?> iter = candidates.iterator();

				while (iter.hasNext()) {
					Entity entity = (Entity) iter.next();
					EntityLiving candidate = (EntityLiving) entity;
					if (this.isSuitableTarget(candidate, false)) {
						this.targetEntity = candidate;
						return true;
					}
				}
			}

			return false;
		}
	}

	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.targetEntity);
		super.startExecuting();
	}
}
