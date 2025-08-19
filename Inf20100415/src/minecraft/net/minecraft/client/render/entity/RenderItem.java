package net.minecraft.client.render.entity;

import java.util.Random;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.block.Block;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class RenderItem extends Render {
	private RenderBlocks renderBlocks = new RenderBlocks();
	private Random random = new Random();

	public RenderItem() {
		this.shadowSize = 0.15F;
		this.shadowOpaque = 0.75F;
	}

	public final void renderItemIntoGUI(RenderEngine renderEngine1, ItemStack itemStack2, int i3, int i4) {
		if(itemStack2 != null) {
			int i9;
			if(itemStack2.itemID < 256 && Block.blocksList[itemStack2.itemID].getRenderType() == 0) {
				i9 = itemStack2.itemID;
				RenderEngine.bindTexture(renderEngine1.getTexture("/terrain.png"));
				Block block8 = Block.blocksList[i9];
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(i3 - 2), (float)(i4 + 3), 0.0F);
				GL11.glScalef(10.0F, 10.0F, 10.0F);
				GL11.glTranslatef(1.0F, 0.5F, 8.0F);
				GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.renderBlocks.renderBlockOnInventory(block8);
				GL11.glPopMatrix();
			} else {
				if(itemStack2.getItem().getIconFromDamage() >= 0) {
					GL11.glDisable(GL11.GL_LIGHTING);
					if(itemStack2.itemID < 256) {
						RenderEngine.bindTexture(renderEngine1.getTexture("/terrain.png"));
					} else {
						RenderEngine.bindTexture(renderEngine1.getTexture("/gui/items.png"));
					}

					int i10000 = i3;
					int i10001 = i4;
					int i10002 = itemStack2.getItem().getIconFromDamage() % 16 << 4;
					int i10003 = itemStack2.getItem().getIconFromDamage() / 16 << 4;
					boolean z6 = true;
					z6 = true;
					i4 = i10003;
					i3 = i10002;
					i9 = i10001;
					int i7 = i10000;
					Tessellator tessellator5 = Tessellator.instance;
					Tessellator.instance.startDrawingQuads();
					tessellator5.addVertexWithUV((double)i7, (double)(i9 + 16), 0.0D, (double)((float)i3 * 0.00390625F), (double)((float)(i4 + 16) * 0.00390625F));
					tessellator5.addVertexWithUV((double)(i7 + 16), (double)(i9 + 16), 0.0D, (double)((float)(i3 + 16) * 0.00390625F), (double)((float)(i4 + 16) * 0.00390625F));
					tessellator5.addVertexWithUV((double)(i7 + 16), (double)i9, 0.0D, (double)((float)(i3 + 16) * 0.00390625F), (double)((float)i4 * 0.00390625F));
					tessellator5.addVertexWithUV((double)i7, (double)i9, 0.0D, (double)((float)i3 * 0.00390625F), (double)((float)i4 * 0.00390625F));
					tessellator5.draw();
					GL11.glEnable(GL11.GL_LIGHTING);
				}

			}
		}
	}

	public final void renderItemOverlayIntoGUI(FontRenderer fontRenderer1, ItemStack itemStack2, int i3, int i4) {
		if(itemStack2 != null) {
			if(itemStack2.stackSize > 1) {
				String string5 = "" + itemStack2.stackSize;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				fontRenderer1.drawStringWithShadow(string5, i3 + 19 - 2 - fontRenderer1.getStringWidth(string5), i4 + 6 + 3, 0xFFFFFF);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}

			if(itemStack2.itemDamage > 0) {
				int i9 = 13 - itemStack2.itemDamage * 13 / itemStack2.getMaxDamage();
				int i7 = 255 - itemStack2.itemDamage * 255 / itemStack2.getMaxDamage();
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				Tessellator tessellator8 = Tessellator.instance;
				int i6 = 255 - i7 << 16 | i7 << 8;
				i7 = (255 - i7) / 4 << 16 | 16128;
				renderQuad(tessellator8, i3 + 2, i4 + 13, 13, 2, 0);
				renderQuad(tessellator8, i3 + 2, i4 + 13, 12, 1, i7);
				renderQuad(tessellator8, i3 + 2, i4 + 13, i9, 1, i6);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

		}
	}

	private static void renderQuad(Tessellator tessellator0, int i1, int i2, int i3, int i4, int i5) {
		tessellator0.startDrawingQuads();
		tessellator0.setColorOpaque_I(i5);
		tessellator0.addVertex((double)i1, (double)i2, 0.0D);
		tessellator0.addVertex((double)i1, (double)(i2 + i4), 0.0D);
		tessellator0.addVertex((double)(i1 + i3), (double)(i2 + i4), 0.0D);
		tessellator0.addVertex((double)(i1 + i3), (double)i2, 0.0D);
		tessellator0.draw();
	}

	public final void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		EntityItem entityItem19 = (EntityItem)entity1;
		RenderItem renderItem18 = this;
		this.random.setSeed(187L);
		ItemStack itemStack24 = entityItem19.item;
		GL11.glPushMatrix();
		float f5 = MathHelper.sin(((float)entityItem19.age + f9) / 10.0F + entityItem19.hoverStart) * 0.1F + 0.1F;
		float f3 = (((float)entityItem19.age + f9) / 20.0F + entityItem19.hoverStart) * 57.295776F;
		byte b26 = 1;
		if(entityItem19.item.stackSize > 1) {
			b26 = 2;
		}

		if(entityItem19.item.stackSize > 5) {
			b26 = 3;
		}

		if(entityItem19.item.stackSize > 20) {
			b26 = 4;
		}

		GL11.glTranslatef((float)d2, (float)d4 + f5, (float)d6);
		GL11.glEnable(GL11.GL_NORMALIZE);
		float f7;
		float f21;
		if(itemStack24.itemID < 256 && Block.blocksList[itemStack24.itemID].getRenderType() == 0) {
			GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
			this.loadTexture("/terrain.png");
			f21 = 0.25F;
			if(!Block.blocksList[itemStack24.itemID].renderAsNormalBlock() && itemStack24.itemID != Block.stairSingle.blockID) {
				f21 = 0.5F;
			}

			GL11.glScalef(f21, f21, f21);

			for(int i23 = 0; i23 < b26; ++i23) {
				GL11.glPushMatrix();
				if(i23 > 0) {
					f5 = (renderItem18.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f21;
					f7 = (renderItem18.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f21;
					f8 = (renderItem18.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f21;
					GL11.glTranslatef(f5, f7, f8);
				}

				renderItem18.renderBlocks.renderBlockOnInventory(Block.blocksList[itemStack24.itemID]);
				GL11.glPopMatrix();
			}
		} else {
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			int i20 = itemStack24.getItem().getIconFromDamage();
			if(itemStack24.itemID < 256) {
				this.loadTexture("/terrain.png");
			} else {
				this.loadTexture("/gui/items.png");
			}

			Tessellator tessellator22 = Tessellator.instance;
			f5 = (float)(i20 % 16 << 4) / 256.0F;
			f7 = (float)((i20 % 16 << 4) + 16) / 256.0F;
			f8 = (float)(i20 / 16 << 4) / 256.0F;
			f21 = (float)((i20 / 16 << 4) + 16) / 256.0F;

			for(int i25 = 0; i25 < b26; ++i25) {
				GL11.glPushMatrix();
				if(i25 > 0) {
					f9 = (renderItem18.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float f10 = (renderItem18.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float f11 = (renderItem18.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					GL11.glTranslatef(f9, f10, f11);
				}

				GL11.glRotatef(180.0F - renderItem18.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
				tessellator22.startDrawingQuads();
				Tessellator.setNormal(0.0F, 1.0F, 0.0F);
				tessellator22.addVertexWithUV(-0.5D, -0.25D, 0.0D, (double)f5, (double)f21);
				tessellator22.addVertexWithUV(0.5D, -0.25D, 0.0D, (double)f7, (double)f21);
				tessellator22.addVertexWithUV(0.5D, 0.75D, 0.0D, (double)f7, (double)f8);
				tessellator22.addVertexWithUV(-0.5D, 0.75D, 0.0D, (double)f5, (double)f8);
				tessellator22.draw();
				GL11.glPopMatrix();
			}
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glPopMatrix();
	}
}