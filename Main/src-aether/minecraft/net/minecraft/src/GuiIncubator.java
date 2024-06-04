package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiIncubator extends GuiContainer {
	private TileEntityIncubator IncubatorInventory;

	public GuiIncubator(InventoryPlayer inventoryplayer, TileEntityIncubator tileentityIncubator) {
		super(new ContainerIncubator(inventoryplayer, tileentityIncubator));
		this.IncubatorInventory = tileentityIncubator;
	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Incubator", 60, 6, 4210752);
		this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float f) {
		int i = this.mc.renderEngine.getTexture("/aether/gui/incubator.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(i);
		int j = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
		int i1;
		if(this.IncubatorInventory.isBurning()) {
			i1 = this.IncubatorInventory.getBurnTimeRemainingScaled(12);
			this.drawTexturedModalRect(j + 74, k + 47 - i1, 176, 12 - i1, 14, i1 + 2);
		}

		i1 = this.IncubatorInventory.getCookProgressScaled(54);
		this.drawTexturedModalRect(j + 103, k + 70 - i1, 179, 70 - i1, 10, i1);
	}
}
