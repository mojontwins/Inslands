package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ItemRendererHD extends ItemRenderer {
	private Minecraft minecraft = null;

	public ItemRendererHD(Minecraft minecraft) {
		super(minecraft);
		this.minecraft = minecraft;
	}

	public void renderItem(EntityLiving entityliving, ItemStack itemstack, int ix) {
		boolean hasForge = Reflector.hasClass(2);
		if(hasForge) {
			Object num = Reflector.getFieldValue(60);
			Object iconIndex = Reflector.call(20, new Object[]{itemstack, num});
			if(iconIndex != null) {
				super.renderItem(entityliving, itemstack, ix);
				return;
			}
		}

		if(itemstack.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())) {
			super.renderItem(entityliving, itemstack, ix);
		} else {
			int num1 = Config.getIconWidthTerrain();
			if(num1 < 16) {
				super.renderItem(entityliving, itemstack, ix);
			} else {
				GL11.glPushMatrix();
				int iconIndex1 = entityliving.getItemIcon(itemstack, ix);
				float texWidth = 256.0F;
				String tessellator;
				if(itemstack.itemID < 256) {
					tessellator = "/terrain.png";
					if(hasForge) {
						tessellator = Reflector.callString(12, new Object[]{"/terrain.png", itemstack.getItem()});
					}

					if(tessellator.equals("/terrain.png") && Config.isMultiTexture()) {
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, Tessellator.getTileTextures(this.minecraft.renderEngine.getTexture(tessellator))[iconIndex1]);
						iconIndex1 = 0;
						texWidth = 16.0F;
					} else {
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.renderEngine.getTexture(tessellator));
					}

					num1 = Config.getIconWidthTerrain();
				} else {
					tessellator = "/gui/items.png";
					if(hasForge) {
						tessellator = Reflector.callString(12, new Object[]{"/gui/items.png", itemstack.getItem()});
					}

					if(tessellator.equals("/gui/items.png") && Config.isMultiTexture()) {
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, Tessellator.getTileTextures(this.minecraft.renderEngine.getTexture(tessellator))[iconIndex1]);
						iconIndex1 = 0;
						texWidth = 16.0F;
					} else {
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.renderEngine.getTexture(tessellator));
					}

					num1 = Config.getIconWidthItems();
				}

				Tessellator tessellator1 = Tessellator.instance;
				float f = ((float)(iconIndex1 % 16 * 16) + 0.0F) / texWidth;
				float f1 = ((float)(iconIndex1 % 16 * 16) + 15.99F) / texWidth;
				float f2 = ((float)(iconIndex1 / 16 * 16) + 0.0F) / texWidth;
				float f3 = ((float)(iconIndex1 / 16 * 16) + 15.99F) / texWidth;
				float f4 = 0.0F;
				float f5 = 0.3F;
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				GL11.glTranslatef(-f4, -f5, 0.0F);
				float f6 = 1.5F;
				GL11.glScalef(f6, f6, f6);
				GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
				this.renderItem3D(tessellator1, f1, f2, f, f3, num1);
				if(itemstack != null && itemstack.hasEffect() && ix == 0) {
					GL11.glDepthFunc(GL11.GL_EQUAL);
					GL11.glDisable(GL11.GL_LIGHTING);
					this.minecraft.renderEngine.bindTexture(this.minecraft.renderEngine.getTexture("%blur%/misc/glint.png"));
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
					float f7 = 0.76F;
					GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
					GL11.glMatrixMode(GL11.GL_TEXTURE);
					GL11.glPushMatrix();
					float f8 = 0.125F;
					GL11.glScalef(f8, f8, f8);
					float f9 = (float)(System.currentTimeMillis() % 3000L) / 3000.0F * 8.0F;
					GL11.glTranslatef(f9, 0.0F, 0.0F);
					GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
					this.renderItem3D(tessellator1, 0.0F, 0.0F, 1.0F, 1.0F, num1);
					GL11.glPopMatrix();
					GL11.glPushMatrix();
					GL11.glScalef(f8, f8, f8);
					f9 = (float)(System.currentTimeMillis() % 4873L) / 4873.0F * 8.0F;
					GL11.glTranslatef(-f9, 0.0F, 0.0F);
					GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
					this.renderItem3D(tessellator1, 0.0F, 0.0F, 1.0F, 1.0F, num1);
					GL11.glPopMatrix();
					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glDepthFunc(GL11.GL_LEQUAL);
				}

				GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				GL11.glPopMatrix();
			}
		}
	}

	private void renderItem3D(Tessellator tessellator, float f1, float f2, float f, float f3, int num) {
		float f4 = 1.0F;
		float f8 = 0.0625F;
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)f1, (double)f3);
		tessellator.addVertexWithUV((double)f4, 0.0D, 0.0D, (double)f, (double)f3);
		tessellator.addVertexWithUV((double)f4, 1.0D, 0.0D, (double)f, (double)f2);
		tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)f1, (double)f2);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		tessellator.addVertexWithUV(0.0D, 1.0D, (double)(0.0F - f8), (double)f1, (double)f2);
		tessellator.addVertexWithUV((double)f4, 1.0D, (double)(0.0F - f8), (double)f, (double)f2);
		tessellator.addVertexWithUV((double)f4, 0.0D, (double)(0.0F - f8), (double)f, (double)f3);
		tessellator.addVertexWithUV(0.0D, 0.0D, (double)(0.0F - f8), (double)f1, (double)f3);
		tessellator.draw();
		float du = 1.0F / (float)(32 * num);
		float dz = 1.0F / (float)num;
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);

		int i1;
		float f12;
		float f16;
		float f20;
		for(i1 = 0; i1 < num; ++i1) {
			f12 = (float)i1 / ((float)num * 1.0F);
			f16 = f1 + (f - f1) * f12 - du;
			f20 = f4 * f12;
			tessellator.addVertexWithUV((double)f20, 0.0D, (double)(0.0F - f8), (double)f16, (double)f3);
			tessellator.addVertexWithUV((double)f20, 0.0D, 0.0D, (double)f16, (double)f3);
			tessellator.addVertexWithUV((double)f20, 1.0D, 0.0D, (double)f16, (double)f2);
			tessellator.addVertexWithUV((double)f20, 1.0D, (double)(0.0F - f8), (double)f16, (double)f2);
		}

		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);

		for(i1 = 0; i1 < num; ++i1) {
			f12 = (float)i1 / ((float)num * 1.0F);
			f16 = f1 + (f - f1) * f12 - du;
			f20 = f4 * f12 + dz;
			tessellator.addVertexWithUV((double)f20, 1.0D, (double)(0.0F - f8), (double)f16, (double)f2);
			tessellator.addVertexWithUV((double)f20, 1.0D, 0.0D, (double)f16, (double)f2);
			tessellator.addVertexWithUV((double)f20, 0.0D, 0.0D, (double)f16, (double)f3);
			tessellator.addVertexWithUV((double)f20, 0.0D, (double)(0.0F - f8), (double)f16, (double)f3);
		}

		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);

		for(i1 = 0; i1 < num; ++i1) {
			f12 = (float)i1 / ((float)num * 1.0F);
			f16 = f3 + (f2 - f3) * f12 - du;
			f20 = f4 * f12 + dz;
			tessellator.addVertexWithUV(0.0D, (double)f20, 0.0D, (double)f1, (double)f16);
			tessellator.addVertexWithUV((double)f4, (double)f20, 0.0D, (double)f, (double)f16);
			tessellator.addVertexWithUV((double)f4, (double)f20, (double)(0.0F - f8), (double)f, (double)f16);
			tessellator.addVertexWithUV(0.0D, (double)f20, (double)(0.0F - f8), (double)f1, (double)f16);
		}

		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);

		for(i1 = 0; i1 < num; ++i1) {
			f12 = (float)i1 / ((float)num * 1.0F);
			f16 = f3 + (f2 - f3) * f12 - du;
			f20 = f4 * f12;
			tessellator.addVertexWithUV((double)f4, (double)f20, 0.0D, (double)f, (double)f16);
			tessellator.addVertexWithUV(0.0D, (double)f20, 0.0D, (double)f1, (double)f16);
			tessellator.addVertexWithUV(0.0D, (double)f20, (double)(0.0F - f8), (double)f1, (double)f16);
			tessellator.addVertexWithUV((double)f4, (double)f20, (double)(0.0F - f8), (double)f, (double)f16);
		}

		tessellator.draw();
	}
}
