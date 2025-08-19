package net.minecraft.client.render.entity;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityTNT;
import net.minecraft.game.world.block.Block;

import org.lwjgl.opengl.GL11;

public final class RenderTNT extends Render {
	private RenderBlocks blockRenderer = new RenderBlocks();

	public RenderTNT() {
		this.shadowSize = 0.5F;
	}

	public final void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		EntityTNT entityTNT10001 = (EntityTNT)entity1;
		double d12 = d2;
		EntityTNT entityTNT18 = entityTNT10001;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d12, (float)d4, (float)d6);
		float f19;
		if((float)entityTNT18.fuse - f9 + 1.0F < 10.0F) {
			if((f19 = 1.0F - ((float)entityTNT18.fuse - f9 + 1.0F) / 10.0F) < 0.0F) {
				f19 = 0.0F;
			}

			if(f19 > 1.0F) {
				f19 = 1.0F;
			}

			f19 = (f19 *= f19) * f19;
			GL11.glScalef(f19 = 1.0F + f19 * 0.3F, f19, f19);
		}

		f19 = (1.0F - ((float)entityTNT18.fuse - f9 + 1.0F) / 100.0F) * 0.8F;
		this.loadTexture("/terrain.png");
		this.blockRenderer.renderBlockOnInventory(Block.tnt);
		if(entityTNT18.fuse / 5 % 2 == 0) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f19);
			this.blockRenderer.renderBlockOnInventory(Block.tnt);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

		GL11.glPopMatrix();
	}
}