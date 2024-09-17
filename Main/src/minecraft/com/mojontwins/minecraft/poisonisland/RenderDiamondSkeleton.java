package com.mojontwins.minecraft.poisonisland;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelBase;
import net.minecraft.src.RenderLiving;

public class RenderDiamondSkeleton extends RenderLiving {
	float scale = 1.5F;
	
	public RenderDiamondSkeleton(ModelBase var1, float var2) {
		super(var1, var2);
	}

    @Override
	protected final void preRenderCallback(EntityLiving var1, float var2) {
        GL11.glScalef(this.scale, this.scale, this.scale);
    }
}
