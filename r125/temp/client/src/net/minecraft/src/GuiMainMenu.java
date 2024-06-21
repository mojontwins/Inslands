package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class GuiMainMenu extends GuiScreen {
	private static final Random rand = new Random();
	private float updateCounter = 0.0F;
	private String splashText = "missingno";
	private GuiButton multiplayerButton;
	private int panoramaTimer = 0;
	private int viewportTexture;

	public GuiMainMenu() {
		try {
			ArrayList arrayList1 = new ArrayList();
			BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(GuiMainMenu.class.getResourceAsStream("/title/splashes.txt"), Charset.forName("UTF-8")));
			String string3 = "";

			while((string3 = bufferedReader2.readLine()) != null) {
				string3 = string3.trim();
				if(string3.length() > 0) {
					arrayList1.add(string3);
				}
			}

			do {
				this.splashText = (String)arrayList1.get(rand.nextInt(arrayList1.size()));
			} while(this.splashText.hashCode() == 125780783);
		} catch (Exception exception4) {
		}

		this.updateCounter = rand.nextFloat();
	}

	public void updateScreen() {
		++this.panoramaTimer;
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	protected void keyTyped(char c1, int i2) {
	}

	public void initGui() {
		this.viewportTexture = this.mc.renderEngine.allocateAndSetupTexture(new BufferedImage(256, 256, 2));
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(new Date());
		if(calendar1.get(2) + 1 == 11 && calendar1.get(5) == 9) {
			this.splashText = "Happy birthday, ez!";
		} else if(calendar1.get(2) + 1 == 6 && calendar1.get(5) == 1) {
			this.splashText = "Happy birthday, Notch!";
		} else if(calendar1.get(2) + 1 == 12 && calendar1.get(5) == 24) {
			this.splashText = "Merry X-mas!";
		} else if(calendar1.get(2) + 1 == 1 && calendar1.get(5) == 1) {
			this.splashText = "Happy new year!";
		}

		StringTranslate stringTranslate2 = StringTranslate.getInstance();
		int i4 = this.height / 4 + 48;
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, i4, stringTranslate2.translateKey("menu.singleplayer")));
		this.controlList.add(this.multiplayerButton = new GuiButton(2, this.width / 2 - 100, i4 + 24, stringTranslate2.translateKey("menu.multiplayer")));
		this.controlList.add(new GuiButton(3, this.width / 2 - 100, i4 + 48, stringTranslate2.translateKey("menu.mods")));
		if(this.mc.hideQuitButton) {
			this.controlList.add(new GuiButton(0, this.width / 2 - 100, i4 + 72, stringTranslate2.translateKey("menu.options")));
		} else {
			this.controlList.add(new GuiButton(0, this.width / 2 - 100, i4 + 72 + 12, 98, 20, stringTranslate2.translateKey("menu.options")));
			this.controlList.add(new GuiButton(4, this.width / 2 + 2, i4 + 72 + 12, 98, 20, stringTranslate2.translateKey("menu.quit")));
		}

		this.controlList.add(new GuiButtonLanguage(5, this.width / 2 - 124, i4 + 72 + 12));
		if(this.mc.session == null) {
			this.multiplayerButton.enabled = false;
		}

	}

	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if(guiButton1.id == 5) {
			this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings));
		}

		if(guiButton1.id == 1) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if(guiButton1.id == 2) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		if(guiButton1.id == 3) {
			this.mc.displayGuiScreen(new GuiTexturePacks(this));
		}

		if(guiButton1.id == 4) {
			this.mc.shutdown();
		}

	}

	private void drawPanorama(int i1, int i2, float f3) {
		Tessellator tessellator4 = Tessellator.instance;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GLU.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		byte b5 = 8;

		for(int i6 = 0; i6 < b5 * b5; ++i6) {
			GL11.glPushMatrix();
			float f7 = ((float)(i6 % b5) / (float)b5 - 0.5F) / 64.0F;
			float f8 = ((float)(i6 / b5) / (float)b5 - 0.5F) / 64.0F;
			float f9 = 0.0F;
			GL11.glTranslatef(f7, f8, f9);
			GL11.glRotatef(MathHelper.sin(((float)this.panoramaTimer + f3) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-((float)this.panoramaTimer + f3) * 0.1F, 0.0F, 1.0F, 0.0F);

			for(int i10 = 0; i10 < 6; ++i10) {
				GL11.glPushMatrix();
				if(i10 == 1) {
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if(i10 == 2) {
					GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if(i10 == 3) {
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if(i10 == 4) {
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if(i10 == 5) {
					GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/title/bg/panorama" + i10 + ".png"));
				tessellator4.startDrawingQuads();
				tessellator4.setColorRGBA_I(0xFFFFFF, 255 / (i6 + 1));
				float f11 = 0.0F;
				tessellator4.addVertexWithUV(-1.0D, -1.0D, 1.0D, (double)(0.0F + f11), (double)(0.0F + f11));
				tessellator4.addVertexWithUV(1.0D, -1.0D, 1.0D, (double)(1.0F - f11), (double)(0.0F + f11));
				tessellator4.addVertexWithUV(1.0D, 1.0D, 1.0D, (double)(1.0F - f11), (double)(1.0F - f11));
				tessellator4.addVertexWithUV(-1.0D, 1.0D, 1.0D, (double)(0.0F + f11), (double)(1.0F - f11));
				tessellator4.draw();
				GL11.glPopMatrix();
			}

			GL11.glPopMatrix();
			GL11.glColorMask(true, true, true, false);
		}

		tessellator4.setTranslation(0.0D, 0.0D, 0.0D);
		GL11.glColorMask(true, true, true, true);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void rotateAndBlurSkybox(float f1) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.viewportTexture);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColorMask(true, true, true, false);
		Tessellator tessellator2 = Tessellator.instance;
		tessellator2.startDrawingQuads();
		byte b3 = 3;

		for(int i4 = 0; i4 < b3; ++i4) {
			tessellator2.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float)(i4 + 1));
			int i5 = this.width;
			int i6 = this.height;
			float f7 = (float)(i4 - b3 / 2) / 256.0F;
			tessellator2.addVertexWithUV((double)i5, (double)i6, (double)this.zLevel, (double)(0.0F + f7), 0.0D);
			tessellator2.addVertexWithUV((double)i5, 0.0D, (double)this.zLevel, (double)(1.0F + f7), 0.0D);
			tessellator2.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(1.0F + f7), 1.0D);
			tessellator2.addVertexWithUV(0.0D, (double)i6, (double)this.zLevel, (double)(0.0F + f7), 1.0D);
		}

		tessellator2.draw();
		GL11.glColorMask(true, true, true, true);
	}

	private void renderSkybox(int i1, int i2, float f3) {
		GL11.glViewport(0, 0, 256, 256);
		this.drawPanorama(i1, i2, f3);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		this.rotateAndBlurSkybox(f3);
		this.rotateAndBlurSkybox(f3);
		this.rotateAndBlurSkybox(f3);
		this.rotateAndBlurSkybox(f3);
		this.rotateAndBlurSkybox(f3);
		this.rotateAndBlurSkybox(f3);
		this.rotateAndBlurSkybox(f3);
		this.rotateAndBlurSkybox(f3);
		GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		Tessellator tessellator4 = Tessellator.instance;
		tessellator4.startDrawingQuads();
		float f5 = this.width > this.height ? 120.0F / (float)this.width : 120.0F / (float)this.height;
		float f6 = (float)this.height * f5 / 256.0F;
		float f7 = (float)this.width * f5 / 256.0F;
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		tessellator4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
		int i8 = this.width;
		int i9 = this.height;
		tessellator4.addVertexWithUV(0.0D, (double)i9, (double)this.zLevel, (double)(0.5F - f6), (double)(0.5F + f7));
		tessellator4.addVertexWithUV((double)i8, (double)i9, (double)this.zLevel, (double)(0.5F - f6), (double)(0.5F - f7));
		tessellator4.addVertexWithUV((double)i8, 0.0D, (double)this.zLevel, (double)(0.5F + f6), (double)(0.5F - f7));
		tessellator4.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(0.5F + f6), (double)(0.5F + f7));
		tessellator4.draw();
	}

	public void drawScreen(int i1, int i2, float f3) {
		this.renderSkybox(i1, i2, f3);
		Tessellator tessellator4 = Tessellator.instance;
		short s5 = 274;
		int i6 = this.width / 2 - s5 / 2;
		byte b7 = 30;
		this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 0xFFFFFF);
		this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/title/mclogo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if((double)this.updateCounter < 1.0E-4D) {
			this.drawTexturedModalRect(i6 + 0, b7 + 0, 0, 0, 99, 44);
			this.drawTexturedModalRect(i6 + 99, b7 + 0, 129, 0, 27, 44);
			this.drawTexturedModalRect(i6 + 99 + 26, b7 + 0, 126, 0, 3, 44);
			this.drawTexturedModalRect(i6 + 99 + 26 + 3, b7 + 0, 99, 0, 26, 44);
			this.drawTexturedModalRect(i6 + 155, b7 + 0, 0, 45, 155, 44);
		} else {
			this.drawTexturedModalRect(i6 + 0, b7 + 0, 0, 0, 155, 44);
			this.drawTexturedModalRect(i6 + 155, b7 + 0, 0, 45, 155, 44);
		}

		tessellator4.setColorOpaque_I(0xFFFFFF);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
		GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
		float f8 = 1.8F - MathHelper.abs(MathHelper.sin((float)(System.currentTimeMillis() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
		f8 = f8 * 100.0F / (float)(this.fontRenderer.getStringWidth(this.splashText) + 32);
		GL11.glScalef(f8, f8, f8);
		this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, 16776960);
		GL11.glPopMatrix();
		this.drawString(this.fontRenderer, "Minecraft 1.2.5", 2, this.height - 10, 0xFFFFFF);
		String string9 = "Copyright Mojang AB. Do not distribute!";
		this.drawString(this.fontRenderer, string9, this.width - this.fontRenderer.getStringWidth(string9) - 2, this.height - 10, 0xFFFFFF);
		super.drawScreen(i1, i2, f3);
	}
}
