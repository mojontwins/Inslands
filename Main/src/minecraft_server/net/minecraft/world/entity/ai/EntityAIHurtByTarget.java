package net.minecraft.world.entity.ai;

import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.phys.AxisAlignedBB;

public class EntityAIHurtByTarget extends EntityAITarget {
	boolean alertAllies;

	public EntityAIHurtByTarget(EntityLiving entityLiving, boolean alertAllies) {
		super(entityLiving, 16.0F, false);
		this.alertAllies = alertAllies;
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		return this.isSuitableTarget(this.taskOwner.getAITarget(), true);
	}

	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
		if (this.alertAllies) {
			List<Entity> nearbyAllies = this.taskOwner.worldObj.getEntitiesWithinAABB(this.taskOwner.getClass(), AxisAlignedBB.getBoundingBoxFromPool(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D).expand((double) this.targetDistance, 4.0D, (double) this.targetDistance));
			Iterator<Entity> iter = nearbyAllies.iterator();

			while (iter.hasNext()) {
				Entity entity = iter.next();
				EntityLiving ally = (EntityLiving) entity;
				if (this.taskOwner != ally && ally.getAttackTarget() == null) {
					ally.setAttackTarget(this.taskOwner.getAITarget());
				}
			}
		}

		super.startExecuting();
	}
}
