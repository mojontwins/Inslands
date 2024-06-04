package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderAerbunny extends RenderLiving {
	public ModelAerbunny mb;

	public RenderAerbunny(ModelBase modelbase, float f) {
		super(modelbase, f);
		this.mb = (ModelAerbunny)modelbase;
	}

	protected void rotAerbunny(EntityAerbunny entitybunny) {
		if(!entitybunny.onGround && entitybunny.ridingEntity == null) {
			if(entitybunny.motionY > 0.5D) {
				GL11.glRotatef(15.0F, -1.0F, 0.0F, 0.0F);
			} else if(entitybunny.motionY < -0.5D) {
				GL11.glRotatef(-15.0F, -1.0F, 0.0F, 0.0F);
			} else {
				GL11.glRotatef((float)(entitybunny.motionY * 30.0D), -1.0F, 0.0F, 0.0F);
			}
		}

		this.mb.puffiness = entitybunny.puffiness;
	}

	protected void preRenderCallback(EntityLiving entityliving, float f) {
		this.rotAerbunny((EntityAerbunny)entityliving);
	}
}
