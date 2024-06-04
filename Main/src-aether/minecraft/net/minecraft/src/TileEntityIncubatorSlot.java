package net.minecraft.src;

public class TileEntityIncubatorSlot extends Slot {
	public TileEntityIncubatorSlot(IInventory inv, int slot, int x, int y) {
		super(inv, slot, x, y);
	}

	public int getSlotStackLimit() {
		return 1;
	}
}
