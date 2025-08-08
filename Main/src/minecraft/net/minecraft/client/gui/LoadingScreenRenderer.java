package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftError;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.src.IProgressUpdate;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class LoadingScreenRenderer implements IProgressUpdate {
	private String loadingString = "";
	private Minecraft mc;
	private String currentlyDisplayedText = "";
	private long timeElapsed = System.currentTimeMillis();
	private boolean isSaving = false;

	public LoadingScreenRenderer(Minecraft minecraft1) {
		this.mc = minecraft1;
	}

	public void printText(String string1) {
		this.isSaving = false;
		this.func_597_c(string1);
	}

	public void displaySavingString(String string1) {
		this.isSaving = true;
		this.func_597_c(this.currentlyDisplayedText);
	}

	public void func_597_c(String string1) {
		if(!this.mc.running) {
			if(!this.isSaving) {
				throw new MinecraftError();
			}
		} else {
			this.currentlyDisplayedText = string1;
			ScaledResolution scaledResolution2 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, scaledResolution2.scaledWidthD, scaledResolution2.scaledHeightD, 0.0D, 100.0D, 300.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		}
	}

	public void displayLoadingString(String string1) {
		if(!this.mc.running) {
			if(!this.isSaving) {
				throw new MinecraftError();
			}
		} else {
			this.timeElapsed = 0L;
			this.loadingString = string1;
			this.setLoadingProgress(-1);
			this.timeElapsed = 0L;
		}
	}

	public void setLoadingProgress(int i1) {
		if(!this.mc.running) {
			if(!this.isSaving) {
				throw new MinecraftError();
			}
		} else {
			long j2 = System.currentTimeMillis();
			if(j2 - this.timeElapsed >= 20L) {
				this.timeElapsed = j2;
				ScaledResolution scaledResolution4 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
				int i5 = scaledResolution4.getScaledWidth();
				int i6 = scaledResolution4.getScaledHeight();
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glOrtho(0.0D, scaledResolution4.scaledWidthD, scaledResolution4.scaledHeightD, 0.0D, 100.0D, 300.0D);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				GL11.glTranslatef(0.0F, 0.0F, -200.0F);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				Tessellator tessellator7 = Tessellator.instance;
				int i8 = this.mc.renderEngine.getTexture("/misc/water.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i8);
				float f9 = 32.0F;
				tessellator7.startDrawingQuads();
				tessellator7.setColorOpaque_I(4210752);
				tessellator7.addVertexWithUV(0.0D, (double)i6, 0.0D, 0.0D, (double)((float)i6 / f9));
				tessellator7.addVertexWithUV((double)i5, (double)i6, 0.0D, (double)((float)i5 / f9), (double)((float)i6 / f9));
				tessellator7.addVertexWithUV((double)i5, 0.0D, 0.0D, (double)((float)i5 / f9), 0.0D);
				tessellator7.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
				tessellator7.draw();
				if(i1 >= 0) {
					byte b10 = 100;
					byte b11 = 2;
					int i12 = i5 / 2 - b10 / 2;
					int i13 = i6 / 2 + 16;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					tessellator7.startDrawingQuads();
					tessellator7.setColorOpaque_I(8421504);
					tessellator7.addVertex((double)i12, (double)i13, 0.0D);
					tessellator7.addVertex((double)i12, (double)(i13 + b11), 0.0D);
					tessellator7.addVertex((double)(i12 + b10), (double)(i13 + b11), 0.0D);
					tessellator7.addVertex((double)(i12 + b10), (double)i13, 0.0D);
					tessellator7.setColorOpaque_I(8454016);
					tessellator7.addVertex((double)i12, (double)i13, 0.0D);
					tessellator7.addVertex((double)i12, (double)(i13 + b11), 0.0D);
					tessellator7.addVertex((double)(i12 + i1), (double)(i13 + b11), 0.0D);
					tessellator7.addVertex((double)(i12 + i1), (double)i13, 0.0D);
					tessellator7.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				this.mc.fontRenderer.drawStringWithShadow(this.currentlyDisplayedText, (i5 - this.mc.fontRenderer.getStringWidth(this.currentlyDisplayedText)) / 2, i6 / 2 - 4 - 16, 0xFFFFFF);
				this.mc.fontRenderer.drawStringWithShadow(this.loadingString, (i5 - this.mc.fontRenderer.getStringWidth(this.loadingString)) / 2, i6 / 2 - 4 + 8, 0xFFFFFF);
				Display.update();

				try {
					Thread.yield();
				} catch (Exception exception14) {
				}

			}
		}
	}
}
