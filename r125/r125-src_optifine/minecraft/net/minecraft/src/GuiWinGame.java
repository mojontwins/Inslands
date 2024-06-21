package net.minecraft.src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

public class GuiWinGame extends GuiScreen {
	private int updateCounter = 0;
	private List lines;
	private int field_41042_d = 0;
	private float field_41043_e = 0.5F;

	public void updateScreen() {
		++this.updateCounter;
		float f1 = (float)(this.field_41042_d + this.height + this.height + 24) / this.field_41043_e;
		if((float)this.updateCounter > f1) {
			this.respawnPlayer();
		}

	}

	protected void keyTyped(char c1, int i2) {
		if(i2 == 1) {
			this.respawnPlayer();
		}

	}

	private void respawnPlayer() {
		if(this.mc.theWorld.isRemote) {
			EntityClientPlayerMP entityClientPlayerMP1 = (EntityClientPlayerMP)this.mc.thePlayer;
			entityClientPlayerMP1.sendQueue.addToSendQueue(new Packet9Respawn(entityClientPlayerMP1.dimension, (byte)this.mc.theWorld.difficultySetting, this.mc.theWorld.getWorldInfo().getTerrainType(), this.mc.theWorld.getHeight(), 0));
		} else {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.respawn(this.mc.theWorld.isRemote, 0, true);
		}

	}

	public boolean doesGuiPauseGame() {
		return true;
	}

	public void initGui() {
		if(this.lines == null) {
			this.lines = new ArrayList();

			try {
				String string1 = "";
				String string2 = "\u00a7f\u00a7k\u00a7a\u00a7b";
				short s3 = 274;
				BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(GuiWinGame.class.getResourceAsStream("/title/win.txt"), Charset.forName("UTF-8")));
				Random random5 = new Random(8124371L);

				int i6;
				while((string1 = bufferedReader4.readLine()) != null) {
					String string7;
					String string8;
					for(string1 = string1.replaceAll("PLAYERNAME", this.mc.session.username); string1.indexOf(string2) >= 0; string1 = string7 + "\u00a7f\u00a7k" + "XXXXXXXX".substring(0, random5.nextInt(4) + 3) + string8) {
						i6 = string1.indexOf(string2);
						string7 = string1.substring(0, i6);
						string8 = string1.substring(i6 + string2.length());
					}

					this.lines.addAll(this.mc.fontRenderer.func_50108_c(string1, s3));
					this.lines.add("");
				}

				for(i6 = 0; i6 < 8; ++i6) {
					this.lines.add("");
				}

				bufferedReader4 = new BufferedReader(new InputStreamReader(GuiWinGame.class.getResourceAsStream("/title/credits.txt"), Charset.forName("UTF-8")));

				while((string1 = bufferedReader4.readLine()) != null) {
					string1 = string1.replaceAll("PLAYERNAME", this.mc.session.username);
					string1 = string1.replaceAll("\t", "    ");
					this.lines.addAll(this.mc.fontRenderer.func_50108_c(string1, s3));
					this.lines.add("");
				}

				this.field_41042_d = this.lines.size() * 12;
			} catch (Exception exception9) {
				exception9.printStackTrace();
			}

		}
	}

	protected void actionPerformed(GuiButton guiButton1) {
	}

	private void func_41040_b(int i1, int i2, float f3) {
		Tessellator tessellator4 = Tessellator.instance;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("%blur%/gui/background.png"));
		tessellator4.startDrawingQuads();
		tessellator4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
		int i5 = this.width;
		float f6 = 0.0F - ((float)this.updateCounter + f3) * 0.5F * this.field_41043_e;
		float f7 = (float)this.height - ((float)this.updateCounter + f3) * 0.5F * this.field_41043_e;
		float f8 = 0.015625F;
		float f9 = ((float)this.updateCounter + f3 - 0.0F) * 0.02F;
		float f10 = (float)(this.field_41042_d + this.height + this.height + 24) / this.field_41043_e;
		float f11 = (f10 - 20.0F - ((float)this.updateCounter + f3)) * 0.005F;
		if(f11 < f9) {
			f9 = f11;
		}

		if(f9 > 1.0F) {
			f9 = 1.0F;
		}

		f9 *= f9;
		f9 = f9 * 96.0F / 255.0F;
		tessellator4.setColorOpaque_F(f9, f9, f9);
		tessellator4.addVertexWithUV(0.0D, (double)this.height, (double)this.zLevel, 0.0D, (double)(f6 * f8));
		tessellator4.addVertexWithUV((double)i5, (double)this.height, (double)this.zLevel, (double)((float)i5 * f8), (double)(f6 * f8));
		tessellator4.addVertexWithUV((double)i5, 0.0D, (double)this.zLevel, (double)((float)i5 * f8), (double)(f7 * f8));
		tessellator4.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, 0.0D, (double)(f7 * f8));
		tessellator4.draw();
	}

	public void drawScreen(int i1, int i2, float f3) {
		this.func_41040_b(i1, i2, f3);
		Tessellator tessellator4 = Tessellator.instance;
		short s5 = 274;
		int i6 = this.width / 2 - s5 / 2;
		int i7 = this.height + 50;
		float f8 = -((float)this.updateCounter + f3) * this.field_41043_e;
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, f8, 0.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/title/mclogo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawTexturedModalRect(i6, i7, 0, 0, 155, 44);
		this.drawTexturedModalRect(i6 + 155, i7, 0, 45, 155, 44);
		tessellator4.setColorOpaque_I(0xFFFFFF);
		int i9 = i7 + 200;

		int i10;
		for(i10 = 0; i10 < this.lines.size(); ++i10) {
			if(i10 == this.lines.size() - 1) {
				float f11 = (float)i9 + f8 - (float)(this.height / 2 - 6);
				if(f11 < 0.0F) {
					GL11.glTranslatef(0.0F, -f11, 0.0F);
				}
			}

			if((float)i9 + f8 + 12.0F + 8.0F > 0.0F && (float)i9 + f8 < (float)this.height) {
				String string12 = (String)this.lines.get(i10);
				if(string12.startsWith("[C]")) {
					this.fontRenderer.drawStringWithShadow(string12.substring(3), i6 + (s5 - this.fontRenderer.getStringWidth(string12.substring(3))) / 2, i9, 0xFFFFFF);
				} else {
					this.fontRenderer.fontRandom.setSeed((long)i10 * 4238972211L + (long)(this.updateCounter / 4));
					this.fontRenderer.func_50101_a(string12, i6 + 1, i9 + 1, 0xFFFFFF, true);
					this.fontRenderer.fontRandom.setSeed((long)i10 * 4238972211L + (long)(this.updateCounter / 4));
					this.fontRenderer.func_50101_a(string12, i6, i9, 0xFFFFFF, false);
				}
			}

			i9 += 12;
		}

		GL11.glPopMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("%blur%/misc/vignette.png"));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
		tessellator4.startDrawingQuads();
		tessellator4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
		i10 = this.width;
		int i13 = this.height;
		tessellator4.addVertexWithUV(0.0D, (double)i13, (double)this.zLevel, 0.0D, 1.0D);
		tessellator4.addVertexWithUV((double)i10, (double)i13, (double)this.zLevel, 1.0D, 1.0D);
		tessellator4.addVertexWithUV((double)i10, 0.0D, (double)this.zLevel, 1.0D, 0.0D);
		tessellator4.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, 0.0D, 0.0D);
		tessellator4.draw();
		GL11.glDisable(GL11.GL_BLEND);
		super.drawScreen(i1, i2, f3);
	}
}
