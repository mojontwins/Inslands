package net.minecraft.src;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiAchievements extends GuiScreen {
	private static final int field_27126_s;
	private static final int field_27125_t;
	private static final int field_27124_u;
	private static final int field_27123_v;
	protected int field_27121_a = 256;
	protected int field_27119_i = 202;
	protected int field_27118_j = 0;
	protected int field_27117_l = 0;
	protected double field_27116_m;
	protected double field_27115_n;
	protected double field_27114_o;
	protected double field_27113_p;
	protected double field_27112_q;
	protected double field_27111_r;
	private int field_27122_w = 0;
	private StatFileWriter field_27120_x;
	private boolean draw = true;
	private static Method met1;
	private static Method met2;

	static {
		try {
			met1 = Class.forName("do").getMethod("a", new Class[]{String.class});
			met2 = Class.forName("do").getMethod("a", new Class[]{String.class, getArrayClass(Object.class)});
		} catch (Exception exception1) {
			exception1.printStackTrace();
		}

		field_27126_s = AchievementList.minDisplayColumn * 24 - 112;
		field_27125_t = AchievementList.minDisplayRow * 24 - 112;
		field_27124_u = AchievementList.maxDisplayColumn * 24 - 77;
		field_27123_v = AchievementList.maxDisplayRow * 24 - 77;
	}

	public GuiAchievements(StatFileWriter xi1) {
		this.field_27120_x = xi1;
		short c1 = 141;
		short c2 = 141;
		this.field_27116_m = this.field_27114_o = this.field_27112_q = (double)(AchievementList.openInventory.displayColumn * 24 - c1 / 2 - 12);
		this.field_27115_n = this.field_27113_p = this.field_27111_r = (double)(AchievementList.openInventory.displayRow * 24 - c2 / 2);
	}

	public void initGui() {
		this.controlList.clear();

		try {
			this.controlList.add(new GuiSmallButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20, (String)met1.invoke((Object)null, new Object[]{"gui.done"})));
		} catch (Exception exception2) {
			exception2.printStackTrace();
		}

		this.controlList.add(new GuiSmallButton(11, this.width / 2 - 113, this.height / 2 + 74, 20, 20, "<"));
		this.controlList.add(new GuiSmallButton(12, this.width / 2 - 93, this.height / 2 + 74, 20, 20, ">"));
	}

	protected void actionPerformed(GuiButton button) {
		if(button.id == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		} else if(button.id == 11) {
			SAPI.acPagePrev();
		} else if(button.id == 12) {
			SAPI.acPageNext();
		}

		super.actionPerformed(button);
	}

	protected void keyTyped(char c1, int i1) {
		if(i1 == this.mc.gameSettings.keyBindInventory.keyCode) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		} else {
			super.keyTyped(c1, i1);
		}

	}

	public void drawScreen(int i1, int j1, float f) {
		if(Mouse.isButtonDown(0)) {
			int k1 = (this.width - this.field_27121_a) / 2;
			int l1 = (this.height - this.field_27119_i) / 2;
			int i2 = k1 + 8;
			int j2 = l1 + 17;
			if((this.field_27122_w == 0 || this.field_27122_w == 1) && i1 >= i2 && i1 < i2 + 224 && j1 >= j2 && j1 < j2 + 155) {
				if(this.field_27122_w == 0) {
					this.field_27122_w = 1;
				} else {
					this.field_27114_o -= (double)(i1 - this.field_27118_j);
					this.field_27113_p -= (double)(j1 - this.field_27117_l);
					this.field_27112_q = this.field_27116_m = this.field_27114_o;
					this.field_27111_r = this.field_27115_n = this.field_27113_p;
				}

				this.field_27118_j = i1;
				this.field_27117_l = j1;
			}

			if(this.field_27112_q < (double)field_27126_s) {
				this.field_27112_q = (double)field_27126_s;
			}

			if(this.field_27111_r < (double)field_27125_t) {
				this.field_27111_r = (double)field_27125_t;
			}

			if(this.field_27112_q >= (double)field_27124_u) {
				this.field_27112_q = (double)(field_27124_u - 1);
			}

			if(this.field_27111_r >= (double)field_27123_v) {
				this.field_27111_r = (double)(field_27123_v - 1);
			}
		} else {
			this.field_27122_w = 0;
		}

		this.drawDefaultBackground();
		this.func_27109_b(i1, j1, f);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		this.func_27110_k();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void updateScreen() {
		this.field_27116_m = this.field_27114_o;
		this.field_27115_n = this.field_27113_p;
		double d = this.field_27112_q - this.field_27114_o;
		double d1 = this.field_27111_r - this.field_27113_p;
		if(d * d + d1 * d1 < 4.0D) {
			this.field_27114_o += d;
			this.field_27113_p += d1;
		} else {
			this.field_27114_o += d * 0.85D;
			this.field_27113_p += d1 * 0.85D;
		}

	}

	protected void func_27110_k() {
		int i1 = (this.width - this.field_27121_a) / 2;
		int j1 = (this.height - this.field_27119_i) / 2;
		this.fontRenderer.drawString("Achievements", i1 + 15, j1 + 5, 4210752);
		this.fontRenderer.drawString(SAPI.acGetCurrentPageTitle(), this.width / 2 - 69, this.height / 2 + 80, 0);
	}

	protected void func_27109_b(int i1, int j1, float f) {
		int k1 = MathHelper.floor_double(this.field_27116_m + (this.field_27114_o - this.field_27116_m) * (double)f);
		int l1 = MathHelper.floor_double(this.field_27115_n + (this.field_27113_p - this.field_27115_n) * (double)f);
		if(k1 < field_27126_s) {
			k1 = field_27126_s;
		}

		if(l1 < field_27125_t) {
			l1 = field_27125_t;
		}

		if(k1 >= field_27124_u) {
			k1 = field_27124_u - 1;
		}

		if(l1 >= field_27123_v) {
			l1 = field_27123_v - 1;
		}

		int i2 = this.mc.renderEngine.getTexture("/terrain.png");
		int j2 = this.mc.renderEngine.getTexture("/achievement/bg.png");
		int k2 = (this.width - this.field_27121_a) / 2;
		int l2 = (this.height - this.field_27119_i) / 2;
		int i3 = k2 + 16;
		int j3 = l2 + 17;
		this.zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_GEQUAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		this.mc.renderEngine.bindTexture(i2);
		int k3 = k1 + 288 >> 4;
		int i4 = l1 + 288 >> 4;
		int j4 = (k1 + 288) % 16;
		int i5 = (l1 + 288) % 16;
		Random random = new Random();

		int ny1;
		int ny3;
		int s1;
		for(ny1 = 0; ny1 * 16 - i5 < 155; ++ny1) {
			float bb1 = 0.6F - (float)(i4 + ny1) / 25.0F * 0.3F;
			GL11.glColor4f(bb1, bb1, bb1, 1.0F);

			for(ny3 = 0; ny3 * 16 - j4 < 224; ++ny3) {
				random.setSeed((long)(1234 + k3 + ny3));
				random.nextInt();
				s1 = SAPI.acGetCurrentPage().bgGetSprite(random, k3 + ny3, i4 + ny1);
				if(s1 != -1) {
					this.drawTexturedModalRect(i3 + ny3 * 16 - j4, j3 + ny1 * 16 - i5, s1 % 16 << 4, s1 >> 4 << 4, 16, 16);
				}
			}
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		int s2;
		int k6;
		int k8;
		int i36;
		for(ny1 = 0; ny1 < AchievementList.achievementList.size(); ++ny1) {
			Achievement achievement30 = (Achievement)AchievementList.achievementList.get(ny1);
			if(achievement30.parentAchievement != null) {
				ny3 = achievement30.displayColumn * 24 - k1 + 11 + i3;
				s1 = achievement30.displayRow * 24 - l1 + 11 + j3;
				s2 = achievement30.parentAchievement.displayColumn * 24 - k1 + 11 + i3;
				k6 = achievement30.parentAchievement.displayRow * 24 - l1 + 11 + j3;
				boolean j7 = false;
				boolean e = this.field_27120_x.hasAchievementUnlocked(achievement30);
				boolean s3 = this.field_27120_x.func_27181_b(achievement30);
				k8 = Math.sin((double)(System.currentTimeMillis() % 600L) / 600.0D * Math.PI * 2.0D) <= 0.6D ? 130 : 255;
				if(e) {
					i36 = -9408400;
				} else if(s3) {
					i36 = 65280 + (k8 << 24);
				} else {
					i36 = 0xFF000000;
				}

				this.draw = this.isVisibleLine(achievement30);
				this.func_27100_a(ny3, s2, s1, i36);
				this.func_27099_b(s2, s1, k6, i36);
			}
		}

		Achievement achievement29 = null;
		RenderItem renderItem31 = new RenderItem();
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);

		int i37;
		for(ny3 = 0; ny3 < AchievementList.achievementList.size(); ++ny3) {
			Achievement achievement32 = (Achievement)AchievementList.achievementList.get(ny3);
			if(this.isVisibleAchievement(achievement32, 1)) {
				s2 = achievement32.displayColumn * 24 - k1;
				k6 = achievement32.displayRow * 24 - l1;
				if(s2 >= -24 && k6 >= -24 && s2 <= 224 && k6 <= 155) {
					float f38;
					if(this.field_27120_x.hasAchievementUnlocked(achievement32)) {
						f38 = 1.0F;
						GL11.glColor4f(f38, f38, f38, 1.0F);
					} else if(this.field_27120_x.func_27181_b(achievement32)) {
						f38 = Math.sin((double)(System.currentTimeMillis() % 600L) / 600.0D * Math.PI * 2.0D) >= 0.6D ? 0.8F : 0.6F;
						GL11.glColor4f(f38, f38, f38, 1.0F);
					} else {
						f38 = 0.3F;
						GL11.glColor4f(f38, f38, f38, 1.0F);
					}

					this.mc.renderEngine.bindTexture(j2);
					i36 = i3 + s2;
					i37 = j3 + k6;
					if(achievement32.getSpecial()) {
						this.drawTexturedModalRect(i36 - 2, i37 - 2, 26, 202, 26, 26);
					} else {
						this.drawTexturedModalRect(i36 - 2, i37 - 2, 0, 202, 26, 26);
					}

					if(!this.field_27120_x.func_27181_b(achievement32)) {
						float f39 = 0.1F;
						GL11.glColor4f(f39, f39, f39, 1.0F);
						renderItem31.field_27004_a = false;
					}

					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glEnable(GL11.GL_CULL_FACE);
					renderItem31.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, achievement32.theItemStack, i36 + 3, i37 + 3);
					GL11.glDisable(GL11.GL_LIGHTING);
					if(!this.field_27120_x.func_27181_b(achievement32)) {
						renderItem31.field_27004_a = true;
					}

					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					if(i1 >= i3 && j1 >= j3 && i1 < i3 + 224 && j1 < j3 + 155 && i1 >= i36 && i1 <= i36 + 22 && j1 >= i37 && j1 <= i37 + 22) {
						achievement29 = achievement32;
					}
				}
			}
		}

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(j2);
		this.drawTexturedModalRect(k2, l2, 0, 0, this.field_27121_a, this.field_27119_i);
		GL11.glPopMatrix();
		this.zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		super.drawScreen(i1, j1, f);
		if(achievement29 != null) {
			Achievement achievement33 = achievement29;
			String string34 = achievement29.statName;
			String string35 = achievement29.getDescription();
			k6 = i1 + 12;
			i36 = j1 - 4;
			if(this.field_27120_x.func_27181_b(achievement29)) {
				i37 = Math.max(this.fontRenderer.getStringWidth(string34), 120);
				int i40 = this.fontRenderer.func_27277_a(string35, i37);
				if(this.field_27120_x.hasAchievementUnlocked(achievement29)) {
					i40 += 12;
				}

				this.drawGradientRect(k6 - 3, i36 - 3, k6 + i37 + 3, i36 + i40 + 3 + 12, -1073741824, -1073741824);
				this.fontRenderer.func_27278_a(string35, k6, i36 + 12, i37, -6250336);
				if(this.field_27120_x.hasAchievementUnlocked(achievement29)) {
					try {
						this.fontRenderer.drawStringWithShadow((String)met1.invoke((Object)null, new Object[]{"achievement.taken"}), k6, i36 + i40 + 4, -7302913);
					} catch (Exception exception28) {
						exception28.printStackTrace();
					}
				}
			} else {
				try {
					i37 = Math.max(this.fontRenderer.getStringWidth(string34), 120);
					String string41 = (String)met2.invoke((Object)null, new Object[]{"achievement.requires", new Object[]{achievement33.parentAchievement.statName}});
					k8 = this.fontRenderer.func_27277_a(string41, i37);
					this.drawGradientRect(k6 - 3, i36 - 3, k6 + i37 + 3, i36 + k8 + 12 + 3, -1073741824, -1073741824);
					this.fontRenderer.func_27278_a(string41, k6, i36 + 12, i37, -9416624);
				} catch (Exception exception27) {
					exception27.printStackTrace();
				}
			}

			this.fontRenderer.drawStringWithShadow(string34, k6, i36, this.field_27120_x.func_27181_b(achievement29) ? (achievement29.getSpecial() ? -128 : -1) : (achievement29.getSpecial() ? -8355776 : -8355712));
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		RenderHelper.disableStandardItemLighting();
	}

	public boolean doesGuiPauseGame() {
		return true;
	}

	protected void drawRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
		int i;
		if(paramInt1 < paramInt3) {
			i = paramInt1;
			paramInt1 = paramInt3;
			paramInt3 = i;
		}

		if(paramInt2 < paramInt4) {
			i = paramInt2;
			paramInt2 = paramInt4;
			paramInt4 = i;
		}

		float f1 = (float)(paramInt5 >> 24 & 255) / 255.0F;
		float f2 = (float)(paramInt5 >> 16 & 255) / 255.0F;
		float f3 = (float)(paramInt5 >> 8 & 255) / 255.0F;
		float f4 = (float)(paramInt5 & 255) / 255.0F;
		Tessellator localns = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(f2, f3, f4, f1);
		if(this.draw) {
			localns.startDrawingQuads();
			localns.addVertex((double)paramInt1, (double)paramInt4, 0.0D);
			localns.addVertex((double)paramInt3, (double)paramInt4, 0.0D);
			localns.addVertex((double)paramInt3, (double)paramInt2, 0.0D);
			localns.addVertex((double)paramInt1, (double)paramInt2, 0.0D);
			localns.draw();
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public boolean isVisibleAchievement(Achievement achievement, int deep) {
		if(this.checkHidden(achievement)) {
			return false;
		} else {
			int tabID = SAPI.acGetPage(achievement).id;
			if(tabID == SAPI.acCurrentPage) {
				return true;
			} else {
				if(deep >= 1) {
					ArrayList list = new ArrayList(AchievementList.achievementList);

					int i;
					Achievement tmpAc;
					for(i = 0; i < list.size(); ++i) {
						tmpAc = (Achievement)list.get(i);
						if(tmpAc.statId == achievement.statId) {
							list.remove(i--);
						} else if(tmpAc.parentAchievement == null) {
							list.remove(i--);
						} else if(tmpAc.parentAchievement.statId != achievement.statId) {
							list.remove(i--);
						}
					}

					for(i = 0; i < list.size(); ++i) {
						tmpAc = (Achievement)list.get(i);
						if(this.isVisibleAchievement(tmpAc, deep - 1)) {
							return true;
						}
					}
				}

				return false;
			}
		}
	}

	public boolean isVisibleLine(Achievement achievement) {
		return achievement.parentAchievement != null && this.isVisibleAchievement(achievement, 1) && this.isVisibleAchievement(achievement.parentAchievement, 1);
	}

	public boolean checkHidden(Achievement achievement) {
		return this.mc.statFileWriter.hasAchievementUnlocked(achievement) ? false : (SAPI.acIsHidden(achievement) ? true : (achievement.parentAchievement == null ? false : this.checkHidden(achievement.parentAchievement)));
	}

	public static Class getArrayClass(Class c) {
		try {
			Object var2 = Array.newInstance(c, 0);
			return var2.getClass();
		} catch (Exception exception2) {
			throw new IllegalArgumentException(exception2);
		}
	}
}
