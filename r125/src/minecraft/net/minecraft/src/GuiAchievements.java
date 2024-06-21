package net.minecraft.src;

import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiAchievements extends GuiScreen {
	private static final int guiMapTop = AchievementList.minDisplayColumn * 24 - 112;
	private static final int guiMapLeft = AchievementList.minDisplayRow * 24 - 112;
	private static final int guiMapBottom = AchievementList.maxDisplayColumn * 24 - 77;
	private static final int guiMapRight = AchievementList.maxDisplayRow * 24 - 77;
	protected int achievementsPaneWidth = 256;
	protected int achievementsPaneHeight = 202;
	protected int mouseX = 0;
	protected int mouseY = 0;
	protected double field_27116_m;
	protected double field_27115_n;
	protected double guiMapX;
	protected double guiMapY;
	protected double field_27112_q;
	protected double field_27111_r;
	private int isMouseButtonDown = 0;
	private StatFileWriter statFileWriter;

	public GuiAchievements(StatFileWriter statFileWriter1) {
		this.statFileWriter = statFileWriter1;
		short s2 = 141;
		short s3 = 141;
		this.field_27116_m = this.guiMapX = this.field_27112_q = (double)(AchievementList.openInventory.displayColumn * 24 - s2 / 2 - 12);
		this.field_27115_n = this.guiMapY = this.field_27111_r = (double)(AchievementList.openInventory.displayRow * 24 - s3 / 2);
	}

	public void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiSmallButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20, StatCollector.translateToLocal("gui.done")));
	}

	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.id == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}

		super.actionPerformed(guiButton1);
	}

	protected void keyTyped(char c1, int i2) {
		if(i2 == this.mc.gameSettings.keyBindInventory.keyCode) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		} else {
			super.keyTyped(c1, i2);
		}

	}

	public void drawScreen(int i1, int i2, float f3) {
		if(Mouse.isButtonDown(0)) {
			int i4 = (this.width - this.achievementsPaneWidth) / 2;
			int i5 = (this.height - this.achievementsPaneHeight) / 2;
			int i6 = i4 + 8;
			int i7 = i5 + 17;
			if((this.isMouseButtonDown == 0 || this.isMouseButtonDown == 1) && i1 >= i6 && i1 < i6 + 224 && i2 >= i7 && i2 < i7 + 155) {
				if(this.isMouseButtonDown == 0) {
					this.isMouseButtonDown = 1;
				} else {
					this.guiMapX -= (double)(i1 - this.mouseX);
					this.guiMapY -= (double)(i2 - this.mouseY);
					this.field_27112_q = this.field_27116_m = this.guiMapX;
					this.field_27111_r = this.field_27115_n = this.guiMapY;
				}

				this.mouseX = i1;
				this.mouseY = i2;
			}

			if(this.field_27112_q < (double)guiMapTop) {
				this.field_27112_q = (double)guiMapTop;
			}

			if(this.field_27111_r < (double)guiMapLeft) {
				this.field_27111_r = (double)guiMapLeft;
			}

			if(this.field_27112_q >= (double)guiMapBottom) {
				this.field_27112_q = (double)(guiMapBottom - 1);
			}

			if(this.field_27111_r >= (double)guiMapRight) {
				this.field_27111_r = (double)(guiMapRight - 1);
			}
		} else {
			this.isMouseButtonDown = 0;
		}

		this.drawDefaultBackground();
		this.genAchievementBackground(i1, i2, f3);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		this.func_27110_k();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void updateScreen() {
		this.field_27116_m = this.guiMapX;
		this.field_27115_n = this.guiMapY;
		double d1 = this.field_27112_q - this.guiMapX;
		double d3 = this.field_27111_r - this.guiMapY;
		if(d1 * d1 + d3 * d3 < 4.0D) {
			this.guiMapX += d1;
			this.guiMapY += d3;
		} else {
			this.guiMapX += d1 * 0.85D;
			this.guiMapY += d3 * 0.85D;
		}

	}

	protected void func_27110_k() {
		int i1 = (this.width - this.achievementsPaneWidth) / 2;
		int i2 = (this.height - this.achievementsPaneHeight) / 2;
		this.fontRenderer.drawString("Achievements", i1 + 15, i2 + 5, 4210752);
	}

	protected void genAchievementBackground(int i1, int i2, float f3) {
		int i4 = MathHelper.floor_double(this.field_27116_m + (this.guiMapX - this.field_27116_m) * (double)f3);
		int i5 = MathHelper.floor_double(this.field_27115_n + (this.guiMapY - this.field_27115_n) * (double)f3);
		if(i4 < guiMapTop) {
			i4 = guiMapTop;
		}

		if(i5 < guiMapLeft) {
			i5 = guiMapLeft;
		}

		if(i4 >= guiMapBottom) {
			i4 = guiMapBottom - 1;
		}

		if(i5 >= guiMapRight) {
			i5 = guiMapRight - 1;
		}

		int i6 = this.mc.renderEngine.getTexture("/terrain.png");
		int i7 = this.mc.renderEngine.getTexture("/achievement/bg.png");
		int i8 = (this.width - this.achievementsPaneWidth) / 2;
		int i9 = (this.height - this.achievementsPaneHeight) / 2;
		int i10 = i8 + 16;
		int i11 = i9 + 17;
		this.zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_GEQUAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		this.mc.renderEngine.bindTexture(i6);
		int i12 = i4 + 288 >> 4;
		int i13 = i5 + 288 >> 4;
		int i14 = (i4 + 288) % 16;
		int i15 = (i5 + 288) % 16;
		Random random21 = new Random();

		int i22;
		int i24;
		int i25;
		int i26;
		for(i22 = 0; i22 * 16 - i15 < 155; ++i22) {
			float f23 = 0.6F - (float)(i13 + i22) / 25.0F * 0.3F;
			GL11.glColor4f(f23, f23, f23, 1.0F);

			for(i24 = 0; i24 * 16 - i14 < 224; ++i24) {
				random21.setSeed((long)(1234 + i12 + i24));
				random21.nextInt();
				i25 = random21.nextInt(1 + i13 + i22) + (i13 + i22) / 2;
				i26 = Block.sand.blockIndexInTexture;
				if(i25 <= 37 && i13 + i22 != 35) {
					if(i25 == 22) {
						if(random21.nextInt(2) == 0) {
							i26 = Block.oreDiamond.blockIndexInTexture;
						} else {
							i26 = Block.oreRedstone.blockIndexInTexture;
						}
					} else if(i25 == 10) {
						i26 = Block.oreIron.blockIndexInTexture;
					} else if(i25 == 8) {
						i26 = Block.oreCoal.blockIndexInTexture;
					} else if(i25 > 4) {
						i26 = Block.stone.blockIndexInTexture;
					} else if(i25 > 0) {
						i26 = Block.dirt.blockIndexInTexture;
					}
				} else {
					i26 = Block.bedrock.blockIndexInTexture;
				}

				this.drawTexturedModalRect(i10 + i24 * 16 - i14, i11 + i22 * 16 - i15, i26 % 16 << 4, i26 >> 4 << 4, 16, 16);
			}
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		int i27;
		int i30;
		for(i22 = 0; i22 < AchievementList.achievementList.size(); ++i22) {
			Achievement achievement33 = (Achievement)AchievementList.achievementList.get(i22);
			if(achievement33.parentAchievement != null) {
				i24 = achievement33.displayColumn * 24 - i4 + 11 + i10;
				i25 = achievement33.displayRow * 24 - i5 + 11 + i11;
				i26 = achievement33.parentAchievement.displayColumn * 24 - i4 + 11 + i10;
				i27 = achievement33.parentAchievement.displayRow * 24 - i5 + 11 + i11;
				boolean z28 = this.statFileWriter.hasAchievementUnlocked(achievement33);
				boolean z29 = this.statFileWriter.canUnlockAchievement(achievement33);
				i30 = Math.sin((double)(System.currentTimeMillis() % 600L) / 600.0D * Math.PI * 2.0D) > 0.6D ? 255 : 130;
				int i31 = 0xFF000000;
				if(z28) {
					i31 = -9408400;
				} else if(z29) {
					i31 = 65280 + (i30 << 24);
				}

				this.drawHorizontalLine(i24, i26, i25, i31);
				this.drawVerticalLine(i26, i25, i27, i31);
			}
		}

		Achievement achievement32 = null;
		RenderItem renderItem34 = new RenderItem();
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);

		int i39;
		int i40;
		for(i24 = 0; i24 < AchievementList.achievementList.size(); ++i24) {
			Achievement achievement35 = (Achievement)AchievementList.achievementList.get(i24);
			i26 = achievement35.displayColumn * 24 - i4;
			i27 = achievement35.displayRow * 24 - i5;
			if(i26 >= -24 && i27 >= -24 && i26 <= 224 && i27 <= 155) {
				float f38;
				if(this.statFileWriter.hasAchievementUnlocked(achievement35)) {
					f38 = 1.0F;
					GL11.glColor4f(f38, f38, f38, 1.0F);
				} else if(this.statFileWriter.canUnlockAchievement(achievement35)) {
					f38 = Math.sin((double)(System.currentTimeMillis() % 600L) / 600.0D * Math.PI * 2.0D) < 0.6D ? 0.6F : 0.8F;
					GL11.glColor4f(f38, f38, f38, 1.0F);
				} else {
					f38 = 0.3F;
					GL11.glColor4f(f38, f38, f38, 1.0F);
				}

				this.mc.renderEngine.bindTexture(i7);
				i39 = i10 + i26;
				i40 = i11 + i27;
				if(achievement35.getSpecial()) {
					this.drawTexturedModalRect(i39 - 2, i40 - 2, 26, 202, 26, 26);
				} else {
					this.drawTexturedModalRect(i39 - 2, i40 - 2, 0, 202, 26, 26);
				}

				if(!this.statFileWriter.canUnlockAchievement(achievement35)) {
					float f41 = 0.1F;
					GL11.glColor4f(f41, f41, f41, 1.0F);
					renderItem34.field_27004_a = false;
				}

				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_CULL_FACE);
				renderItem34.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, achievement35.theItemStack, i39 + 3, i40 + 3);
				GL11.glDisable(GL11.GL_LIGHTING);
				if(!this.statFileWriter.canUnlockAchievement(achievement35)) {
					renderItem34.field_27004_a = true;
				}

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				if(i1 >= i10 && i2 >= i11 && i1 < i10 + 224 && i2 < i11 + 155 && i1 >= i39 && i1 <= i39 + 22 && i2 >= i40 && i2 <= i40 + 22) {
					achievement32 = achievement35;
				}
			}
		}

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(i7);
		this.drawTexturedModalRect(i8, i9, 0, 0, this.achievementsPaneWidth, this.achievementsPaneHeight);
		GL11.glPopMatrix();
		this.zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		super.drawScreen(i1, i2, f3);
		if(achievement32 != null) {
			String string36 = StatCollector.translateToLocal(achievement32.getName());
			String string37 = achievement32.getDescription();
			i26 = i1 + 12;
			i27 = i2 - 4;
			if(this.statFileWriter.canUnlockAchievement(achievement32)) {
				i39 = Math.max(this.fontRenderer.getStringWidth(string36), 120);
				i40 = this.fontRenderer.splitStringWidth(string37, i39);
				if(this.statFileWriter.hasAchievementUnlocked(achievement32)) {
					i40 += 12;
				}

				this.drawGradientRect(i26 - 3, i27 - 3, i26 + i39 + 3, i27 + i40 + 3 + 12, -1073741824, -1073741824);
				this.fontRenderer.drawSplitString(string37, i26, i27 + 12, i39, -6250336);
				if(this.statFileWriter.hasAchievementUnlocked(achievement32)) {
					this.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("achievement.taken"), i26, i27 + i40 + 4, -7302913);
				}
			} else {
				i39 = Math.max(this.fontRenderer.getStringWidth(string36), 120);
				String string42 = StatCollector.translateToLocalFormatted("achievement.requires", new Object[]{StatCollector.translateToLocal(achievement32.parentAchievement.getName())});
				i30 = this.fontRenderer.splitStringWidth(string42, i39);
				this.drawGradientRect(i26 - 3, i27 - 3, i26 + i39 + 3, i27 + i30 + 12 + 3, -1073741824, -1073741824);
				this.fontRenderer.drawSplitString(string42, i26, i27 + 12, i39, -9416624);
			}

			this.fontRenderer.drawStringWithShadow(string36, i26, i27, this.statFileWriter.canUnlockAchievement(achievement32) ? (achievement32.getSpecial() ? -128 : -1) : (achievement32.getSpecial() ? -8355776 : -8355712));
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		RenderHelper.disableStandardItemLighting();
	}

	public boolean doesGuiPauseGame() {
		return true;
	}
}
