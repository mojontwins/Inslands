package net.minecraft.src;

public class ChestItemRenderHelper {
	public static ChestItemRenderHelper instance = new ChestItemRenderHelper();
	private TileEntityChest field_35610_b = new TileEntityChest();

	public void func_35609_a(Block block1, int i2, float f3) {
		TileEntityRenderer.instance.renderTileEntityAt(this.field_35610_b, 0.0D, 0.0D, 0.0D, 0.0F);
	}
}
