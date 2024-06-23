package com.mojang.minecraft.ocelot;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelBase;
import net.minecraft.src.RenderLiving;

public class RenderOcelot extends RenderLiving {
	public RenderOcelot(ModelBase modelBase1, float f2) {
		super(modelBase1, f2);
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.doRenderLiving((EntityLiving)entity1, d2, d4, d6, f8, f9);
	}
}
