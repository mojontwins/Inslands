package net.minecraft.client.gui;

import net.minecraft.client.GuiMainMenu;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.game.world.World;

import org.lwjgl.opengl.GL11;

public final class GuiGameOver extends GuiScreen {
	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 72, "Respawn"));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96, "Title menu"));
		if(this.mc.session == null) {
			((GuiButton)this.controlList.get(1)).enabled = false;
		}

	}

	protected final void keyTyped(char c1, int i2) {
	}

	protected final void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.id == 1) {
			this.mc.respawn();
			this.mc.displayGuiScreen((GuiScreen)null);
		}

		if(guiButton1.id == 2) {
			this.mc.closeWorld((World)null);
			this.mc.displayGuiScreen(new GuiMainMenu());
		}

	}

	public final void drawScreen(int i1, int i2, float f3) {
		drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		drawCenteredString(this.fontRenderer, "Game over!", this.width / 2 / 2, 30, 0xFFFFFF);
		GL11.glPopMatrix();
		FontRenderer fontRenderer10000 = this.fontRenderer;
		StringBuilder stringBuilder10001 = (new StringBuilder()).append("Score: &e");
		EntityPlayerSP entityPlayerSP4 = this.mc.thePlayer;
		drawCenteredString(fontRenderer10000, stringBuilder10001.append(this.mc.thePlayer.score).toString(), this.width / 2, 100, 0xFFFFFF);
		super.drawScreen(i1, i2, f3);
	}

	public final boolean doesGuiPauseGame() {
		return false;
	}
}