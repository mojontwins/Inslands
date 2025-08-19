package net.minecraft.client.render;

import java.util.Comparator;

import net.minecraft.game.entity.Entity;

public final class EntitySorter implements Comparator {
	private Entity entity;

	public EntitySorter(Entity entity1) {
		this.entity = entity1;
	}

	public final int compare(Object object1, Object object2) {
		WorldRenderer worldRenderer10001 = (WorldRenderer)object1;
		WorldRenderer worldRenderer3 = (WorldRenderer)object2;
		WorldRenderer worldRenderer4 = worldRenderer10001;
		return worldRenderer4.distanceToEntitySquared(this.entity) < worldRenderer3.distanceToEntitySquared(this.entity) ? -1 : 1;
	}
}