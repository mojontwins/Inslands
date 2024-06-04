package net.minecraft.src;

public class ItemBlockQuicksoil extends ItemBlock {
	public ItemBlockQuicksoil(int itemID) {
		super(itemID);
		this.setItemName("BlockQuicksoil");
		this.setHasSubtypes(true);
	}

	public String getItemNameIS(ItemStack itemstack) {
		return this.getItemName();
	}

	public int getPlacedBlockMetadata(int damage) {
		return 1;
	}
}
