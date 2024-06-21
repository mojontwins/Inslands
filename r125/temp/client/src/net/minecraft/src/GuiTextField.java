package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiTextField extends Gui {
	private final FontRenderer fontRenderer;
	private final int xPos;
	private final int yPos;
	private final int width;
	private final int height;
	private String text = "";
	private int maxStringLength = 32;
	private int cursorCounter;
	private boolean field_50044_j = true;
	private boolean field_50045_k = true;
	private boolean isFocused = false;
	private boolean field_50043_m = true;
	private int field_50041_n = 0;
	private int field_50042_o = 0;
	private int field_50048_p = 0;
	private int field_50047_q = 14737632;
	private int field_50046_r = 7368816;

	public GuiTextField(FontRenderer fontRenderer1, int i2, int i3, int i4, int i5) {
		this.fontRenderer = fontRenderer1;
		this.xPos = i2;
		this.yPos = i3;
		this.width = i4;
		this.height = i5;
	}

	public void updateCursorCounter() {
		++this.cursorCounter;
	}

	public void setText(String string1) {
		if(string1.length() > this.maxStringLength) {
			this.text = string1.substring(0, this.maxStringLength);
		} else {
			this.text = string1;
		}

		this.func_50038_e();
	}

	public String getText() {
		return this.text;
	}

	public String func_50039_c() {
		int i1 = this.field_50042_o < this.field_50048_p ? this.field_50042_o : this.field_50048_p;
		int i2 = this.field_50042_o < this.field_50048_p ? this.field_50048_p : this.field_50042_o;
		return this.text.substring(i1, i2);
	}

	public void func_50031_b(String string1) {
		String string2 = "";
		String string3 = ChatAllowedCharacters.func_52019_a(string1);
		int i4 = this.field_50042_o < this.field_50048_p ? this.field_50042_o : this.field_50048_p;
		int i5 = this.field_50042_o < this.field_50048_p ? this.field_50048_p : this.field_50042_o;
		int i6 = this.maxStringLength - this.text.length() - (i4 - this.field_50048_p);
		boolean z7 = false;
		if(this.text.length() > 0) {
			string2 = string2 + this.text.substring(0, i4);
		}

		int i8;
		if(i6 < string3.length()) {
			string2 = string2 + string3.substring(0, i6);
			i8 = i6;
		} else {
			string2 = string2 + string3;
			i8 = string3.length();
		}

		if(this.text.length() > 0 && i5 < this.text.length()) {
			string2 = string2 + this.text.substring(i5);
		}

		this.text = string2;
		this.func_50023_d(i4 - this.field_50048_p + i8);
	}

	public void func_50021_a(int i1) {
		if(this.text.length() != 0) {
			if(this.field_50048_p != this.field_50042_o) {
				this.func_50031_b("");
			} else {
				this.func_50020_b(this.func_50028_c(i1) - this.field_50042_o);
			}
		}
	}

	public void func_50020_b(int i1) {
		if(this.text.length() != 0) {
			if(this.field_50048_p != this.field_50042_o) {
				this.func_50031_b("");
			} else {
				boolean z2 = i1 < 0;
				int i3 = z2 ? this.field_50042_o + i1 : this.field_50042_o;
				int i4 = z2 ? this.field_50042_o : this.field_50042_o + i1;
				String string5 = "";
				if(i3 >= 0) {
					string5 = this.text.substring(0, i3);
				}

				if(i4 < this.text.length()) {
					string5 = string5 + this.text.substring(i4);
				}

				this.text = string5;
				if(z2) {
					this.func_50023_d(i1);
				}

			}
		}
	}

	public int func_50028_c(int i1) {
		return this.func_50024_a(i1, this.func_50035_h());
	}

	public int func_50024_a(int i1, int i2) {
		int i3 = i2;
		boolean z4 = i1 < 0;
		int i5 = Math.abs(i1);

		for(int i6 = 0; i6 < i5; ++i6) {
			if(!z4) {
				int i7 = this.text.length();
				i3 = this.text.indexOf(32, i3);
				if(i3 == -1) {
					i3 = i7;
				} else {
					while(i3 < i7 && this.text.charAt(i3) == 32) {
						++i3;
					}
				}
			} else {
				while(i3 > 0 && this.text.charAt(i3 - 1) == 32) {
					--i3;
				}

				while(i3 > 0 && this.text.charAt(i3 - 1) != 32) {
					--i3;
				}
			}
		}

		return i3;
	}

	public void func_50023_d(int i1) {
		this.func_50030_e(this.field_50048_p + i1);
	}

	public void func_50030_e(int i1) {
		this.field_50042_o = i1;
		int i2 = this.text.length();
		if(this.field_50042_o < 0) {
			this.field_50042_o = 0;
		}

		if(this.field_50042_o > i2) {
			this.field_50042_o = i2;
		}

		this.func_50032_g(this.field_50042_o);
	}

	public void func_50034_d() {
		this.func_50030_e(0);
	}

	public void func_50038_e() {
		this.func_50030_e(this.text.length());
	}

	public boolean func_50037_a(char c1, int i2) {
		if(this.field_50043_m && this.isFocused) {
			switch(c1) {
			case '\u0001':
				this.func_50038_e();
				this.func_50032_g(0);
				return true;
			case '\u0003':
				GuiScreen.func_50050_a(this.func_50039_c());
				return true;
			case '\u0016':
				this.func_50031_b(GuiScreen.getClipboardString());
				return true;
			case '\u0018':
				GuiScreen.func_50050_a(this.func_50039_c());
				this.func_50031_b("");
				return true;
			default:
				switch(i2) {
				case 14:
					if(GuiScreen.func_50051_l()) {
						this.func_50021_a(-1);
					} else {
						this.func_50020_b(-1);
					}

					return true;
				case 199:
					if(GuiScreen.func_50049_m()) {
						this.func_50032_g(0);
					} else {
						this.func_50034_d();
					}

					return true;
				case 203:
					if(GuiScreen.func_50049_m()) {
						if(GuiScreen.func_50051_l()) {
							this.func_50032_g(this.func_50024_a(-1, this.func_50036_k()));
						} else {
							this.func_50032_g(this.func_50036_k() - 1);
						}
					} else if(GuiScreen.func_50051_l()) {
						this.func_50030_e(this.func_50028_c(-1));
					} else {
						this.func_50023_d(-1);
					}

					return true;
				case 205:
					if(GuiScreen.func_50049_m()) {
						if(GuiScreen.func_50051_l()) {
							this.func_50032_g(this.func_50024_a(1, this.func_50036_k()));
						} else {
							this.func_50032_g(this.func_50036_k() + 1);
						}
					} else if(GuiScreen.func_50051_l()) {
						this.func_50030_e(this.func_50028_c(1));
					} else {
						this.func_50023_d(1);
					}

					return true;
				case 207:
					if(GuiScreen.func_50049_m()) {
						this.func_50032_g(this.text.length());
					} else {
						this.func_50038_e();
					}

					return true;
				case 211:
					if(GuiScreen.func_50051_l()) {
						this.func_50021_a(1);
					} else {
						this.func_50020_b(1);
					}

					return true;
				default:
					if(ChatAllowedCharacters.isAllowedCharacter(c1)) {
						this.func_50031_b(Character.toString(c1));
						return true;
					} else {
						return false;
					}
				}
			}
		} else {
			return false;
		}
	}

	public void mouseClicked(int i1, int i2, int i3) {
		boolean z4 = i1 >= this.xPos && i1 < this.xPos + this.width && i2 >= this.yPos && i2 < this.yPos + this.height;
		if(this.field_50045_k) {
			this.func_50033_b(this.field_50043_m && z4);
		}

		if(this.isFocused && i3 == 0) {
			int i5 = i1 - this.xPos;
			if(this.field_50044_j) {
				i5 -= 4;
			}

			String string6 = this.fontRenderer.func_50107_a(this.text.substring(this.field_50041_n), this.func_50019_l());
			this.func_50030_e(this.fontRenderer.func_50107_a(string6, i5).length() + this.field_50041_n);
		}

	}

	public void drawTextBox() {
		if(this.func_50022_i()) {
			drawRect(this.xPos - 1, this.yPos - 1, this.xPos + this.width + 1, this.yPos + this.height + 1, -6250336);
			drawRect(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, 0xFF000000);
		}

		int i1 = this.field_50043_m ? this.field_50047_q : this.field_50046_r;
		int i2 = this.field_50042_o - this.field_50041_n;
		int i3 = this.field_50048_p - this.field_50041_n;
		String string4 = this.fontRenderer.func_50107_a(this.text.substring(this.field_50041_n), this.func_50019_l());
		boolean z5 = i2 >= 0 && i2 <= string4.length();
		boolean z6 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && z5;
		int i7 = this.field_50044_j ? this.xPos + 4 : this.xPos;
		int i8 = this.field_50044_j ? this.yPos + (this.height - 8) / 2 : this.yPos;
		int i9 = i7;
		if(i3 > string4.length()) {
			i3 = string4.length();
		}

		if(string4.length() > 0) {
			String string10 = z5 ? string4.substring(0, i2) : string4;
			i9 = this.fontRenderer.drawStringWithShadow(string10, i7, i8, i1);
		}

		boolean z13 = this.field_50042_o < this.text.length() || this.text.length() >= this.func_50040_g();
		int i11 = i9;
		if(!z5) {
			i11 = i2 > 0 ? i7 + this.width : i7;
		} else if(z13) {
			i11 = i9 - 1;
			--i9;
		}

		if(string4.length() > 0 && z5 && i2 < string4.length()) {
			this.fontRenderer.drawStringWithShadow(string4.substring(i2), i9, i8, i1);
		}

		if(z6) {
			if(z13) {
				Gui.drawRect(i11, i8 - 1, i11 + 1, i8 + 1 + this.fontRenderer.FONT_HEIGHT, -3092272);
			} else {
				this.fontRenderer.drawStringWithShadow("_", i11, i8, i1);
			}
		}

		if(i3 != i2) {
			int i12 = i7 + this.fontRenderer.getStringWidth(string4.substring(0, i3));
			this.func_50029_c(i11, i8 - 1, i12 - 1, i8 + 1 + this.fontRenderer.FONT_HEIGHT);
		}

	}

	private void func_50029_c(int i1, int i2, int i3, int i4) {
		int i5;
		if(i1 < i3) {
			i5 = i1;
			i1 = i3;
			i3 = i5;
		}

		if(i2 < i4) {
			i5 = i2;
			i2 = i4;
			i4 = i5;
		}

		Tessellator tessellator6 = Tessellator.instance;
		GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glLogicOp(GL11.GL_OR_REVERSE);
		tessellator6.startDrawingQuads();
		tessellator6.addVertex((double)i1, (double)i4, 0.0D);
		tessellator6.addVertex((double)i3, (double)i4, 0.0D);
		tessellator6.addVertex((double)i3, (double)i2, 0.0D);
		tessellator6.addVertex((double)i1, (double)i2, 0.0D);
		tessellator6.draw();
		GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void setMaxStringLength(int i1) {
		this.maxStringLength = i1;
		if(this.text.length() > i1) {
			this.text = this.text.substring(0, i1);
		}

	}

	public int func_50040_g() {
		return this.maxStringLength;
	}

	public int func_50035_h() {
		return this.field_50042_o;
	}

	public boolean func_50022_i() {
		return this.field_50044_j;
	}

	public void func_50027_a(boolean z1) {
		this.field_50044_j = z1;
	}

	public void func_50033_b(boolean z1) {
		if(z1 && !this.isFocused) {
			this.cursorCounter = 0;
		}

		this.isFocused = z1;
	}

	public boolean func_50025_j() {
		return this.isFocused;
	}

	public int func_50036_k() {
		return this.field_50048_p;
	}

	public int func_50019_l() {
		return this.func_50022_i() ? this.width - 8 : this.width;
	}

	public void func_50032_g(int i1) {
		int i2 = this.text.length();
		if(i1 > i2) {
			i1 = i2;
		}

		if(i1 < 0) {
			i1 = 0;
		}

		this.field_50048_p = i1;
		if(this.fontRenderer != null) {
			if(this.field_50041_n > i2) {
				this.field_50041_n = i2;
			}

			int i3 = this.func_50019_l();
			String string4 = this.fontRenderer.func_50107_a(this.text.substring(this.field_50041_n), i3);
			int i5 = string4.length() + this.field_50041_n;
			if(i1 == this.field_50041_n) {
				this.field_50041_n -= this.fontRenderer.func_50104_a(this.text, i3, true).length();
			}

			if(i1 > i5) {
				this.field_50041_n += i1 - i5;
			} else if(i1 <= this.field_50041_n) {
				this.field_50041_n -= this.field_50041_n - i1;
			}

			if(this.field_50041_n < 0) {
				this.field_50041_n = 0;
			}

			if(this.field_50041_n > i2) {
				this.field_50041_n = i2;
			}
		}

	}

	public void func_50026_c(boolean z1) {
		this.field_50045_k = z1;
	}
}
