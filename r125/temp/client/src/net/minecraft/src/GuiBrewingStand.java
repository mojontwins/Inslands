package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiBrewingStand extends GuiContainer {
	private TileEntityBrewingStand field_40217_h;

	public GuiBrewingStand(InventoryPlayer inventoryPlayer1, TileEntityBrewingStand tileEntityBrewingStand2) {
		super(new ContainerBrewingStand(inventoryPlayer1, tileEntityBrewingStand2));
		this.field_40217_h = tileEntityBrewingStand2;
	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.brewing"), 56, 6, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float f1, int i2, int i3) {
		int i4 = this.mc.renderEngine.getTexture("/gui/alchemy.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(i4);
		int i5 = (this.width - this.xSize) / 2;
		int i6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i5, i6, 0, 0, this.xSize, this.ySize);
		int i7 = this.field_40217_h.getBrewTime();
		if(i7 > 0) {
			int i8 = (int)(28.0F * (1.0F - (float)i7 / 400.0F));
			if(i8 > 0) {
				this.drawTexturedModalRect(i5 + 97, i6 + 16, 176, 0, 9, i8);
			}

			int i9 = i7 / 2 % 7;
			switch(i9) {
			case 0:
				i8 = 29;
				break;
			case 1:
				i8 = 24;
				break;
			case 2:
				i8 = 20;
				break;
			case 3:
				i8 = 16;
				break;
			case 4:
				i8 = 11;
				break;
			case 5:
				i8 = 6;
				break;
			case 6:
				i8 = 0;
			}

			if(i8 > 0) {
				this.drawTexturedModalRect(i5 + 65, i6 + 14 + 29 - i8, 185, 29 - i8, 12, i8);
			}
		}

	}
}
