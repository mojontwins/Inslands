package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.monster.EntityCreeper;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class RenderCreeper extends RenderLiving {
	public RenderCreeper() {
		super(new ModelCreeper(), 0.5F);
	}

	protected final void preRenderCallback(EntityLiving entityLiving1, float f2) {
		float f4 = ((EntityCreeper)entityLiving1).c(f2);
		f2 = 1.0F + MathHelper.sin(f4 * 100.0F) * f4 * 0.01F;
		if(f4 < 0.0F) {
			f4 = 0.0F;
		}

		if(f4 > 1.0F) {
			f4 = 1.0F;
		}

		f4 = (f4 *= f4) * f4;
		float f3 = (1.0F + f4 * 0.4F) * f2;
		f4 = (1.0F + f4 * 0.1F) / f2;
		GL11.glScalef(f3, f4, f3);
	}

	protected final int getColorMultiplier(EntityLiving entityLiving1, float f2, float f3) {
		float f4;
		if((int)((f4 = ((EntityCreeper)entityLiving1).c(f3)) * 10.0F) % 2 == 0) {
			return 0;
		} else {
			int i5;
			if((i5 = (int)(f4 * 0.2F * 255.0F)) < 0) {
				i5 = 0;
			}

			if(i5 > 255) {
				i5 = 255;
			}

			return i5 << 24 | 16711680 | 65280 | 255;
		}
	}
}