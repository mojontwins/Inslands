package net.minecraft.client.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;

import net.minecraft.client.GameSettings;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class FontRenderer {
	private int[] charWidth = new int[256];
	private int fontTextureInt = 0;
	private int fontDisplayLists;
	private IntBuffer buffer = BufferUtils.createIntBuffer(1024);

	public FontRenderer(GameSettings gameSettings1, String string2, RenderEngine renderEngine3) {
		BufferedImage bufferedImage4;
		try {
			bufferedImage4 = ImageIO.read(RenderEngine.class.getResourceAsStream(string2));
		} catch (IOException iOException15) {
			throw new RuntimeException(iOException15);
		}

		int i5 = bufferedImage4.getWidth();
		int i6 = bufferedImage4.getHeight();
		int[] i7 = new int[i5 * i6];
		bufferedImage4.getRGB(0, 0, i5, i6, i7, 0, i5);

		int i8;
		int i9;
		int i11;
		int i13;
		for(int i17 = 0; i17 < 128; ++i17) {
			i6 = i17 % 16;
			i8 = i17 / 16;
			i9 = 0;

			for(boolean z10 = false; i9 < 8 && !z10; ++i9) {
				i11 = (i6 << 3) + i9;
				z10 = true;

				for(int i12 = 0; i12 < 8 && z10; ++i12) {
					i13 = ((i8 << 3) + i12) * i5;
					if((i7[i11 + i13] & 255) > 128) {
						z10 = false;
					}
				}
			}

			if(i17 == 32) {
				i9 = 4;
			}

			this.charWidth[i17] = i9;
		}

		this.fontTextureInt = renderEngine3.getTexture(string2);
		this.fontDisplayLists = GL11.glGenLists(288);
		Tessellator tessellator18 = Tessellator.instance;

		for(i6 = 0; i6 < 256; ++i6) {
			GL11.glNewList(this.fontDisplayLists + i6, GL11.GL_COMPILE);
			tessellator18.startDrawingQuads();
			i8 = i6 % 16 << 3;
			i9 = i6 / 16 << 3;
			tessellator18.addVertexWithUV(0.0D, 7.989999771118164D, 0.0D, (double)((float)i8 / 128.0F), (double)(((float)i9 + 7.99F) / 128.0F));
			tessellator18.addVertexWithUV(7.989999771118164D, 7.989999771118164D, 0.0D, (double)(((float)i8 + 7.99F) / 128.0F), (double)(((float)i9 + 7.99F) / 128.0F));
			tessellator18.addVertexWithUV(7.989999771118164D, 0.0D, 0.0D, (double)(((float)i8 + 7.99F) / 128.0F), (double)((float)i9 / 128.0F));
			tessellator18.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)((float)i8 / 128.0F), (double)((float)i9 / 128.0F));
			tessellator18.draw();
			GL11.glTranslatef((float)this.charWidth[i6], 0.0F, 0.0F);
			GL11.glEndList();
		}

		for(i6 = 0; i6 < 32; ++i6) {
			i8 = (i6 & 8) << 3;
			i9 = (i6 & 1) * 191 + i8;
			int i19 = ((i6 & 2) >> 1) * 191 + i8;
			i11 = ((i6 & 4) >> 2) * 191 + i8;
			boolean z20 = i6 >= 16;
			if(gameSettings1.anaglyph) {
				i13 = (i11 * 30 + i19 * 59 + i9 * 11) / 100;
				int i14 = (i11 * 30 + i19 * 70) / 100;
				int i16 = (i11 * 30 + i9 * 70) / 100;
				i11 = i13;
				i19 = i14;
				i9 = i16;
			}

			i6 += 2;
			if(z20) {
				i11 /= 4;
				i19 /= 4;
				i9 /= 4;
			}

			GL11.glColor4f((float)i11 / 255.0F, (float)i19 / 255.0F, (float)i9 / 255.0F, 1.0F);
		}

	}

	public final void drawStringWithShadow(String string1, int i2, int i3, int i4) {
		this.renderString(string1, i2 + 1, i3 + 1, i4, true);
		this.drawString(string1, i2, i3, i4);
	}

	public final void drawString(String string1, int i2, int i3, int i4) {
		this.renderString(string1, i2, i3, i4, false);
	}

	private void renderString(String string1, int i2, int i3, int i4, boolean z5) {
		if(string1 != null) {
			char[] c8 = string1.toCharArray();
			if(z5) {
				i4 = (i4 & 16579836) >> 2;
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureInt);
			float f6 = (float)(i4 >> 16 & 255) / 255.0F;
			float f7 = (float)(i4 >> 8 & 255) / 255.0F;
			float f9 = (float)(i4 & 255) / 255.0F;
			GL11.glColor4f(f6, f7, f9, 1.0F);
			this.buffer.clear();
			GL11.glPushMatrix();
			GL11.glTranslatef((float)i2, (float)i3, 0.0F);

			for(int i10 = 0; i10 < c8.length; ++i10) {
				for(; c8[i10] == 38 && c8.length > i10 + 1; i10 += 2) {
					int i11;
					if((i11 = "0123456789abcdef".indexOf(c8[i10 + 1])) < 0 || i11 > 15) {
						i11 = 15;
					}

					this.buffer.put(this.fontDisplayLists + 256 + i11 + (z5 ? 16 : 0));
					if(this.buffer.remaining() == 0) {
						this.buffer.flip();
						GL11.glCallLists(this.buffer);
						this.buffer.clear();
					}
				}

				this.buffer.put(this.fontDisplayLists + c8[i10]);
				if(this.buffer.remaining() == 0) {
					this.buffer.flip();
					GL11.glCallLists(this.buffer);
					this.buffer.clear();
				}
			}

			this.buffer.flip();
			GL11.glCallLists(this.buffer);
			GL11.glPopMatrix();
		}
	}

	public final int getStringWidth(String string1) {
		if(string1 == null) {
			return 0;
		} else {
			char[] c4 = string1.toCharArray();
			int i2 = 0;

			for(int i3 = 0; i3 < c4.length; ++i3) {
				if(c4[i3] == 38) {
					++i3;
				} else {
					i2 += this.charWidth[c4[i3]];
				}
			}

			return i2;
		}
	}
}