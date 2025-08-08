package net.minecraft.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityLiving;

public class RenderMooshroom extends RenderLiving {
	public RenderMooshroom(ModelBase modelBase1, float f2) {
		super(modelBase1, f2);
	}

	public void renderCow(EntityCow entityCow1, double d2, double d4, double d6, float f8, float f9) {
		super.doRenderLiving(entityCow1, d2, d4, d6, f8, f9);
	}

	public void doRenderLiving(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		this.renderCow((EntityCow)entityLiving1, d2, d4, d6, f8, f9);
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.renderCow((EntityCow)entity1, d2, d4, d6, f8, f9);
	}
	
	protected void renderEquippedItems(EntityLiving entityLiving1, float f2) {
		loadTexture("/terrain.png");
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPushMatrix();
		GL11.glScalef(1.0F, -1F, 1.0F);
		GL11.glTranslatef(0.2F, 0.4F, 0.5F);
		GL11.glRotatef(42F, 0.0F, 1.0F, 0.0F);
		renderBlocks.renderBlockOnInventory(Block.mushroomRed, 0, 1.0F);
		GL11.glTranslatef(0.1F, 0.0F, -0.6F);
		GL11.glRotatef(42F, 0.0F, 1.0F, 0.0F);
		renderBlocks.renderBlockOnInventory(Block.mushroomRed, 0, 1.0F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		((ModelQuadruped)mainModel).head.postRender(0.0625F);
		GL11.glScalef(1.0F, -1F, 1.0F);
		GL11.glTranslatef(0.0F, 0.75F, -0.2F);
		GL11.glRotatef(12F, 0.0F, 1.0F, 0.0F);
		renderBlocks.renderBlockOnInventory(Block.mushroomRed, 0, 1.0F);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		return;
	}
}
