package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class LoadingScreenRendererTrivia extends LoadingScreenRenderer {
	private String trivia;
	private String string1 = "";
	private String string2 = "";
	private long time = System.currentTimeMillis();
	private boolean someRandomBoolean = false;
	private Minecraft game;

	public LoadingScreenRendererTrivia(Minecraft minecraft) {
		super(minecraft);
		this.game = minecraft;
		this.trivia = "";
	}

	public void printText(String s) {
		this.someRandomBoolean = false;
		this.func_597_c(s);
	}

	public void func_594_b(String s) {
		this.someRandomBoolean = true;
		this.func_597_c(this.string2);
	}

	public void func_597_c(String s) {
		if(!this.game.running) {
			if(!this.someRandomBoolean) {
				throw new MinecraftError();
			}
		} else {
			this.string2 = s;
			if(this.string1 == "" && this.string2 == "") {
				this.trivia = "";
			}

			if(this.string2 != "" && this.trivia == "") {
				this.trivia = mod_Trivia.getNewString();
			}

			ScaledResolution scaledresolution = new ScaledResolution(this.game.gameSettings, this.game.displayWidth, this.game.displayHeight);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, scaledresolution.field_25121_a, scaledresolution.field_25120_b, 0.0D, 100.0D, 300.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		}
	}

	public void displayLoadingString(String s) {
		if(!this.game.running) {
			if(!this.someRandomBoolean) {
				throw new MinecraftError();
			}
		} else {
			this.time = 0L;
			this.string1 = s;
			this.setLoadingProgress(-1);
			this.time = 0L;
			this.time = 0L;
			if(this.string1 == "" && this.string2 == "") {
				this.trivia = "";
			}

			if(this.string1 != "" && this.string2 == "" && this.trivia == "") {
				this.trivia = mod_Trivia.getNewString();
			}

		}
	}

	public void setLoadingProgress(int i) {
		if(!this.game.running) {
			if(!this.someRandomBoolean) {
				throw new MinecraftError();
			}
		} else {
			long l = System.currentTimeMillis();
			if(l - this.time >= 20L) {
				this.time = l;
				ScaledResolution scaledresolution = new ScaledResolution(this.game.gameSettings, this.game.displayWidth, this.game.displayHeight);
				int j = scaledresolution.getScaledWidth();
				int k = scaledresolution.getScaledHeight();
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glOrtho(0.0D, scaledresolution.field_25121_a, scaledresolution.field_25120_b, 0.0D, 100.0D, 300.0D);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				GL11.glTranslatef(0.0F, 0.0F, -200.0F);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				Tessellator tessellator = Tessellator.instance;
				int i1 = this.game.renderEngine.getTexture("/gui/background.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i1);
				float f = 32.0F;
				tessellator.startDrawingQuads();
				tessellator.setColorOpaque_I(4210752);
				tessellator.addVertexWithUV(0.0D, (double)k, 0.0D, 0.0D, (double)((float)k / f));
				tessellator.addVertexWithUV((double)j, (double)k, 0.0D, (double)((float)j / f), (double)((float)k / f));
				tessellator.addVertexWithUV((double)j, 0.0D, 0.0D, (double)((float)j / f), 0.0D);
				tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
				tessellator.draw();
				if(i >= 0) {
					byte exception = 100;
					byte byte1 = 2;
					int j1 = j / 2 - exception / 2;
					int k1 = k / 2 + 16;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					tessellator.startDrawingQuads();
					tessellator.setColorOpaque_I(8421504);
					tessellator.addVertex((double)j1, (double)k1, 0.0D);
					tessellator.addVertex((double)j1, (double)(k1 + byte1), 0.0D);
					tessellator.addVertex((double)(j1 + exception), (double)(k1 + byte1), 0.0D);
					tessellator.addVertex((double)(j1 + exception), (double)k1, 0.0D);
					tessellator.setColorOpaque_I(8454016);
					tessellator.addVertex((double)j1, (double)k1, 0.0D);
					tessellator.addVertex((double)j1, (double)(k1 + byte1), 0.0D);
					tessellator.addVertex((double)(j1 + i), (double)(k1 + byte1), 0.0D);
					tessellator.addVertex((double)(j1 + i), (double)k1, 0.0D);
					tessellator.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				this.game.fontRenderer.drawStringWithShadow(this.string2, (j - this.game.fontRenderer.getStringWidth(this.string2)) / 2, k / 2 - 4 - 16, 0xFFFFFF);
				this.game.fontRenderer.drawStringWithShadow(this.string1, (j - this.game.fontRenderer.getStringWidth(this.string1)) / 2, k / 2 - 4 + 8, 0xFFFFFF);
				this.game.fontRenderer.drawStringWithShadow(this.trivia, (j - this.game.fontRenderer.getStringWidth(this.trivia)) / 2, k - 16, 16777113);
				Display.update();

				try {
					Thread.yield();
				} catch (Exception exception14) {
				}

			}
		}
	}
}
