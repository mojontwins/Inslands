package net.minecraft.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.monster.EntityExplodingZombie;

public class RenderExplodingZombie extends RenderZombie {

	public RenderExplodingZombie(ModelBiped modelBase1, float f2) {
		super(modelBase1, f2, "zombie");
	}
	
	private boolean tintRedWithCounter(EntityExplodingZombie entityExplodingZombie) {
		if(entityExplodingZombie.collidingTicks > 0) {
			this.loadTexture("/mob/zombie_red.png");
			float f3 = (float)entityExplodingZombie.collidingTicks / (float)entityExplodingZombie.ticksToExplode;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f3);
			return true;
		} else return false;
	}
	
	protected boolean shouldRenderPass(EntityLiving entityLiving1, int i2, float f3) {
		return this.tintRedWithCounter((EntityExplodingZombie)entityLiving1);
	}
}
