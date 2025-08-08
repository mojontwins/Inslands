package net.minecraft.client.renderer.entity.mojontwins;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.src.EntityLiving;

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
