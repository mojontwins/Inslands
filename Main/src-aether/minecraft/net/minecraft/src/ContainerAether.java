package net.minecraft.src;

public class ContainerAether extends ContainerPlayer {
	public ContainerAether(InventoryPlayer inventoryplayer, InventoryAether inv) {
		this(inventoryplayer, inv, true);
	}

	public ContainerAether(InventoryPlayer inventoryplayer, InventoryAether inv, boolean flag) {
		super(inventoryplayer, flag);
		this.slots.clear();
		this.craftMatrix = new InventoryCrafting(this, 2, 2);
		this.craftResult = new InventoryCraftResult();
		this.isSinglePlayer = flag;
		this.addSlot(new SlotCrafting(inventoryplayer.player, this.craftMatrix, this.craftResult, 0, 134, 62));

		int i;
		int j;
		for(i = 0; i < 2; ++i) {
			for(j = 0; j < 2; ++j) {
				this.addSlot(new Slot(this.craftMatrix, j + i * 2, 125 + j * 18, 8 + i * 18));
			}
		}

		for(i = 0; i < 4; ++i) {
			this.addSlot(new SlotArmor(this, inventoryplayer, inventoryplayer.getSizeInventory() - 1 - i, 62, 8 + i * 18, i));
		}

		for(i = 0; i < 3; ++i) {
			for(j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inventoryplayer, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(i = 0; i < 9; ++i) {
			this.addSlot(new Slot(inventoryplayer, i, 8 + i * 18, 142));
		}

		for(i = 1; i < 3; ++i) {
			for(j = 0; j < 4; ++j) {
				int armorType = 4 * (i - 1) + j;
				this.addSlot(new SlotMoreArmor(this, inv, armorType, 62 + i * 18, 8 + j * 18, armorType + 4));
			}
		}

		this.onCraftMatrixChanged(this.craftMatrix);
	}
}
