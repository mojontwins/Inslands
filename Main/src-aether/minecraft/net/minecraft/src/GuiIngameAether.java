package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class GuiIngameAether extends GuiIngame {
	private Minecraft mc;

	public GuiIngameAether(Minecraft minecraft) {
		super(minecraft);
		this.mc = minecraft;
	}

	public void renderGameOverlay(float f, boolean flag, int i, int j) {
		if(GuiMainMenu.mmactive) {
			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			this.mc.entityRenderer.func_905_b();
		} else {
			super.renderGameOverlay(f, flag, i, j);
		}

	}
}
