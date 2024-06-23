package com.benimatic.twilightforest;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderBiped;

public class RenderTFWraith extends RenderBiped {
	
	public RenderTFWraith(ModelBiped modelbiped, float f) {
		super(modelbiped, f);
		this.setRenderPassModel(new ModelTFWraith());
	}

	protected boolean shouldRenderPass(EntityLiving entityLiving1, int i2, float f) {
		if(i2 == 2) {
			this.loadTexture("/com/benimatic/twilightforest/textures/ghost.png");
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
			return true;
		} else {
			return false;
		}
	}
	
}
