package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.Bidi;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

public class FontRenderer {
	private static final Pattern field_52015_r = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
	private int[] charWidth = new int[256];
	public int fontTextureName = 0;
	public int FONT_HEIGHT = 8;
	public Random fontRandom = new Random();
	private byte[] glyphWidth = new byte[65536];
	private final int[] glyphTextureName = new int[256];
	private int[] colorCode = new int[32];
	private int boundTextureName;
	private final RenderEngine renderEngine;
	private float posX;
	private float posY;
	private boolean unicodeFlag;
	private boolean bidiFlag;
	private float field_50115_n;
	private float field_50116_o;
	private float field_50118_p;
	private float field_50117_q;

	FontRenderer() {
		this.renderEngine = null;
	}

	public FontRenderer(GameSettings gameSettings1, String string2, RenderEngine renderEngine3, boolean z4) {
		this.renderEngine = renderEngine3;
		this.unicodeFlag = z4;

		BufferedImage bufferedImage5;
		try {
			bufferedImage5 = ImageIO.read(RenderEngine.class.getResourceAsStream(string2));
			InputStream inputStream6 = RenderEngine.class.getResourceAsStream("/font/glyph_sizes.bin");
			inputStream6.read(this.glyphWidth);
		} catch (IOException iOException18) {
			throw new RuntimeException(iOException18);
		}

		int i19 = bufferedImage5.getWidth();
		int i7 = bufferedImage5.getHeight();
		int[] i8 = new int[i19 * i7];
		bufferedImage5.getRGB(0, 0, i19, i7, i8, 0, i19);

		int i9;
		int i10;
		int i11;
		int i12;
		int i13;
		int i15;
		int i16;
		for(i9 = 0; i9 < 256; ++i9) {
			i10 = i9 % 16;
			i11 = i9 / 16;

			for(i12 = 7; i12 >= 0; --i12) {
				i13 = i10 * 8 + i12;
				boolean z14 = true;

				for(i15 = 0; i15 < 8 && z14; ++i15) {
					i16 = (i11 * 8 + i15) * i19;
					int i17 = i8[i13 + i16] & 255;
					if(i17 > 0) {
						z14 = false;
					}
				}

				if(!z14) {
					break;
				}
			}

			if(i9 == 32) {
				i12 = 2;
			}

			this.charWidth[i9] = i12 + 2;
		}

		this.fontTextureName = renderEngine3.allocateAndSetupTexture(bufferedImage5);

		for(i9 = 0; i9 < 32; ++i9) {
			i10 = (i9 >> 3 & 1) * 85;
			i11 = (i9 >> 2 & 1) * 170 + i10;
			i12 = (i9 >> 1 & 1) * 170 + i10;
			i13 = (i9 >> 0 & 1) * 170 + i10;
			if(i9 == 6) {
				i11 += 85;
			}

			if(gameSettings1.anaglyph) {
				int i20 = (i11 * 30 + i12 * 59 + i13 * 11) / 100;
				i15 = (i11 * 30 + i12 * 70) / 100;
				i16 = (i11 * 30 + i13 * 70) / 100;
				i11 = i20;
				i12 = i15;
				i13 = i16;
			}

			if(i9 >= 16) {
				i11 /= 4;
				i12 /= 4;
				i13 /= 4;
			}

			this.colorCode[i9] = (i11 & 255) << 16 | (i12 & 255) << 8 | i13 & 255;
		}

	}

	private float func_50112_a(int i1, char c2, boolean z3) {
		return c2 == 32 ? 4.0F : (i1 > 0 && !this.unicodeFlag ? this.func_50106_a(i1 + 32, z3) : this.func_50111_a(c2, z3));
	}

	private float func_50106_a(int i1, boolean z2) {
		float f3 = (float)(i1 % 16 * 8);
		float f4 = (float)(i1 / 16 * 8);
		float f5 = z2 ? 1.0F : 0.0F;
		if(this.boundTextureName != this.fontTextureName) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureName);
			this.boundTextureName = this.fontTextureName;
		}

		float f6 = (float)this.charWidth[i1] - 0.01F;
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(f3 / 128.0F, f4 / 128.0F);
		GL11.glVertex3f(this.posX + f5, this.posY, 0.0F);
		GL11.glTexCoord2f(f3 / 128.0F, (f4 + 7.99F) / 128.0F);
		GL11.glVertex3f(this.posX - f5, this.posY + 7.99F, 0.0F);
		GL11.glTexCoord2f((f3 + f6) / 128.0F, f4 / 128.0F);
		GL11.glVertex3f(this.posX + f6 + f5, this.posY, 0.0F);
		GL11.glTexCoord2f((f3 + f6) / 128.0F, (f4 + 7.99F) / 128.0F);
		GL11.glVertex3f(this.posX + f6 - f5, this.posY + 7.99F, 0.0F);
		GL11.glEnd();
		return (float)this.charWidth[i1];
	}

	private void loadGlyphTexture(int i1) {
		String string3 = String.format("/font/glyph_%02X.png", new Object[]{i1});

		BufferedImage bufferedImage2;
		try {
			bufferedImage2 = ImageIO.read(RenderEngine.class.getResourceAsStream(string3));
		} catch (IOException iOException5) {
			throw new RuntimeException(iOException5);
		}

		this.glyphTextureName[i1] = this.renderEngine.allocateAndSetupTexture(bufferedImage2);
		this.boundTextureName = this.glyphTextureName[i1];
	}

	private float func_50111_a(char c1, boolean z2) {
		if(this.glyphWidth[c1] == 0) {
			return 0.0F;
		} else {
			int i3 = c1 / 256;
			if(this.glyphTextureName[i3] == 0) {
				this.loadGlyphTexture(i3);
			}

			if(this.boundTextureName != this.glyphTextureName[i3]) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.glyphTextureName[i3]);
				this.boundTextureName = this.glyphTextureName[i3];
			}

			int i4 = this.glyphWidth[c1] >>> 4;
			int i5 = this.glyphWidth[c1] & 15;
			float f6 = (float)i4;
			float f7 = (float)(i5 + 1);
			float f8 = (float)(c1 % 16 * 16) + f6;
			float f9 = (float)((c1 & 255) / 16 * 16);
			float f10 = f7 - f6 - 0.02F;
			float f11 = z2 ? 1.0F : 0.0F;
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			GL11.glTexCoord2f(f8 / 256.0F, f9 / 256.0F);
			GL11.glVertex3f(this.posX + f11, this.posY, 0.0F);
			GL11.glTexCoord2f(f8 / 256.0F, (f9 + 15.98F) / 256.0F);
			GL11.glVertex3f(this.posX - f11, this.posY + 7.99F, 0.0F);
			GL11.glTexCoord2f((f8 + f10) / 256.0F, f9 / 256.0F);
			GL11.glVertex3f(this.posX + f10 / 2.0F + f11, this.posY, 0.0F);
			GL11.glTexCoord2f((f8 + f10) / 256.0F, (f9 + 15.98F) / 256.0F);
			GL11.glVertex3f(this.posX + f10 / 2.0F - f11, this.posY + 7.99F, 0.0F);
			GL11.glEnd();
			return (f7 - f6) / 2.0F + 1.0F;
		}
	}

	public int drawStringWithShadow(String string1, int i2, int i3, int i4) {
		if(this.bidiFlag) {
			string1 = this.bidiReorder(string1);
		}

		int i5 = this.func_50101_a(string1, i2 + 1, i3 + 1, i4, true);
		i5 = Math.max(i5, this.func_50101_a(string1, i2, i3, i4, false));
		return i5;
	}

	public void drawString(String string1, int i2, int i3, int i4) {
		if(this.bidiFlag) {
			string1 = this.bidiReorder(string1);
		}

		this.func_50101_a(string1, i2, i3, i4, false);
	}

	private String bidiReorder(String string1) {
		if(string1 != null && Bidi.requiresBidi(string1.toCharArray(), 0, string1.length())) {
			Bidi bidi2 = new Bidi(string1, -2);
			byte[] b3 = new byte[bidi2.getRunCount()];
			String[] string4 = new String[b3.length];

			int i7;
			for(int i5 = 0; i5 < b3.length; ++i5) {
				int i6 = bidi2.getRunStart(i5);
				i7 = bidi2.getRunLimit(i5);
				int i8 = bidi2.getRunLevel(i5);
				String string9 = string1.substring(i6, i7);
				b3[i5] = (byte)i8;
				string4[i5] = string9;
			}

			String[] string11 = (String[])string4.clone();
			Bidi.reorderVisually(b3, 0, string4, 0, b3.length);
			StringBuilder stringBuilder12 = new StringBuilder();

			for(i7 = 0; i7 < string4.length; ++i7) {
				byte b13 = b3[i7];

				int i14;
				for(i14 = 0; i14 < string11.length; ++i14) {
					if(string11[i14].equals(string4[i7])) {
						b13 = b3[i14];
						break;
					}
				}

				if((b13 & 1) == 0) {
					stringBuilder12.append(string4[i7]);
				} else {
					for(i14 = string4[i7].length() - 1; i14 >= 0; --i14) {
						char c10 = string4[i7].charAt(i14);
						if(c10 == 40) {
							c10 = 41;
						} else if(c10 == 41) {
							c10 = 40;
						}

						stringBuilder12.append(c10);
					}
				}
			}

			return stringBuilder12.toString();
		} else {
			return string1;
		}
	}

	private void renderStringAtPos(String string1, boolean z2) {
		boolean z3 = false;
		boolean z4 = false;
		boolean z5 = false;
		boolean z6 = false;
		boolean z7 = false;

		for(int i8 = 0; i8 < string1.length(); ++i8) {
			char c9 = string1.charAt(i8);
			int i10;
			int i11;
			if(c9 == 167 && i8 + 1 < string1.length()) {
				i10 = "0123456789abcdefklmnor".indexOf(string1.toLowerCase().charAt(i8 + 1));
				if(i10 < 16) {
					z3 = false;
					z4 = false;
					z7 = false;
					z6 = false;
					z5 = false;
					if(i10 < 0 || i10 > 15) {
						i10 = 15;
					}

					if(z2) {
						i10 += 16;
					}

					i11 = this.colorCode[i10];
					GL11.glColor3f((float)(i11 >> 16) / 255.0F, (float)(i11 >> 8 & 255) / 255.0F, (float)(i11 & 255) / 255.0F);
				} else if(i10 == 16) {
					z3 = true;
				} else if(i10 == 17) {
					z4 = true;
				} else if(i10 == 18) {
					z7 = true;
				} else if(i10 == 19) {
					z6 = true;
				} else if(i10 == 20) {
					z5 = true;
				} else if(i10 == 21) {
					z3 = false;
					z4 = false;
					z7 = false;
					z6 = false;
					z5 = false;
					GL11.glColor4f(this.field_50115_n, this.field_50116_o, this.field_50118_p, this.field_50117_q);
				}

				++i8;
			} else {
				i10 = ChatAllowedCharacters.allowedCharacters.indexOf(c9);
				if(z3 && i10 > 0) {
					do {
						i11 = this.fontRandom.nextInt(ChatAllowedCharacters.allowedCharacters.length());
					} while(this.charWidth[i10 + 32] != this.charWidth[i11 + 32]);

					i10 = i11;
				}

				float f14 = this.func_50112_a(i10, c9, z5);
				if(z4) {
					++this.posX;
					this.func_50112_a(i10, c9, z5);
					--this.posX;
					++f14;
				}

				Tessellator tessellator12;
				if(z7) {
					tessellator12 = Tessellator.instance;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					tessellator12.startDrawingQuads();
					tessellator12.addVertex((double)this.posX, (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0.0D);
					tessellator12.addVertex((double)(this.posX + f14), (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0.0D);
					tessellator12.addVertex((double)(this.posX + f14), (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0F), 0.0D);
					tessellator12.addVertex((double)this.posX, (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0F), 0.0D);
					tessellator12.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				if(z6) {
					tessellator12 = Tessellator.instance;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					tessellator12.startDrawingQuads();
					int i13 = z6 ? -1 : 0;
					tessellator12.addVertex((double)(this.posX + (float)i13), (double)(this.posY + (float)this.FONT_HEIGHT), 0.0D);
					tessellator12.addVertex((double)(this.posX + f14), (double)(this.posY + (float)this.FONT_HEIGHT), 0.0D);
					tessellator12.addVertex((double)(this.posX + f14), (double)(this.posY + (float)this.FONT_HEIGHT - 1.0F), 0.0D);
					tessellator12.addVertex((double)(this.posX + (float)i13), (double)(this.posY + (float)this.FONT_HEIGHT - 1.0F), 0.0D);
					tessellator12.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				this.posX += f14;
			}
		}

	}

	public int func_50101_a(String string1, int i2, int i3, int i4, boolean z5) {
		if(string1 != null) {
			this.boundTextureName = 0;
			if((i4 & -67108864) == 0) {
				i4 |= 0xFF000000;
			}

			if(z5) {
				i4 = (i4 & 16579836) >> 2 | i4 & 0xFF000000;
			}

			this.field_50115_n = (float)(i4 >> 16 & 255) / 255.0F;
			this.field_50116_o = (float)(i4 >> 8 & 255) / 255.0F;
			this.field_50118_p = (float)(i4 & 255) / 255.0F;
			this.field_50117_q = (float)(i4 >> 24 & 255) / 255.0F;
			GL11.glColor4f(this.field_50115_n, this.field_50116_o, this.field_50118_p, this.field_50117_q);
			this.posX = (float)i2;
			this.posY = (float)i3;
			this.renderStringAtPos(string1, z5);
			return (int)this.posX;
		} else {
			return 0;
		}
	}

	public int getStringWidth(String string1) {
		if(string1 == null) {
			return 0;
		} else {
			int i2 = 0;
			boolean z3 = false;

			for(int i4 = 0; i4 < string1.length(); ++i4) {
				char c5 = string1.charAt(i4);
				int i6 = this.func_50105_a(c5);
				if(i6 < 0 && i4 < string1.length() - 1) {
					++i4;
					c5 = string1.charAt(i4);
					if(c5 != 108 && c5 != 76) {
						if(c5 == 114 || c5 == 82) {
							z3 = false;
						}
					} else {
						z3 = true;
					}

					i6 = this.func_50105_a(c5);
				}

				i2 += i6;
				if(z3) {
					++i2;
				}
			}

			return i2;
		}
	}

	public int func_50105_a(char c1) {
		if(c1 == 167) {
			return -1;
		} else {
			int i2 = ChatAllowedCharacters.allowedCharacters.indexOf(c1);
			if(i2 >= 0 && !this.unicodeFlag) {
				return this.charWidth[i2 + 32];
			} else if(this.glyphWidth[c1] != 0) {
				int i3 = this.glyphWidth[c1] >> 4;
				int i4 = this.glyphWidth[c1] & 15;
				if(i4 > 7) {
					i4 = 15;
					i3 = 0;
				}

				++i4;
				return (i4 - i3) / 2 + 1;
			} else {
				return 0;
			}
		}
	}

	public String func_50107_a(String string1, int i2) {
		return this.func_50104_a(string1, i2, false);
	}

	public String func_50104_a(String string1, int i2, boolean z3) {
		StringBuilder stringBuilder4 = new StringBuilder();
		int i5 = 0;
		int i6 = z3 ? string1.length() - 1 : 0;
		int i7 = z3 ? -1 : 1;
		boolean z8 = false;
		boolean z9 = false;

		for(int i10 = i6; i10 >= 0 && i10 < string1.length() && i5 < i2; i10 += i7) {
			char c11 = string1.charAt(i10);
			int i12 = this.func_50105_a(c11);
			if(z8) {
				z8 = false;
				if(c11 != 108 && c11 != 76) {
					if(c11 == 114 || c11 == 82) {
						z9 = false;
					}
				} else {
					z9 = true;
				}
			} else if(i12 < 0) {
				z8 = true;
			} else {
				i5 += i12;
				if(z9) {
					++i5;
				}
			}

			if(i5 > i2) {
				break;
			}

			if(z3) {
				stringBuilder4.insert(0, c11);
			} else {
				stringBuilder4.append(c11);
			}
		}

		return stringBuilder4.toString();
	}

	public void drawSplitString(String string1, int i2, int i3, int i4, int i5) {
		if(this.bidiFlag) {
			string1 = this.bidiReorder(string1);
		}

		this.renderSplitStringNoShadow(string1, i2, i3, i4, i5);
	}

	private void renderSplitStringNoShadow(String string1, int i2, int i3, int i4, int i5) {
		this.renderSplitString(string1, i2, i3, i4, i5, false);
	}

	private void renderSplitString(String string1, int i2, int i3, int i4, int i5, boolean z6) {
		String[] string7 = string1.split("\n");
		if(string7.length > 1) {
			for(int i14 = 0; i14 < string7.length; ++i14) {
				this.renderSplitStringNoShadow(string7[i14], i2, i3, i4, i5);
				i3 += this.splitStringWidth(string7[i14], i4);
			}

		} else {
			String[] string8 = string1.split(" ");
			int i9 = 0;
			String string10 = "";

			while(i9 < string8.length) {
				String string11;
				for(string11 = string10 + string8[i9++] + " "; i9 < string8.length && this.getStringWidth(string11 + string8[i9]) < i4; string11 = string11 + string8[i9++] + " ") {
				}

				int i12;
				for(; this.getStringWidth(string11) > i4; string11 = string10 + string11.substring(i12)) {
					for(i12 = 0; this.getStringWidth(string11.substring(0, i12 + 1)) <= i4; ++i12) {
					}

					if(string11.substring(0, i12).trim().length() > 0) {
						String string13 = string11.substring(0, i12);
						if(string13.lastIndexOf("\u00a7") >= 0) {
							string10 = "\u00a7" + string13.charAt(string13.lastIndexOf("\u00a7") + 1);
						}

						this.func_50101_a(string13, i2, i3, i5, z6);
						i3 += this.FONT_HEIGHT;
					}
				}

				if(this.getStringWidth(string11.trim()) > 0) {
					if(string11.lastIndexOf("\u00a7") >= 0) {
						string10 = "\u00a7" + string11.charAt(string11.lastIndexOf("\u00a7") + 1);
					}

					this.func_50101_a(string11, i2, i3, i5, z6);
					i3 += this.FONT_HEIGHT;
				}
			}

		}
	}

	public int splitStringWidth(String string1, int i2) {
		String[] string3 = string1.split("\n");
		int i5;
		if(string3.length > 1) {
			int i9 = 0;

			for(i5 = 0; i5 < string3.length; ++i5) {
				i9 += this.splitStringWidth(string3[i5], i2);
			}

			return i9;
		} else {
			String[] string4 = string1.split(" ");
			i5 = 0;
			int i6 = 0;

			while(i5 < string4.length) {
				String string7;
				for(string7 = string4[i5++] + " "; i5 < string4.length && this.getStringWidth(string7 + string4[i5]) < i2; string7 = string7 + string4[i5++] + " ") {
				}

				int i8;
				for(; this.getStringWidth(string7) > i2; string7 = string7.substring(i8)) {
					for(i8 = 0; this.getStringWidth(string7.substring(0, i8 + 1)) <= i2; ++i8) {
					}

					if(string7.substring(0, i8).trim().length() > 0) {
						i6 += this.FONT_HEIGHT;
					}
				}

				if(string7.trim().length() > 0) {
					i6 += this.FONT_HEIGHT;
				}
			}

			if(i6 < this.FONT_HEIGHT) {
				i6 += this.FONT_HEIGHT;
			}

			return i6;
		}
	}

	public void setUnicodeFlag(boolean z1) {
		this.unicodeFlag = z1;
	}

	public void setBidiFlag(boolean z1) {
		this.bidiFlag = z1;
	}

	public List func_50108_c(String string1, int i2) {
		return Arrays.asList(this.func_50113_d(string1, i2).split("\n"));
	}

	String func_50113_d(String string1, int i2) {
		int i3 = this.func_50102_e(string1, i2);
		if(string1.length() <= i3) {
			return string1;
		} else {
			String string4 = string1.substring(0, i3);
			String string5 = func_50114_c(string4) + string1.substring(i3 + (string1.charAt(i3) == 32 ? 1 : 0));
			return string4 + "\n" + this.func_50113_d(string5, i2);
		}
	}

	private int func_50102_e(String string1, int i2) {
		int i3 = string1.length();
		int i4 = 0;
		int i5 = 0;
		int i6 = -1;

		for(boolean z7 = false; i5 < i3; ++i5) {
			char c8 = string1.charAt(i5);
			switch(c8) {
			case ' ':
				i6 = i5;
			default:
				i4 += this.func_50105_a(c8);
				if(z7) {
					++i4;
				}
				break;
			case '\u00a7':
				if(i5 != i3) {
					++i5;
					char c9 = string1.charAt(i5);
					if(c9 != 108 && c9 != 76) {
						if(c9 == 114 || c9 == 82) {
							z7 = false;
						}
					} else {
						z7 = true;
					}
				}
			}

			if(c8 == 10) {
				++i5;
				i6 = i5;
				break;
			}

			if(i4 > i2) {
				break;
			}
		}

		return i5 != i3 && i6 != -1 && i6 < i5 ? i6 : i5;
	}

	private static boolean func_50110_b(char c0) {
		return c0 >= 48 && c0 <= 57 || c0 >= 97 && c0 <= 102 || c0 >= 65 && c0 <= 70;
	}

	private static boolean func_50109_c(char c0) {
		return c0 >= 107 && c0 <= 111 || c0 >= 75 && c0 <= 79 || c0 == 114 || c0 == 82;
	}

	private static String func_50114_c(String string0) {
		String string1 = "";
		int i2 = -1;
		int i3 = string0.length();

		while((i2 = string0.indexOf(167, i2 + 1)) != -1) {
			if(i2 < i3 - 1) {
				char c4 = string0.charAt(i2 + 1);
				if(func_50110_b(c4)) {
					string1 = "\u00a7" + c4;
				} else if(func_50109_c(c4)) {
					string1 = string1 + "\u00a7" + c4;
				}
			}
		}

		return string1;
	}

	public static String func_52014_d(String string0) {
		return field_52015_r.matcher(string0).replaceAll("");
	}
}
