package net.minecraft.client.render;

import java.util.Comparator;

import net.minecraft.game.entity.player.EntityPlayer;

public final class RenderSorter implements Comparator {
	private EntityPlayer entityPlayer;

	public RenderSorter(EntityPlayer entityPlayer1) {
		this.entityPlayer = entityPlayer1;
	}

	public final int compare(Object object1, Object object2) {
		WorldRenderer worldRenderer10001 = (WorldRenderer)object1;
		WorldRenderer worldRenderer3 = (WorldRenderer)object2;
		WorldRenderer worldRenderer6 = worldRenderer10001;
		boolean z4 = worldRenderer6.isInFrustum;
		boolean z5 = worldRenderer3.isInFrustum;
		return z4 && !z5 ? 1 : ((!z5 || z4) && worldRenderer6.distanceToEntitySquared(this.entityPlayer) < worldRenderer3.distanceToEntitySquared(this.entityPlayer) ? 1 : -1);
	}
}