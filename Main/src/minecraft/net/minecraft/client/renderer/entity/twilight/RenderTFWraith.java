package net.minecraft.client.renderer.entity.twilight;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.twilight.ModelTFWraith;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.world.entity.EntityLiving;

public class RenderTFWraith extends RenderBiped {
	
	public RenderTFWraith(ModelBiped modelbiped, float f) {
		super(modelbiped, f);
		this.setRenderPassModel(new ModelTFWraith());
	}

	protected boolean shouldRenderPass(EntityLiving entityLiving1, int i2, float f) {
		if(i2 == 2) {
			this.loadTexture("/mob/wraith.png");
			GL11.glEnable(GL11.GL_BLEND);
			//GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			int i5 = 61680;
			int i6 = i5 % 65536;
			int i7 = i5 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i6 / 1.0F, (float)i7 / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
			return true;
		} else {
			return false;
		}
	}
	
}
