package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class GuiAetherButton extends GuiButton {
	public int scrollMax = 100;
	public int scrollHeight = this.scrollMax;
	public int scrollMin = 115;
	public int scrollCrop = 20;
	public int scrollCropMax = 90;
	public boolean retracting = false;

	public GuiAetherButton(int i, int j, int k, String s) {
		super(i, j, k, s);
	}

	public GuiAetherButton(int i, int j, int k, int l, int i1, String s) {
		super(i, j, k, l, i1, s);
		this.enabled = true;
		this.enabled2 = true;
	}

	protected int getHoverState(boolean flag) {
		byte byte0 = 1;
		if(GuiMainMenu.themeOption) {
			if(!this.enabled) {
				byte0 = 0;
			} else if(flag) {
				if(byte0 < 2) {
					++byte0;
				}

				if(this.scrollCrop < this.scrollCropMax) {
					++this.scrollCrop;
				}

				if(this.scrollHeight < this.scrollMin) {
					++this.scrollHeight;
				}
			} else {
				if(this.scrollCrop > this.scrollCropMax) {
					--this.scrollCrop;
				}

				if(this.scrollHeight > this.scrollMax) {
					--this.scrollHeight;
				}

				if(this.scrollHeight == this.scrollMax) {
					this.retracting = false;
				}
			}
		} else if(!GuiMainMenu.themeOption) {
			if(!this.enabled) {
				byte0 = 0;
			} else if(flag) {
				byte0 = 2;
			}
		}

		return byte0;
	}

	public void drawButton(Minecraft minecraft, int i, int j) {
		if(this.enabled2) {
			FontRenderer fontrenderer;
			boolean flag;
			int k;
			if(GuiMainMenu.themeOption) {
				fontrenderer = minecraft.fontRenderer;
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture("/aether/gui/buttons.png"));
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				flag = i >= this.xPosition && j >= this.yPosition && i < this.xPosition + this.width && j < this.yPosition + this.height;
				k = this.getHoverState(flag);
				this.drawTexturedModalRect(this.xPosition + this.scrollHeight - 90, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
				this.drawTexturedModalRect(this.xPosition + this.scrollHeight + this.width / 2 - 90, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
				this.mouseDragged(minecraft, i, j);
				if(!this.enabled) {
					this.drawString(fontrenderer, this.displayString, this.xPosition + this.width / 10 + this.scrollHeight - 80, this.yPosition + (this.height - 8) / 2, -6250336);
				} else if(flag) {
					this.drawString(fontrenderer, this.displayString, this.xPosition + this.width / 10 + this.scrollHeight - 80, this.yPosition + (this.height - 8) / 2, 7851212);
				} else {
					this.drawString(fontrenderer, this.displayString, this.xPosition + this.width / 10 + this.scrollHeight - 80, this.yPosition + (this.height - 8) / 2, 14737632);
				}
			}

			if(!GuiMainMenu.themeOption) {
				fontrenderer = minecraft.fontRenderer;
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture("/gui/gui.png"));
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				flag = i >= this.xPosition && j >= this.yPosition && i < this.xPosition + this.width && j < this.yPosition + this.height;
				k = this.getHoverState(flag);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
				this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
				this.mouseDragged(minecraft, i, j);
				if(!this.enabled) {
					this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, -6250336);
				} else if(flag) {
					this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 16777120);
				} else {
					this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 14737632);
				}
			}

		}
	}

	protected void mouseDragged(Minecraft var1, int var2, int var3) {
	}

	public void mouseReleased(int var1, int var2) {
	}

	public boolean mousePressed(Minecraft var1, int var2, int var3) {
		return this.enabled && var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
	}
}
