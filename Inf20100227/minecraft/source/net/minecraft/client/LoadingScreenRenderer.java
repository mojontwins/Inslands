package net.minecraft.client;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import util.IProgressUpdate;

public final class LoadingScreenRenderer implements IProgressUpdate {
	private String text = "";
	private Minecraft mc;
	private String title = "";
	private long start = System.currentTimeMillis();

	public LoadingScreenRenderer(Minecraft var1) {
		this.mc = var1;
	}

	public final void displayProgressMessage(String var1) {
		if(!this.mc.running) {
			throw new MinecraftError();
		} else {
			this.title = var1;
			ScaledResolution var3 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
			int var2 = var3.getScaledWidth();
			int var4 = var3.getScaledHeight();
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, (double)var2, (double)var4, 0.0D, 100.0D, 300.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		}
	}

	public final void displayLoadingString(String var1) {
		if(!this.mc.running) {
			throw new MinecraftError();
		} else {
			this.start = 0L;
			this.text = var1;
			boolean var7 = true;
			if(!this.mc.running) {
				throw new MinecraftError();
			} else {
				long var4 = System.currentTimeMillis();
				if(var4 - this.start >= 20L) {
					this.start = var4;
					ScaledResolution var2 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
					int var3 = var2.getScaledWidth();
					int var8 = var2.getScaledHeight();
					GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
					GL11.glMatrixMode(GL11.GL_PROJECTION);
					GL11.glLoadIdentity();
					GL11.glOrtho(0.0D, (double)var3, (double)var8, 0.0D, 100.0D, 300.0D);
					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					GL11.glLoadIdentity();
					GL11.glTranslatef(0.0F, 0.0F, -200.0F);
					GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
					Tessellator var9 = Tessellator.instance;
					int var5 = this.mc.renderEngine.getTexture("/dirt.png");
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5);
					var9.startDrawingQuads();
					var9.setColorOpaque_I(4210752);
					var9.addVertexWithUV(0.0F, (float)var8, 0.0F, 0.0F, (float)var8 / 32.0F);
					var9.addVertexWithUV((float)var3, (float)var8, 0.0F, (float)var3 / 32.0F, (float)var8 / 32.0F);
					var9.addVertexWithUV((float)var3, 0.0F, 0.0F, (float)var3 / 32.0F, 0.0F);
					var9.addVertexWithUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
					var9.draw();
					this.mc.fontRenderer.drawStringWithShadow(this.title, (var3 - this.mc.fontRenderer.getStringWidth(this.title)) / 2, var8 / 2 - 4 - 16, 16777215);
					this.mc.fontRenderer.drawStringWithShadow(this.text, (var3 - this.mc.fontRenderer.getStringWidth(this.text)) / 2, var8 / 2 - 4 + 8, 16777215);
					Display.update();

					try {
						Thread.yield();
					} catch (Exception var6) {
					}
				}

				this.start = 0L;
			}
		}
	}
}
