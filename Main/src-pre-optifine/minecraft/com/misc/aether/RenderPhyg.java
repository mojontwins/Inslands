package com.misc.aether;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelBase;
import net.minecraft.src.RenderLiving;

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
