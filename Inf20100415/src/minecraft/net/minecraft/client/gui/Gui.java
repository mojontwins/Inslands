package net.minecraft.client.gui;

import net.minecraft.client.render.Tessellator;

import org.lwjgl.opengl.GL11;

public class Gui {
	protected float zLevel = 0.0F;

	protected static void drawGradientRect(int i0, int i1, int i2, int i3, int i4, int i5) {
		float f6 = (float)(i4 >>> 24) / 255.0F;
		float f7 = (float)(i4 >> 16 & 255) / 255.0F;
		float f8 = (float)(i4 >> 8 & 255) / 255.0F;
		float f13 = (float)(i4 & 255) / 255.0F;
		float f9 = (float)(i5 >>> 24) / 255.0F;
		float f10 = (float)(i5 >> 16 & 255) / 255.0F;
		float f11 = (float)(i5 >> 8 & 255) / 255.0F;
		float f14 = (float)(i5 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator12 = Tessellator.instance;
		Tessellator.instance.startDrawingQuads();
		tessellator12.setColorRGBA_F(f7, f8, f13, f6);
		tessellator12.addVertex((double)i2, (double)i1, 0.0D);
		tessellator12.addVertex((double)i0, (double)i1, 0.0D);
		tessellator12.setColorRGBA_F(f10, f11, f14, f9);
		tessellator12.addVertex((double)i0, (double)i3, 0.0D);
		tessellator12.addVertex((double)i2, (double)i3, 0.0D);
		tessellator12.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void drawCenteredString(FontRenderer fontRenderer0, String string1, int i2, int i3, int i4) {
		fontRenderer0.drawStringWithShadow(string1, i2 - fontRenderer0.getStringWidth(string1) / 2, i3, i4);
	}

	public static void drawString(FontRenderer fontRenderer0, String string1, int i2, int i3, int i4) {
		fontRenderer0.drawStringWithShadow(string1, i2, i3, i4);
	}

	public final void drawTexturedModalRect(int i1, int i2, int i3, int i4, int i5, int i6) {
		Tessellator tessellator7 = Tessellator.instance;
		Tessellator.instance.startDrawingQuads();
		tessellator7.addVertexWithUV((double)i1, (double)(i2 + i6), (double)this.zLevel, (double)((float)i3 * 0.00390625F), (double)((float)(i4 + i6) * 0.00390625F));
		tessellator7.addVertexWithUV((double)(i1 + i5), (double)(i2 + i6), (double)this.zLevel, (double)((float)(i3 + i5) * 0.00390625F), (double)((float)(i4 + i6) * 0.00390625F));
		tessellator7.addVertexWithUV((double)(i1 + i5), (double)i2, (double)this.zLevel, (double)((float)(i3 + i5) * 0.00390625F), (double)((float)i4 * 0.00390625F));
		tessellator7.addVertexWithUV((double)i1, (double)i2, (double)this.zLevel, (double)((float)i3 * 0.00390625F), (double)((float)i4 * 0.00390625F));
		tessellator7.draw();
	}
}