package net.minecraft.world.entity.helper;

import java.util.ArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;

public class EntitySenses {
	EntityLiving entityObj;
	ArrayList<Entity> canSeeCachePositive = new ArrayList<Entity>();
	ArrayList<Entity> canSeeCacheNegative = new ArrayList<Entity>();

	public EntitySenses(EntityLiving entityLiving) {
		this.entityObj = entityLiving;
	}

	public void clearSensingCache() {
		this.canSeeCachePositive.clear();
		this.canSeeCacheNegative.clear();
	}

	public boolean canSee(Entity target) {
		if (this.canSeeCachePositive.contains(target)) {
			return true;
		} else if (this.canSeeCacheNegative.contains(target)) {
			return false;
		} else {
			boolean canSee = this.entityObj.canEntityBeSeen(target);
			if (canSee) {
				this.canSeeCachePositive.add(target);
			} else {
				this.canSeeCacheNegative.add(target);
			}

			return canSee;
		}
	}
}
