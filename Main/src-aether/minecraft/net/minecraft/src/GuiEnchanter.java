package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiEnchanter extends GuiContainer {
	private TileEntityEnchanter enchanterInventory;

	public GuiEnchanter(InventoryPlayer inventoryplayer, TileEntityEnchanter tileentityEnchanter) {
		super(new ContainerEnchanter(inventoryplayer, tileentityEnchanter));
		this.enchanterInventory = tileentityEnchanter;
	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Enchanter", 60, 6, 4210752);
		this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float f) {
		int i = this.mc.renderEngine.getTexture("/aether/gui/enchanter.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(i);
		int j = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
		int i1;
		if(this.enchanterInventory.isBurning()) {
			i1 = this.enchanterInventory.getBurnTimeRemainingScaled(12);
			this.drawTexturedModalRect(j + 57, k + 47 - i1, 176, 12 - i1, 14, i1 + 2);
		}

		i1 = this.enchanterInventory.getCookProgressScaled(24);
		this.drawTexturedModalRect(j + 79, k + 35, 176, 14, i1 + 1, 16);
	}
}
