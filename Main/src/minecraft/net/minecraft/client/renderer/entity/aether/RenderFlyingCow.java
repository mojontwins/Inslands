package net.minecraft.client.renderer.entity.aether;

import com.misc.aether.EntityFlyingCow;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.aether.ModelFlyingCow2;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.world.entity.EntityLiving;

public class RenderFlyingCow extends RenderLiving {
	public RenderFlyingCow(ModelBase modelbase, ModelBase modelbase1, float f) {
		super(modelbase, f);
		this.setRenderPassModel(modelbase1);
	}

	protected boolean setWoolColorAndRender(EntityFlyingCow flyingcow, int i, float f) {
		if(i == 0) {
			this.loadTexture("/mob/Mob_FlyingPigWings.png");
			ModelFlyingCow2.flyingcow = flyingcow;
			return true;
		} else {
			return false;
		}
	}

	protected boolean shouldRenderPass(EntityLiving entityliving, int i, float f) {
		return this.setWoolColorAndRender((EntityFlyingCow)entityliving, i, f);
	}
}
