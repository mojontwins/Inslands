package com.benimatic.twilightforest;

import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderBiped;
import org.lwjgl.opengl.GL11;

public class RenderTFMinoshroom extends RenderBiped {
	public RenderTFMinoshroom(ModelBiped var1, float var2) {
		super(var1, var2);
	}

	protected void renderMooshroomEquippedItems(EntityLiving var1, float var2) {
		super.renderEquippedItems(var1, var2);
		this.loadTexture("/terrain.png");
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPushMatrix();
		GL11.glScalef(1.0F, -1.0F, 1.0F);
		GL11.glTranslatef(0.2F, 0.375F, 0.5F);
		GL11.glRotatef(42.0F, 0.0F, 1.0F, 0.0F);
		this.renderBlocks.renderBlockAsItem(Block.mushroomRed, 1.0F);
		GL11.glTranslatef(0.1F, 0.0F, -0.6F);
		GL11.glRotatef(42.0F, 0.0F, 1.0F, 0.0F);
		this.renderBlocks.renderBlockAsItem(Block.mushroomRed, 1.0F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		((ModelBiped) this.mainModel).bipedHead.postRender(0.0625F);
		GL11.glScalef(1.0F, -1.0F, 1.0F);
		GL11.glTranslatef(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(12.0F, 0.0F, 1.0F, 0.0F);
		this.renderBlocks.renderBlockAsItem(Block.mushroomRed, 1.0F);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	protected void renderEquippedItems(EntityLiving var1, float var2) {
		this.renderMooshroomEquippedItems(var1, var2);
	}
}
