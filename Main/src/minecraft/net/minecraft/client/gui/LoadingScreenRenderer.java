package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftError;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.level.chunk.storage.IProgressUpdate;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class LoadingScreenRenderer implements IProgressUpdate {
	private String loadingString = "";
	private Minecraft mc;
	private String currentlyDisplayedText = "";
	private long timeElapsed = System.currentTimeMillis();
	private static long hintTimer = System.currentTimeMillis();
	private boolean isSaving = false;
	private String hint = "";

	public LoadingScreenRenderer(Minecraft minecraft1) {
		this.mc = minecraft1;
		this.selectHint();
	}

	private void selectHint() {
		this.setHint(GameHints.getRandomHint());
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public void printText(String string1) {
		this.isSaving = false;
		this.updateTile(string1);
	}

	public void displaySavingString(String string1) {
		this.isSaving = true;
		this.updateTile(this.currentlyDisplayedText);
	}

	public void updateTile(String string1) {
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

	public void setLoadingProgress(int progress) {
		if(!this.mc.running) {
			if(!this.isSaving) {
				throw new MinecraftError();
			}
		} else {
			long currentMillis = System.currentTimeMillis();
			
			if(currentMillis - hintTimer >= 4000) {
				this.selectHint();
				hintTimer = currentMillis;
			}
			
			if(currentMillis - this.timeElapsed >= 20L) {
				this.timeElapsed = currentMillis;
				this.drawLoadingScreen(this.currentlyDisplayedText, this.loadingString, progress, -1);
				Display.update();

				try {
					Thread.yield();
				} catch (Exception e) {
				}

			}
		}
	}

	/**
	 * Dummy second progress bar: holds the current caption (e.g. the destination
	 * world's name) on screen for a moment and fills a white bar from 0 to 100 over
	 * the requested duration. Runs on the calling thread.
	 */
	public void runSimulation(long durationMillis) {
		if(!this.mc.running) return;
		long startMillis = System.currentTimeMillis();
		long lastRedraw = 0L;
		int progress = 0;
		while(progress < 100) {
			if(!this.mc.running) return;
			progress = (int)((System.currentTimeMillis() - startMillis) * 100L / durationMillis);
			if(progress < 0) progress = 0;
			if(progress > 100) progress = 100;
			long now = System.currentTimeMillis();
			if(now - lastRedraw >= 20L) {
				lastRedraw = now;
				this.drawLoadingScreen(this.currentlyDisplayedText, this.loadingString, -1, progress);
				Display.update();
				Thread.yield();
			}
		}
	}

	private void drawLoadingScreen(String title, String label, int mainProgress, int dummyProgress) {
		ScaledResolution scaledResolution4 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int w = scaledResolution4.getScaledWidth();
		int h = scaledResolution4.getScaledHeight();
		
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledResolution4.scaledWidthD, scaledResolution4.scaledHeightD, 0.0D, 100.0D, 300.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		Tessellator tes = Tessellator.instance;
		int texId = this.mc.renderEngine.getTexture("/misc/water.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

		float tileSize = 32.0F;
		tes.startDrawingQuads();
		tes.setColorOpaque_I(4210752);
		tes.addVertexWithUV(0.0D, (double)h, 0.0D, 0.0D, (double)((float)h / tileSize));
		tes.addVertexWithUV((double)w, (double)h, 0.0D, (double)((float)w / tileSize), (double)((float)h / tileSize));
		tes.addVertexWithUV((double)w, 0.0D, 0.0D, (double)((float)w / tileSize), 0.0D);
		tes.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		tes.draw();

		if(mainProgress >= 0) {
			this.drawProgressBar(w, h, h / 2 + 16, mainProgress, 8454016);
		}

		if(dummyProgress >= 0) {
			int dummyY = mainProgress >= 0 ? h / 2 + 22 : h / 2 + 16;
			this.drawProgressBar(w, h, dummyY, dummyProgress, 16777215);
		}

		// Draw text
		this.mc.fontRenderer.drawStringWithShadow(title, (w - this.mc.fontRenderer.getStringWidth(title)) / 2, h / 2 - 4 - 16, 0xFFFFFF);
		this.mc.fontRenderer.drawStringWithShadow(label, (w - this.mc.fontRenderer.getStringWidth(label)) / 2, h / 2 - 4 + 8, 0xFFFFFF);
		this.mc.fontRenderer.drawStringWithShadow(this.hint, (w - this.mc.fontRenderer.getStringWidth(this.hint)) / 2, h - 24, 0xFFFF00);
	}

	private void drawProgressBar(int w, int h, int y, int progress, int fillColor) {
		int x = w / 2 - 100 / 2;
		Tessellator tes = Tessellator.instance;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tes.startDrawingQuads();
		tes.setColorOpaque_I(8421504);
		tes.addVertex((double)x, (double)y, 0.0D);
		tes.addVertex((double)x, (double)(y + 2), 0.0D);
		tes.addVertex((double)(x + 100), (double)(y + 2), 0.0D);
		tes.addVertex((double)(x + 100), (double)y, 0.0D);
		tes.setColorOpaque_I(fillColor);
		tes.addVertex((double)x, (double)y, 0.0D);
		tes.addVertex((double)x, (double)(y + 2), 0.0D);
		tes.addVertex((double)(x + progress), (double)(y + 2), 0.0D);
		tes.addVertex((double)(x + progress), (double)y, 0.0D);
		tes.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}