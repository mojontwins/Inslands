package net.minecraft.client.renderer.entity.aether;

import com.misc.aether.EntityPhyg;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.aether.ModelFlyingPig2;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.world.entity.EntityLiving;

public class RenderPhyg extends RenderLiving {
	public RenderPhyg(ModelBase modelbase, ModelBase modelbase1, float f) {
		super(modelbase, f);
		this.setRenderPassModel(modelbase1);
	}

	protected boolean setWoolColorAndRender(EntityPhyg pig, int i, float f) {
		if(i == 0) {
			this.loadTexture("/mob/Mob_FlyingPigWings.png");
			ModelFlyingPig2.pig = pig;
			return true;
		} else {
			return false;
		}
	}

	protected boolean shouldRenderPass(EntityLiving entityliving, int i, float f) {
		return this.setWoolColorAndRender((EntityPhyg)entityliving, i, f);
	}
}
