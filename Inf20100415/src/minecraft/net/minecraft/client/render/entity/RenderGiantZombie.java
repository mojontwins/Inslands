package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.EntityLiving;

import org.lwjgl.opengl.GL11;

public final class RenderGiantZombie extends RenderLiving {
	private float scale = 6.0F;

	public RenderGiantZombie(ModelBase modelBase1, float f2, float f3) {
		super(modelBase1, 3.0F);
	}

	protected final void preRenderCallback(EntityLiving entityLiving1, float f2) {
		GL11.glScalef(this.scale, this.scale, this.scale);
	}
}