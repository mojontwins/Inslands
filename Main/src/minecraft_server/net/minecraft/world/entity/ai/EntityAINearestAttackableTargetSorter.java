package net.minecraft.world.entity.ai;

import java.util.Comparator;
import net.minecraft.world.entity.Entity;

public class EntityAINearestAttackableTargetSorter implements Comparator<Object> {
	private Entity theEntity;
	final EntityAINearestAttackableTarget parent;

	public EntityAINearestAttackableTargetSorter(EntityAINearestAttackableTarget parent, Entity theEntity) {
		this.parent = parent;
		this.theEntity = theEntity;
	}

	public int compareByDistance(Entity entity1, Entity entity2) {
		double d1 = this.theEntity.getDistanceSqToEntity(entity1);
		double d2 = this.theEntity.getDistanceSqToEntity(entity2);
		return d1 < d2 ? -1 : (d1 > d2 ? 1 : 0);
	}

	public int compare(Object object1, Object object2) {
		return this.compareByDistance((Entity) object1, (Entity) object2);
	}
}
