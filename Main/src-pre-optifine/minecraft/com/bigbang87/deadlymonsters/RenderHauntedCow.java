package com.bigbang87.deadlymonsters;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelBase;
import net.minecraft.src.RenderLiving;

public class RenderHauntedCow extends RenderLiving {
	public RenderHauntedCow(ModelBase modelBase1, float f2) {
		super(modelBase1, f2);
	}

	public void renderCow(EntityHauntedCow entityCow1, double d2, double d4, double d6, float f8, float f9) {
		super.doRenderLiving(entityCow1, d2, d4, d6, f8, f9);
	}

	public void doRenderLiving(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		this.renderCow((EntityHauntedCow)entityLiving1, d2, d4, d6, f8, f9);
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.renderCow((EntityHauntedCow)entity1, d2, d4, d6, f8, f9);
	}
}

